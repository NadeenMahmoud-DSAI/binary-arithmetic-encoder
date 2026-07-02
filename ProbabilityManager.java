import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ProbabilityManager {

    public static Map<Character, double[]> generateAndSaveProbabilities(String inputPath, String probPath, int[] totalLengthTracker) throws IOException {
        String text = new String(Files.readAllBytes(Paths.get(inputPath)));
        int totalChars = text.length();
        totalLengthTracker[0] = totalChars;

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : text.toCharArray()) freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);

        List<Character> sortedSymbols = new ArrayList<>(freqMap.keySet());
        Collections.sort(sortedSymbols);

        Map<Character, double[]> ranges = new HashMap<>();
        double currentLow = 0.0;

        try (FileWriter probWriter = new FileWriter(probPath)) {
            probWriter.write("TotalLength," + totalChars + "\n");
            
            for (char c : sortedSymbols) {
                double prob = (double) freqMap.get(c) / totalChars;
                ranges.put(c, new double[]{currentLow, currentLow + prob});
                currentLow += prob;

                String displayChar = (c == '\n') ? "\\n" : (c == '\r') ? "\\r" : String.valueOf(c);
                probWriter.write(displayChar + "," + prob + "\n");
            }
        }
        return ranges;
    }

    public static Map<Character, double[]> loadProbabilities(String probPath, int[] totalLengthTracker) throws IOException {
        Map<Character, double[]> ranges = new LinkedHashMap<>();
        double currentLow = 0.0;

        try (Scanner probScanner = new Scanner(new File(probPath))) {
            while (probScanner.hasNextLine()) {
                String line = probScanner.nextLine();
                if (line.trim().isEmpty()) continue;
                
                if (line.startsWith("TotalLength,")) {
                    totalLengthTracker[0] = Integer.parseInt(line.split(",")[1]);
                    continue;
                }
                
                int lastComma = line.lastIndexOf(',');
                String symbolStr = line.substring(0, lastComma);
                char symbol = symbolStr.equals("\\n") ? '\n' : symbolStr.equals("\\r") ? '\r' : symbolStr.charAt(0);
                double prob = Double.parseDouble(line.substring(lastComma + 1));
                
                ranges.put(symbol, new double[]{currentLow, currentLow + prob});
                currentLow += prob;
            }
        }
        return ranges;
    }
}