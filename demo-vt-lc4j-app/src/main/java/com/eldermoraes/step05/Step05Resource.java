package com.eldermoraes.step05;

import com.eldermoraes.api.*;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.concurrent.*;

@Path("step05")
@Produces(MediaType.TEXT_PLAIN )
@RunOnVirtualThread
public class Step05Resource {

    @Inject
    Step05Service service;

    @GET
    public Response create() {
        return Response.ok(service.create()).build();
    }
}