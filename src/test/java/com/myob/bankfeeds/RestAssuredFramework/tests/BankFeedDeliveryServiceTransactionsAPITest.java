
package com.myob.bankfeeds.RestAssuredFramework.tests;

import com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints.RestEndPoint;
import com.myob.bankfeeds.RestAssuredFramework.Data.Env.Environment;
import com.myob.bankfeeds.RestAssuredFramework.Data.Requests.UrlCreator;
import com.myob.bankfeeds.RestAssuredFramework.common.SecurityAccess;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeTrue;

@RunWith(JUnit4.class)
/**
 * Created by manoj.tharayil on 8/3/17.
 */
public class BankFeedDeliveryServiceTransactionsAPITest {

    Logger logger = LoggerFactory.getLogger(BankFeedDeliveryServiceAPITest.class);
    RestEndPoint restEndPoint = Environment.getRestEndPoint();
    static String securityAccessToken;


    @BeforeClass
    public static void initialize() throws JSONException {
        RestEndPoint restEndPoint = Environment.getRestEndPoint();

        RestAssured.baseURI = restEndPoint.getBaseUrl();
        RestAssured.basePath = restEndPoint.getApiTransactionUrl();
        securityAccessToken = SecurityAccess.getAccessToken();
        assumeTrue(StringUtils.isNotEmpty(securityAccessToken));
        RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().allowAllHostnames());
    }

    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void verifyDeliveryServiceReturnsSuccessfulResponseForValidRequestWithProductAndTransactionDetails() throws Exception {
        String product = "EXO";
        long transactionId = 1;
        for(int i=0;i<1000;i++){
            String requestURL = UrlCreator.getRequestURLWithProductAndTransactionId(product, (long) Math.floor(Math.random() * 1000001));
            logger.info("Request url = " + requestURL);
            givenSecurityCredentials()
                    .when()
                    .get(requestURL)
                    .then()
                    .log().ifError();

        }

    }

    @Category({SmokeTests.class, RegressionTests.class})
    @Test
    public void verifySuccessfulResponseForValidRequestForSpecificProductWithoutAnyLastTransaction() throws Exception {
        String product = "EXO";
        String requestURL = UrlCreator.getRequestURLWithProduct(product);
        logger.info("Request url = " + requestURL);
        givenSecurityCredentials()
                .when()
                .get(requestURL)
                .then()
                .statusCode(200);
    }

    @Category({RegressionTests.class})
    @Test
    public void verifyAllFieldsOfATransactionsForAProduct_ContractTest() throws Exception {
        givenSecurityCredentials()
                .when()
                .get()
                .then()
                .statusCode(200)
                .body("transactions.product", equalTo("Essentials"),
                        "transactions.country", equalTo("AU"),
                        "transactions.businessId", equalTo("11396330"),
                        "transactions.feedId", equalTo("3002461485"),
                        "transactions.accountId", equalTo("1722722"),
                        "transactions.transactionId", equalTo(2380255104l),
                        "transactions.valueDate", equalTo("2017-06-06T00:00:00"),
                        "transactions.originalDate", equalTo("2017-06-05T00:00:00"),
                        "transactions.tranTypeId", equalTo("3"),
                        "transactions.tranTypeDescription", equalTo("Auto Payment"),
                        "transactions.amount", equalTo("25.99"),
                        "transactions.refNo", equalTo("PAYMENT"),
                        "transactions.narration", equalTo("VISA DEBIT PURCHASE CARD 8007 7-ELEVEN 1009 PASCOE VALE"));
    }

    @Category(RegressionTests.class)
    @Test
    public void verifyNoTransactionsAreReturnedWhenWeProvideLastTransactionIdInDatabase() throws Exception {
        Response response;
        ArrayList transactions;
        String product = "EXO";
        long transactionId = Long.MAX_VALUE;
        String requestURL = UrlCreator.getRequestURLWithProductAndTransactionId(product, transactionId);
        logger.info("Request url = " + requestURL);
        response = givenSecurityCredentials()
                .when()
                .get(requestURL)
                .then()
                .statusCode(200)
                .extract().response();
        transactions = response.path("transactions");
        assertThat(transactions.isEmpty(), is(true));

    }

    @Category(RegressionTests.class)
    @Test
    public void verifySelfLinkIsPresent() throws Exception {
        String product = "EXO";
        long transactionId = 1;
        String requestURL = UrlCreator.getRequestURLWithProductAndTransactionId(product, transactionId);
        logger.info("Request url = " + requestURL);
        givenSecurityCredentials()
                .when()
                .get(requestURL)
                .then()
                .statusCode(200)
                .body("_links.self.href", equalTo(requestURL));
    }


    @Category(RegressionTests.class)
    @Test
    public void verifyNextLinkIsPresent() throws Exception {
        String product = "EXO";
        long transactionId = 1;
        String requestURL = UrlCreator.getRequestURLWithProductAndTransactionId(product, transactionId);
        logger.info("Request url = " + requestURL);
        givenSecurityCredentials()
                .when()
                .get(requestURL)
                .then()
                .statusCode(200)
                .body("_links.next.href", containsString(restEndPoint.getBaseUrl() + restEndPoint.getApiTransactionUrl()));
    }

    @Category(RegressionTests.class)
    @Test
    public void verifyPaginationNextLinkIsEqualToSelfLinkInTheNextResponse() throws Exception {
        Response response;
        String nextUrl;
        String product = "EXO";
        long transactionId = 1;
        String requestURL = UrlCreator.getRequestURLWithProductAndTransactionId(product, transactionId);
        logger.info("Request url = " + requestURL);
        response = givenSecurityCredentials()
                .when()
                .get(requestURL)
                .then()
                .statusCode(200)
                .body("_links.next.href", containsString(requestURL))
                .extract().response();

        //extract the next url
        nextUrl = response.path("_links.next.href");
        givenSecurityCredentials()
                .when()
                .get(nextUrl)
                .then()
                .statusCode(200)
                .body("_links.self.href", equalTo(nextUrl));
    }

    private RequestSpecification givenSecurityCredentials() {
        return given()
                .auth().preemptive().oauth2(securityAccessToken);
    }
}


