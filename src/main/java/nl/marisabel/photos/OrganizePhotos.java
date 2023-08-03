package nl.marisabel.photos;

import org.apache.commons.imaging.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

public class OrganizePhotos {


    /**
     * Actively organize the files from the source folder and its subfolders to the destination folder in years and months subfolders,
     * skipping duplicates.
     *
     * @param sourceFolderPath      The source folder to organize.
     * @param destinationFolderPath The destination folder to move the organized files to.
     * @return An array of two integers, where the first element is the count of processed files and the second element is the count of skipped files.
     */
    public int[] organizePhotos(String sourceFolderPath, String destinationFolderPath) {
        PhotosProcessor photosProcessor = new PhotosProcessor();
        Set<String> fileNames = new HashSet<>();
        int[] result = processFilesInFolder(new File(sourceFolderPath), destinationFolderPath, photosProcessor, fileNames);
        System.out.println("[=== TOTAL FILES PROCESSED: " + result[0]);
        System.out.println("[=== TOTAL FILES SKIPPED: " + result[1]);
        return result;
    }

    /**
     * Recursively processes the files from the given folder and its subfolders,
     * organizing them directly in the destination folder.
     *
     * @param picture
     * @param destinationFolderPath
     * @param photosProcessor
     * @param fileNames
     * @return An array of two integers, where the first element is the count of processed files and the second element is the count of skipped files.
     */
    private int[] processFilesInFolder(File picture, String destinationFolderPath, PhotosProcessor photosProcessor, Set<String> fileNames) {
        int[] counters = new int[2];
        File[] files = picture.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        photosProcessor.processImage(file, destinationFolderPath);
                        fileNames.add(file.getName());
                        counters[0]++; // Increment the processed files counter
                    } catch (IOException | ImageReadException e) {
                        System.out.println("[!] Error processing file: " + file.getName() + " - " + e.getMessage());
                        counters[1]++; // Increment the skipped files counter
                    } catch (ParseException e) {
                        System.out.println("[!] Error parsing file: " + file.getName() + " - " + e.getMessage());
                        counters[1]++; // Increment the skipped files counter
                    }
                } else if (file.isDirectory()) {
                    // Process the subfolder recursively, organizing files directly in the destination folder
                    int[] subCounters = processFilesInFolder(file, destinationFolderPath, photosProcessor, fileNames);
                    counters[0] += subCounters[0]; // Add processed files from the subfolder
                    counters[1] += subCounters[1]; // Add skipped files from the subfolder
                }
            }
        }
        return counters;
    }

}