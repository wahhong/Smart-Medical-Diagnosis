package model;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.paypal.base.rest.OAuthTokenCredential;
import java.util.HashMap;
import java.util.Map;

public class PayPalConfig {

    public static APIContext getAPIContext() {
        try {
            String clientID = "AZskFHzPC4Js-pirszoJ5BAq7r-uKfi1PgcGinPYODXz0xFBUG3SjEjllrCWJ_XLvcilMrZa78VGO8fz";
            String clientSecret = "EPqZ8YVK0k0Dh_r_MbQlwyuWiW0h9wO2l8DWJBR_LIejkAQQQ5wBTbSEq1OEEZ6iZPumAdsO1nGykt0A";
            String mode = "sandbox";

            Map<String, String> sdkConfig = new HashMap<>();
            sdkConfig.put("mode", mode);

            OAuthTokenCredential credential = new OAuthTokenCredential(clientID, clientSecret, sdkConfig);
            String accessToken = credential.getAccessToken();

            APIContext apiContext = new APIContext(accessToken);
            apiContext.setConfigurationMap(sdkConfig);

            return apiContext;
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return null;
        }
    }
}
