package nl.marisabel;

import nl.marisabel.ui.PictureOrganizerUI;

import javax.swing.*;

public class MainApp {
 /**
  * Given path is processed by nl.marisabel.images.ProcessImage.java and Organized by nl.marisabel.UI.Organize.java
  */
 public static void main(String[] args) {
  SwingUtilities.invokeLater(new Runnable() {
   public void run() {
    PictureOrganizerUI app = new PictureOrganizerUI();
    app.setVisible(true);
   }
  });
 }
}

//TODO only read PNG & JPG : IllegalArgumentException -> then skip file
//Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Unknown Extension: IMG_0010.MOV
