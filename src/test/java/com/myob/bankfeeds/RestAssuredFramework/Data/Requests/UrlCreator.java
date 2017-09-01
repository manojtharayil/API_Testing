package com.myob.bankfeeds.RestAssuredFramework.Data.Requests;

import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

/**
 * Created by manoj.tharayil on 8/10/17.
 */
public class UrlCreator {

    public static final String PRODUCT_REQUEST = "?product=";
    public static final String CONCATENATION_CHARACTER = "&";
    public static final String FROM_TRANSACTION_REQUEST = "fromTransactionId=";
    public static final String MANAGED_HEALTH = "/managed/health";

    public static String getRequestURLWithProductAndTransactionId(String product, long transactionId) {
        return baseURI + basePath + PRODUCT_REQUEST + product
                + CONCATENATION_CHARACTER
                + FROM_TRANSACTION_REQUEST + transactionId;
    }

    public static String getRequestURLWithProduct(String product) {
        return baseURI + basePath + PRODUCT_REQUEST + product;
    }
}
