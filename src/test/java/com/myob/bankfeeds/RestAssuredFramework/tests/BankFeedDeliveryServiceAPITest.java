package com.myob.bankfeeds.RestAssuredFramework.tests;

import com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints.RestEndPoint;
import com.myob.bankfeeds.RestAssuredFramework.Data.Env.Environment;
import com.myob.bankfeeds.RestAssuredFramework.Data.Requests.UrlCreator;
import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.when;

/**
 * Created by manoj.tharayil on 6/15/17.
 */
@RunWith(JUnit4.class)
public class BankFeedDeliveryServiceAPITest {

    Logger logger = LoggerFactory.getLogger(BankFeedDeliveryServiceAPITest.class);


    @BeforeClass
    public static void initialize() {
        RestEndPoint restEndPoint = Environment.getRestEndPoint();
        RestAssured.config = RestAssured.config().sslConfig(SSLConfig.sslConfig().allowAllHostnames());
        RestAssured.baseURI = restEndPoint.getBaseUrl();

    }

    @Category({SmokeTests.class})
    @Test
    public void verifyBankFeedServieIsUpAndRunning() throws Exception {
        String managedHealthEndPoint = RestAssured.baseURI + UrlCreator.MANAGED_HEALTH;
        logger.info(managedHealthEndPoint);
        when().
                get(managedHealthEndPoint).
                then().
                statusCode(200);
    }


    @Category(SmokeTests.class)
    @Test
    public void verifyIDAMTokenIsNotReturnedWithoutCorrectAuthorization() throws Exception {
        RestEndPoint restEndPoint = Environment.getRestEndPoint();
        when()
                .get(restEndPoint.getLoginUrl() + restEndPoint.getAccessTokenUrl()).
                then().
                statusCode(404);
    }
}
