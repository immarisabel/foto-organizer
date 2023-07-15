package nl.marisabel;


import  nl.marisabel.ui.*;
import javax.swing.*;

public class MainApp {

 public static void main(String[] args) {
  SwingUtilities.invokeLater(new Runnable() {
   public void run() {
    PictureOrganizerUI pictureOrganizerUI = new PictureOrganizerUI();
    pictureOrganizerUI.setVisible(true);
   }
  });
 }
}

//TODO only read PNG & JPG : IllegalArgumentException -> then skip file
//Exception in thread "AWT-EventQueue-0" java.lang.IllegalArgumentException: Unknown Extension: IMG_0010.MOV
