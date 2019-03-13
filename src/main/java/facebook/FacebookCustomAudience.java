package facebook;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import com.google.gson.JsonObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

public class FacebookCustomAudience {
    private final String VIETNAM_HASHCODE = DigestUtils.sha256Hex("vn");
    private final String AD_ACCOUNT_ID;
    private final String ACCESS_TOKEN;
    private final APIContext CONTEXT;
    private final long TIMEOUT;

    public FacebookCustomAudience(){
        Properties prop = this.readConfig("config.properties");
        AD_ACCOUNT_ID = prop.getProperty("AD.AD_ACCOUNT_ID");
        ACCESS_TOKEN = prop.getProperty("AD.ACCESS_TOKEN");
        CONTEXT = new APIContext(ACCESS_TOKEN).enableDebug(true);
        TIMEOUT = 10000;
    }

    public Boolean deleteCustomAudience(String customAudienceId){
        Logger logger = Logger.getLogger(TestRunner.class.getName());
        try {
            //to delete an custom audience account
            CustomAudience audience = new CustomAudience(customAudienceId, CONTEXT);
            JsonObject response = audience.delete().execute().getRawResponseAsJsonObject();

            if(response.get("success").getAsString().equals("true")) {
                logger.info("successfully delete customAudience with id " + customAudienceId + response.toString());
                return true;
            }
            else {
                logger.info("fail to delete customAudience with id " + customAudienceId + response.toString());
                return false;
            }

        } catch (APIException e) {
            logger.error("Error delete customAudience", e);
            return false;
        }
    }

    public String createCustomAudience(String name, String description){
        Logger logger = Logger.getLogger(TestRunner.class.getName());
        CustomAudience customAudience;

        try {
            //to create an custom audience account
            customAudience = new AdAccount(AD_ACCOUNT_ID, CONTEXT).createCustomAudience()
                    .setName(name)
                    .setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
                    .setCustomerFileSource(CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
                    .setDescription(description)
                    .execute();
            logger.info("\ncreate Audience with Id" + customAudience.getId());
        } catch (APIException e) {
            logger.error("Error create Audience");
            e.printStackTrace();
            return null;
        }
        return customAudience.getId();
    }

    public boolean uploads(List<String> phoneList, String customAudienceId) {
        Logger logger = Logger.getLogger(TestRunner.class.getName());
        logger.info("start to upload ");
        Callable<Boolean> uploadTask = new Callable<Boolean>() {

            public Boolean call(){
                ArrayList<String> hashedDataList = new ArrayList<>();

                for (String phoneNumber: phoneList) {
                    hashedDataList.add("[\"" + DigestUtils.sha256Hex(phoneNumber) + "\"," + "\""+VIETNAM_HASHCODE+"\"]");
                }
                String data = String.join(",", hashedDataList);
                try {
                    CustomAudience customAudience = new CustomAudience(customAudienceId, CONTEXT).createUser()
                            .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":[" + data + "]}")
                            .execute();
                    logger.info(customAudience.getRawResponseAsJsonObject());
                    return true;
                } catch (APIException e) {
                    logger.error("Error exception: ", e);
                    return false;
                }
            }
        };
        RunnableFuture<Boolean> future = new FutureTask(uploadTask);
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(future);
        Boolean result = null;
        try
        {
            result = future.get(TIMEOUT, TimeUnit.MILLISECONDS);    // wait for seconds
            System.out.println("Result "+result);
            logger.info("Result " +result);
            return true;
        }
        catch (TimeoutException ex)
        {
            // timed out. Try to stop the code if possible.
            future.cancel(true);
            logger.info("Result " , ex);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
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
