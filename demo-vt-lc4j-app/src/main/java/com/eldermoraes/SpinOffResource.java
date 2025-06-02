package com.eldermoraes;

import com.eldermoraes.api.PeopleService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Path("spin-offs")
public class SpinOffResource {

    @Inject
    SwapiGenBot swapiGenBot;

    @RestClient
    PeopleService peopleService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response createSpinOff() {
        String people = peopleService.getRandomPeople().getName();
        String planet = "Tatooine";
        String specie = "Human";
        String starship = "X-wing";
        String vehicle = "Speeder";

        return Response.ok(swapiGenBot.chat(people, planet, specie, starship, vehicle)).build();
    }
}
