package nl.marisabel;


import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.whatsappImages.WhatsAppPhotosProcessor;

public class TerminalApp {

 private static final String SRC_FOLDER = "C:\\Users\\Administrator\\Desktop\\PHOTOS to BU";
 private static final String DEST_FOLDER = "C:\\Users\\Administrator\\Desktop\\PHOTOS to BU\\ORGANIZED";

 /**
  * nl.marisabel.TerminalApp for just running in the terminal instead of nl.marisabel.UI
  */
 public static void main(String[] args) {

//  OrganizePhotos organizePhotos = new OrganizePhotos();
//  organizePhotos.organizePhotos(SRC_FOLDER, DEST_FOLDER);
  WhatsAppPhotosProcessor whatsAppPhotosProcessor = new WhatsAppPhotosProcessor();
  whatsAppPhotosProcessor.scanFiles(SRC_FOLDER);
 }
}
