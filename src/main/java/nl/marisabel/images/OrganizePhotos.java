package nl.marisabel.images;

import org.apache.commons.imaging.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class OrganizePhotos {

    private static Logger log = Logger.getLogger(PhotosProcessor.class.getName());

    /**
     * Actively organize the files from the source folder and its subfolders to the destination folder in years and months subfolders,
     * skipping duplicates.
     *
     * @param sourceFolderPath      The source folder to organize.
     * @param destinationFolderPath The destination folder to move the organized files to.
     */
    public void organizePhotos(String sourceFolderPath, String destinationFolderPath) {
        PhotosProcessor photosProcessor = new PhotosProcessor();
        Set<String> fileNames = new HashSet<>();
        File folder = new File(sourceFolderPath);
        processFilesInFolder(folder, destinationFolderPath, photosProcessor, fileNames);
        log.info("TOTAL FILES PROCESSED: " + fileNames.size());
    }

    /**
     * Recursively processes the files from the given folder and its subfolders,
     * organizing them directly in the destination folder.
     * @param picture
     * @param destinationFolderPath
     * @param photosProcessor
     * @param fileNames
     */
    private void processFilesInFolder(File picture, String destinationFolderPath, PhotosProcessor photosProcessor, Set<String> fileNames) {
        File[] files = picture.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    try {
                        photosProcessor.processImage(file, destinationFolderPath);
                        fileNames.add(file.getName());
                    } catch (IOException | ImageReadException e) {
                        log.warning("Error processing file: " + file.getName() + " - " + e.getMessage());
                    }
                } else if (file.isDirectory()) {
                    // Process the subfolder recursively, organizing files directly in the destination folder
                    log.info(file.getName());
                    processFilesInFolder(file, destinationFolderPath, photosProcessor, fileNames);
                }
            }
        }
    }
}
