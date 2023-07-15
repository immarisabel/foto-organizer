package nl.marisabel.ui;

import nl.marisabel.images.ProcessImage;
import org.apache.commons.imaging.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class Organize {

    private static Logger log = Logger.getLogger(ProcessImage.class.getName());

    /**
     * Actively organize the files from the source folder and its subfolders to the destination folder in years and months subfolders,
     * skipping duplicates.
     *
     * @param sourceFolder      The source folder to organize.
     * @param destinationFolder The destination folder to move the organized files to.
     */
    public void organizePhotos(String sourceFolder, String destinationFolder) {
        ProcessImage processImage = new ProcessImage();
        Set<String> fileNames = new HashSet<>();
        File folder = new File(sourceFolder);
        processFilesInFolder(folder, destinationFolder, processImage, fileNames);
        log.info("TOTAL FILES PROCESSED: " + fileNames.size());
    }

    /**
     * Recursively processes the files from the given folder and its subfolders,
     * organizing them directly in the destination folder.
     *
     * @param folder            The folder to process.
     * @param destinationFolder The destination folder to move the organized files to.
     * @param processImage      The ProcessImage instance for organizing the images.
     * @param fileNames         The set to track processed file names.
     */
    private void processFilesInFolder(File folder, String destinationFolder, ProcessImage processImage, Set<String> fileNames) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        processImage.processImage(file, destinationFolder);
                        fileNames.add(file.getName());
                    } catch (IOException | ImageReadException e) {
                        log.warning("Error processing file: " + file.getName() + " - " + e.getMessage());
                    }
                } else if (file.isDirectory()) {
                    // Process the subfolder recursively, organizing files directly in the destination folder
                    processFilesInFolder(file, destinationFolder, processImage, fileNames);
                }
            }
        }
    }
}
