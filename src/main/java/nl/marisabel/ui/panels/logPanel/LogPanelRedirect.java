package nl.marisabel.ui.panels.logPanel;

import javax.swing.*;
import java.awt.*;


public class LogPanelRedirect {

 public LogPanelRedirect() {
  EventQueue.invokeLater(new Runnable() {
   @Override
   public void run() {
    try {
     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
     ex.printStackTrace();
    }

    JPanel logPanel = LogPanelUtils.createLogPanel();

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(logPanel, BorderLayout.CENTER);
    frame.setSize(400, 300);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
   }
  });
 }
}
