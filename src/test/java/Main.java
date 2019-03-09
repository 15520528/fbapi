
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import org.apache.commons.codec.digest.DigestUtils;

public class Main {
    public static void main(String args[]) throws APIException {
        String AD_ACCOUNT_ID = "act_628852617555465";
        String access_token = "EAAcVaCfs0KEBAGT5WjbsktP2maJdg3HLPD4OiCjjxuGDO3TznZAlmRVZCAOowY3t5fs0BMbES9BJjS4PFbcg572edAmc3Xng1yVdMaMudxivAjXiNmogDGobmoCpob5YAkHxl5AtxRZA5qiyZCgO6AUrNVAbFC7JXmpJ05EMNQZDZD";
        APIContext context = new APIContext(access_token).enableDebug(true);
        CustomAudience customAudience;

        customAudience = new AdAccount(AD_ACCOUNT_ID, context).createCustomAudience()
                .setName("My new " +
                        "CA from JavabusinessJDK")
                .setSubtype(com.facebook.ads.sdk.CustomAudience.EnumSubtype.VALUE_CUSTOM)
                .setCustomerFileSource(com.facebook.ads.sdk.CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
                .setDescription("People who bought from my website")
                .execute();

        System.out.println("ID " + customAudience.getId().toString());

                String data = "BB897ACB06BE63F41F39D30AC6D99948A4E55D8DC3E6056E08C5F01DBA20A8DC";
        new CustomAudience(customAudience.getId(), context).createUser()
                .setPayload("{\"schema\":\"PHONE\",\"data\":["+data+"]}")
                .execute();
    }
}
