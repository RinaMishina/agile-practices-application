package com.acme.dbo.it.client;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;


public class RestAssuredTests implements ClientEndpoint {
    private RequestSpecification givenRequest;

    @BeforeEach
    public void createGivenRequest() {
        givenRequest = given().
                baseUri(BASE_URL).
                basePath(DBO_API).
                port(PORT).
                header(HEADER_NAME, HEADER_VALUE).
                contentType(ContentType.JSON).
                accept(ContentType.JSON).filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void shouldGetClientByFirstId() {


        givenRequest.
                when().
                get(CLIENT_ID, 4).
                then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(4), "login", equalTo("adm22i2n@email.com"));
    }


    @Test
    public void shouldDeleteClientByFirstId() {
        Response response =
                givenRequest.get("client").
                        then().
                        contentType(ContentType.JSON).
                        extract().
                        response();

        int id = response.body().path("[1].id");

        givenRequest.
                when().
                delete(CLIENT_ID, id).
                then().
                statusCode(HttpStatus.SC_OK);
    }

}
