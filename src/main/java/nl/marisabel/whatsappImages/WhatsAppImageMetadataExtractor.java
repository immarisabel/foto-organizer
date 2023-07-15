package nl.marisabel.whatsappImages;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatsAppImageMetadataExtractor {

    private static final Logger log = Logger.getLogger(WhatsAppImageMetadataExtractor.class.getName());


    static String formatDate(String inputDate) throws ParseException {
        String inputFormat = "yyyy-MM-dd hh:mm:ss";
        String outputFormat = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormatter = new SimpleDateFormat(inputFormat);
        SimpleDateFormat outputFormatter = new SimpleDateFormat(outputFormat);

        Date date = inputFormatter.parse(inputDate);
        String outputDate = outputFormatter.format(date);
        return outputDate;

    }

    public String getWhatsAppMetadata(String folderPath) throws ParseException {

        // Define the regular expression pattern
        String pattern = "WhatsApp Image (\\d{4}-\\d{2}-\\d{2}) at (\\d{2}\\.\\d{2}\\.\\d{2} [AP]M)";
        Pattern regex = Pattern.compile(pattern);

        // Create a File object representing the folder
        File folder = new File(folderPath);

        // Check if the folder exists and is a directory
        if (folder.exists() && folder.isDirectory()) {
            // Get a list of files in the folder
            File[] files = folder.listFiles();

            // Iterate over the files
            for (File file : files) {
                // Check if the file is a regular file
                if (file.isFile()) {
                    // Get the filename
                    String filename = file.getName();

                    // Match the pattern against the filename
                    Matcher matcher = regex.matcher(filename);

                    // Check if the pattern matches
                    if (matcher.find()) {
                        // Extract the date and time groups from the matched pattern
                        String date = matcher.group(1);
                        String time = matcher.group(2);
                        String fullDateSignature = date + " " + time;

                        // Pre-process the time string to replace dots with colons
                        fullDateSignature = fullDateSignature.replace('.', ':');

                        // Convert the extracted date and time into the desired format

                        String newDate = formatDate(fullDateSignature);
                        log.info("New date " + newDate);

                        return newDate;

                    } else {
                        log.warning("Pattern not found in the filename: " + filename);
                    }


                } else {
                    log.severe("Invalid folder path: " + folderPath);
                    return "";
                }
            }
        }
        return "";
    }
}