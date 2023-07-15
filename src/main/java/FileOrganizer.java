import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class FileOrganizer {
 private static final String SRC_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP";
 private static final String DEST_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP\\ORGANIZED";
 private static final Pattern DATE_PATTERN = Pattern.compile("your_date_pattern");
 private static final Pattern ORIGIN_PATTERN = Pattern.compile("ORIGIN:(.*?)\n");

 static Logger log = Logger.getLogger(FileOrganizer.class.getName());

 public static void main(String[] args) {
  try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(SRC_FOLDER))) {
   for (Path path : directoryStream) {
    log.info(path.toString());
    if (Files.isRegularFile(path)) {
     BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
     LocalDateTime creationTime = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());
     log.info("creation time: " + creationTime);
     String fileName = path.getFileName().toString();
     log.info(fileName);
     Matcher matcher = DATE_PATTERN.matcher(fileName);
     if (matcher.find()) {
      LocalDateTime fileTime = LocalDateTime.parse(matcher.group(), DateTimeFormatter.ofPattern("your_date_format"));
      if (fileTime.isBefore(creationTime)) {
       creationTime = fileTime;
       log.info(fileTime.toString());
      }
     }

     String fileContent = FileUtils.readFileToString(path.toFile(), "UTF-8");
     Matcher originMatcher = ORIGIN_PATTERN.matcher(fileContent);
     String originMetadata = "";
     log.info("Origin metadata: " + originMetadata);
     log.info("!!! ORIGIN 1: " + originMatcher);
     if (originMatcher.find()) {
      originMetadata = originMatcher.group(1);
      log.info("!!! ORIGIN 2: " + originMatcher);
     }

     FileUtils.touch(path.toFile());
     Files.setAttribute(path, "lastModifiedTime", FileTime.fromMillis(creationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

     Path destDir = Paths.get(DEST_FOLDER, String.valueOf(creationTime.getYear()), String.valueOf(creationTime.getMonthValue()), originMetadata);
     Files.createDirectories(destDir);

     Files.move(path, destDir.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }
   }
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
}
