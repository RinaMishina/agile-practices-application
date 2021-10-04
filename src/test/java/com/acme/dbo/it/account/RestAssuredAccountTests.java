package com.acme.dbo.it.account;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static com.acme.dbo.it.account.AccountEndpoint.ACCOUNT;
import static com.acme.dbo.it.common.DefaultEndpoint.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class RestAssuredAccountTests {
    private RequestSpecification givenRequest;
    private Connection connection;

    @BeforeEach
    public void setUpDbConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:derby://localhost:1527/dbo-db");
    }

    @BeforeEach
    public void setUpRestAssuredRequest() {
        givenRequest = given().
                baseUri(BASE_URL).
                basePath(DBO_API).
                port(PORT).
                header(HEADER_NAME, HEADER_VALUE).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @AfterEach
    public void closeDbConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void shouldGetCountOfAllAccounts() throws SQLException {
        int accountsCount;
        try (final PreparedStatement countAccounts = connection.prepareStatement("Select count(*) from account");
             final ResultSet resultSet = countAccounts.executeQuery()) {
            assumeTrue(resultSet.next());
            accountsCount = resultSet.getInt(1);
        }

        givenRequest.
                when().
                get(ACCOUNT).
                then().body("size()", is(accountsCount)).statusCode(HttpStatus.SC_OK);
    }
}
