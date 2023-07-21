package nl.marisabel.ui;

import nl.marisabel.ui.OnEmptyQueueListener;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Class that models a ProgressBar as a panel.
 *
 * Author: d.narvaez11
 */
public class CustomProgressBar extends JPanel {

    /**
     * Thread that handles the progress bar positioning and painting.
     */
    private class ProgressThread extends Thread {

        /**
         * Progress to be painted.
         */
        private long currentProgress;

        /**
         * Movement delay.
         */
        private long timeDelay;

        public ProgressThread(long currentProgress, long timeDelay) {
            this.currentProgress = currentProgress;
            this.timeDelay = timeDelay;
        }

        @Override
        public void run() {
            setProgressBar(currentProgress, timeDelay);

            int onQueue = threadsOnQueue.size();
            if (onQueue > 0) {
                threadsOnQueue.get(0).start();
                threadsOnQueue.remove(0);
                onQueue = threadsOnQueue.size();
                if ((onEmptyQueueListener != null) && (onQueue == 0)) {
                    onEmptyQueueListener.OnEmptyQueue();
                }
            }
        }

        @Override
        public void start() {
            threadActive = true;
            super.start();
        }
    }

    private static final long serialVersionUID = -7392056679892630730L;

    /**
     * For console printing
     */
    // private int queueResolved;

    /**
     * Color of the complement progress of the ProgressBar.
     */
    private Color complementColor;

    /**
     * Color of the progress of the ProgressBar.
     */
    private Color progressColor;

    /**
     * Image shown when the progress bar reaches 100%.
     */
    private ImageIcon endImage;

    /**
     * Image of the progress bar.
     */
    private ImageIcon progressBarImage;

    /**
     * Leading image of the progress bar.
     */
    private ImageIcon cursorImage;

    /**
     * FinishOnQueueListener that is notified when there are no threads in the queue.
     */
    private OnEmptyQueueListener onEmptyQueueListener;

    /**
     * Progress of the ProgressBar.
     */
    private double progress;

    /**
     * Checks if there is any running thread.
     */
    private boolean threadActive;

    /**
     * List of threads in the queue.
     */
    private ArrayList<Thread> threadsOnQueue;

    /**
     * Constructor of the ProgressBar. <br>
     * Sets the colors:
     * <ul>
     * <li>complementColor = Color.WHITE
     * <li>progressColor = Color.GRAY
     * </ul>
     */
    public CustomProgressBar() {
        threadsOnQueue = new ArrayList<>();
        complementColor = Color.WHITE;
        progressColor = Color.GRAY;
    }

    /**
     * Sets the OnEmptyQueueListener, which is notified when there are no threads in the queue.
     *
     * @param finishListener OnEmptyQueueListener to be notified.
     */
    public void addOnEmptyQueueListener(OnEmptyQueueListener finishListener) {
        onEmptyQueueListener = finishListener;
    }

