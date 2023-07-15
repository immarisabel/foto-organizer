package nl.marisabel.images;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class ProcessImage {
 private static final Pattern DATE_PATTERN = Pattern.compile("yyyy:MM:dd HH:mm:ss");

 private static final Logger log = Logger.getLogger(ProcessImage.class.getName());

 public void processImage(File imageFile, String toFolder) throws IOException, ImageReadException {
  ImageMetadata metadata = Imaging.getMetadata(imageFile);

  if (metadata instanceof JpegImageMetadata) {
   JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
   TiffImageMetadata exif = jpegMetadata.getExif();

   if (exif != null) {
    TiffField field = exif.findField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
    if (field != null) {
     String dateTime = field.getStringValue();

     // Parse the date and extract year and month
     String[] dateParts = dateTime.split(":");
     if (dateParts.length >= 3) {
      String year = dateParts[0];
      String month = dateParts[1];

      // Create the destination folder if it doesn't exist
      Path destinationFolder = Path.of(toFolder, year, month);
      Files.createDirectories(destinationFolder);

      // Rename the image file with the taken date data
      String originalFileName = imageFile.getName();
      String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
      String newFileName = generateNewFileName(dateTime, extension);
      Path destinationFile = destinationFolder.resolve(newFileName);

      // Skip renaming if the file already exists in the destination folder
      if (!Files.exists(destinationFile)) {
       Files.move(imageFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
      } else {
       log.info("Skipped file: " + originalFileName + " - Already exists in the destination folder.");
      }
     }
    }
   }
  }
 }

 private String generateNewFileName(String dateTime, String extension) {
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
  Date date = parseDate(dateTime);
  String formattedDate = dateFormat.format(date);
  return formattedDate + extension;
 }

 private Date parseDate(String dateTime) {
  try {
   return new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(dateTime);
  } catch (java.text.ParseException e) {
   log.warning("Error parsing date: " + dateTime);
   return new Date();
  }
 }
}
