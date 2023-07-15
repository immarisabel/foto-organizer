package nl.marisabel.whatsappImages;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.logging.Logger;

public class WhatsAppImageProcessor {

    private static final Logger log = Logger.getLogger(WhatsAppImageProcessor.class.getName());

    private WhatsAppImageMetadataExtractor metadataExtractor;

    public WhatsAppImageProcessor() {
        metadataExtractor = new WhatsAppImageMetadataExtractor();
    }

    public void processImages(String sourceFolderPath, String destinationFolderPath) {
        try {
            // Create the destination folder if it doesn't exist
            File destinationFolder = new File(destinationFolderPath);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdirs();
            }

            // Get a list of files in the source folder
            File[] files = new File(sourceFolderPath).listFiles();

            // Iterate over the files
            for (File file : files) {
                // Process the file
                processImage(file, destinationFolderPath);
            }
        } catch (ParseException e) {
            log.severe("Error parsing date: " + e.getMessage());
        }
    }

    private void processImage(File file, String destinationFolderPath) throws ParseException {
        // Extract metadata from the image
        String extractedDate = metadataExtractor.getWhatsAppMetadata(String.valueOf(file));

        if (extractedDate != null) {
            try {
                // Format the date
                String formattedDate = metadataExtractor.formatDate(extractedDate);

                // Create the destination folder based on the year and month
                String year = formattedDate.substring(0, 4);
                String month = formattedDate.substring(5, 7);
                String destinationPath = destinationFolderPath + "/" + year + "/" + month;

                // Create the destination folder if it doesn't exist
                File destinationFolder = new File(destinationPath);
                if (!destinationFolder.exists()) {
                    destinationFolder.mkdirs();
                }

                // Move the image to the destination folder
                Path sourcePath = file.toPath();
                Path destinationPathFile = new File(destinationPath, file.getName()).toPath();
                Files.move(sourcePath, destinationPathFile, StandardCopyOption.REPLACE_EXISTING);

                log.info("Processed image: " + file.getName() + ", Destination: " + destinationPathFile);
            } catch (IOException e) {
                log.severe("Error moving image: " + e.getMessage());
            }
        } else {
            log.warning("Date not found in image metadata: " + file.getName());
        }
    }
}
