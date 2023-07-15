package nl.marisabel.images;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

public class ModifyCreatedDate {

 private static final Logger log = Logger.getLogger(ModifyCreatedDate.class.getName());

 public void modify(File file, String dateTime) throws IOException {
  Path filePath = file.toPath();
  BasicFileAttributeView attributeView = Files.getFileAttributeView(filePath, BasicFileAttributeView.class);
  BasicFileAttributes attributes = attributeView.readAttributes();
  FileTime originalCreationTime = attributes.creationTime();

  try {
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
   Date takenDate = dateFormat.parse(dateTime);
   log.info("TAKEN DATE: " + takenDate);
   // Extract only the characters needed from the date string
   SimpleDateFormat modifiedDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

   String modifiedDate = modifiedDateFormat.format(takenDate);

   FileTime newCreationTime = FileTime.fromMillis(modifiedDateFormat.parse(modifiedDate).getTime());

   log.info("MODIFIED DATE: " + modifiedDate);

   attributeView.setTimes(newCreationTime, newCreationTime, originalCreationTime);



  } catch (Exception e) {
   log.warning("Error modifying created date: " + e.getMessage());
  }
 }
}
