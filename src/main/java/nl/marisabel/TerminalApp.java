package nl.marisabel;


import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.whatsappImages.WhatsAppImageMetadataExtractor;
import nl.marisabel.whatsappImages.WhatsAppPhotosProcessor;

import java.io.File;
import java.text.ParseException;

public class TerminalApp {

 private static final String SRC_FOLDER = "C:\\Users\\phaer\\Desktop\\test fotos organizer";
 private static final String DEST_FOLDER = "C:\\Users\\phaer\\Desktop\\test fotos organizer";

 private static final String FILE_EXAMPLE = "\\IMG-20190410-WA0002.jpg";

 /**
  * nl.marisabel.TerminalApp for just running in the terminal instead of nl.marisabel.UI
  */
 public static void main(String[] args) throws ParseException {

//  OrganizePhotos organizePhotos = new OrganizePhotos();
//  organizePhotos.organizePhotos(SRC_FOLDER, DEST_FOLDER);
//  WhatsAppImageMetadataExtractor metadataExtractor = new WhatsAppImageMetadataExtractor();
//  System.out.println(metadataExtractor.getWhatsAppMetadata(new File(SRC_FOLDER + FILE_EXAMPLE)));

  WhatsAppPhotosProcessor process = new WhatsAppPhotosProcessor();
  process.scanFiles(SRC_FOLDER);

 }
}
