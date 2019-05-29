package vn.zalopay.activator.service.facebook;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.CustomAudience;
import com.google.gson.JsonObject;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FbClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(FbClient.class);

  private final String VIETNAM_HASHCODE = DigestUtils.sha256Hex("vn");
  private final APIContext CONTEXT;
  private final Builder builder;

  private static FbClient instance = null;

  public static FbClient getInstance(Builder builder) {
    if (instance == null) {
      instance = new FbClient(builder);
    }
    return instance;
  }

  protected FbClient(Builder builder) {
    this.builder = builder;
    this.CONTEXT = new APIContext(builder.accessToken).enableDebug(builder.isDebug);
  }

  /**
   * createCustomAudience for get Id to uploads, Id can re-use it many times
   *
   * @param name
   * @param description
   * @return
   */
  public String createCustomAudience(String name, String description) {
    CustomAudience customAudience;

    try {
      // to create an custom audience account
      customAudience =
          new AdAccount(builder.accountId, CONTEXT)
              .createCustomAudience()
              .setName(name)
              .setSubtype(CustomAudience.EnumSubtype.VALUE_CUSTOM)
              .setCustomerFileSource(CustomAudience.EnumCustomerFileSource.VALUE_USER_PROVIDED_ONLY)
              .setDescription(description)
              .execute();

      LOGGER.info("create Audience with Id: {}", customAudience.getId());
    } catch (APIException e) {
      LOGGER.error("Error create Audience", e);
      return null;
    }

    return customAudience.getId();
  }

  /**
   * @param phoneList
   * @param customAudienceId
   * @return CompletableFuture<Boolean>
   */
  private CompletableFuture<Boolean> uploads(List<String> phoneList, String customAudienceId) {
    CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
    ArrayList<String> hashedDataList = new ArrayList<>();

    for (String phoneNumber : phoneList) {
      String dataHashed =
          String.format("[\"%s\",\"%s\"]", DigestUtils.sha256Hex(phoneNumber), "vn");
      hashedDataList.add(dataHashed);
    }

    String data = String.join(",", hashedDataList);
    try {
      CustomAudience customAudience =
          new CustomAudience(customAudienceId, CONTEXT)
              .createUser()
              .setPayload("{\"schema\":[\"PHONE\", \"COUNTRY\"],\"data\":[" + data + "]}")
              .execute();

      LOGGER.debug(customAudience.toString());
      completableFuture.complete(true);
    } catch (APIException e) {
      LOGGER.error("uploads fb ex: ", e);
      completableFuture.complete(false);
    }

    return completableFuture;
  }

  /**
   * @param phoneList
   * @param customAudienceId
   * @param numRetries
   * @return true if success or false if failure
   */
  public boolean uploads(List<String> phoneList, String customAudienceId, int numRetries) {
    boolean result;
    int internalCount = numRetries;
    while (internalCount > 0) {
      internalCount--;
      CompletableFuture<Boolean> future = uploads(phoneList, customAudienceId);
      try {
        result = future.get(builder.timeoutMs, TimeUnit.MILLISECONDS); // wait for seconds
        LOGGER.info("Result {}", result);
        if (!result) {
          continue;
        }
        return true;
      } catch (InterruptedException | ExecutionException ex) {
        // timed out. Try to stop the code if possible.
        future.cancel(true);
        LOGGER.info("uploads ex ", ex);
      } catch (TimeoutException ex) {
        // timed out. Try to stop the code if possible.
        future.cancel(true);
        LOGGER.info("uploads timeout ex ", ex);
      }
    }
    return false;
  }

  /**
   * if upload audience failed for some reasonable, delete audiences with `customAudienceId` created
   * before
   *
   * @param customAudienceId
   * @return true if success or false if failure
   */
  public Boolean deleteCustomAudience(String customAudienceId) {
    try {
      // to delete an custom audience account
      CustomAudience audience = new CustomAudience(customAudienceId, CONTEXT);
      JsonObject response = audience.delete().execute().getRawResponseAsJsonObject();

      if (response.get("success").getAsString().equals("true")) {
        LOGGER.info(
            "Delete customAudience successfully with id ={}",
            customAudienceId + response.toString());
        return true;
      } else {
        LOGGER.error(
            "Delete customAudience failed with id={}, response={} ",
            customAudienceId,
            response.toString());
        return false;
      }

    } catch (APIException e) {
      LOGGER.error("Delete customAudience ex", e);
      return false;
    }
  }

  /** this builder for set configurations */
  @lombok.Builder
  public static class Builder {
    @NonNull private String accountId = "";
    @NonNull private String accessToken = "";
    private boolean isDebug = false;
    private int timeoutMs = 60000;
  }
}
