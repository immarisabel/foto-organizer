package nl.marisabel.ui.utils;

import javax.swing.*;
import java.awt.*;

public class Alerts extends Component {
 public void showAlert(String message) {
  JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
 }

}
