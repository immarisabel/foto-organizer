package nl.marisabel.ui;

import nl.marisabel.backup.FolderBackup;
import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;
import java.util.List;


public class PictureOrganizerUI extends JFrame {
 private JTextField sourceFolderField;
 private JTextField destinationFolderField;
 private JTextField whatsappSourceFolderField;
 private JLabel infoLabel;
 private JProgressBar organizeProgressBar;
 private JProgressBar whatsappProgressBar;

 // ICONS
 private static final ImageIcon PIXIE_SORT_ICON = new ImageIcon(Objects.requireNonNull(PictureOrganizerUI.class.getResource("/icons/pixiesort_icon.png")));
 private static final ImageIcon PIXIE_SORT_HEADER = new ImageIcon(Objects.requireNonNull(PictureOrganizerUI.class.getResource("/images/pixiesort_header.png")));


 // COLORS

 private static final Color PRIMARY_COLOR = new Color(16, 124, 254);
 private static final Color SECONDARY_COLOR = new Color(232, 77, 144);
 private static final Color FIELDS_COLOR = new Color(255, 255, 209);

 public PictureOrganizerUI() throws IOException {
  initializeUI();
 }

 private void initializeUI() throws IOException {
  setTitle("PixieSort");
  setIconImage(PIXIE_SORT_ICON.getImage());
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setSize(500, 700);
  setLocationRelativeTo(null);

  JPanel mainPanel = createMainPanel();
  getContentPane().setBackground(Color.WHITE);
  add(mainPanel);
 }

 private JPanel createMainPanel() {
  JPanel mainPanel = new JPanel();
  mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
  mainPanel.setBackground(Color.white);
  mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

  JPanel headerPanel = createHeaderPanel();
  JPanel warningPanel = createWarningPanel();

  mainPanel.add(headerPanel);
  mainPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
  mainPanel.add(warningPanel);
  mainPanel.add(Box.createVerticalStrut(20)); // Empty notification panel

  JPanel organizePanel = createOrganizePanel();
  mainPanel.add(organizePanel);
  mainPanel.add(Box.createVerticalStrut(20)); // Vertical spacing

  JPanel whatsappPanel = createWhatsAppPanel();
  mainPanel.add(whatsappPanel);

  JPanel footerPanel = new JPanel(); // Placeholder for the footer panel, add any extra info here if needed
  footerPanel.setBackground(Color.white);
  footerPanel.setPreferredSize(new Dimension(10, 50));

  mainPanel.add(footerPanel);

  return mainPanel;
 }

 private JPanel createWhatsAppPanel() {
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
  whatsappSourceFolderField.setBackground(FIELDS_COLOR); // Set the background color for fields
  whatsappSourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border

  // WhatsApp button
  CustomFlatButton whatsappButton = new CustomFlatButton("WhatsApp", SECONDARY_COLOR, Color.WHITE);
  whatsappButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

  // Add components to the whatsappPanel
  whatsAppProcessingPanel.add(whatsappProgressBar);
  whatsAppProcessingPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  whatsAppProcessingPanel.add(whatsappSourceFolderField);
  whatsAppProcessingPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
  whatsAppProcessingPanel.add(whatsappButton);

  return whatsAppProcessingPanel;
 }


