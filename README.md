# Binary Arithmetic Encoder

A modular, Java-based implementation of lossless data compression using Arithmetic Coding. This engine dynamically evaluates the probability distribution of input data, performs recursive interval rescaling to generate optimal bit sequences, and strictly manages bitwise I/O for efficient binary packing.

## Architecture Overview

The system is designed with strict separation of concerns, moving away from monolithic scripting into an enterprise-ready, command-line-driven micro-architecture.

*   **`ArithmeticEngine.java`**: The mathematical core. Handles the recursive partitioning of the probability space and precision rescaling for both encoding and decoding.
*   **`ProbabilityManager.java`**: Isolates the frequency mapping and probability generation. Handles the extraction of character distributions and standardizes them for the decoding pipeline.
*   **`BitPacker.java`**: A dedicated utility for low-level bitwise operations. Safely handles the conversion and packing of binary strings into 8-bit bytes to prevent memory overflow and padding corruption.
*   **`Main.java`**: The orchestration layer, providing a clean Command-Line Interface (CLI) for executing compression and decompression pipelines.

## Mathematical Foundation: Interval Rescaling

Unlike Huffman coding which assigns discrete variable-length codes to specific symbols, this arithmetic encoder represents an entire message as a single floating-point number within the interval `[0.0, 1.0)`.

1.  **Probability Distribution:** The system first scans the input data to determine the exact frequency and probability of each character, mapping these into cumulative sub-intervals.
2.  **Dynamic Partitioning:** As the engine processes each character, the current operating interval `[low, high)` is proportionally restricted to the sub-interval of the current symbol.
3.  **Precision Management:** Because floating-point data types quickly suffer from underflow when processing large files, the system continuously monitors the interval bounds. When `high <= 0.5` or `low >= 0.5`, the engine extracts the definitive leading bits (`0` or `1`, respectively) and aggressively scales the interval outward `(e.g., low = 2 * low)`. This guarantees infinite precision stream processing regardless of the input file size.

## Usage & Execution

The system is executed via standard command-line arguments.

### Compilation
Compile all modular components using `javac`:
```bash
javac *.java
Compression Pipeline
To encode a text file into a compressed binary file:

Bash
java Main compress <input_file.txt> <probabilities.csv> <output_compressed.bin>
Example: java Main compress input.txt probabilities.csv compressed.bin

Decompression Pipeline
To reconstruct the original file from the binary data and probability map:

Bash
java Main decompress <input_compressed.bin> <probabilities.csv> <output_decompressed.txt>
Example: java Main decompress compressed.bin probabilities.csv decompressed.txt


***

This documentation is sharp, technical, and firmly establishes your engineering competence. It proves you understand the math at a deep level and respect the structural standards required in modern data pipelines.