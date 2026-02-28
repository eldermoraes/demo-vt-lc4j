package com.eldermoraes.step05;

import com.eldermoraes.ai.SwAPIGenBot;
import com.eldermoraes.api.*;
import io.quarkus.runtime.Startup;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

@ApplicationScoped
@RunOnVirtualThread
public class Step05Service {

    static List<People> peopleList = new ArrayList<>();
    static List<Planet> planetList = new ArrayList<>();
    static List<Specie> specieList = new ArrayList<>();
    static List<Starship> starshipList = new ArrayList<>();
    static List<Vehicle> vehicleList = new ArrayList<>();

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


    @Startup
    void init() throws InterruptedException, ExecutionException {
        try (var scope = StructuredTaskScope.open(
                StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {

            StructuredTaskScope.Subtask<List<People>> peopleSubtask = scope.fork(() -> {
                return peopleService.getAllPeople();
            });

            StructuredTaskScope.Subtask<List<Planet>> planetSubtask = scope.fork(() -> {
                return planetService.getAllPlanets();
            });

            StructuredTaskScope.Subtask<List<Specie>> specieSubtask = scope.fork(() -> {
                return specieService.getAllSpecies();
            });

            StructuredTaskScope.Subtask<List<Starship>> starshipSubtask = scope.fork(() -> {
                return starshipService.getAllStarships();
            });

            StructuredTaskScope.Subtask<List<Vehicle>> vehicleSubtask = scope.fork(() -> {
                return vehicleService.getAllVehicles();
            });

            scope.join();

            peopleList = peopleSubtask.get();
            planetList = planetSubtask.get();
            specieList = specieSubtask.get();
            starshipList = starshipSubtask.get();
            vehicleList = vehicleSubtask.get();
        }
    }

    @Retry(maxRetries = 5, delay = 3000)
    @Timeout(value = 300000)
    public String create() {

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