 private JPanel createOrganizePanel() {
  JPanel organizationPanel = new JPanel();
  organizationPanel.setLayout(new BoxLayout(organizationPanel, BoxLayout.Y_AXIS));
  organizationPanel.setBorder(BorderFactory.createTitledBorder("Organize Pictures"));
  organizationPanel.setBackground(Color.white);
  organizationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

  // Initialize progress bar
  CustomProgressBar organizeProgressBar = new CustomProgressBar();
  organizeProgressBar.setColorComplemento(Color.LIGHT_GRAY); // Set the complement color
  organizeProgressBar.setColorProgreso(PRIMARY_COLOR); // Set the progress color
  // Set the image for the progress bar (Optional, you can use null if you don't have any image)
  ImageIcon progressBarImage = new ImageIcon(PIXIE_SORT_ICON.getImage()); // Replace "path_to_image.png" with your actual image path
  organizeProgressBar.setImageBar(progressBarImage);
  // Set the image for the progress cursor (Optional, you can use null if you don't have any image)
  ImageIcon progressCursorImage = new ImageIcon(PIXIE_SORT_ICON.getImage()); // Replace "path_to_cursor_image.png" with your actual image path
  organizeProgressBar.setImageCursor(progressCursorImage);
  // Set the image for the progress end (Optional, you can use null if you don't have any image)
  ImageIcon progressEndImage = new ImageIcon(PIXIE_SORT_ICON.getImage()); // Replace "path_to_end_image.png" with your actual image path
  organizeProgressBar.setEndImage(progressEndImage);

  // Source and Destination folders fields
  PlaceHolderTextField sourceFolderField = new PlaceHolderTextField();
  sourceFolderField.setPlaceholder("Source Folder");
  sourceFolderField.setBackground(FIELDS_COLOR); // Set the background color for fields
  sourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border
  PlaceHolderTextField destinationFolderField = new PlaceHolderTextField();
  destinationFolderField.setPlaceholder("Destination Folder");
  destinationFolderField.setBackground(FIELDS_COLOR); // Set the background color for fields
  destinationFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, 10)); // Set the rounded border

  // Button
  CustomFlatButton organizeButton = new CustomFlatButton("Organize", PRIMARY_COLOR, Color.WHITE);
  organizeButton.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the button horizontally

  // Add components to the organizePanel
  organizationPanel.add(organizeProgressBar);
  organizationPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  organizationPanel.add(sourceFolderField);
  organizationPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
  organizationPanel.add(destinationFolderField);
  organizationPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
  organizationPanel.add(organizeButton);

  return organizationPanel;
 }


 private JPanel createHeaderPanel() {
  JPanel headerPanel = new JPanel();
  headerPanel.setBackground(Color.white);
  headerPanel.setLayout(new BorderLayout());

  JLabel headerLabel = new JLabel(PIXIE_SORT_HEADER);
  headerPanel.add(headerLabel, BorderLayout.CENTER);

  return headerPanel;
 }

 private JPanel createWarningPanel() {
  JPanel warningPanel = new JPanel();
  warningPanel.setBackground(Color.white);
  JLabel warningLabel = new JLabel("before processing, a backup will be created");
  warningPanel.add(warningLabel);
  return warningPanel;
 }



 private boolean backUpAllPicturesBeforeProcessing(String sourceFolder) {

  FolderBackup folderBackup = new FolderBackup();
  folderBackup.backUpAllFiles(sourceFolder);
  return true;
 }

 private boolean processWhatsAppPictures(String sourceFolder) {
  WhatsAppPhotosMetadataUpdater photosProcessor = new WhatsAppPhotosMetadataUpdater();
  try {
   photosProcessor.scanFiles(sourceFolder);
   return true;
  } catch (ParseException | IOException ex) {
   ex.printStackTrace();
   // Handle the exception as needed
   return false;
  }
 }

 private boolean processPictures(String sourceFolder, String destinationFolder) {
  OrganizePhotos organizePhotos = new OrganizePhotos();
  try {
   organizePhotos.organizePhotos(sourceFolder, destinationFolder);
   return true;
  } catch (Exception ex) {
   ex.printStackTrace();
   // Handle the exception as needed
   return false;
  }
 }


 public static void main(String[] args) {
  SwingUtilities.invokeLater(() -> {
   PictureOrganizerUI pictureOrganizerUI = null;
   try {
    pictureOrganizerUI = new PictureOrganizerUI();
   } catch (IOException e) {
    throw new RuntimeException(e);
   }
   pictureOrganizerUI.setVisible(true);
  });
 }
}
