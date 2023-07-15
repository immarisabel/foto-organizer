import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;

public class FileOrganizer {
 private static final String SRC_FOLDER = "source_folder";
 private static final String DEST_FOLDER = "destination_folder";
 private static final Pattern DATE_PATTERN = Pattern.compile("your_date_pattern");

 public static void main(String[] args) {
  try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(SRC_FOLDER))) {
   for (Path path : directoryStream) {
    if (Files.isRegularFile(path)) {
     BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
     LocalDateTime creationTime = LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault());

     String fileName = path.getFileName().toString();
     Matcher matcher = DATE_PATTERN.matcher(fileName);
     if (matcher.find()) {
      LocalDateTime fileTime = LocalDateTime.parse(matcher.group(), DateTimeFormatter.ofPattern("your_date_format"));
      if (fileTime.isBefore(creationTime)) {
       creationTime = fileTime;
      }
     }

     FileUtils.touch(path.toFile());
     Files.setAttribute(path, "lastModifiedTime", FileTime.fromMillis(creationTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));

     Path destDir = Paths.get(DEST_FOLDER, String.valueOf(creationTime.getYear()), String.valueOf(creationTime.getMonthValue()));
     Files.createDirectories(destDir);

     Files.move(path, destDir.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
    }
   }
  } catch (IOException e) {
   e.printStackTrace();
  }
 }
}