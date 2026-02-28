package com.eldermoraes.step03;

import com.eldermoraes.ai.SwAPIGenBot;
import com.eldermoraes.api.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class Step03Service {

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

        AtomicReference<String> response = new AtomicReference<>();

        try (var executor  = Executors.newVirtualThreadPerTaskExecutor()){

            List<People> peopleList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    peopleList.add(peopleService.getRandomPeople());
                });
            }

            List<Planet> planetList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    planetList.add(planetService.getRandomPlanet());
                });
            }

            List<Specie> specieList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    specieList.add(specieService.getRandomSpecie());
                });
            }

            List<Starship> starshipList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    starshipList.add(starshipService.getRandomStarship());
                });
            }

            List<Vehicle> vehicleList = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                executor.submit(() -> {
                    vehicleList.add(vehicleService.getRandomVehicle());
                });
            }


            executor.submit(() -> {
                String people = peopleList.get(ThreadLocalRandom.current().nextInt(peopleList.size())).getName();
                String planet = planetList.get(ThreadLocalRandom.current().nextInt(planetList.size())).getName();
                String specie = specieList.get(ThreadLocalRandom.current().nextInt(specieList.size())).getName();
                String starship = starshipList.get(ThreadLocalRandom.current().nextInt(starshipList.size())).getName();
                String vehicle = vehicleList.get(ThreadLocalRandom.current().nextInt(vehicleList.size())).getName();

                response.set(swapiGenBot.chat(1L, people, planet, specie, starship, vehicle));
            });
        }

        return response.get();
    }
}
