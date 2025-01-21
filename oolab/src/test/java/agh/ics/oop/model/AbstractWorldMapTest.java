package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static agh.ics.oop.model.util.AnimalCleaner.cleanDeadAnimals;
import static org.junit.jupiter.api.Assertions.*;

class AbstractWorldMapTest {

    @Test
    void testMove() {
        AbstractWorldMap map = new Earth(10, 10);
        Animal animal1 = new Animal(new Vector2d(2, 2), 10, 10);
        assertDoesNotThrow(() -> map.place(animal1));
        map.move(animal1);
        assertNotEquals(new Vector2d(2,2), animal1.getPosition());
        assertEquals(animal1.getGeneTracker(), 1);
    }

    @Test
    void testCleanDeadAnimals() {
        AbstractWorldMap map = new Earth(10, 10);
        Animal animal1 = new Animal(new Vector2d(2, 2), 10, 1);
        assertDoesNotThrow(() -> map.place(animal1));
        ArrayList<Animal> animals = new ArrayList<>(List.of(animal1));
        map.move(animal1);
        cleanDeadAnimals(animals, map);
        assertEquals(animals.size(), 0);
    }

    @Test
    void testGrowPlantsAndRemovePlants() {
        AbstractWorldMap map = new Earth(10, 10);
        map.growPlants(5);
        assertEquals(map.getElements().size(), 5);
        for (WorldElement plant : map.getElements()){
            map.removePlant(plant.getPosition());
        }
        assertEquals(map.getElements().size(), 0);
    }

}