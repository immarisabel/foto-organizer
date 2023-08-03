package nl.marisabel.ui.panels.logPanel;

public class AppendLog {
 public void logsAppend(String logs, boolean isHeader) {
  if (isHeader) {
   System.out.println("[======" + logs + "======]");
  } else {
   System.out.println(logs);
  }
 }
}
