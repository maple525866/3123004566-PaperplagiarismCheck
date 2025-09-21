package com.code;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;

/**
 * Paper plagiarism checker
 * Uses LCS (Longest Common Subsequence) algorithm to calculate similarity between texts
 */
public class PlagiarismChecker {
    
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java PlagiarismChecker <original_file_path> <plagiarized_file_path> <output_file_path>");
            System.exit(1);
        }
        
        String originalFilePath = args[0];
        String plagiarizedFilePath = args[1];
        String outputFilePath = args[2];
        
        try {
            // Read content of both files
            String originalText = readFile(originalFilePath);
            String plagiarizedText = readFile(plagiarizedFilePath);
            
            // Calculate similarity
            double similarity = calculateSimilarity(originalText, plagiarizedText);
            
            // Write result to output file
            writeResult(outputFilePath, similarity);
            
            System.out.println("Plagiarism check completed. Result written to: " + outputFilePath);
            
        } catch (IOException e) {
            System.err.println("File operation error: " + e.getMessage());
            System.exit(1);
        }
    }
    
    /**
     * Read file content
     * @param filePath file path
     * @return file content as string
     * @throws IOException file read exception
     */
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
    
    /**
     * Write result to output file
     * @param outputFilePath output file path
     * @param similarity similarity value
     * @throws IOException file write exception
     */
    private static void writeResult(String outputFilePath, double similarity) throws IOException {
        DecimalFormat df = new DecimalFormat("0.00");
        double percentage = similarity * 100;
        String result = df.format(percentage) + "%";
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFilePath), StandardCharsets.UTF_8))) {
            writer.write(result);
        }
    }
    
    /**
     * Calculate similarity between two texts
     * Based on LCS (Longest Common Subsequence) algorithm
     * @param text1 original text
     * @param text2 plagiarized text
     * @return similarity (0.00-1.00)
     */
    private static double calculateSimilarity(String text1, String text2) {
        if (text1 == null || text2 == null) {
            return 0.0;
        }
        
        if (text1.equals(text2)) {
            return 1.0;
        }
        
        // Remove whitespaces for comparison
        text1 = text1.replaceAll("\\s+", "");
        text2 = text2.replaceAll("\\s+", "");
        
        if (text1.length() == 0 && text2.length() == 0) {
            return 1.0;
        }
        
        if (text1.length() == 0 || text2.length() == 0) {
            return 0.0;
        }
        
        // Calculate LCS length
        int lcsLength = longestCommonSubsequence(text1, text2);
        
        // Calculate similarity: LCS length / average length of two texts
        double avgLength = (text1.length() + text2.length()) / 2.0;
        return lcsLength / avgLength;
    }
    
    /**
     * Calculate longest common subsequence length
     * @param text1 text 1
     * @param text2 text 2
     * @return LCS length
     */
    private static int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();
        
        // Create DP table
        int[][] dp = new int[m + 1][n + 1];
        
        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        
        return dp[m][n];
    }
}
