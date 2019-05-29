package test;

import com.facebook.ads.sdk.*;

public class TestGetUser {
    public static void main (String args[]) throws APIException {
        String AD_ACCOUNT_ID = "act_628852617555465";
        String access_token = "EAAcVaCfs0KEBADZAqmT7Elr7IOJkMKPyNuMsY7Vq4mortPUHrWnGV6GNYZCKBZAZBPZB72ZCEsJpt6W1AS5YcgU803x5dPfBGF0i4QLRZBRPPS2WZCzobEoxAagZATZAEJBa2RbhDlBv3feUVSORrygxJc3hF2fQ1TmNZB5xxMP7hAYHgZDZD";
        APIContext context = new APIContext(access_token).enableDebug(true);
//        APINodeList<CustomAudience> customAudience;
//        customAudience = new AdAccount(AD_ACCOUNT_ID, context).getCustomAudiences()
//            .requestField("id")
//            .execute();
        new AdAccount(AD_ACCOUNT_ID, context).getCustomAudiences()
                .execute();
    }

}
