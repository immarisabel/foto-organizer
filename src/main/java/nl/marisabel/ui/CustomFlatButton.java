package nl.marisabel.ui;

import javax.swing.*;
import java.awt.*;


 class CustomFlatButton extends JButton {
  private Color bgColor;
  private Color textColor;

  public CustomFlatButton(String text, Color bgColor, Color textColor) {
   super(text);
   this.bgColor = bgColor;
   this.textColor = textColor;
   setFocusPainted(false);
   setContentAreaFilled(false);
   setOpaque(true);
   setFont(getFont().deriveFont(Font.BOLD, 14f));
   setForeground(textColor);
   setBorder(new RoundedBorder(bgColor, 10)); // Apply the round border
   setBackground(bgColor);
   setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  }

  @Override
  protected void paintComponent(Graphics g) {
   if (getModel().isPressed()) {
    g.setColor(bgColor.darker());
   } else {
    g.setColor(bgColor);
   }
   g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10); // Fill the rounded rectangle
   super.paintComponent(g);
  }
 }
