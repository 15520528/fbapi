package facebook;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TestRunner {
    static Logger logger = Logger.getLogger(TestRunner.class.getName());

    /**
     * Test 1 test Upload RawCustomAudience Without CountryCode
     **/
    @Test
    public void testUploadRawCustomAudienceWithoutCountryCode(){
        FacebookCustomAudience audience = new FacebookCustomAudience();
        String customAudienceId = audience.createCustomAudience("[lần 1 > Audience test 1 Without CountryCode by api]", "People who purchased on my website");
        try {
            String fileName = "/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/resources/extracted file/extractedSDT.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> phoneList = new ArrayList<>();
            String line = reader.readLine();

            long maxSize = 10000;
            int count = 0;
            int lineNumber = 0;
            while (line != null) {
                phoneList.add(line);
                line = reader.readLine();
                count++;
                lineNumber++;
                if (count == maxSize) {
                    Boolean result = audience.uploadsWithoutCountryCode(phoneList, customAudienceId);

                    phoneList.clear();
                    System.out.println("send result: "+result);
                    if(!result){
                        audience.deleteCustomAudience(customAudienceId);
                        break;
                    }
//                    logger.info("lineNumber " +lineNumber +" được up lên");
                    count = 0;
                }
            }
            if (count < maxSize) {
                audience.uploadsWithoutCountryCode(phoneList, customAudienceId);
//                System.out.println("\nresponse num_received:" +customAudience.getRawResponseAsJsonObject().get("num_received"));
//                System.out.println("\nresponse num_invalid_entries:" +customAudience.getRawResponseAsJsonObject().get("num_invalid_entries"));
//                System.out.println("\nresponse invalid_entry_samples:" +customAudience.getRawResponseAsJsonObject().get("invalid_entry_samples"));
//                logger.info("số phone thứ " +lineNumber + " được up lên");
            }
            System.out.println("line number: " + lineNumber);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Test 1 test Upload RawCustomAudience With CountryCode
     **/
    @Test
    public void testUploadRawCustomAudienceWithCountryCode(){
        FacebookCustomAudience audience = new FacebookCustomAudience();
        String customAudienceId = audience.createCustomAudience("[lần 1->Audience test 1 with Country Code by api]", "People who purchased on my website");
        try {
            String fileName = "/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/resources/extracted file/extractedSDT.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> phoneList = new ArrayList<>();
            String line = reader.readLine();

            long maxSize = 10000;
            int count = 0;
            int lineNumber = 0;
            while (line != null) {
                phoneList.add(line);
                line = reader.readLine();
                count++;
                lineNumber++;
                if (count == maxSize) {
                    Boolean result = audience.uploadsWithCountryCode(phoneList, customAudienceId);

                    phoneList.clear();
                    System.out.println("send result: "+result);
                    if(!result){
                        audience.deleteCustomAudience(customAudienceId);
                        break;
                    }
//                    logger.info("lineNumber " +lineNumber +" được up lên");
                    count = 0;
                }
            }
            if (count < maxSize) {
                audience.uploadsWithCountryCode(phoneList, customAudienceId);
//                System.out.println("\nresponse num_received:" +customAudience.getRawResponseAsJsonObject().get("num_received"));
//                System.out.println("\nresponse num_invalid_entries:" +customAudience.getRawResponseAsJsonObject().get("num_invalid_entries"));
//                System.out.println("\nresponse invalid_entry_samples:" +customAudience.getRawResponseAsJsonObject().get("invalid_entry_samples"));
//                logger.info("số phone thứ " +lineNumber + " được up lên");
            }
            System.out.println("line number: " + lineNumber);
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    /**
//     * Test 2
//     **/
//
//    @Test
//    public void testUploadPrefixCountryCustomAudience(){
//        FacebookCustomAudience audience = new FacebookCustomAudience();
//        String customAudienceId = audience.createCustomAudience("[Audience test 2]", "People who purchased on my website");
//        try {
//            String fileName = "/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/resources/extracted file/PrefixSDT.txt";
//            BufferedReader reader = new BufferedReader(new FileReader(fileName));
//            List<String> phoneList = new ArrayList<>();
//            String line = reader.readLine();
//
//            long maxSize = 10000;
//            int count = 0;
//            int lineNumber = 0;
//            while (line != null) {
//                phoneList.add(line);
//                line = reader.readLine();
//                count++;
//                lineNumber++;
//                if (count == maxSize) {
//                    Boolean result = audience.uploads(phoneList, customAudienceId);
//
//                    phoneList.clear();
//                    System.out.println("send result: "+result);
//                    if(!result){
//                        audience.deleteCustomAudience(customAudienceId);
//                        break;
//                    }
////                    logger.info("lineNumber " +lineNumber +" được up lên");
//                    count = 0;
//                }
//            }
//            if (count < maxSize) {
//                audience.uploads(phoneList, customAudienceId);
////                System.out.println("\nresponse num_received:" +customAudience.getRawResponseAsJsonObject().get("num_received"));
////                System.out.println("\nresponse num_invalid_entries:" +customAudience.getRawResponseAsJsonObject().get("num_invalid_entries"));
////                System.out.println("\nresponse invalid_entry_samples:" +customAudience.getRawResponseAsJsonObject().get("invalid_entry_samples"));
////                logger.info("số phone thứ " +lineNumber + " được up lên");
//            }
//            System.out.println("line number: " + lineNumber);
//            reader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    /**
//     * Test 3
//     **/
//
//    @Test
//    public void testUploadStandardCountryCustomAudience(){
//        FacebookCustomAudience audience = new FacebookCustomAudience();
//        String customAudienceId = audience.createCustomAudience("[Audience test 3]", "People who purchased on my website");
//        try {
//            String fileName = "/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/resources/extracted file/StandardSDT.txt";
//            BufferedReader reader = new BufferedReader(new FileReader(fileName));
//            List<String> phoneList = new ArrayList<>();
//            String line = reader.readLine();
//
//            long maxSize = 10000;
//            int count = 0;
//            int lineNumber = 0;
//            while (line != null) {
//                phoneList.add(line);
//                line = reader.readLine();
//                count++;
//                lineNumber++;
//                if (count == maxSize) {
//                    Boolean result = audience.uploads(phoneList, customAudienceId);
//
//                    phoneList.clear();
//                    System.out.println("send result: "+result);
//                    if(!result){
//                        audience.deleteCustomAudience(customAudienceId);
//                        break;
//                    }
////                    logger.info("lineNumber " +lineNumber +" được up lên");
//                    count = 0;
//                }
//            }
//            if (count < maxSize) {
//                audience.uploads(phoneList, customAudienceId);
////                System.out.println("\nresponse num_received:" +customAudience.getRawResponseAsJsonObject().get("num_received"));
////                System.out.println("\nresponse num_invalid_entries:" +customAudience.getRawResponseAsJsonObject().get("num_invalid_entries"));
////                System.out.println("\nresponse invalid_entry_samples:" +customAudience.getRawResponseAsJsonObject().get("invalid_entry_samples"));
////                logger.info("số phone thứ " +lineNumber + " được up lên");
//            }
//            System.out.println("line number: " + lineNumber);
//            reader.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

    @Ignore
    public void testDeleteCustomAudience(){
        FacebookCustomAudience audience = new FacebookCustomAudience();
        audience.deleteCustomAudience("23843303860100668");
    }
}
