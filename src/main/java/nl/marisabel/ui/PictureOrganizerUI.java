package nl.marisabel.ui;

import nl.marisabel.ui.customElements.Elements;
import nl.marisabel.ui.panels.LeftPanel;
import nl.marisabel.ui.panels.logPanel.LogPanelUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;


public class PictureOrganizerUI extends JFrame {

 private JTextArea logTextArea;
 private Elements elements;

 public PictureOrganizerUI() throws IOException {
  elements = new Elements(); // Initialize the Elements object here
  initializeUI();
 }

 private void initializeUI() throws IOException {

  setTitle("PixieSort");
  setIconImage(elements.images.PIXIE_SORT_ICON.getImage());
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
  mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

  LeftPanel left = new LeftPanel();

  JPanel leftPanel = left.createLeftPanel(); // Existing left panel with other components
  JPanel logPanel = LogPanelUtils.createLogPanel(); // New log panel


  // Create a JSplitPane to split the main panel into left and right panels
  JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, logPanel);
  splitPane.setResizeWeight(.1); // Set the initial width ratio of the left and right panels
  splitPane.setBorder(null); // Set the border of JSplitPane to null to remove the lines
  splitPane.setDividerSize(0); // Set the divider size to 0 to remove the divider line

  mainPanel.add(splitPane, BorderLayout.CENTER); // Add the split pane to the main panel

  return mainPanel;
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
