package com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints;

import static java.lang.System.getenv;

/**
 * Created by manoj.tharayil on 8/11/17.
 */
public enum RestEndPoint {

    SIT_END_POINT("SIT_BASE_URL",
            "SIT_AUTH_URL",
            "SIT_AUTH_EXTENSION",
            "SIT_ENV_RESOURCE",
            "SIT_IDAM_CLIENT",
            "SIT_IDAM_SECRET",
            "SIT_COMMON_URL"),
    PROD_END_POINT("PROD_BASE_URL",
            "PROD_AUTH_URL",
            "PROD_AUTH_EXTENSION",
            "PROD_ENV_RESOURCE",
            "PROD_IDAM_CLIENT",
            "PROD_IDAM_SECRET",
            "PROD_COMMON_URL");

    private String baseUrl;
    private String loginUrl;
    private String environmentResource;
    private String accessTokenUrl;
    private String idamClient;
    private String idamSecret;
    private String apiTransactionUrl;

    RestEndPoint(String baseUrl, String loginUrl, String accessTokenUrl, String environmentResource, String idamClient, String idamSecret, String apiTransactions) {
        this.baseUrl = baseUrl;
        this.loginUrl = loginUrl;
        this.accessTokenUrl = accessTokenUrl;
        this.environmentResource = environmentResource;
        this.idamClient = idamClient;
        this.idamSecret = getenv(idamSecret);
        this.apiTransactionUrl = apiTransactions;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public String getEnvironmentResource() {
        return environmentResource;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public String getIdamClient() {
        return idamClient;
    }

    public String getIdamSecret() {
        return idamSecret;
    }

    public String getApiTransactionUrl() {
        return apiTransactionUrl;
    }
}
