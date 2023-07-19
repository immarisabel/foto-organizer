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
 private static final String SAVED_WA_PATTERN = "WhatsApp Image (\\d{4}-\\d{2}-\\d{2}) at (\\d{1,2}\\.\\d{2}\\.\\d{2} [APM]{2})(?: \\(\\d+\\))?\\.jpeg";
 private static final Pattern patternOriginal = Pattern.compile(ORIGINAL_WA_PATTERN);
 private static final Pattern patternDownloaded = Pattern.compile(SAVED_WA_PATTERN);


 private SimpleDateFormat inputFormatter;
 private SimpleDateFormat outputFormatter;
 private Pattern filenamePattern;
 private Pattern filenamePatternDownloaded;


 public WhatsAppImageMetadataExtractor() {
  inputFormatter = new SimpleDateFormat(INPUT_DATE_FORMAT);
  outputFormatter = new SimpleDateFormat(OUTPUT_DATE_FORMAT);
  filenamePattern = Pattern.compile(ORIGINAL_WA_PATTERN);
  filenamePatternDownloaded = Pattern.compile(SAVED_WA_PATTERN);
 }


 /**
  * There are 2 types of WhatsApp images, originals inside the folder of WhatsApp and saved ones in the phone itself.
  * @param imageName
  * @return date from file according ot pattern
  */
 private static String extractDateFromImageName(String imageName) {
  Matcher matcher = patternOriginal.matcher(imageName);
  if (matcher.find()) {
   System.out.println(matcher.group(1));
   return matcher.group(1);
  }
  return null;
 }

 private static String extractDateFromImageNameDownloaded(String imageName) throws ParseException {
  Matcher matcher = patternDownloaded.matcher(imageName);
  if (matcher.find()) {
   String date = matcher.group(1);
   String time = matcher.group(2);

   // Update the input format to handle "1.44.43 PM"
   DateFormat inputFormat = new SimpleDateFormat("h.mm.ss a", Locale.ENGLISH);
   DateFormat outputFormat = new SimpleDateFormat("HH:mm:ss");

   Date parsedTime;
   try {
    parsedTime = inputFormat.parse(time);
   } catch (ParseException e) {
    // Handle the exception or rethrow it if you need to
    throw new ParseException("Unparseable date: " + time, e.getErrorOffset());
   }

   time = outputFormat.format(parsedTime);
   String metadata = date + " " + time;
   return metadata;
  }
  return null;
 }



 public String formatDate(String inputDate) throws ParseException {
  Date date = inputFormatter.parse(inputDate);
  return outputFormatter.format(date);
 }


 public String getWhatsAppMetadata(File file) throws ParseException {
  if (file.isFile()) {
   String result = extractDateFromImageName(file.getName());
   if (result != null) {
    return formatDate(result) + " 00:00:00";
   }
  }
  return null;
 }

 public String getWhatsAppMetadataDownloaded(File file) throws ParseException {
  if (file.isFile()) {
   String result = extractDateFromImageNameDownloaded(file.getName());
   if (result != null) {
    System.out.println(result);
    return result;
   }
  }
  return null;
 }
}
