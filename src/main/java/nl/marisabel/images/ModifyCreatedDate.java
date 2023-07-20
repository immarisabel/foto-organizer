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
   Path filePath = file.toPath();
   // TIME MANIPULATION FOR METADATA
   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
   Date metadataDateStamp = dateFormat.parse(dateTime);
   FileTime newMetadataDate = FileTime.fromMillis(metadataDateStamp.getTime());
   // INSERT TIME TO CREATED AND MODIFIED DATE
   Files.setAttribute(filePath, "basic:creationTime", newMetadataDate);
   file.setLastModified(newMetadataDate.toMillis());

   log.info("Modified creation time of the file: " + file.getAbsolutePath());
  } catch (ParseException | IOException e) {
   log.warning("Error modifying created date: " + e.getMessage());
  }
 }
}
