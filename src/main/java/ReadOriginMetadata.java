import java.io.File;
import java.io.IOException;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffField;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

public class ReadOriginMetadata {

 public static void main(String[] args) {
  String folderPath = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP"; // Replace with the actual folder path

  File folder = new File(folderPath);
  File[] files = folder.listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.isFile()) {
     try {
      processImage(file);
     } catch (IOException | ImageReadException e) {
      e.printStackTrace();
     }
    }
   }
  }
 }

 private static void processImage(File imageFile) throws IOException, ImageReadException {
  System.out.println("Processing: " + imageFile.getName());

  ImageMetadata metadata = Imaging.getMetadata(imageFile);
  if (metadata instanceof JpegImageMetadata) {
   JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;
   TiffImageMetadata exif = jpegMetadata.getExif();

   if (exif != null) {
    TiffField field = exif.findField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);
    if (field != null) {
     String dateTime = field.getStringValue();
     System.out.println("Taken Date: " + dateTime);
    }
   }
  }

  System.out.println("------------------------------------");
 }
}
