package vn.zalopay.activator.service.segment.facebook;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import vn.zalopay.activator.service.facebook.FbClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FbClientTest {
  private static FbClient.Builder builder;
  private static List<String> phones;
  private static String customAudience;
  private static FbClient fbClient;

  @BeforeClass
  public static void tearUp() {
    builder =
        FbClient.Builder.builder()
            .accessToken(
                "EAAjNcKHW6NsBAGTo9ZAnXnYAUz2COQ1zZBxZCpZBGskyyreuZCXphfTZAJuClRNHPaHsVX8xUtzwQwXfSBP4imurqIZA5GHV5oXAscUv69FnHQ1LWVwAZCsWT6wtluREYYz27khfGSZAv8l0bxOzXJ7xkwfu3SlHfky38IAb4S6gpi5qw5ZA31pT6cnmlbVVp1duAZD")
            .accountId("771778553207925")
            .isDebug(false)
            .timeoutMs(5000)
            .build();

    phones = new ArrayList<>(Arrays.asList("84949245898", "84949245831", "84946245892"));
    fbClient = FbClient.getInstance(builder);
  }

  @Test
  public void createCustomAudienceTest() {
    customAudience = fbClient.createCustomAudience("nameTest", "descriptionTest");
    Assert.assertTrue(customAudience != null);
  }

  @Test
  public void uploadsTest() {
    boolean result = fbClient.uploads(phones, customAudience, 1);
    Assert.assertTrue(result);
  }

  @Test
  public void deleteTest() {
    boolean result = fbClient.deleteCustomAudience(customAudience);
    Assert.assertTrue(result);
  }
}
