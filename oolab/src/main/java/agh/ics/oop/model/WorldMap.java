package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;
import agh.ics.oop.model.util.IncorrectPositionException;

import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;


public interface WorldMap extends MoveValidator {

    /**
     * Place an animal on the map.
     *
     * @param animal The animal to place on the map.
     * @return True if the animal was placed. The animal cannot be placed if the move is not valid.
     */
    void place(Animal animal) throws IncorrectPositionException;

    /**
     * Moves an animal (if it is present on the map) according to specified direction.
     * If the move is not possible, this method has no effect.
     */
    void move(Animal animal);

    /**
     * If the map variant has water, it changes the current tide
     */
    void changeTide();

    void newDay(int date);

    /**\
     * @param position given position on the map
     * @return True if there is water at given position
     */
    boolean isWaterPresent(Vector2d position);

    /**
     * Cleans deceased animals from the map.
     */
    void cleanDeadAnimals(HashSet<Vector2d> positions);

    /**
     * Function spawns new plants on the map.
     */
    void growPlants(int plantsPerDay);

    /**
     * Return true if given position on the map is occupied. Should not be
     * confused with canMove since there might be empty positions where the animal
     * cannot move.
     *
     * @param position Position to check.
     * @return True if the position is occupied.
     */
    boolean isOccupied(Vector2d position);

    /**
     * Return an animal at a given position.
     *
     * @param position The position of the animal.
     * @return animal or null if the position is not occupied.
     */
    WorldElement objectAt(Vector2d position);


    boolean plantAt(Vector2d position);

    void removePlant(Vector2d position);

    int[] getPreferredStrip();

    int getNumberOfAnimals();
    int getNumberOfPlants();

    CopyOnWriteArrayList<WorldElement> getElements();

    CopyOnWriteArrayList<Animal> getAnimalsAt(Vector2d position);

    Boundary getCurrentBounds();

    UUID getID();
}