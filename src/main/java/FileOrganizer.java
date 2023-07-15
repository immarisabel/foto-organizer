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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.logging.Logger;

public class FileOrganizer {
 private static final Pattern DATE_PATTERN = Pattern.compile("your_date_pattern");

 static Logger log = Logger.getLogger(FileOrganizer.class.getName());




 public void organizePhotos(String fromFolder, String toFolder) {
  List<String> fileNames = new ArrayList<>();
  File folder = new File(fromFolder);
  File[] files = folder.listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.isFile()) {
     try {
      processImage(file, toFolder);
      fileNames.add(String.valueOf(file));
     } catch (IOException | ImageReadException e) {
      e.printStackTrace();
     }
    }
   }
  }
  log.info("TOTAL FILES PROCESSED: " + String.valueOf(fileNames.size()));
 }

 private static void processImage(File imageFile, String toFolder) throws IOException, ImageReadException {

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

      // Move the image file to the destination folder
      Path destinationFile = destinationFolder.resolve(imageFile.getName());
      Files.move(imageFile.toPath(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
     }
    }
   }
  }
 }
}
