package nl.marisabel.ui.customElements;

import javax.swing.*;
import java.awt.*;
import javax.swing.text.Document;

public class PlaceHolderTextField extends JTextField {
 private String placeholder;

 public PlaceHolderTextField() {
 }

 public PlaceHolderTextField(String text) {
  super(text);
 }

 public PlaceHolderTextField(int columns) {
  super(columns);
 }

 public PlaceHolderTextField(String text, int columns) {
  super(text, columns);
 }

 public PlaceHolderTextField(Document doc, String text, int columns) {
  super(doc, text, columns);
 }

 public String getPlaceholder() {
  return placeholder;
 }

 public void setPlaceholder(String placeholder) {
  this.placeholder = placeholder;
 }

 @Override
 protected void paintComponent(Graphics g) {
  super.paintComponent(g);
  if (placeholder != null && getText().isEmpty()) {
   int x = getInsets().left;
   int y = (getHeight() - g.getFontMetrics().getHeight()) / 2;
   g.setColor(Color.gray);
   g.drawString(placeholder, x, y + g.getFontMetrics().getAscent());
  }
 }
}
