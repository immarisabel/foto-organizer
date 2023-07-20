package nl.marisabel;


import nl.marisabel.backup.FolderBackup;
import nl.marisabel.images.PhotosProcessor;
import org.apache.commons.imaging.ImageReadException;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

public class TerminalApp {

 private static final String SRC_FOLDER = "C:\\Users\\Administrator\\Desktop\\test folder";
 private static final String DEST_FOLDER = "C:\\Users\\Administrator\\Desktop\\test folder";

 private static final String FILE_EXAMPLE = "\\IMG-20190410-WA0002.jpg";

 private static final String FILE_BEING_SKIPPED_CASE = "\\PSX_20200926_144152.jpg";
 private final static String TEST_FOLDER = "C:\\Users\\Administrator\\Desktop\\test folder";


 /**
  * nl.marisabel.TerminalApp for just running in the terminal instead of nl.marisabel.UI
  */
 public static void main(String[] args) throws ParseException, IOException, ImageReadException {

//  OrganizePhotos organizePhotos = new OrganizePhotos();
//  organizePhotos.organizePhotos(SRC_FOLDER, DEST_FOLDER);

//  WhatsAppImageMetadataExtractor metadataExtractor = new WhatsAppImageMetadataExtractor();
//  System.out.println(metadataExtractor.getWhatsAppMetadata(new File(SRC_FOLDER + FILE_EXAMPLE)));

//  WhatsAppPhotosProcessor process = new WhatsAppPhotosProcessor();
//  process.scanFiles(SRC_FOLDER);

//  PhotosProcessor processor = new PhotosProcessor();
//  processor.processImage(new File(SRC_FOLDER + FILE_BEING_SKIPPED_CASE),DEST_FOLDER);

  FolderBackup folderBackup = new FolderBackup();
  folderBackup.backUpAllFiles(TEST_FOLDER);

 }
}
