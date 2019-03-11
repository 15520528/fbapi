package facebook;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CustomAudience {
    private final String VietNamHashCode = DigestUtils.sha256Hex("vn");
    private final String AD_ACCOUNT_ID;
    private final String ACCESS_TOKEN;
    private final APIContext CONTEXT;

    public CustomAudience(){
        Properties prop = this.readConfig("config.properties");
        AD_ACCOUNT_ID = prop.getProperty("AD.AD_ACCOUNT_ID");
        ACCESS_TOKEN = prop.getProperty("AD.ACCESS_TOKEN");
        CONTEXT = new APIContext(ACCESS_TOKEN).enableDebug(true);
    }

    public String createCustomAudience(String name, String description){
        com.facebook.ads.sdk.CustomAudience customAudience;

        try {
            //to create an custom audience account
            customAudience = new AdAccount(AD_ACCOUNT_ID, CONTEXT).createCustomAudience()
                    .setName(name)
                    .setSubtype(com.facebook.ads.sdk.CustomAudience.EnumSubtype.VALUE_CUSTOM)
                    .setCustomerFileSource(com.facebook.ads.sdk.CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
                    .setDescription(description)
                    .execute();
        } catch (APIException e) {
            e.printStackTrace();
            return null;
        }
        return customAudience.getId();
    }

    public void uploads(List<String> phoneList, String customAudienceId) throws APIException {
        ArrayList<String> hashedDataList = new ArrayList<>();

        for (String phoneNumber: phoneList) {
            hashedDataList.add("[\"" + DigestUtils.sha256Hex(phoneNumber) + "\"," + "\""+VietNamHashCode+"\"]");
        }

        String data = String.join(",", hashedDataList);

        new com.facebook.ads.sdk.CustomAudience(customAudienceId, CONTEXT).createUser()
                .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":[" + data + "]}")
                .execute();
    }

    public Properties readConfig(String fileName) {
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

}
