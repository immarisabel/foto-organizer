package nl.marisabel.ui;

import nl.marisabel.images.OrganizePhotos;
import nl.marisabel.whatsappImages.WhatsAppPhotosMetadataUpdater;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

public class PictureOrganizerUI extends JFrame {
    private JTextField sourceFolderField;
    private JTextField destinationFolderField;
    private JTextField whatsappSourceFolderField;


    public PictureOrganizerUI() {
        setTitle("Picture Organizer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.white);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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
        fieldsPanel.add(sourceFolderField);
        fieldsPanel.add(destinationFolderField);
        fieldsPanel.add(whatsappSourceFolderField);

        // Create buttons panel
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
                processPictures(sourceFolder, destinationFolder);
            }
        });
        JButton processWhatsAppButton = new JButton("Process WhatsApp Pictures");
        processWhatsAppButton.setBackground(new Color(46, 204, 113));
        processWhatsAppButton.setForeground(Color.white);
        processWhatsAppButton.setFocusPainted(false);
        processWhatsAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String whatsappSourceFolder = whatsappSourceFolderField.getText();
                processWhatsAppPictures(whatsappSourceFolder);
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


    private void processWhatsAppPictures(String sourceFolder) {
        WhatsAppPhotosMetadataUpdater photosProcessor = new WhatsAppPhotosMetadataUpdater();
        try {
            photosProcessor.scanFiles(sourceFolder);
        } catch (ParseException | IOException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void processPictures(String sourceFolder, String destinationFolder) {
        OrganizePhotos organizePhotos = new OrganizePhotos();
        organizePhotos.organizePhotos(sourceFolder, destinationFolder);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PictureOrganizerUI pictureOrganizerUI = new PictureOrganizerUI();
            pictureOrganizerUI.setVisible(true);
        });
    }
}
