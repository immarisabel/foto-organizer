public class Main {

 private static final String SRC_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP";
 private static final String DEST_FOLDER = "C:\\Users\\phaer\\Desktop\\TEST FOTOS APP\\ORGANIZED";


 public static void main(String[] args) {

  ReadOriginMetadata readMetadata = new ReadOriginMetadata();
  readMetadata.readMetadata(SRC_FOLDER);

  FileOrganizer fileOrganizer = new FileOrganizer(readMetadata);
  fileOrganizer.organizePhotos(SRC_FOLDER, DEST_FOLDER);

 }
}
