package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> animals;
    private final WorldMap map;

    public Simulation(List<Vector2d> startingPoints, WorldMap map) {

        this.map = map;
        this.animals = new ArrayList<>();

        placeAnimals(startingPoints);
    }

    @Override
    public void run(){

        while(animals.get(0).getEnergyLevel() > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            map.move(animals.get(0));

            System.out.printf("Animal %d: %s%n", 0,
                    animals.get(0).getPosition());

        }
    }

    private void placeAnimals(List<Vector2d> startingPoints){
        for (Vector2d point : startingPoints) {
            Animal animal = new Animal(point, 10);
            System.out.println(Arrays.toString(animal.getGenes().getGenesSequence()));
            try {
                map.place(animal);
                animals.add(animal);
            }
            catch(IncorrectPositionException e){
                // nothing, because the program will continue without adding the animal
            }
        }
    }
    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
