package nl.marisabel;

import nl.marisabel.images.ProcessImage;
import org.apache.commons.imaging.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Organize {

 private ProcessImage processImage;
 static Logger log = Logger.getLogger(ProcessImage.class.getName());

 /**
  * Actively organize the files fromFoldern toFolder in years and months subfolders
  * @param fromFolder
  * @param toFolder
  */
 public void organizePhotos(String fromFolder, String toFolder) {
  List<String> fileNames = new ArrayList<>();
  File folder = new File(fromFolder);
  File[] files = folder.listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.isFile()) {
     try {
      processImage.processImage(file, toFolder);
      fileNames.add(String.valueOf(file));
     } catch (IOException | ImageReadException e) {
      e.printStackTrace();
     }
    }
   }
  }
  log.info("TOTAL FILES PROCESSED: " + String.valueOf(fileNames.size()));
 }
}
