package com.eldermoraes.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface VehicleService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("random")
    public Vehicle getRandomVehicle();
}
