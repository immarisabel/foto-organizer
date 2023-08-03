package nl.marisabel.ui.panels.processPanels;

import nl.marisabel.ui.utils.AllUtils;
import nl.marisabel.ui.utils.BackupPictures;
import nl.marisabel.ui.customElements.*;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;

public class UpdateWhatsAppMetadataPanel {

 private BackupPictures backupPictures;
 private AllUtils utils;
 private Elements elements;


 public JPanel createWhatsAppPanel() {
  JPanel whatsAppProcessingPanel = new JPanel();
  whatsAppProcessingPanel.setLayout(new BoxLayout(whatsAppProcessingPanel, BoxLayout.Y_AXIS));
  whatsAppProcessingPanel.setBorder(BorderFactory.createTitledBorder("WhatsApp Pictures"));
  whatsAppProcessingPanel.setBackground(Color.white);
  whatsAppProcessingPanel.setBorder(new EmptyBorder(10, 10, 10, 10)); // Add margin to the panel

  // Initialize WhatsApp progress bar
  JProgressBar whatsappProgressBar = new JProgressBar(0, 100);
  whatsappProgressBar.setStringPainted(true); // Display progress percentage text

  // WhatsApp source folder field
  PlaceHolderTextField whatsappSourceFolderField = new PlaceHolderTextField();
  whatsappSourceFolderField.setPlaceholder("WhatsApp Source Folder");
  whatsappSourceFolderField.setBackground(elements.colors.FIELDS_COLOR); // Set the background color for fields
  whatsappSourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border

  // WhatsApp button
  CustomFlatButton whatsappButton = new CustomFlatButton("WhatsApp", elements.colors.SECONDARY_COLOR, Color.WHITE);
  whatsappButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

  whatsappButton.addActionListener(e -> {

   String whatsappSourceFolder = whatsappSourceFolderField.getText();
   utils.logs.logsAppend("BACKING UP FILES", true);

   boolean backup = backupPictures.backUpAllPicturesBeforeProcessing(whatsappSourceFolder);
   if (backup) {
    utils.alerts.showAlert("Backup done!");
    utils.logs.logsAppend("BACKUP COMPLETED", true);
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
    utils.alerts.showAlert("Update WhatsApp Metadata: WhatsApp metadata updated successfully!");
   }
  });

  // Add components to the whatsappPanel
//  whatsAppProcessingPanel.add(whatsappProgressBar);
//  whatsAppProcessingPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  whatsAppProcessingPanel.add(whatsappSourceFolderField);
  whatsAppProcessingPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  whatsAppProcessingPanel.add(whatsappButton);

  return whatsAppProcessingPanel;
 }

 private boolean processWhatsAppPictures(String sourceFolder) throws ParseException, IOException {
  WhatsAppPhotosMetadataUpdater photosProcessor = new WhatsAppPhotosMetadataUpdater();
  photosProcessor.scanFiles(sourceFolder);
  return true;
 }


}
