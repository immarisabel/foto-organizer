package nl.marisabel;


import nl.marisabel.images.OrganizePhotos;

public class TerminalApp {

 private static final String SRC_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP";
 private static final String DEST_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP\\ORGANIZED";

 /**
  * nl.marisabel.TerminalApp for just running in the terminal instead of nl.marisabel.UI
  */
 public static void main(String[] args) {

  OrganizePhotos organizePhotos = new OrganizePhotos();
  organizePhotos.organizePhotos(SRC_FOLDER, DEST_FOLDER);

 }
}
