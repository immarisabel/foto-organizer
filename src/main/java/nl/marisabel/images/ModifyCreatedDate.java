package nl.marisabel.images;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ModifyCreatedDate {
 private static final Logger log = Logger.getLogger(ModifyCreatedDate.class.getName());

 public void modify(File file, String dateTime) {
  try {
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
   Date newCreationDate = dateFormat.parse(dateTime);

   Path filePath = file.toPath();
   FileTime newCreationTime = FileTime.fromMillis(newCreationDate.getTime());

   Files.setAttribute(filePath, "basic:creationTime", newCreationTime);

   log.info("Modified creation time of the file: " + file.getAbsolutePath());
  } catch (ParseException | IOException e) {
   log.warning("Error modifying created date: " + e.getMessage());
  }
 }
}
