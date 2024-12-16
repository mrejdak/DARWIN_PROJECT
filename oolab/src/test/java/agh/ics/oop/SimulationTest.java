package agh.ics.oop;

import agh.ics.oop.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SimulationTest {

    @Test
    void testChangeDirectionRun() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);

        WorldMap map = new RectangularMap(4, 4);
        Simulation simulation = new Simulation(startingPoints, moveCommands, map);
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.getFirst().getDirection(), MapDirection.EAST);
    }

    @Test
    void testMovementOnlyRun() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);


        WorldMap map = new RectangularMap(5, 5);
        Simulation simulation = new Simulation(startingPoints, moveCommands, map);
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.getFirst().getPosition(), new Vector2d(2, 4));
    }

    @Test
    void testSingleAnimalRun() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.BACKWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 1);
        assertEquals(animals.getFirst().getDirection(), MapDirection.SOUTH);
        assertEquals(animals.getFirst().getPosition(), new Vector2d(1, 4));
    }

    @Test
    void testAnimalRunOutOfMap() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.getFirst().getDirection(), MapDirection.EAST);
        assertEquals(animals.getFirst().getPosition(), new Vector2d(0, 4));
    }

    @Test
    void testMultipleAnimalRunNoCollision() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(0, 0));
        startingPoints.add(new Vector2d(1, 4));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 3);

        assertEquals(animals.get(0).getDirection(), MapDirection.SOUTH);
        assertEquals(animals.get(1).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(2).getDirection(), MapDirection.EAST);

        assertEquals(animals.get(0).getPosition(), new Vector2d(3, 3));
        assertEquals(animals.get(1).getPosition(), new Vector2d(0, 0));
        assertEquals(animals.get(2).getPosition(), new Vector2d(1, 3));
    }

    @Test
    void testStringInputNoCollision() {
        String[] testInput = new String[]{"r", "b", "b", "l", "l", "f", "f", "f", "r"};
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = null;
        try {
            moveCommands = OptionsParser.parseOptions(testInput);
        } catch (Exception e) {
            fail("Unexpected Illegal Argument Exception");
        }

        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(3, 3));

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 2);
        assertEquals(animals.get(0).getDirection(), MapDirection.EAST);
        assertEquals(animals.get(1).getDirection(), MapDirection.WEST);
        assertEquals(animals.get(0).getPosition(), new Vector2d(1, 3));
        assertEquals(animals.get(1).getPosition(), new Vector2d(1, 2));
    }

    @Test
    void testMultipleAnimalsCreatedOnOnePosition() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(1, 4));


        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 2);
        assertEquals(animals.get(0).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(1).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(0).getPosition(), new Vector2d(2, 2));
        assertEquals(animals.get(1).getPosition(), new Vector2d(1, 4));
    }


    @Test
    void testDifferentSizeOfMap() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(0, 0));
        startingPoints.add(new Vector2d(1, 4));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(7, 7));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 3);

        assertEquals(animals.get(0).getDirection(), MapDirection.SOUTH);
        assertEquals(animals.get(1).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(2).getDirection(), MapDirection.EAST);

        assertEquals(animals.get(0).getPosition(), new Vector2d(3, 0));
        assertEquals(animals.get(1).getPosition(), new Vector2d(0, 3));
        assertEquals(animals.get(2).getPosition(), new Vector2d(5, 4));
    }

    @Test
    void testDifferentShapeOfMap() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(1, 3));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(3, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 2);

        assertEquals(animals.get(0).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(1).getDirection(), MapDirection.EAST);

        assertEquals(animals.get(0).getPosition(), new Vector2d(2, 4));
        assertEquals(animals.get(1).getPosition(), new Vector2d(2, 3));
    }


    @Test
    void testCollision() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(2, 3));
        moveCommands.add(MoveDirection.FORWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(3, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 2);

        assertEquals(animals.get(0).getDirection(), MapDirection.NORTH);
        assertEquals(animals.get(1).getDirection(), MapDirection.NORTH);

        assertEquals(animals.get(0).getPosition(), new Vector2d(2, 2));
        assertEquals(animals.get(1).getPosition(), new Vector2d(2, 3));
    }

    @Test
    void testMultipleAnimalsCollide() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(2, 3));
        startingPoints.add(new Vector2d(4, 3));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.BACKWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(5, 5));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 3);

        assertEquals(animals.get(0).getDirection(), MapDirection.EAST);
        assertEquals(animals.get(1).getDirection(), MapDirection.WEST);
        assertEquals(animals.get(2).getDirection(), MapDirection.NORTH);

        assertEquals(animals.get(0).getPosition(), new Vector2d(3, 1));
        assertEquals(animals.get(1).getPosition(), new Vector2d(3, 4));
        assertEquals(animals.get(2).getPosition(), new Vector2d(4, 4));
    }


    @Test
    void testAllCombined() {
        List<Vector2d> startingPoints = new ArrayList<>();
        List<MoveDirection> moveCommands = new ArrayList<>();


        startingPoints.add(new Vector2d(2, 2));
        startingPoints.add(new Vector2d(2, 3));
        startingPoints.add(new Vector2d(2, 3));
        startingPoints.add(new Vector2d(4, 3));
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.LEFT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.RIGHT);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.FORWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);
        moveCommands.add(MoveDirection.BACKWARD);

        Simulation simulation = new Simulation(startingPoints, moveCommands, new RectangularMap(7, 4));
        simulation.run();

        List<Animal> animals = simulation.getAnimals();

        assertEquals(animals.size(), 3);

        assertEquals(animals.get(0).getDirection(), MapDirection.EAST);
        assertEquals(animals.get(1).getDirection(), MapDirection.WEST);
        assertEquals(animals.get(2).getDirection(), MapDirection.NORTH);

        assertEquals(animals.get(0).getPosition(), new Vector2d(0, 1));
        assertEquals(animals.get(1).getPosition(), new Vector2d(6, 3));
        assertEquals(animals.get(2).getPosition(), new Vector2d(4, 0));
    }
}