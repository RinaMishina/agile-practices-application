package com.acme.dbo.it.client;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientService {
    @GET("client")
    @Headers("X-API-VERSION:1")
    Call<List<Client>> getClients();

    @POST("client")
    @Headers("X-API-VERSION:1")
    Call<CreateClient> createClient(@Body CreateClient createClient);
}
