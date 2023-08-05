package nl.marisabel.ui.panels.processPanels;

import nl.marisabel.exceptions.ModifyException;
import nl.marisabel.ui.utils.AllUtils;
import nl.marisabel.ui.utils.BackupPictures;
import nl.marisabel.ui.customElements.*;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class UpdateWhatsAppMetadataPanel {

 private BackupPictures backupPictures;
 private AllUtils utils;
 private Elements elements;

 public JPanel createWhatsAppPanel(BackupPictures backupPictures, Elements elements, AllUtils utils) {
  this.elements = elements;
  this.backupPictures = backupPictures;
  this.utils = utils;

  JPanel whatsAppProcessingPanel = new JPanel();
  whatsAppProcessingPanel.setLayout(new BoxLayout(whatsAppProcessingPanel, BoxLayout.Y_AXIS));
  whatsAppProcessingPanel.setBorder(BorderFactory.createTitledBorder("WhatsApp Pictures"));
  whatsAppProcessingPanel.setBackground(Color.white);
  whatsAppProcessingPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add margin to the panel

  // Initialize WhatsApp progress bar
  CustomProgressBar whatsappProgressBar = new CustomProgressBar();
  whatsappProgressBar.setComplementColor(Color.LIGHT_GRAY); // Set the complement color
  whatsappProgressBar.setProgressColor(elements.colors.SECONDARY_COLOR); // Set the progress color
  // Set the image for the progress bar (Optional, you can use null if you don't have any image)
  ImageIcon progressBarImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_image.png" with your actual image path
  whatsappProgressBar.setProgressBarImage(progressBarImage);
  // Set the image for the progress cursor (Optional, you can use null if you don't have any image)
  ImageIcon progressCursorImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_cursor_image.png" with your actual image path
  whatsappProgressBar.setCursorImage(progressCursorImage);
  // Set the image for the progress end (Optional, you can use null if you don't have any image)
  ImageIcon progressEndImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_end_image.png" with your actual image path
  whatsappProgressBar.setEndImage(progressEndImage);

  // WhatsApp source folder field
  PlaceHolderTextField whatsappSourceFolderField = new PlaceHolderTextField();
  whatsappSourceFolderField.setPlaceholder("WhatsApp Source Folder");
  whatsappSourceFolderField.setBackground(elements.colors.FIELDS_COLOR); // Set the background color for fields
  whatsappSourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border

  // WhatsApp button
  CustomFlatButton whatsappButton = new CustomFlatButton("WhatsApp", elements.colors.SECONDARY_COLOR, Color.WHITE);
  whatsappButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

  // Add ActionListener to the button
  whatsappButton.addActionListener(e -> {
   String whatsappSourceFolder = whatsappSourceFolderField.getText();

   // Perform backup asynchronously using SwingWorker
   SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
    @Override
    protected Void doInBackground() throws Exception {
     utils.logs.logsAppend("BACKING UP FILES", true);

     boolean backup = backupPictures.backUpAllPicturesBeforeProcessing(whatsappSourceFolder);
     if (backup) {
      publish("BACKUP COMPLETED");
     }

     boolean success = false;
     try {
      success = processWhatsAppPictures(whatsappSourceFolder);
     } catch (ParseException ex) {
      throw new RuntimeException(ex);
     } catch (IOException ex) {
      throw new RuntimeException(ex);
     }
     if (success) {
      publish("WhatsApp metadata updated successfully!");
     }
     return null;
    }

    @Override
    protected void process(List<String> chunks) {
     // Update the log messages live as they are published
     for (String message : chunks) {
      utils.logs.logsAppend(message, true);
     }
    }

   };

   // Execute the SwingWorker
   worker.execute();

   // Show dots during backup process
   new Thread(() -> {
    try {
     while (!worker.isDone()) {
      Thread.sleep(500); // Change the delay here to control the speed of dots appearing
      System.out.print("  .  ");
     }
    } catch (InterruptedException e1) {
     e1.printStackTrace();
    }
   }).start();
  });

  whatsAppProcessingPanel.add(whatsappSourceFolderField);
  whatsAppProcessingPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  whatsAppProcessingPanel.add(whatsappButton);

  return whatsAppProcessingPanel;
 }

 private boolean processWhatsAppPictures(String sourceFolder) throws ParseException, IOException, ModifyException {
  WhatsAppPhotosMetadataUpdater photosProcessor = new WhatsAppPhotosMetadataUpdater();
  photosProcessor.scanFiles(sourceFolder);
  return true;
 }

}