    /**
     * Checks if there are threads in the queue.
     *
     * @return True if there are threads in the queue, false otherwise.
     */
    public boolean hasOnQueue() {
        return threadsOnQueue.size() > 0;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        int pos = ((int) progress * getWidth()) / 100;

        if (progressBarImage == null) {
            g2d.setColor(complementColor);
            Rectangle2D.Double back = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
            g2d.fill(back);

            g2d.setColor(progressColor);
            Rectangle2D.Double rec = new Rectangle2D.Double(0, 0, pos, getHeight());
            g2d.fill(rec);

            if (cursorImage != null) {
                int x0 = pos - (cursorImage.getIconWidth() / 2);
                int y0 = (getHeight() / 2) - (cursorImage.getIconHeight() / 2);

                if ((pos == getWidth()) && (endImage != null)) {
                    x0 = pos - endImage.getIconWidth();
                    y0 = (getHeight() / 2) - (endImage.getIconHeight() / 2);
                    g2d.drawImage(endImage.getImage(), x0, y0, null);
                } else {
                    g2d.drawImage(cursorImage.getImage(), x0, y0, null);
                }
            }
        } else {
            g2d.setColor(progressColor);
            Rectangle2D.Double back = new Rectangle2D.Double(0, 0, getWidth(), getHeight());
            g2d.fill(back);

            g2d.drawImage(progressBarImage.getImage(), 0, 0, getWidth(), getHeight(), null);

            g2d.setColor(complementColor);
            Rectangle2D.Double rec = new Rectangle2D.Double(pos, 0, getWidth(), getHeight());
            g2d.fill(rec);

            if (cursorImage != null) {
                int x0 = pos - (cursorImage.getIconWidth() / 2);
                int y0 = (getHeight() / 2) - (cursorImage.getIconHeight() / 2);

                if ((pos == getWidth()) && (endImage != null)) {
                    x0 = pos - endImage.getIconWidth();
                    y0 = (getHeight() / 2) - (endImage.getIconHeight() / 2);
                    g2d.drawImage(endImage.getImage(), x0, y0, null);
                } else {
                    g2d.drawImage(cursorImage.getImage(), x0, y0, null);
                }
            }
        }
    }

    /**
     * Sets the complement color with the given color.
     *
     * @param complementColor Complement color.
     */
    public void setComplementColor(Color complementColor) {
        this.complementColor = complementColor;
    }

    /**
     * Sets the progress color with the given color.
     *
     * @param progressColor Progress color.
     */
    public void setProgressColor(Color progressColor) {
        this.progressColor = progressColor;
    }

    /**
     * Sets the image that will be shown when the progress bar reaches 100%.
     *
     * @param endImage Image to be shown when the progress bar reaches 100%.
     */
    public void setEndImage(ImageIcon endImage) {
        this.endImage = endImage;
    }

    /**
     * Sets the image of the progress bar. <br>
     * Sets the PreferredSize as the dimension of the image passed as a parameter. <br>
     * However, the size of the Panel may vary, and so will the progress bar.
     *
     * @param image Image of the progress bar.
     */
    public void setProgressBarImage(ImageIcon image) {
        progressBarImage = image;

        if (progressBarImage != null) {
            int width = progressBarImage.getIconWidth();
            int height = progressBarImage.getIconHeight();
            setPreferredSize(new Dimension(width, height));
        }
    }

    /**
     * Sets the cursor image of the progress bar.
     *
     * @param image Cursor image of the progress bar.
     */
    public void setCursorImage(ImageIcon image) {
        cursorImage = image;
    }

    /**
     * Sets the progress of the ProgressBar. <br>
     * The entered progress is automatically painted. <br>
     * Requests are handled in the order they arrive, adding the requirement to the queue.
     *
     * @param progress Progress percentage of the progress bar. progress <= 100
     * @param timeDelay Delay with which the cursor and the progress bar will move forward.
     */
    public void setProgress(final long progress, final long timeDelay) {
        ProgressThread progressThread = new ProgressThread(progress, timeDelay);

        if (!threadActive) {
            progressThread.start();
        } else {
            threadsOnQueue.add(progressThread);
        }
    }

    /**
     * Moves the Cursor and progress bar with the given delay.
     *
     * @param progress Progress to be painted.
     * @param timeDelay Delay with which the cursor and the progress bar will move forward.
     */
    private void setProgressBar(double progress, long timeDelay) {
        timeDelay = (timeDelay == -1) ? 50 : timeDelay;

        double temp = progress > 100 ? 100 : progress;

        if (this.progress != temp) {
            if (temp >= this.progress) { // Positive Progress
                for (double i = this.progress; i <= temp; i++) {
                    this.progress = i;
                    repaint();
                    try {
                        Thread.sleep(timeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } else { // Negative Progress
                for (double i = this.progress; i >= temp; i--) {
                    this.progress = i;
                    repaint();
                    try {
                        Thread.sleep(timeDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if ((temp == 0) && (this.progress == 0)) {
            repaint();
        }
        threadActive = false;
    }
}
