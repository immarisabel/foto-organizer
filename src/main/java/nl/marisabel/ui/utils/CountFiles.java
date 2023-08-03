package nl.marisabel.ui.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CountFiles {
 public int getTotalFiles(String sourceFolder) {
  try {
   return (int) Files.walk(Paths.get(sourceFolder))
           .filter(Files::isRegularFile)
           .count();
  } catch (IOException e) {
   e.printStackTrace();
   return -1;
  }
 }
}
