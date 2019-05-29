package test;

import org.junit.BeforeClass;
import org.junit.Test;
//import test.FbClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FbClientTest {
  private static FbClient.Builder builder;
  private static List<String> phones;

  @BeforeClass
  public static void tearUp() {
    builder =
        FbClient.Builder.builder()
            .accessToken(
                "EAAjNcKHW6NsBALEJsGbRcaU0ZAJgRCnAMBln6IaQwuRsuAkZCh2IHFZB6rqQ1cISjzBTZCrvOnAwsmLapqD5VkFGIsGihMqWFSjKUFmgPNNeGvKbHJp3Q1K0faQy9sVpdZATXJFxqvczfPIZBOAfDHJgoxFBfTkA1C9oKt2HT9i7TcaZCSM2unlkof7obtrTSQZD")
            .accountId("371754423670931")
            .isDebug(false)
            .numThreads(2)
            .timeoutMs(5000)
            .build();

    phones = new ArrayList<>(Arrays.asList("0949245894", "0949245891", "0949245892"));
  }

  @Test
  public void uploadsTest() {
    String customAudience =
        FbClient.getInstance(builder).createCustomAudience("nameTest", "descriptionTest");

      boolean uploadResult = FbClient.getInstance(builder).uploads(phones, customAudience, 1);

      System.out.println("uploadResult=" + uploadResult);
  }
}
