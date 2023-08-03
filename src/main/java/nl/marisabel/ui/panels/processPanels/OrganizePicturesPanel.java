package nl.marisabel.ui.panels.processPanels;

import nl.marisabel.photos.OrganizePhotos;
import nl.marisabel.ui.utils.AllUtils;
import nl.marisabel.ui.utils.BackupPictures;
import nl.marisabel.ui.customElements.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class OrganizePicturesPanel {

 private BackupPictures backupPictures;
 private AllUtils utils;
 private Elements elements;

 public JPanel createOrganizePanel(BackupPictures backupPictures, Elements elements, AllUtils utils) {
  this.elements = elements;
  this.backupPictures = backupPictures;
  this.utils = utils;

  JPanel organizationPanel = new JPanel();
  organizationPanel.setLayout(new BoxLayout(organizationPanel, BoxLayout.Y_AXIS));
  organizationPanel.setBorder(BorderFactory.createTitledBorder("Organize Pictures"));
  organizationPanel.setBackground(Color.white);
  organizationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

  // Initialize progress bar
  CustomProgressBar organizeProgressBar = new CustomProgressBar();
  organizeProgressBar.setComplementColor(Color.LIGHT_GRAY); // Set the complement color
  organizeProgressBar.setProgressColor(elements.colors.PRIMARY_COLOR); // Set the progress color
  // Set the image for the progress bar (Optional, you can use null if you don't have any image)
  ImageIcon progressBarImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_image.png" with your actual image path
  organizeProgressBar.setProgressBarImage(progressBarImage);
  // Set the image for the progress cursor (Optional, you can use null if you don't have any image)
  ImageIcon progressCursorImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_cursor_image.png" with your actual image path
  organizeProgressBar.setCursorImage(progressCursorImage);
  // Set the image for the progress end (Optional, you can use null if you don't have any image)
  ImageIcon progressEndImage = new ImageIcon(elements.images.PIXIE_SORT_ICON.getImage()); // Replace "path_to_end_image.png" with your actual image path
  organizeProgressBar.setEndImage(progressEndImage);

  // Source and Destination folders fields
  PlaceHolderTextField sourceFolderField = new PlaceHolderTextField();
  sourceFolderField.setPlaceholder("Source Folder");
  sourceFolderField.setBackground(elements.colors.FIELDS_COLOR); // Set the background color for fields
  sourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border
  PlaceHolderTextField destinationFolderField = new PlaceHolderTextField();
  destinationFolderField.setPlaceholder("Destination Folder");
  destinationFolderField.setBackground(elements.colors.FIELDS_COLOR); // Set the background color for fields
  destinationFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border

  // Button
  CustomFlatButton organizeButton = new CustomFlatButton("Organize", elements.colors.PRIMARY_COLOR, Color.WHITE);
  organizeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

  // Add ActionListener to the button
  organizeButton.addActionListener(e -> {
   String sourceFolder = sourceFolderField.getText();
   String destinationFolder = destinationFolderField.getText();

   // Perform backup asynchronously using SwingWorker
   SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
    @Override
    protected Void doInBackground() throws Exception {
     boolean backup = backupPictures.backUpAllPicturesBeforeProcessing(sourceFolder);
     if (backup) {
      publish("BACKUP COMPLETED");
     }
     boolean success = processPictures(sourceFolder, destinationFolder, organizeProgressBar);
     if (success) {
      publish("Organize Pictures: Pictures organized successfully!");
     }
     return null;
    }

    @Override
    protected void process(List<String> chunks) {
     // Update the log messages live as they are published
     for (String message : chunks) {
      utils.logs.logsAppend(message, false);
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

  organizationPanel.add(sourceFolderField);
  organizationPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  organizationPanel.add(destinationFolderField);
  organizationPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
  organizationPanel.add(organizeButton);

  return organizationPanel;
 }

 public boolean processPictures(String sourceFolder, String destinationFolder, CustomProgressBar organizeProgressBar) {
  OrganizePhotos process = new OrganizePhotos();
  process.organizePhotos(sourceFolder, destinationFolder);
  return true;
 }

}
