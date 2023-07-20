package nl.marisabel.ui;

import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Objects;


public class PictureOrganizerUI extends JFrame {
    private JTextField sourceFolderField;
    private JTextField destinationFolderField;
    private JTextField whatsappSourceFolderField;
    private JLabel infoLabel;

    // ICONS
    private static final ImageIcon PIXIE_SORT_ICON = new ImageIcon(Objects.requireNonNull(PictureOrganizerUI.class.getResource("/icons/pixiesort_icon.png")));
    private static final ImageIcon PIXIE_SORT_HEADER = new ImageIcon(Objects.requireNonNull(PictureOrganizerUI.class.getResource("/images/pixiesort_header.png")));


    // COLORS

    private static final Color PRIMARY_COLOR = new Color(16, 124, 254);
    private static final Color SECONDARY_COLOR = new Color(232, 77, 144);
    private static final Color FIELDS_COLOR = new Color(255, 255, 209);

    public PictureOrganizerUI() throws IOException {
        setTitle("PixieSort");
        setIconImage(PIXIE_SORT_ICON.getImage());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);


        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));


        // Create the header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(Color.white);
        headerPanel.setLayout(new BorderLayout());

// Add the header image to the header
        JLabel headerLabel = new JLabel(PIXIE_SORT_HEADER);
        headerPanel.add(headerLabel, BorderLayout.CENTER);

// Add the header to the main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);

// Create the warning panel
        JPanel warningPanel = new JPanel();
        warningPanel.setBackground(Color.white);
        JLabel warningLabel = new JLabel("⚠ before processing, a backup will be created");
        warningPanel.add(warningLabel);

// Create a nested panel to hold the header and warning
        JPanel headerAndWarningPanel = new JPanel(new BorderLayout());
        headerAndWarningPanel.setBackground(Color.white);
        headerAndWarningPanel.add(headerPanel, BorderLayout.NORTH);
        headerAndWarningPanel.add(warningPanel, BorderLayout.CENTER);

// Add the header and warning panel to the main panel
        mainPanel.add(headerAndWarningPanel, BorderLayout.NORTH);


        // Create labels panel
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

        // Create text fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        fieldsPanel.setOpaque(false);
        sourceFolderField = new JTextField();
        destinationFolderField = new JTextField();
        whatsappSourceFolderField = new JTextField();
        // Apply the provided colors, rounded corners, and padding to the fields
        sourceFolderField.setBackground(FIELDS_COLOR);
        destinationFolderField.setBackground(FIELDS_COLOR);
        whatsappSourceFolderField.setBackground(FIELDS_COLOR);
        int radius = 10; // You can adjust the radius for different roundness
        sourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, radius));
        destinationFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, radius));
        whatsappSourceFolderField.setBorder(new RoundedBorder(Color.LIGHT_GRAY, radius));
        fieldsPanel.add(sourceFolderField);
        fieldsPanel.add(destinationFolderField);
        fieldsPanel.add(whatsappSourceFolderField);

        // Create buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Create buttons with icons and tooltips
        JButton organizeButton = new JButton("Organize Pictures");
        organizeButton.setToolTipText("Organize pictures");
        organizeButton.setBackground(PRIMARY_COLOR);
        int buttonRadius = 10;
        organizeButton.setBorder(new RoundedBorder(Color.LIGHT_GRAY, radius));
        organizeButton.setBorderPainted(false);
        organizeButton.setFocusPainted(false);
        organizeButton.addActionListener(e -> {
            String sourceFolder = sourceFolderField.getText();
            String destinationFolder = destinationFolderField.getText();
            boolean success = processPictures(sourceFolder, destinationFolder);
            if (success) {
                showAlert("Pictures organized successfully!");
            }
        });

        JButton processWhatsAppButton = new JButton("Update WhatsApp Metadata");
        processWhatsAppButton.setToolTipText("Update metadata for WhatsApp pictures");
        processWhatsAppButton.setBackground(SECONDARY_COLOR);
        processWhatsAppButton.setBorder(new RoundedBorder(Color.WHITE, buttonRadius));
        processWhatsAppButton.setBorderPainted(false);
        processWhatsAppButton.setFocusPainted(false);
        processWhatsAppButton.addActionListener(e -> {
            String whatsappSourceFolder = whatsappSourceFolderField.getText();
            boolean success = processWhatsAppPictures(whatsappSourceFolder);
            if (success) {
                showAlert("WhatsApp metadata updated successfully!");
            }
        });

        buttonPanel.add(organizeButton);
        buttonPanel.add(processWhatsAppButton);


        // Add components to the main panel
        mainPanel.add(labelsPanel, BorderLayout.WEST);
        mainPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Set background color of the frame
        getContentPane().setBackground(Color.white);

        // Add the main panel to the frame
        add(mainPanel);
    }

    // Show alert dialog with given message
    private void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
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
