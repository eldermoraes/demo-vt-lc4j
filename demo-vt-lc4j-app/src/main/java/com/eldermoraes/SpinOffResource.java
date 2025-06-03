package com.eldermoraes;

import com.eldermoraes.api.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("spin-offs")
@RequestScoped
public class SpinOffResource {

    @Inject
    SwapiGenBot swapiGenBot;

    @RestClient
    PeopleService peopleService;

    @RestClient
    PlanetService planetService;

    @RestClient
    SpecieService specieService;

    @RestClient
    StarshipService starshipService;

    @RestClient
    VehicleService vehicleService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response createSpinOff() {
        String people = peopleService.getRandomPeople().getName();
        String planet = planetService.getRandomPlanet().getName();
        String specie = specieService.getRandomSpecie().getName();
        String starship = starshipService.getRandomStarship().getName();
        String vehicle = vehicleService.getRandomVehicle().getName();

        return Response.ok(swapiGenBot.chat(people, planet, specie, starship, vehicle)).build();
    }
}
