package nl.marisabel.ui.panels.logPanel;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class LogPanelUtils {

 public static JPanel createLogPanel() {
  JPanel logPanel = new JPanel();
  logPanel.setLayout(new BorderLayout());
  logPanel.setBackground(Color.white);
  logPanel.setBorder(BorderFactory.createTitledBorder("Logs"));

  JTextArea logTextArea = new JTextArea();
  logTextArea.setEditable(false);

  // Set the font for the log text area
  Font font = new Font("Verdana", Font.PLAIN, 12); // You can change the font and size here
  logTextArea.setFont(font);

  JScrollPane scrollPane = new JScrollPane(logTextArea);
  scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

  logPanel.add(scrollPane, BorderLayout.CENTER);

  // Redirect System.out and System.err to the JTextArea
  PrintStream printStream = new PrintStream(new CustomOutputStream(logTextArea));
  System.setOut(printStream);
  System.setErr(printStream);

  return logPanel;
 }
}