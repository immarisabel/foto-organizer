package nl.marisabel.ui.panels;

import javax.swing.*;
import java.awt.*;

public class WarningPanel {
 JPanel createWarningPanel() {
  JPanel warningPanel = new JPanel();
  warningPanel.setBackground(Color.white);
  JLabel warningLabel = new JLabel("before processing, a backup will be created");
  warningPanel.add(warningLabel);
  return warningPanel;
 }


}
