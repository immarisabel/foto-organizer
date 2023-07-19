package nl.marisabel.whatsappImages;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhatsAppImageMetadataExtractor {

    private static final Logger log = Logger.getLogger(WhatsAppImageMetadataExtractor.class.getName());

    private static final String INPUT_DATE_FORMAT = "yyyyMMdd";
    private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd";
    private static final String ORIGINAL_WA_PATTERN = "IMG-(\\d{8})-WA\\d+\\.jpg";
    private static final Pattern patternOriginal = Pattern.compile(ORIGINAL_WA_PATTERN);


    private SimpleDateFormat inputFormatter;
    private SimpleDateFormat outputFormatter;
    private Pattern filenamePattern;


    public WhatsAppImageMetadataExtractor() {
        inputFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
        outputFormatter = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
        filenamePattern = Pattern.compile(ORIGINAL_WA_PATTERN);
    }



    /**
     * Extract date from image name on normal whatsapp backup pictures
     * @param imageName
     * @return the date string inside the name : IMG-YYYYMMDD-WAXXXX.jpg
     */
    private static String extractDateFromImageName(String imageName) {
        Matcher matcher = patternOriginal.matcher(imageName);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        return null;
    }


    /**
     * Converts the extracted date into metadata for use on the image
     * @param file
     * @return date + 00:00:00
     * @throws ParseException
     */
    public String getWhatsAppMetadata(File file) throws ParseException {
        if (file.isFile()) {
            String result = extractDateFromImageName(file.getName());
            if (result != null) {
                return formatDate(result) + " 00:00:00";
            }
        }
        return null;
    }

    /**
     * Formats the date to use YYYY-MM-DD format
     * @param inputDate
     * @return date YYYY-MM-DD
     * @throws ParseException
     */
    public String formatDate(String inputDate) throws ParseException {
        Date date = inputFormatter.parse(inputDate);
        return outputFormatter.format(date);
    }


}

