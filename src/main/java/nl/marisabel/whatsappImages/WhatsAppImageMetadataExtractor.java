package nl.marisabel.whatsappImages;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatsAppImageMetadataExtractor {

    private static final Logger log = Logger.getLogger(WhatsAppImageMetadataExtractor.class.getName());

    private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String FILENAME_PATTERN = "IMG-(\\d{8})-WA\\d+\\.jpeg";

    private SimpleDateFormat inputFormatter;
    private SimpleDateFormat outputFormatter;
    private Pattern filenamePattern;

    public WhatsAppImageMetadataExtractor() {
        inputFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
        outputFormatter = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
        filenamePattern = Pattern.compile(FILENAME_PATTERN);
    }

    public String formatDate(String inputDate) throws ParseException {
        Date date = inputFormatter.parse(inputDate);
        return outputFormatter.format(date);
    }

    public String getWhatsAppMetadata(File file) {
        if (file.isFile()) {
            String filename = file.getName();
            log.info("Processing " + filename);
            Matcher matcher = filenamePattern.matcher(filename);

            if (matcher.find()) {
                String date = matcher.group(1);

                String dateStr = date;
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate newDate = LocalDate.parse(dateStr, inputFormatter);
                String metadata = newDate + " 00:00:00";
                return metadata;
            } else {
                log.warning("Pattern not found in the filename: " + filename);
            }
        } else {
            log.severe("Invalid file: " + file);
        }

        return null;
    }
}
