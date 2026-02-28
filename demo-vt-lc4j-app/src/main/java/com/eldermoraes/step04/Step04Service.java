package com.eldermoraes.step04;

import com.eldermoraes.ai.SwAPIGenBot;
import com.eldermoraes.api.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
public class Step04Service {

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

    public String create() throws InterruptedException, ExecutionException {

        List<People> peopleList;
        List<Planet> planetList;
        List<Specie> specieList;
        List<Starship> starshipList;
        List<Vehicle> vehicleList;


        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {

            StructuredTaskScope.Subtask<List<People>> peopleSubtask = scope.fork(() -> {
                List<People> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(peopleService.getRandomPeople());
                }
                return list;
            });

            StructuredTaskScope.Subtask<List<Planet>> planetSubtask = scope.fork(() -> {
                List<Planet> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(planetService.getRandomPlanet());
                }
                return list;
            });

            StructuredTaskScope.Subtask<List<Specie>> specieSubtask = scope.fork(() -> {
                List<Specie> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(specieService.getRandomSpecie());
                }
                return list;
            });

            StructuredTaskScope.Subtask<List<Starship>> starshipSubtask = scope.fork(() -> {
                List<Starship> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(starshipService.getRandomStarship());
                }
                return list;
            });

            StructuredTaskScope.Subtask<List<Vehicle>> vehicleSubtask = scope.fork(() -> {
                List<Vehicle> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(vehicleService.getRandomVehicle());
                }
                return list;
            });

            scope.join();

            peopleList = peopleSubtask.get();
            planetList = planetSubtask.get();
            specieList = specieSubtask.get();
            starshipList = starshipSubtask.get();
            vehicleList = vehicleSubtask.get();
        }

        AtomicReference<String> response = new AtomicReference<>();

        try (var executor  = Executors.newVirtualThreadPerTaskExecutor()){
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
