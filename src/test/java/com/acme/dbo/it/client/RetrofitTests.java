package com.acme.dbo.it.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.UUID;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.jupiter.api.Assertions.*;


public class RetrofitTests {
    private ClientService service;

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

    }

    @Test
    public void shouldGetAllClients() throws IOException {
        int size = service.getClients().execute().body().size();
        assertTrue(size > 0);
    }
}
