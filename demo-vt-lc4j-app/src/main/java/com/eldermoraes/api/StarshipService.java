package com.eldermoraes.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient
public interface StarshipService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("random")
    public Starship getRandomStarship();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Starship> getAllStarships();
}
