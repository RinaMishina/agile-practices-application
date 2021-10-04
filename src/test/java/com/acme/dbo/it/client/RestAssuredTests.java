package com.acme.dbo.it.client;

import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;


import static io.restassured.RestAssured.given;


public class RestAssuredTests implements ClientEndpoint {
    private RequestSpecification givenRequest;
    private CreateClient client;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeAll
    public static void setUpJpa() {
        entityManagerFactory = Persistence.createEntityManagerFactory("dbo");
    }

    @AfterAll
    public static void tearDownJpa() {
        entityManagerFactory.close();
    }

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

    @BeforeEach
    public void createNewClient() {
        final String login = UUID.randomUUID() + "@mail.ru";
        final String salt = "some-salt";
        final String secret = "749f09bade8aca7556749f09bade8aca7556";
        client = new CreateClient(login, salt, secret);
    }

    @Test
    public void shouldCreateAndGetClientById() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();

        givenRequest.
                when().
                get(CLIENT_ID, client.getId()).
                then().
                statusCode(HttpStatus.SC_OK).
                body("id", equalTo(client.getId().intValue()), "login", equalTo(client.getLogin()));

        entityManager.getTransaction().begin();
        final CreateClient clientSaved = entityManager.find(CreateClient.class, client.getId());
        assertEquals(client.getId(), clientSaved.getId());
        entityManager.remove(clientSaved);
        entityManager.getTransaction().commit();

        entityManager.close();
    }


    @Test
    public void shouldDeleteNewClient() {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(client);
        entityManager.getTransaction().commit();

        Long newClientId = client.getId();

        givenRequest.
                when().
                delete(CLIENT_ID, newClientId).
                then().
                statusCode(HttpStatus.SC_OK);

        entityManager.close();
    }

}
