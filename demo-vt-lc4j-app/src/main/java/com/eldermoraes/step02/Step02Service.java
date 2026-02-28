package com.eldermoraes.step02;

import com.eldermoraes.ai.SwAPIGenBot;
import com.eldermoraes.api.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class Step02Service {
    @Inject
    SwAPIGenBot swapiGenBot;

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

    public String create() {
        String people = peopleService.getRandomPeople().getName();
        String planet = planetService.getRandomPlanet().getName();
        String specie = specieService.getRandomSpecie().getName();
        String starship = starshipService.getRandomStarship().getName();
        String vehicle = vehicleService.getRandomVehicle().getName();

        return swapiGenBot.chat(1L, people, planet, specie, starship, vehicle);
    }
}
