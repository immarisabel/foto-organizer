package nl.marisabel.ui;

import nl.marisabel.backup.FolderBackup;
import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.images.PhotosProcessor;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PictureOrganizerUI extends JFrame {


 private static Logger log = Logger.getLogger(PictureOrganizerUI.class.getName());

 private JTextArea logTextArea;


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
  setSize(800, 600);
  setLocationRelativeTo(null);

  JPanel mainPanel = createMainPanel();
  getContentPane().setBackground(Color.WHITE);
  add(mainPanel);
 }



 private JPanel createMainPanel() {
  JPanel mainPanel = new JPanel();
  mainPanel.setLayout(new BorderLayout()); // Use BorderLayout for arranging components
  mainPanel.setBackground(Color.white);
  mainPanel.setBorder(new EmptyBorder(10,10,10,10));

  JPanel leftPanel = createLeftPanel(); // Existing left panel with other components
  JPanel logPanel = createLogPanel(); // New log panel

  // Create a JSplitPane to split the main panel into left and right panels
  JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, logPanel);
  splitPane.setResizeWeight(.1); // Set the initial width ratio of the left and right panels
  splitPane.setBorder(null); // Set the border of JSplitPane to null to remove the lines
  splitPane.setDividerSize(0); // Set the divider size to 0 to remove the divider line

  mainPanel.add(splitPane, BorderLayout.CENTER); // Add the split pane to the main panel

  return mainPanel;
 }

 private JPanel createLogPanel() {
  JPanel logPanel = new JPanel();
  logPanel.setLayout(new BorderLayout());
  logPanel.setBackground(Color.white);
  logPanel.setBorder(BorderFactory.createTitledBorder("Logs"));

  logTextArea = new JTextArea();
  logTextArea.setEditable(false);

  JScrollPane scrollPane = new JScrollPane(logTextArea);
  scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

  logPanel.add(scrollPane, BorderLayout.CENTER);

  // Redirect System.out and System.err to the JTextArea
  PrintStream printStream = new PrintStream(new CustomOutputStream(logTextArea));
  System.setOut(printStream);
  System.setErr(printStream);

  return logPanel;
 }

 private JPanel createLeftPanel() {
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
  mainPanel.add(Box.createVerticalStrut(15)); // Vertical spacing

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

  whatsappButton.addActionListener(e -> {

   String whatsappSourceFolder = whatsappSourceFolderField.getText();
   logsAppend("BACKING UP FILES", true);

   boolean backup = backUpAllPicturesBeforeProcessing(whatsappSourceFolder);
   if (backup) {
    showAlert("Backup done!");
    logsAppend("BACKUP COMPLETED", true);
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
    showAlert("Update WhatsApp Metadata: WhatsApp metadata updated successfully!");
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

 private JPanel createOrganizePanel() {
  JPanel organizationPanel = new JPanel();
  organizationPanel.setLayout(new BoxLayout(organizationPanel, BoxLayout.Y_AXIS));
  organizationPanel.setBorder(BorderFactory.createTitledBorder("Organize Pictures"));
  organizationPanel.setBackground(Color.white);
  organizationPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

  // Initialize progress bar
  CustomProgressBar organizeProgressBar = new CustomProgressBar();
  organizeProgressBar.setComplementColor(Color.LIGHT_GRAY); // Set the complement color
  organizeProgressBar.setProgressColor(PRIMARY_COLOR); // Set the progress color
  // Set the image for the progress bar (Optional, you can use null if you don't have any image)
  ImageIcon progressBarImage = new ImageIcon(PIXIE_SORT_ICON.getImage()); // Replace "path_to_image.png" with your actual image path
  organizeProgressBar.setProgressBarImage(progressBarImage);
  // Set the image for the progress cursor (Optional, you can use null if you don't have any image)
  ImageIcon progressCursorImage = new ImageIcon(PIXIE_SORT_ICON.getImage()); // Replace "path_to_cursor_image.png" with your actual image path
  organizeProgressBar.setCursorImage(progressCursorImage);
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

  // Add ActionListener to the button
  organizeButton.addActionListener(e -> {
   String sourceFolder = sourceFolderField.getText();
   String destinationFolder = destinationFolderField.getText();
   boolean backup = backUpAllPicturesBeforeProcessing(sourceFolder);
   if (backup) {
    showAlert("Backup done!");
   }
   boolean success = processPictures(sourceFolder, destinationFolder, organizeProgressBar);
   if (success) {
    showAlert("Organize Pictures: Pictures organized successfully!");
   }
  });

  // Add components to the organizationPanel
//  organizationPanel.add(organizeProgressBar);
//  organizationPanel.add(Box.createVerticalStrut(5)); // Add some vertical spacing
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


 // Helper method to create a JDialog with the backup progress bar
 private JDialog createBackupDialog(CustomProgressBar backupProgressBar) {
  JDialog dialog = new JDialog();
  dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  dialog.setSize(300, 100);
  dialog.setResizable(false);
  dialog.setTitle("Backup Progress");
  dialog.setLayout(new BorderLayout());
  dialog.add(backupProgressBar, BorderLayout.CENTER);
  dialog.setLocationRelativeTo(null); // Center the dialog on the screen
  dialog.setVisible(true);
  return dialog;
 }


 // HELPER METHODS

 private void showAlert(String message) {
  JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
 }

 private int getTotalFiles(String sourceFolder) {
  try {
   return (int) Files.walk(Paths.get(sourceFolder))
           .filter(Files::isRegularFile)
           .count();
  } catch (IOException e) {
   e.printStackTrace();
   return -1;
  }
 }
 private void logsAppend(String logs, boolean isHeader) {
  if (isHeader) {
   System.out.println("[======" + logs + "======]");
  } else {
   System.out.println(logs);  }
 }


 // Proceses

 private boolean backUpAllPicturesBeforeProcessing(String sourceFolder) {
  FolderBackup folderBackup = new FolderBackup();
  logsAppend("backing up " + getTotalFiles(sourceFolder) + " files....", false);
  folderBackup.backUpAllFiles(sourceFolder);
  return true;
 }


 private boolean processWhatsAppPictures(String sourceFolder) throws ParseException, IOException {
  WhatsAppPhotosMetadataUpdater photosProcessor = new WhatsAppPhotosMetadataUpdater();
  photosProcessor.scanFiles(sourceFolder);
  return true;
 }


 private boolean processPictures(String sourceFolder, String destinationFolder, CustomProgressBar organizeProgressBar) {
  OrganizePhotos process = new OrganizePhotos();
  process.organizePhotos(sourceFolder, destinationFolder);
  return true;
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
