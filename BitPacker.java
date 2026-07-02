
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BitPacker {

    public static void packAndWriteBytes(String bitString, String binPath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(binPath)) {
            int bitCount = 0;
            int currentByte = 0;

            for (int i = 0; i < bitString.length(); i++) {
                currentByte = (currentByte << 1) | (bitString.charAt(i) - '0');
                bitCount++;

                if (bitCount == 8) {
                    fos.write(currentByte);
                    bitCount = 0;
                    currentByte = 0;
                }
            }
            
            if (bitCount > 0) {
                currentByte = currentByte << (8 - bitCount);
                fos.write(currentByte);
            }
        }
    }

    public static String readBitsFromBytes(String binPath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(binPath));
        StringBuilder bitString = new StringBuilder();

        for (byte b : bytes) {
            for (int i = 7; i >= 0; i--) {
                bitString.append((b >> i) & 1);
            }
        }
        return bitString.toString();
    }
}