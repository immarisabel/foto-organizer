package nl.marisabel.backup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FolderBackup {


    public void backUpAllFiles(String sourceFolderPath) {
        String backupFolderPath = createBackupFolder(sourceFolderPath);
        if (backupFolderPath != null) {
            System.out.println("Backup created successfully at: " + backupFolderPath);
        } else {
            System.out.println("Backup creation failed.");
        }
    }

    public static String createBackupFolder(String sourceFolderPath) {

        File sourceFolder = new File(sourceFolderPath);

        // Get the path where the application is running
        String appPath = System.getProperty("user.dir");

        // Create the "backups" folder under the application path
        File backupsFolder = new File(appPath, "backups");

        if (!backupsFolder.exists()) {
            if (!backupsFolder.mkdir()) {
                System.err.println("Failed to create the backups folder.");
                return null;
            }
        }

        if (!sourceFolder.exists() || !sourceFolder.isDirectory()) {
            System.err.println("Source folder does not exist or is not a directory.");
            return null;
        }

        // Create the backup folder name with the specified format and timestamp
        String backupFolderName = sourceFolder.getName() + " BU " + new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

        File backupFolder = new File(backupsFolder, backupFolderName);

        // Create the backup folder
        if (!backupFolder.mkdir()) {
            System.err.println("Failed to create the backup folder.");
            return null;
        }

        try {
            copyFolder(sourceFolder, backupFolder);
            return backupFolder.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void copyFolder(File originalFile, File newFile) throws IOException {
        File[] files = originalFile.listFiles();
        if (files != null) {
            for (File file : files) {
                File newCopyOfFile = new File(newFile, file.getName());
                if (file.isDirectory()) {
                    newCopyOfFile.mkdir();
                    copyFolder(file, newCopyOfFile);
                } else {
                    Files.copy(file.toPath(), newCopyOfFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
}
