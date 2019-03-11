package facebook;
import com.facebook.ads.sdk.APIException;
import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
public class TestRunner {
    @Test
    public void testCreateCustomAudience(){
        CustomAudience audience = new CustomAudience();
        String customAudienceId = audience.createCustomAudience("My Audience", "People who purchased on my website");
        try {
            String fileName = "/home/lap11105-local/Documents/My repos/" +
                    "FacebookAPI/CustomFacebookAPI/src/main/java/DemoSDT.txt";
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            List<String> phoneList = new ArrayList<>();
            String line = reader.readLine();

            long maxSize = 100000;
            int count = 0;
            int lineNumber = 0;
            while (line != null) {
                phoneList.add(line);
                line = reader.readLine();
                count++;
                if (count == maxSize) {
                    audience.uploads(phoneList, customAudienceId);
                }
                lineNumber++;
            }
            if (count < maxSize) {
                audience.uploads(phoneList, customAudienceId);
//                System.out.println("\nresponse num_received:" +customAudience.getRawResponseAsJsonObject().get("num_received"));
//                System.out.println("\nresponse num_invalid_entries:" +customAudience.getRawResponseAsJsonObject().get("num_invalid_entries"));
//                System.out.println("\nresponse invalid_entry_samples:" +customAudience.getRawResponseAsJsonObject().get("invalid_entry_samples"));
            }
            System.out.println("line number: " + lineNumber);
            reader.close();

        } catch (IOException | APIException e) {
            e.printStackTrace();
        }

    }
}
