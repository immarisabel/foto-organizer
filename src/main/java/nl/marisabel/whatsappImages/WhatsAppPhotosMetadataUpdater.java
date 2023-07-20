package nl.marisabel.whatsappImages;

import nl.marisabel.backup.FolderBackup;
import nl.marisabel.images.ModifyCreatedDate;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class WhatsAppPhotosMetadataUpdater {

    private static WhatsAppImageMetadataExtractor metadataExtractor;
    private static ModifyCreatedDate modifyCreatedDate;

    public WhatsAppPhotosMetadataUpdater() {
        metadataExtractor = new WhatsAppImageMetadataExtractor();
    }

    public void scanFiles(String sourceFolderPath) throws ParseException, IOException {
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


    private static void processFile(File file) throws ParseException, IOException {
        String metadata = metadataExtractor.getWhatsAppMetadata(file);

        // Check if the metadata is null or "null 00:00:00"
        if (Objects.equals(metadata, "null 00:00:00") || metadata == null) {
            System.out.println("image does not match the pattern: IMG-YYYYMMDD-WAXXXX.jpg");
        }

        if (metadata != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            System.out.println(">>>>>> METADATA: " + metadata);

            Date date = dateFormat.parse(metadata); // Parse the metadata string to a Date object

            System.out.println("DATE : " + date);

            // Convert Date to milliseconds since the epoch
            long milliseconds = date.getTime();

            // Modify the "created date" metadata of the file
            Path filePath = file.toPath();
            BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
            BasicFileAttributes fileAttributes = attributes.readAttributes();
            FileTime createdTime = FileTime.fromMillis(milliseconds);

            attributes.setTimes(createdTime, fileAttributes.lastModifiedTime(), fileAttributes.creationTime());

            System.out.println("Updated metadata");
        }
    }
}



