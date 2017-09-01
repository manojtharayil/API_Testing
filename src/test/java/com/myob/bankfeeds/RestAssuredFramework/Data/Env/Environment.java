package com.myob.bankfeeds.RestAssuredFramework.Data.Env;

import com.myob.bankfeeds.RestAssuredFramework.Data.Env.EndPoints.RestEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.System.getenv;

/**
 * Created by manoj.tharayil on 8/8/17.
 */
public class Environment {

    private static final Logger logger = LoggerFactory.getLogger(Environment.class);

    private static final String SIT_ENV = "sit";
    private static final String PROD_ENV = "prod";
    private static final String ENV_VAR_NAME = "ENV";


    public static RestEndPoint getRestEndPoint() {
        RestEndPoint restEndPoint;
        final String env = getenv(ENV_VAR_NAME);
        if (env == null ) {
            throw new RuntimeException("Env variable " + ENV_VAR_NAME + " is not set");
        }
        logger.info(("Environment set to " + env));
        switch (env) {
            case SIT_ENV:
                restEndPoint = RestEndPoint.SIT_END_POINT;
                break;
            case PROD_ENV:
                restEndPoint = RestEndPoint.PROD_END_POINT;
                break;
            default:
                throw new RuntimeException("Invalid environment " + env);
        }
        return restEndPoint;
    }

}
