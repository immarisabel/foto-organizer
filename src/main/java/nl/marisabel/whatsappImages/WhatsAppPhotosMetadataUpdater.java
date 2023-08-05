package nl.marisabel.whatsappImages;

import nl.marisabel.exceptions.ModifyException;
import nl.marisabel.photos.ModifyCreatedDate;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class WhatsAppPhotosMetadataUpdater {

    private static WhatsAppImageMetadataExtractor metadataExtractor;
    private static ModifyCreatedDate modifyCreatedDate;

    public WhatsAppPhotosMetadataUpdater() {
        metadataExtractor = new WhatsAppImageMetadataExtractor();
        modifyCreatedDate = new ModifyCreatedDate();
    }



    public void scanFiles(String sourceFolderPath) throws ParseException, IOException, ModifyException {
        File folder = new File(sourceFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();

            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        System.out.println(file.getName());
                        processFile(file);
                    }
                }
            }
        } else {
            System.out.println("Invalid folder path!");
        }
    }


    private static void processFile(File file) throws ParseException, IOException, ModifyException {
        String metadata = metadataExtractor.getWhatsAppMetadata(file);

        // Check if the metadata is null or "null 00:00:00"
        if (Objects.equals(metadata, "null 00:00:00") || metadata == null) {
            System.out.println("image does not match the pattern: IMG-YYYYMMDD-WAXXXX.jpg");
        }

        if (metadata != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println(">>>>>> METADATA: " + metadata);

            // Modify the "created date" metadata of the file
            modifyCreatedDate.modify(file, metadata);

            System.out.println("Updated metadata");
        }
    }
}



