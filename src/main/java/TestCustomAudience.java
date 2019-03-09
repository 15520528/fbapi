import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class TestCustomAudience {

    public static void main (String args[]) throws APIException {
        TestCustomAudience test = new TestCustomAudience();
        String name = "My new Custom Audience";
        String description = "People who purchased on my website";
        String fileName = "/home/lap11105-local/Documents/My repos/" +
                "FacebookAPI/CustomFacebookAPI/src/main/java/customer.txt";
        test.createCustomeAudience(name, description, fileName);
    }

    //create a custom audience account

    public boolean createCustomeAudience(String name, String description, String fileName){
        Properties prop = this.readConfig("config.properties");

        String AD_ACCOUNT_ID = prop.getProperty("AD.AD_ACCOUNT_ID");
        String access_token = prop.getProperty("AD.ACCESS_TOKEN");
        APIContext context = new APIContext(access_token).enableDebug(true);
        CustomAudience customAudience;

        try {
            //to create an custom audience account
            customAudience = new AdAccount(AD_ACCOUNT_ID, context).createCustomAudience()
                    .setName(name)
                    .setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
                    .setCustomerFileSource(CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
                    .setDescription(description)
                    .execute();

            String customAudienceId = customAudience.getId();

            //to upload telephone File to created audienceAccount
            this.uploadPhones(customAudienceId, context, fileName);

            return true;
        } catch (APIException e) {
            e.printStackTrace();
        }
        return false;
    }

    //load config file

    public Properties readConfig(String fileName){
        Properties prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(fileName);
            // load a properties file
            prop.load(input);

//            System.out.println(prop.getProperty("AD.AD_ACCOUNT_ID"));
//            System.out.println(prop.getProperty("AD.ACCESS_TOKEN"));
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    //upload phone lists to custom account

    public boolean uploadPhones(String custom_audience_id, APIContext context, String fileName){
        BufferedReader reader;
        ArrayList<String> dataList = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String line = reader.readLine();
            long maxSize = 100000;
            int count = 0;
            int lineNumber = 0;
            while (line != null) {
                dataList.add("[\""+ DigestUtils.sha256Hex(line)+"\","+"\"0217E4BA5939E0F93036EB33734D1D722B25AF6362662F1DD49267A2FAFF1B54\"]");
                line = reader.readLine();
                count++;
                if(count == maxSize){
                    String data = String.join(",", dataList);
                    new CustomAudience(custom_audience_id, context).createUser()
                            .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":["+data+"]}")
                            .execute();
                    dataList.clear();
                    System.out.println("send");
                    count = 0;
                }
                lineNumber++;
            }
            if(count<maxSize){
                String data = String.join(",", dataList);
                new CustomAudience(custom_audience_id, context).createUser()
                        .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":["+data+"]}")
                        .execute();
            }
            System.out.println("line number: "+lineNumber);
            reader.close();

        } catch (IOException | APIException e) {
            e.printStackTrace();
        }
        return true;
    }

    //read phone dataset from file and return hashedData
//    public static String readData(String fileName){
//        BufferedReader reader;
//        ArrayList<String> dataList = new ArrayList<>();
//        try {
//            reader = new BufferedReader(new FileReader(fileName));
//            String line = reader.readLine();
//            long maxSize = 300000;
//            int count = 0;
//
//            while (line != null) {
////                System.out.println(DigestUtils.sha256Hex(line));
//
//                dataList.add("[\""+ DigestUtils.sha256Hex(line)+"\","+"\"0217E4BA5939E0F93036EB33734D1D722B25AF6362662F1DD49267A2FAFF1B54\"]");
//                line = reader.readLine();
//                count++;
//            }
//
//            reader.close();
//            return String.join(",", dataList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    String AD_ACCOUNT_ID = "act_628852617555465";
//    String access_token = "EAAcVaCfs0KEBAGT5WjbsktP2maJdg3HLPD4OiCjjxuGDO3TznZAlmRVZCAOowY3t5fs0BMbES9BJjS4PFbcg572edAmc3Xng1yVdMaMudxivAjXiNmogDGobmoCpob5YAkHxl5AtxRZA5qiyZCgO6AUrNVAbFC7JXmpJ05EMNQZDZD";
//    APIContext context = new APIContext(access_token).enableDebug(true);
//    CustomAudience customAudience;
//
//    customAudience = new AdAccount(AD_ACCOUNT_ID, context).createCustomAudience()
//                .setName("My new " +
//                                 "CA from JavabusinessJDK")
//                .setSubtype(com.facebook.ads.sdk.CustomAudience.EnumSubtype.VALUE_CUSTOM)
//                .setCustomerFileSource(com.facebook.ads.sdk.CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
//                .setDescription("People who bought from my website")
//                .execute();
//
//        System.out.println("ID "+customAudience.getId().toString());
//
//    //        String data = readData("/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/java/smallcustomer.txt");
////        new CustomAudience(customAudience.getId(), context).createUser()
////                .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":["+data+"]}")
////                .execute();
//    BufferedReader reader;
//    ArrayList<String> dataList = new ArrayList<>();
//        try {
//        reader = new BufferedReader(new FileReader("/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/java/DemoSDT.txt"));
//        String line = reader.readLine();
//        long maxSize = 100000;
//        int count = 0;
//        int lineNumber = 0;
//        while (line != null) {
//            dataList.add("[\""+ DigestUtils.sha256Hex(line)+"\","+"\"0217E4BA5939E0F93036EB33734D1D722B25AF6362662F1DD49267A2FAFF1B54\"]");
//            line = reader.readLine();
//            count++;
//            if(count == maxSize){
//                String data = String.join(",", dataList);
//                new CustomAudience(customAudience.getId(), context).createUser()
//                        .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":["+data+"]}")
//                        .execute();
//                dataList.clear();
//                System.out.println("send");
//                count = 0;
//            }
//            lineNumber++;
//        }
//        if(count<maxSize){
//            String data = String.join(",", dataList);
//            new CustomAudience(customAudience.getId(), context).createUser()
//                    .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":["+data+"]}")
//                    .execute();
//        }
//        System.out.println("line number: "+lineNumber);
//        reader.close();
//
//    } catch (IOException e) {
//        e.printStackTrace();
//    }
}
