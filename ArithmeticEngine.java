import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ArithmeticEngine {

    public static void compress(String inputPath, String probPath, String binPath) throws IOException {
        int[] totalLengthTracker = new int[1];
        Map<Character, double[]> ranges = ProbabilityManager.generateAndSaveProbabilities(inputPath, probPath, totalLengthTracker);
        
        String text = new String(Files.readAllBytes(Paths.get(inputPath)));
        double low = 0.0;
        double high = 1.0;
        StringBuilder encodedBits = new StringBuilder();

        for (char c : text.toCharArray()) {
            double range = high - low;
            double charLow = ranges.get(c)[0];
            double charHigh = ranges.get(c)[1];

            high = low + range * charHigh;
            low = low + range * charLow;

            while (high <= 0.5 || low >= 0.5) {
                if (high <= 0.5) {
                    encodedBits.append("0");
                    low = 2 * low;
                    high = 2 * high;
                } else if (low >= 0.5) {
                    encodedBits.append("1");
                    low = 2 * (low - 0.5);
                    high = 2 * (high - 0.5);
                }
            }
        }

        encodedBits.append("10000000"); 
        BitPacker.packAndWriteBytes(encodedBits.toString(), binPath);
    }

    public static void decompress(String binPath, String probPath, String outputPath) throws IOException {
        int[] totalLengthTracker = new int[1];
        Map<Character, double[]> ranges = ProbabilityManager.loadProbabilities(probPath, totalLengthTracker);
        int totalLength = totalLengthTracker[0];

        String encodedBits = BitPacker.readBitsFromBytes(binPath);
        
        double low = 0.0;
        double high = 1.0;
        double value = 0.0;
        
        int precision = Math.min(32, encodedBits.length());
        for (int i = 0; i < precision; i++) {
            if (encodedBits.charAt(i) == '1') {
                value += Math.pow(0.5, i + 1);
            }
        }
        
        int bitIndex = precision;
        StringBuilder decodedText = new StringBuilder();

        for (int count = 0; count < totalLength; count++) {
            char decodedChar = '\0';
            for (Map.Entry<Character, double[]> entry : ranges.entrySet()) {
                double charLow = entry.getValue()[0];
                double charHigh = entry.getValue()[1];
                
                double currentRange = high - low;
                double relativeValue = (value - low) / currentRange;
                
                if (relativeValue >= charLow && relativeValue < charHigh) {
                    decodedChar = entry.getKey();
                    high = low + currentRange * charHigh;
                    low = low + currentRange * charLow;
                    break;
                }
            }
            
            if (decodedChar == '\0') break;
            decodedText.append(decodedChar);
            
            while (high <= 0.5 || low >= 0.5) {
                if (high <= 0.5) {
                    low = 2 * low;
                    high = 2 * high;
                    value = 2 * value;
                } else if (low >= 0.5) {
                    low = 2 * (low - 0.5);
                    high = 2 * (high - 0.5);
                    value = 2 * (value - 0.5);
                }
                
                if (bitIndex < encodedBits.length()) {
                    if (encodedBits.charAt(bitIndex) == '1') {
                        value += Math.pow(0.5, precision);
                    }
                    bitIndex++;
                }
            }
        }

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write(decodedText.toString());
        }
    }
}