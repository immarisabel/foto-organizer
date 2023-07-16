package nl.marisabel.whatsappImages;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputField;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WhatsAppPhotosProcessor {

    private static WhatsAppImageMetadataExtractor metadataExtractor;

    public WhatsAppPhotosProcessor() {
        metadataExtractor = new WhatsAppImageMetadataExtractor();
    }

    public void scanFiles(String folderPath) {
        File folder = new File(folderPath);

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


    private static void processFile(File file) {
        String metadata = metadataExtractor.getWhatsAppMetadata(file);
        if (metadata != null) {
            // Get the current date and time
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDate = dateFormat.format(new Date());

            // Update the created and modified date in the metadata string
            metadata = metadata.replaceAll("Created: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "Created: " + currentDate);
            metadata = metadata.replaceAll("Modified: \\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}", "Modified: " + currentDate);

            // Update the file's metadata
            Path filePath = file.toPath();
            BasicFileAttributeView attributes = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
            FileTime currentTime = FileTime.fromMillis(System.currentTimeMillis());

            try {
                Files.write(filePath, metadata.getBytes()); // Update the file content
                attributes.setTimes(currentTime, currentTime, currentTime); // Update the file's created, modified, and access times
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("updated metadata");
    }



}