package nl.marisabel.ui.panels.logPanel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TestRedirect {

 public static void main(String[] args) {
  new LogPanelRedirect();
 }

 public TestRedirect() {
  EventQueue.invokeLater(new Runnable() {
   @Override
   public void run() {
    try {
     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException ex) {
    } catch (InstantiationException ex) {
    } catch (IllegalAccessException ex) {
    } catch (UnsupportedLookAndFeelException ex) {
    }

    CapturePane capturePane = new CapturePane();
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.add(capturePane);
    frame.setSize(200, 200);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

    PrintStream ps = System.out;
    System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));

    // Using a ScheduledExecutorService to print log messages at regular intervals
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    Runnable printLogTask = new Runnable() {
     @Override
     public void run() {
      System.out.println("Hello, this is a test");
      System.out.println("Wave if you can see me");
     }
    };
    // Schedule the task with an initial delay of 1 second and a repeat rate of 2 seconds
    executor.scheduleAtFixedRate(printLogTask, 1, 2, TimeUnit.SECONDS);
   }

  });
 }

 public class CapturePane extends JPanel implements Consumer {

  private JTextArea output;

  public CapturePane() {
   setLayout(new BorderLayout());
   output = new JTextArea();
   add(new JScrollPane(output));
  }

  @Override
  public void appendText(final String text) {
   if (EventQueue.isDispatchThread()) {
    output.append(text);
    output.setCaretPosition(output.getText().length());
   } else {

    EventQueue.invokeLater(new Runnable() {
     @Override
     public void run() {
      appendText(text);
     }
    });

   }
  }
 }

 public interface Consumer {
  public void appendText(String text);
 }

 public class StreamCapturer extends OutputStream {

  private StringBuilder buffer;
  private String prefix;
  private Consumer consumer;
  private PrintStream old;

  public StreamCapturer(String prefix, Consumer consumer, PrintStream old) {
   this.prefix = prefix;
   buffer = new StringBuilder(128);
   buffer.append("[").append(prefix).append("] ");
   this.old = old;
   this.consumer = consumer;
  }

  @Override
  public void write(int b) throws IOException {
   char c = (char) b;
   String value = Character.toString(c);
   buffer.append(value);
   if (value.equals("\n")) {
    consumer.appendText(buffer.toString());
    buffer.delete(0, buffer.length());
    buffer.append("[").append(prefix).append("] ");
   }
   old.print(c);
  }
 }
}