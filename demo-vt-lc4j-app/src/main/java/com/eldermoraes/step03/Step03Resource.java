package com.eldermoraes.step03;

import com.eldermoraes.api.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("step03")
@Produces(MediaType.TEXT_PLAIN)
public class Step03Resource {

    @Inject
    Step03Service service;

    @GET
    public Response create() {
        return Response.ok(service.create()).build();
    }
}
