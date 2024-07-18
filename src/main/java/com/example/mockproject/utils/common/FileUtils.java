package com.example.mockproject.utils.common;

public class FileUtils {
    private static final String INFIX = "Candidate";

    /**
     * return a new file name with the format "Candidate" + candidateId + originalFileName.
     * @param fileName originalFileName
     * @param candidateId candidateId
     * @return String
     */
    public static String getNewFileName(String fileName, Integer candidateId) {
        return (new StringBuffer()).append(candidateId).append(INFIX).append(fileName).toString();
    }

    /**
     * return original file name.
     * @param fileName newFileName
     * @param candidateId candidateId
     * @return String
     */
    public static String getOriginalFileName(String fileName, Integer candidateId) {
        return fileName.substring(Integer.toString(candidateId).length() + INFIX.length());
    }

    public static boolean hasIdInFileName(String fileName, String id) {
        return fileName.substring(0, fileName.indexOf(INFIX)).equals(id);
    }
}
