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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.StructuredTaskScope.*;
import java.util.concurrent.atomic.AtomicReference;

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
    @Path("create-one")
    public Response createOne() {
        String people = peopleService.getRandomPeople().getName();
        String planet = planetService.getRandomPlanet().getName();
        String specie = specieService.getRandomSpecie().getName();
        String starship = starshipService.getRandomStarship().getName();
        String vehicle = vehicleService.getRandomVehicle().getName();

        return Response.ok(swapiGenBot.chat(1L, people, planet, specie, starship, vehicle)).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("create-10")
    public Response createTen() {
        String people = peopleService.getRandomPeople().getName();
        String planet = planetService.getRandomPlanet().getName();
        String specie = specieService.getRandomSpecie().getName();
        String starship = starshipService.getRandomStarship().getName();
        String vehicle = vehicleService.getRandomVehicle().getName();

        Map<String, String> spinMap = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            spinMap.put(String.valueOf(i), swapiGenBot.chat(1L, people, planet, specie, starship, vehicle));
        }

        return Response.ok(spinMap).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("create-10-vt")
    public Response createTenVt() {

        List<People> peopleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            peopleList.add(peopleService.getRandomPeople());
        }

        List<Planet> planetList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            planetList.add(planetService.getRandomPlanet());
        }

        List<Specie> specieList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            specieList.add(specieService.getRandomSpecie());
        }

        List<Starship> starshipList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            starshipList.add(starshipService.getRandomStarship());
        }

        List<Vehicle> vehicleList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            vehicleList.add(vehicleService.getRandomVehicle());
        }


        Map<Integer, String> spinMap = new ConcurrentHashMap<>();

        try (var executor  = Executors.newVirtualThreadPerTaskExecutor()){
            for (int i = 0; i < 10; i++) {
                final int key = i;
                executor.submit(() -> {
                    String people = peopleList.get(ThreadLocalRandom.current().nextInt(peopleList.size())).getName();
                    String planet = planetList.get(ThreadLocalRandom.current().nextInt(planetList.size())).getName();
                    String specie = specieList.get(ThreadLocalRandom.current().nextInt(specieList.size())).getName();
                    String starship = starshipList.get(ThreadLocalRandom.current().nextInt(starshipList.size())).getName();
                    String vehicle = vehicleList.get(ThreadLocalRandom.current().nextInt(vehicleList.size())).getName();

                    spinMap.put(key, swapiGenBot.chat(1L, people, planet, specie, starship, vehicle));
                });
            }
        }

        return Response.ok(spinMap).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("create-10-vt-full")
    public Response createTenVtFull() {

        Map<Integer, String> spinMap = new ConcurrentHashMap<>();

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

            for (int i = 0; i < 10; i++) {
                final int key = i;
                executor.submit(() -> {
                    String people = peopleList.get(ThreadLocalRandom.current().nextInt(peopleList.size())).getName();
                    String planet = planetList.get(ThreadLocalRandom.current().nextInt(planetList.size())).getName();
                    String specie = specieList.get(ThreadLocalRandom.current().nextInt(specieList.size())).getName();
                    String starship = starshipList.get(ThreadLocalRandom.current().nextInt(starshipList.size())).getName();
                    String vehicle = vehicleList.get(ThreadLocalRandom.current().nextInt(vehicleList.size())).getName();

                    spinMap.put(key, swapiGenBot.chat(1L, people, planet, specie, starship, vehicle));
                });
            }
        }

        return Response.ok(spinMap).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("create-10-vt-sc")
    public Response createTenVtSc() throws InterruptedException, ExecutionException {

        List<People> peopleList;
        List<Planet> planetList;
        List<Specie> specieList;
        List<Starship> starshipList;
        List<Vehicle> vehicleList;

        try(var scope = new StructuredTaskScope.ShutdownOnFailure()){
            Subtask<List<People>> peopleSubtask = scope.fork(() -> {
                List<People> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(peopleService.getRandomPeople());
                }
                return list;
            });

            Subtask<List<Planet>> planetSubtask = scope.fork(() -> {
                List<Planet> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(planetService.getRandomPlanet());
                }
                return list;
            });

            Subtask<List<Specie>> specieSubtask = scope.fork(() -> {
                List<Specie> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(specieService.getRandomSpecie());
                }
                return list;
            });

            Subtask<List<Starship>> starshipSubtask = scope.fork(() -> {
                List<Starship> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(starshipService.getRandomStarship());
                }
                return list;
            });

            Subtask<List<Vehicle>> vehicleSubtask = scope.fork(() -> {
                List<Vehicle> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add(vehicleService.getRandomVehicle());
                }
                return list;
            });

            scope.join();
            scope.throwIfFailed();

            peopleList = peopleSubtask.get();
            planetList = planetSubtask.get();
            specieList = specieSubtask.get();
            starshipList = starshipSubtask.get();
            vehicleList = vehicleSubtask.get();
        }

        Map<Integer, String> spinMap = new ConcurrentHashMap<>();

        try (var executor  = Executors.newVirtualThreadPerTaskExecutor()){
            for (int i = 0; i < 10; i++) {
                final int key = i;
                executor.submit(() -> {
                    String people = peopleList.get(ThreadLocalRandom.current().nextInt(peopleList.size())).getName();
                    String planet = planetList.get(ThreadLocalRandom.current().nextInt(planetList.size())).getName();
                    String specie = specieList.get(ThreadLocalRandom.current().nextInt(specieList.size())).getName();
                    String starship = starshipList.get(ThreadLocalRandom.current().nextInt(starshipList.size())).getName();
                    String vehicle = vehicleList.get(ThreadLocalRandom.current().nextInt(vehicleList.size())).getName();

                    spinMap.put(key, swapiGenBot.chat(1L, people, planet, specie, starship, vehicle));
                });
            }
        }

        return Response.ok(spinMap).build();
    }




}
