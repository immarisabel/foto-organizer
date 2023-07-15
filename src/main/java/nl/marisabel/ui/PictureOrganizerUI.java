package nl.marisabel.ui;

import nl.marisabel.Organize;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * nl.marisabel.UI CLASS
 */
public class PictureOrganizerUI extends JFrame {
 private JTextField sourceFolderField;
 private JTextField destinationFolderField;

 public PictureOrganizerUI() {
  setTitle("Picture Organizer");
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setSize(400, 150);
  setLocationRelativeTo(null);

  // Create the main panel
  JPanel mainPanel = new JPanel();
  mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

  // Create labels
  JLabel sourceLabel = new JLabel("Source Folder:");
  JLabel destinationLabel = new JLabel("Destination Folder:");

  // Create text fields
  sourceFolderField = new JTextField();
  destinationFolderField = new JTextField();

  // Create button
  JButton organizeButton = new JButton("Organize");
  organizeButton.addActionListener(new ActionListener() {



   @Override
   public void actionPerformed(ActionEvent e) {
    String sourceFolder = sourceFolderField.getText();
    String destinationFolder = destinationFolderField.getText();
    Organize organize = new Organize();
    organize.organizePhotos(sourceFolder, destinationFolder);
   }
  });

  // Add components to the main panel
  mainPanel.add(sourceLabel);
  mainPanel.add(sourceFolderField);
  mainPanel.add(destinationLabel);
  mainPanel.add(destinationFolderField);
  mainPanel.add(new JLabel()); // Empty label for spacing
  mainPanel.add(organizeButton);

  // Add the main panel to the frame
  add(mainPanel);
 }

}
