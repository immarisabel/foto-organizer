package nl.marisabel.ui.panels;

import nl.marisabel.ui.customElements.Elements;
import nl.marisabel.ui.panels.processPanels.OrganizePicturesPanel;
import nl.marisabel.ui.panels.processPanels.UpdateWhatsAppMetadataPanel;
import nl.marisabel.ui.utils.AllUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LeftPanel {
 private Elements elements;
 private JPanel createHeaderPanel() {
  elements = new Elements(); // Initialize the Elements object here

  JPanel headerPanel = new JPanel();
  headerPanel.setBackground(Color.white);
  headerPanel.setLayout(new BorderLayout());

  JLabel headerLabel = new JLabel(elements.images.PIXIE_SORT_HEADER);
  headerPanel.add(headerLabel, BorderLayout.CENTER);

  return headerPanel;
 }

 public JPanel createLeftPanel() {
  JPanel mainPanel = new JPanel();
  mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
  mainPanel.setBackground(Color.white);
  mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

  WarningPanel warning = new WarningPanel();
  JPanel headerPanel = createHeaderPanel();
  JPanel warningPanel = warning.createWarningPanel();

  mainPanel.add(headerPanel);
  mainPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
  mainPanel.add(warningPanel);
  mainPanel.add(Box.createVerticalStrut(20)); // Empty notification panel

  OrganizePicturesPanel organizePicturesPanel = new OrganizePicturesPanel();
  JPanel organizePanel = organizePicturesPanel.createOrganizePanel();
  mainPanel.add(organizePanel);
  mainPanel.add(Box.createVerticalStrut(15)); // Vertical spacing

  UpdateWhatsAppMetadataPanel updateWhatsAppMetadataPanel = new UpdateWhatsAppMetadataPanel();
  JPanel whatsappPanel = updateWhatsAppMetadataPanel.createWhatsAppPanel();
  mainPanel.add(whatsappPanel);

  JPanel footerPanel = new JPanel(); // Placeholder for the footer panel, add any extra info here if needed
  footerPanel.setBackground(Color.white);
  footerPanel.setPreferredSize(new Dimension(10, 50));

  mainPanel.add(footerPanel);

  return mainPanel;
 }
}
