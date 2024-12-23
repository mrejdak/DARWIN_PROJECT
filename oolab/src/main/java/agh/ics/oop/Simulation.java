package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable{

    private final List<Animal> animals;
    private final List<MoveDirection> moveCommands;
    private final WorldMap map;

    public Simulation(List<Vector2d> startingPoints, List<MoveDirection> moveCommands, WorldMap map) {

        this.moveCommands = moveCommands;
        this.map = map;
        this.animals = new ArrayList<>();

        for (Vector2d point : startingPoints) {
            Animal animal = new Animal(point, 5,10);
            try {
                map.place(animal);
                this.animals.add(animal);
            }
            catch(IncorrectPositionException e){
                // nothing, because the program will continue without adding the animal
            }
        }
    }

    @Override
    public void run(){
        int currentAnimalIndex = 0;

        for (MoveDirection direction : this.moveCommands) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                System.out.println("InterruptedException: " + e.getMessage());
            }
            this.map.move(animals.get(currentAnimalIndex), direction);

            System.out.printf("Animal %d: %s%n", currentAnimalIndex,
                    this.animals.get(currentAnimalIndex).getPosition());
            currentAnimalIndex = (currentAnimalIndex + 1) % this.animals.size();
        }
    }

    public List<Animal> getAnimals() {
        return new ArrayList<>(animals);
    }
}
