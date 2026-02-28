package com.eldermoraes.step02;

import com.eldermoraes.api.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("step02")
@Produces(MediaType.TEXT_PLAIN)
public class Step02Resource {

    @Inject
    Step02Service service;

    @GET
    public Response create() {
        return Response.ok(service.create()).build();
    }
}
