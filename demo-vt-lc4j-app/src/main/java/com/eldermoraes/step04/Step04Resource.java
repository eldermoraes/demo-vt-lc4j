package com.eldermoraes.step04;

import com.eldermoraes.api.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.*;

@Path("step04")
@Produces(MediaType.TEXT_PLAIN)
public class Step04Resource {

    @Inject
    Step04Service service;

    @GET
    public Response create() throws InterruptedException, ExecutionException {
        return Response.ok(service.create()).build();
    }

}
