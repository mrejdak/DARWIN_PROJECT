package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LowAndHighTidesTest {

    @Test
    void getElementsAndPlace() {
        WorldMap tideMap = new LowAndHighTides(10, 10);
        Animal animal1 = new Animal(new Vector2d(2, 2), 1,10);
        Animal animal2 = new Animal(new Vector2d(2, 2), 1,10);

        assertDoesNotThrow(() -> tideMap.place(animal1));

        assertDoesNotThrow(() -> tideMap.place(animal2));

        assertEquals(tideMap.getElements().size(), 12);
    }
}