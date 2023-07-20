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
        setSize(500, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = createMainPanel();
        getContentPane().setBackground(Color.WHITE);
        add(mainPanel);
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = createHeaderPanel();

        JPanel warningPanel = createWarningPanel();
        JPanel headerAndWarningPanel = new JPanel(new BorderLayout());
        headerAndWarningPanel.setBackground(Color.white);
        headerAndWarningPanel.add(headerPanel, BorderLayout.NORTH);
        headerAndWarningPanel.add(warningPanel, BorderLayout.CENTER);

        // Create a new panel for the progress bars
        JPanel progressBarPanel = new JPanel();
        progressBarPanel.setLayout(new BorderLayout());
        progressBarPanel.setBackground(Color.white);

        // Initialize progress bars
        organizeProgressBar = new JProgressBar(0, 100);
        whatsappProgressBar = new JProgressBar(0, 100);

        // Set progress bar properties
        organizeProgressBar.setStringPainted(true); // Display progress percentage text
        whatsappProgressBar.setStringPainted(true); // Display progress percentage text

        // Add progress bars to the progressBarPanel
        progressBarPanel.add(organizeProgressBar, BorderLayout.NORTH);
        progressBarPanel.add(whatsappProgressBar, BorderLayout.SOUTH);

        // Add the progressBarPanel to the headerAndWarningPanel in the CENTER position
        headerAndWarningPanel.add(progressBarPanel, BorderLayout.CENTER);

        mainPanel.add(headerAndWarningPanel, BorderLayout.NORTH);
        mainPanel.add(createLabelsPanel(), BorderLayout.WEST);
        mainPanel.add(createFieldsPanel(), BorderLayout.CENTER);
        mainPanel.add(createButtonsPanel(), BorderLayout.SOUTH);

        return mainPanel;
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

    private JPanel createLabelsPanel() {
        JPanel labelsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        labelsPanel.setOpaque(false);
        JLabel sourceLabel = new JLabel("Source Folder:");
        JLabel destinationLabel = new JLabel("Destination Folder:");
        JLabel whatsappSourceLabel = new JLabel("WhatsApp Source Folder:");
        sourceLabel.setForeground(Color.gray);
        destinationLabel.setForeground(Color.gray);
        whatsappSourceLabel.setForeground(Color.gray);
        labelsPanel.add(sourceLabel);
        labelsPanel.add(destinationLabel);
        labelsPanel.add(whatsappSourceLabel);
        return labelsPanel;
    }

    private JPanel createFieldsPanel() {
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        fieldsPanel.setOpaque(false);
        sourceFolderField = createFormattedTextField();
        destinationFolderField = createFormattedTextField();
        whatsappSourceFolderField = createFormattedTextField();
        fieldsPanel.add(sourceFolderField);
        fieldsPanel.add(destinationFolderField);
        fieldsPanel.add(whatsappSourceFolderField);
        return fieldsPanel;
    }

    private JTextField createFormattedTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(FIELDS_COLOR);
        int radius = 10; // You can adjust the radius for different roundness
        textField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, radius));
        return textField;
    }

    private JPanel createButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        JPanel progressPanel = new JPanel(new FlowLayout()); // FlowLayout to place progress bars in a row
        progressPanel.setOpaque(false);


        JButton organizeButton = createButton("Organize Pictures", PRIMARY_COLOR);
        organizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sourceFolder = sourceFolderField.getText();
                String destinationFolder = destinationFolderField.getText();

                // Create and execute a SwingWorker for background processing
                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        boolean backUpAllFiles = backUpAllPicturesBeforeProcessing(sourceFolder);

                        for (int i = 0; i <= 100; i += 10) {
                            // Update progress using setProgress()
                            setProgress(i);
                            Thread.sleep(500); // Add a delay to simulate the process time
                        }

                        if (backUpAllFiles) {
                            showAlert("Backup created, just in case!");
                        }

                        processPictures(sourceFolder, destinationFolder);

                        return null;
                    }

                    @Override
                    protected void process(List<Integer> chunks) {
                        // Get the last value from the list (latest progress value)
                        int latestProgress = chunks.get(chunks.size() - 1);
                        // Update the progress bar with the latest progress value
                        organizeProgressBar.setValue(latestProgress);
                    }

                    @Override
                    protected void done() {
                        organizeProgressBar.setValue(0); // Reset progress bar after completion
                        showAlert("pictures organized!");
                    }
                };

                // Bind the progress bar to the SwingWorker
                organizeProgressBar.setValue(0);
                organizeProgressBar.setStringPainted(true); // To show the progress value on the progress bar

                // Execute the SwingWorker
                worker.execute();
            }
        });


        JButton processWhatsAppButton = createButton("Update WhatsApp Metadata", SECONDARY_COLOR);
        processWhatsAppButton.addActionListener(e -> {
            // Start the processing task in a separate thread
            SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                @Override
                protected Void doInBackground() throws Exception {
                    String whatsappSourceFolder = whatsappSourceFolderField.getText();
                    boolean backUpAllFiles = backUpAllPicturesBeforeProcessing(whatsappSourceFolder);

                    for (int i = 0; i <= 100; i += 10) {
                        publish(i); // Update progress
                        Thread.sleep(500); // Add a delay to simulate the process time
                    }

                    if (backUpAllFiles) {
                        showAlert("Backup created, just in case!");
                    }

                    processWhatsAppPictures(whatsappSourceFolder);

                    return null;
                }

                @Override
                protected void process(List<Integer> chunks) {
                    // Check if the list is empty to avoid errors
                    if (!chunks.isEmpty()) {
                        // Loop through all the chunks and update the progress bar with the latest progress value
                        int latestProgress = chunks.get(chunks.size() - 1);
                        whatsappProgressBar.setValue(latestProgress);
                    }
                }

                @Override
                protected void done() {
                    whatsappProgressBar.setValue(0); // Reset progress bar after completion
                    showAlert("Update WhatsApp Metadata: WhatsApp metadata updated successfully!");
                }
            };

            worker.execute(); // Start the SwingWorker
        });


        // Initialize progress bars
        organizeProgressBar = new JProgressBar(0, 100);
        whatsappProgressBar = new JProgressBar(0, 100);

        // Set progress bar properties
        organizeProgressBar.setStringPainted(true); // Display progress percentage text
        whatsappProgressBar.setStringPainted(true); // Display progress percentage text
        organizeProgressBar.setOpaque(false); // Set opaque to false to allow other components to show through
        whatsappProgressBar.setOpaque(false); // Set opaque to false to allow other components to show through

        organizeProgressBar.setVisible(true);
        whatsappProgressBar.setVisible(true);
        // Add buttons to the buttonPanel
        buttonPanel.add(organizeButton);
        buttonPanel.add(processWhatsAppButton);

        // Add progress bars to the progressPanel
        progressPanel.add(organizeProgressBar);
        progressPanel.add(whatsappProgressBar);

        // Add the progressPanel to the buttonPanel
        buttonPanel.add(progressPanel);

        progressPanel.setVisible(true);
        return buttonPanel;
    }

    private JButton createButton(String label, Color backgroundColor) {
        JButton button = new JButton(label);
        button.setToolTipText(label);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    // Show alert dialog with given message
    private void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
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
