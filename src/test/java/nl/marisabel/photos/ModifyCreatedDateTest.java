package nl.marisabel.photos;

import nl.marisabel.exceptions.ModifyException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

public class ModifyCreatedDateTest {

 private static final String NON_EXISTENT_FILE_PATH = "non_existent_file.txt";

 @Test
 public void testModify_ValidDateTime_ModifiesFileCreationDate() throws IOException, ParseException {
  // Create a temporary file
  File tempFile = File.createTempFile("temp", ".txt");
  tempFile.deleteOnExit();

  // Get the initial creation time of the file
  long initialCreationTime = tempFile.lastModified();

  // Modify the file's creation date
  String dateTime = "2023-08-05 12:34:56"; // Replace this with a valid date
  ModifyCreatedDate modifyCreatedDate = new ModifyCreatedDate();
  modifyCreatedDate.modify(tempFile, dateTime);

  // Get the modified creation time of the file
  long modifiedCreationTime = tempFile.lastModified();

  // Assert that the creation time has been modified
  assertNotEquals(initialCreationTime, modifiedCreationTime);
 }

 @Test
 public void testModify_InvalidDateTime_Format_ExceptionThrown() {
  // Arrange
  ModifyCreatedDate modifier = new ModifyCreatedDate();
  File testFile = new File(NON_EXISTENT_FILE_PATH);

  // Act and Assert
  assertThrows(IOException.class, () -> modifier.modify(testFile, "InvalidDateTime"));
 }

 @Test
 public void testModify_FileDoesNotExist_ExceptionThrown() {
  // Arrange
  ModifyCreatedDate modifier = new ModifyCreatedDate();
  File testFile = new File(NON_EXISTENT_FILE_PATH);

  // Act and Assert
  ModifyException exception = assertThrows(ModifyException.class, () -> modifier.modify(testFile, "2023-08-05 12:34:56"));
  assertNotNull(exception);
  assertEquals("File does not exist.", exception.getMessage());
 }
}
