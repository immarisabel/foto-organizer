package nl.marisabel.photos;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class PhotosProcessor {
    private static final Logger log = Logger.getLogger(PhotosProcessor.class.getName());
    private static ModifyCreatedDate modifyCreatedDate;

    public PhotosProcessor() {
        modifyCreatedDate = new ModifyCreatedDate();
    }

    /**
     * Main business logic method to process all images from UI
     *
     * @param file             the image .jpg & .png to process
     * @param destinationFolderPath the path of the folder you wish to save to
     * @throws IOException
     * @throws ImageReadException
     */
    public void processImage(File file, String destinationFolderPath) throws IOException, ImageReadException, ParseException {
        // Skip file if not image
        String fileName = file.getName().toLowerCase();
        if (!fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg") && !fileName.endsWith(".png")) {
            log.info("Skipped file: " + fileName + " - Only PNG and JPG files are supported.");
            return;
        }

        ImageMetadata metadata = Imaging.getMetadata(file);

        if (metadata instanceof JpegImageMetadata) {
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
            TiffImageMetadata exif = jpegMetadata.getExif();

            if (exif != null) {
                TiffField field = exif.findField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
                if (field != null) {
                    String dateTime = field.getStringValue();
                    // Modify the "created date" metadata of the file
                    modifyCreatedDate.modify(file, dateTime);

                    // Parse the date and extract year and month
                    String[] dateParts = dateTime.split(":");
                    if (dateParts.length >= 3) {
                        String year = dateParts[0];
                        String month = dateParts[1];

                        // Create the destination folder if it doesn't exist
                        Path destinationFolder = Path.of(destinationFolderPath, year, month);
                        Files.createDirectories(destinationFolder);

                        // Rename the image file with the taken date data
                        String originalFileName = file.getName();
                        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                        String newFileName = generateNewFileName(dateTime, extension);
                        Path destinationFile = destinationFolder.resolve(newFileName);

                        // Skip renaming if the file already exists in the destination folder
                        if (!Files.exists(destinationFile)) {
                            Files.move(file.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
                        } else {
                            log.info("Skipped file: " + originalFileName + " - Already exists in the destination folder.");
                        }
                    }
                }
            }
        }
    }


    /**
     * Generates the new file name according to the taken signature
     *
     * @param dateTime  The original date and time value.
     * @param extension The file extension.
     * @return The formatted date for the name as a String along with the file format.
     */
    private String generateNewFileName(String dateTime, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Date date = parseDate(dateTime);
        String formattedDate = dateFormat.format(date);
        return formattedDate + extension;
    }

    /**
     * Parse the date for the Created and Modified date of the file.
     *
     * @param dateTime The original date and time value.
     * @return The new Date object.
     */
    private Date parseDate(String dateTime) {
        try {
            return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(dateTime);
        } catch (java.text.ParseException e) {
            log.warning("Error parsing date: " + dateTime);
            return new Date();
        }
    }
}
