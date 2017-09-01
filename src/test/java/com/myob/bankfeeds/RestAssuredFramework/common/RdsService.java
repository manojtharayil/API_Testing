package com.myob.bankfeeds.RestAssuredFramework.common;

import com.myob.bankfeeds.config.DataSourceConfigUtils;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

/**
 * Created by cedric.bauza on 8/2/17.
 * <p>
 * Usage
 * <p>
 * Multiple inserts
 * new RdsService().execute("REPLACE INTO bankfeedsdelivery.transactions(TransactionId,FeedId,ProductId) VALUES(1,1,1),(2,1,1),(3,1,1)")
 * Delete
 * new RdsService().execute("DELETE * from bankfeedsdelivery.transactions")
 */
public class RdsService {

    Logger logger = LoggerFactory.getLogger(RdsService.class);

    public void execute(String query) {
        HikariDataSource dataSource = (HikariDataSource) new DataSourceConfigUtils().getConfig(1);
        try {
            Connection connection = dataSource.getConnection();

            if (connection != null) {
                connection.prepareStatement(query).execute();
                logger.info("Successfully executed query='{}' ", query);
            }
        } catch (Exception e) {
            logger.error("Failed to execute query='{}' exception='{}'", query, e);
        } finally {
            dataSource.close();
        }
    }
}
