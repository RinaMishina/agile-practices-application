package com.acme.dbo.it.client;

public interface ClientEndpoint {
    String BASE_URL = "http://localhost";
    int PORT = 8080;
    String DBO_API = "/dbo/api/";
    String CLIENT_ID = "client/{id}";
    String HEADER_NAME = "X-API-VERSION";
    String HEADER_VALUE = "1";
    String CLIENT = "client";
}
