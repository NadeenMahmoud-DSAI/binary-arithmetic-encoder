public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java Main <compress|decompress> [arguments]");
            System.exit(1);
        }

        String mode = args[0].toLowerCase();

        try {
            if (mode.equals("compress")) {
                String inputPath = args.length > 1 ? args[1] : "input.txt";
                String probPath = args.length > 2 ? args[2] : "probabilities.csv";
                String binPath = args.length > 3 ? args[3] : "compressed.bin";
                
                ArithmeticEngine.compress(inputPath, probPath, binPath);
                System.out.println("Compression complete. Output saved to: " + binPath);
                
            } else if (mode.equals("decompress")) {
                String binPath = args.length > 1 ? args[1] : "compressed.bin";
                String probPath = args.length > 2 ? args[2] : "probabilities.csv";
                String outputPath = args.length > 3 ? args[3] : "decompressed.txt";
                
                ArithmeticEngine.decompress(binPath, probPath, outputPath);
                System.out.println("Decompression complete. Output saved to: " + outputPath);
                
            } else {
                System.err.println("Invalid mode. Use 'compress' or 'decompress'.");
            }
        } catch (Exception e) {
            System.err.println("Execution failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}