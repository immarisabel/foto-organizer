package nl.marisabel.ui;

import javax.swing.*;
import java.awt.*;

public class CustomProgressBar extends JComponent {
    private int minimumValue;
    private int maximumValue;
    private int currentValue;

    public CustomProgressBar(int min, int max) {
        minimumValue = min;
        maximumValue = max;
        currentValue = min;
    }

    public void setValue(int value) {
        if (value >= minimumValue && value <= maximumValue) {
            currentValue = value;
            repaint(); // Refresh the progress bar display
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int barWidth = getWidth() - 10; // Reserve some space for padding
        int barHeight = getHeight() - 10;
        int progressBarWidth = (int) (barWidth * ((double) (currentValue - minimumValue) / (maximumValue - minimumValue)));

        g.setColor(Color.WHITE);
        g.fillRect(5, 5, barWidth, barHeight); // Draw the background

        g.setColor(Color.GREEN);
        g.fillRect(5, 5, progressBarWidth, barHeight); // Draw the progress bar
    }
}
