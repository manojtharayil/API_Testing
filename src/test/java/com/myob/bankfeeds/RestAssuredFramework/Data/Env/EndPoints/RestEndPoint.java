package com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints;

import static java.lang.System.getenv;

/**
 * Created by manoj.tharayil on 8/11/17.
 */
public enum RestEndPoint {

    SIT_END_POINT("https://sit-bankfeeds-service.external-data-interfaces.dev.myob.com",
            "https://sit.login.myob.com",
            "/oauth2/token",
            "bankfeedsdeliveryservicesit",
            "bankfeedsdeliveryservicetestclientsit",
            "TEST_CLIENT_SECRET_SIT",
            "/api/transactions"),
    PROD_END_POINT("https://prod-bankfeeds-service.external-data-interfaces.prod.myob.com",
            "https://login.myob.com/common",
            "/oauth2/token",
            "bankfeedsdeliveryserviceprd",
            "bankfeedsdeliveryservicetestclientprod",
            "TEST_CLIENT_SECRET_PROD",
            "/api/transactions");

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
