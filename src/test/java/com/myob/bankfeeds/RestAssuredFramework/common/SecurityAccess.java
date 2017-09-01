package com.myob.bankfeeds.RestAssuredFramework.common;

import com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints.RestEndPoint;
import com.myob.bankfeeds.RestAssuredFramework.Data.Env.Environment;
import io.restassured.RestAssured;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;


/**
 * Created by manoj.tharayil on 6/18/17.
 */
public class SecurityAccess {

    private static String getBase64token() {
        RestEndPoint restEndPoint = Environment.getRestEndPoint();
        String idamClient = restEndPoint.getIdamClient();
        String idamSecret = restEndPoint.getIdamSecret();

        if (StringUtils.isEmpty(idamClient) || StringUtils.isEmpty(idamSecret)) {
            throw new RuntimeException("Idam client/ idam secret is not set in environment variables" +
                    "\n Idam client = " + idamClient + "\n Idam secret =" + idamSecret);
        }

        return Base64.getEncoder().encodeToString(String.format("%s:%s", idamClient, idamSecret).getBytes());
    }

    public static String getAccessToken() throws JSONException {
        Logger logger = LoggerFactory.getLogger(SecurityAccess.class);
        RestEndPoint restEndPoint = Environment.getRestEndPoint();

        Map<String, Object> jsonAsMap = jsonMap(restEndPoint.getEnvironmentResource());

        String base64Token;
        base64Token = getBase64token();

        String response =
                RestAssured.given()
                        .contentType(JSON)
                        .header("Authorization", "Basic " + base64Token)
                        .formParams(jsonAsMap)
                        .when()
                        .post(restEndPoint.getLoginUrl() + restEndPoint.getAccessTokenUrl()).asString();


        logger.info("URL = " + restEndPoint.getLoginUrl() + restEndPoint.getAccessTokenUrl());
        logger.info("Response = " + response.substring(0, Math.min(response.length(), 50)));

        JSONObject jsonObj = new JSONObject(response);

        return jsonObj.getString("access_token");

    }

    private static Map<String, Object> jsonMap(String environmentResource) {
        Map<String, Object> jsonAsMap = new HashMap<>();
        jsonAsMap.put("grant_type", "client_credentials");
        jsonAsMap.put("resource", environmentResource);
        jsonAsMap.put("scope", "retrieve.transactions");
        return jsonAsMap;
    }


}
