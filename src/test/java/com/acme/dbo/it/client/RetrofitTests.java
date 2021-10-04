package com.acme.dbo.it.client;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.jupiter.api.Assertions.*;

public class RetrofitTests {
    private ClientService service;
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
    public void setUp() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .baseUrl("http://localhost:8080/dbo/api/")
                .build();

        service = retrofit.create(ClientService.class);
    }

    @Test
    public void shouldPost() throws IOException {
        String email = UUID.randomUUID() + "@mail.ru";
        String salt = "some-salt";
        String secret = "749f09bade8aca7556749f09bade8aca7556";

        Response<CreateClient> execute = service.createClient(new CreateClient(email, salt, secret)).execute();
        assertEquals(SC_CREATED, execute.code());
        assertEquals(email, execute.body().getLogin());
        assertEquals(salt, execute.body().getSalt());
        assertEquals(secret, execute.body().getSecret());

        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        final CreateClient clientSaved = entityManager.find(CreateClient.class, execute.body().getId());
        entityManager.remove(clientSaved);
        entityManager.getTransaction().commit();

        entityManager.close();

    }

    @Test
    public void shouldGetAllClients() throws IOException {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();

        final List<Client> clients = entityManager.createQuery("SELECT c FROM testClient c", Client.class).getResultList();
        MatcherAssert.assertThat(clients, Matchers.allOf(
                hasItem(hasProperty("login", is("admin@acme.com"))),
                hasItem(hasProperty("login", is("account@acme.com"))),
                hasItem(hasProperty("login", is("disabled@acme.com")))
        ));

    }
}
