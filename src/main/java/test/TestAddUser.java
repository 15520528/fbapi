import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.CustomAudience;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class TestAddUser {
    public static void main (String args[]) throws APIException {
        String access_token = "EAAcVaCfs0KEBAAEuIeWY1OSAJY4RUYli6ZCxtOJoOfZC1VPm1KS1xAnfbMQGzgy1cBb6qTTCjau0LTjWpD5NC6w4wzU5Q3AbEGHiCc3BqoQ0eXCK6QQ5iviCSOB2x4VNw8K6PUBsbRZBrqIVmmFUZCZCkigIgVyZAI6fbYpdBhZAgZDZD";
        String CUSTOM_AUDIENCE_ID = "23843291431050668";
        APIContext context = new APIContext(access_token).enableDebug(true);
                String data = readData("/home/lap11105-local/Documents/My repos/FacebookAPI/CustomFacebookAPI/src/main/java/customer.txt");
        new CustomAudience(CUSTOM_AUDIENCE_ID, context).createUser()
                .setPayload("{\"schema\":\"PHONE_SHA256\",\"data\":["+data+"]}")
                .execute();
    }
    public static String readData(String fileName){
//        BufferedReader reader;
//        ArrayList<String> dataList = new ArrayList<>();
//        try {
//            reader = new BufferedReader(new FileReader(fileName));
//            String line = reader.readLine();
//            while (line != null) {
////                System.out.println(DigestUtils.sha256Hex(line));
//                dataList.add("\""+DigestUtils.sha256Hex(line)+"\"");
//                line = reader.readLine();
//            }
//            reader.close();
//            return String.join(",", dataList);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }
}
