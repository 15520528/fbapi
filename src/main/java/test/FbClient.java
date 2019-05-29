package test;

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
import java.util.concurrent.*;

public class FbClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(FbClient.class);

  private final String VIETNAM_HASHCODE = DigestUtils.sha256Hex("vn");
  private final APIContext CONTEXT;
  private final ExecutorService EXECUTORS;
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
    this.CONTEXT = new APIContext(builder.accountId).enableDebug(builder.isDebug);

    EXECUTORS = Executors.newFixedThreadPool(builder.numThreads);
  }

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

      LOGGER.info("create Audience with Id" + customAudience.getId());
    } catch (APIException e) {
      LOGGER.error("Error create Audience", e);
      return null;
    }

    return customAudience.getId();
  }

  public boolean uploads(List<String> phoneList, String customAudienceId) {
    Callable<Boolean> uploadTask =
        () -> {
          ArrayList<String> hashedDataList = new ArrayList<>();

          for (String phoneNumber : phoneList) {
            String dataHashed = String.format("[%s,vn]", DigestUtils.sha256Hex(phoneNumber));
            //            String dataHashed =
            //                "[\""
            //                    + DigestUtils.sha256Hex(phoneNumber)
            //                    + "\","
            //                    + "\""
            //                    + VIETNAM_HASHCODE
            //                    + "\"]";
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
            return true;
          } catch (APIException e) {
            LOGGER.error("uploads fb ex: ", e);
            return false;
          }
        };

    RunnableFuture<Boolean> future = new FutureTask(uploadTask);
    EXECUTORS.execute(future);
    try {
      Boolean result = future.get(builder.timeoutMs, TimeUnit.MILLISECONDS); // wait for seconds
      LOGGER.info("Result {}", result);
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
    return false;
  }

  public boolean uploads(List<String> phoneList, String customAudienceId, int numRetry) {
    boolean result;
    int internalCount = numRetry;
    while (internalCount > 0) {
      result = uploads(phoneList, customAudienceId, internalCount - 1);
      if (!result) {
        continue;
      }
      return true;
    }
    return false;
  }

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

  @lombok.Builder
  public static class Builder {
    @NonNull private String accountId = "";
    @NonNull private String accessToken = "";
    private boolean isDebug = false;
    private int numThreads = 2;
    private int timeoutMs = 60000;
  }
}
