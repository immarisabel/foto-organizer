package nl.marisabel.ui.utils;

import nl.marisabel.backup.FolderBackup;
import nl.marisabel.ui.customElements.CustomProgressBar;

import javax.swing.*;
import java.awt.*;

public class BackupPictures {
 private AllUtils utils;

 public BackupPictures(AllUtils utils) {
  this.utils = utils;
 }

 public boolean backUpAllPicturesBeforeProcessing(String sourceFolder) {
  FolderBackup folderBackup = new FolderBackup();

  utils.logs.logsAppend("backing up " + utils.countFiles.getTotalFiles(sourceFolder) + " files....", false);
  folderBackup.backUpAllFiles(sourceFolder);
  return true;
 }

 private JDialog createBackupDialog(CustomProgressBar backupProgressBar) {
  JDialog dialog = new JDialog();
  dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
  dialog.setSize(300, 100);
  dialog.setResizable(false);
  dialog.setTitle("Backup Progress");
  dialog.setLayout(new BorderLayout());
  dialog.add(backupProgressBar, BorderLayout.CENTER);
  dialog.setLocationRelativeTo(null); // Center the dialog on the screen
  dialog.setVisible(true);
  return dialog;
 }


}
