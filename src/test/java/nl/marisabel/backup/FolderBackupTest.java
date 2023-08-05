package nl.marisabel.backup;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class FolderBackupTest {

 private static final String SOURCE_FOLDER = "src/test/resources/source_folder";
 private static final String NON_EXISTENT_FOLDER = "src/test/resources/non_existent_folder";

 @BeforeAll
 public static void setUp() {
  // Create a temporary source folder and files for testing
  File sourceFolder = new File(SOURCE_FOLDER);
  sourceFolder.mkdirs();
  try {
   new File(sourceFolder, "file1.txt").createNewFile();
   new File(sourceFolder, "file2.txt").createNewFile();
   File subFolder = new File(sourceFolder, "subfolder");
   subFolder.mkdir();
   new File(subFolder, "file3.txt").createNewFile();
  } catch (IOException e) {
   e.printStackTrace();
  }
 }

 @AfterAll
 public static void tearDown() {
  // Clean up the temporary source folder and files after testing
  File sourceFolder = new File(SOURCE_FOLDER);
  deleteFolder(sourceFolder);
 }

 @Test
 public void testBackUpAllFiles_ValidSourceFolder_BackupCreated() {
  FolderBackup folderBackup = new FolderBackup();
  folderBackup.backUpAllFiles(SOURCE_FOLDER);
  // Check if the backup folder is created successfully
  File backupFolder = new File(System.getProperty("user.dir") + "/backups");
  assertTrue(backupFolder.exists());
 }

 @Test
 public void testBackUpAllFiles_InvalidSourceFolder_BackupFailed() {
  FolderBackup folderBackup = new FolderBackup();
  // Redirect System.err to a custom PrintStream
  ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  PrintStream originalErr = System.err;
  System.setErr(new PrintStream(outContent));

  folderBackup.backUpAllFiles(NON_EXISTENT_FOLDER);

  // Restore the original System.err
  System.setErr(originalErr);

  // Check if the error message is printed correctly
  String errorMessage = "[!] Source folder does not exist or is not a directory.";
  String consoleOutput = outContent.toString();
  assertEquals(errorMessage, consoleOutput.trim(), "Expected error message: " + errorMessage + "\nActual output: " + consoleOutput);
 }


 @Test
 public void testCreateBackupFolder_ValidSourceFolder_BackupFolderCreated() {
  String backupFolderPath = FolderBackup.createBackupFolder(SOURCE_FOLDER);
  // Check if the backup folder is created successfully
  assertNotNull(backupFolderPath);
  File backupFolder = new File(backupFolderPath);
  assertTrue(backupFolder.exists());
 }

 @Test
 public void testCreateBackupFolder_NonExistentSourceFolder_NullReturned() {
  String backupFolderPath = FolderBackup.createBackupFolder(NON_EXISTENT_FOLDER);
  // Check if null is returned when source folder doesn't exist
  assertNull(backupFolderPath);
 }

 // Utility method to delete a folder and its contents
 private static void deleteFolder(File folder) {
  File[] files = folder.listFiles();
  if (files != null) {
   for (File file : files) {
    if (file.isDirectory()) {
     deleteFolder(file);
    } else {
     file.delete();
    }
   }
  }
  folder.delete();
 }
}
