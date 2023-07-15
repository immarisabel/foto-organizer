public class TerminalApp {

 private static final String SRC_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP";
 private static final String DEST_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP\\ORGANIZED";


 public static void main(String[] args) {

  FileOrganizer fileOrganizer = new FileOrganizer();
  fileOrganizer.organizePhotos(SRC_FOLDER, DEST_FOLDER);

 }
}
