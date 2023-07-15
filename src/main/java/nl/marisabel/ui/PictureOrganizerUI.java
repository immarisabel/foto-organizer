package nl.marisabel.ui;


import nl.marisabel.images.OrganizePhotos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PictureOrganizerUI extends JFrame {
 private JTextField sourceFolderField;
 private JTextField destinationFolderField;

 public PictureOrganizerUI() {
  setTitle("Picture Organizer");
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setSize(400, 200);
  setLocationRelativeTo(null);

  // Create the main panel
  JPanel mainPanel = new JPanel();
  mainPanel.setLayout(new BorderLayout(10, 10));
  mainPanel.setBackground(Color.white);
  mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

  // Create labels panel
  JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
  labelsPanel.setOpaque(false);
  JLabel sourceLabel = new JLabel("Source Folder:");
  JLabel destinationLabel = new JLabel("Destination Folder:");
  sourceLabel.setForeground(Color.gray);
  destinationLabel.setForeground(Color.gray);
  labelsPanel.add(sourceLabel);
  labelsPanel.add(destinationLabel);

  // Create text fields panel
  JPanel fieldsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
  fieldsPanel.setOpaque(false);
  sourceFolderField = new JTextField();
  destinationFolderField = new JTextField();
  fieldsPanel.add(sourceFolderField);
  fieldsPanel.add(destinationFolderField);

  // Create button panel
  JPanel buttonPanel = new JPanel();
  buttonPanel.setOpaque(false);
  JButton organizeButton = new JButton("Organize");
  organizeButton.setBackground(new Color(52, 152, 219));
  organizeButton.setForeground(Color.white);
  organizeButton.setFocusPainted(false);
  organizeButton.addActionListener(new ActionListener() {
   @Override
   public void actionPerformed(ActionEvent e) {
    String sourceFolder = sourceFolderField.getText();
    String destinationFolder = destinationFolderField.getText();
    OrganizePhotos organizePhotos = new OrganizePhotos();
    organizePhotos.organizePhotos(sourceFolder, destinationFolder);
   }
  });
  buttonPanel.add(organizeButton);

  // Add components to the main panel
  mainPanel.add(labelsPanel, BorderLayout.WEST);
  mainPanel.add(fieldsPanel, BorderLayout.CENTER);
  mainPanel.add(buttonPanel, BorderLayout.SOUTH);

  // Set background color of the frame
  getContentPane().setBackground(Color.white);

  // Add the main panel to the frame
  add(mainPanel);
 }




}
