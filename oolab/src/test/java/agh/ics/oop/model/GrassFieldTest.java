package agh.ics.oop.model;

import agh.ics.oop.model.util.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GrassFieldTest {

    @Test
    void testPlace() {
        WorldMap grassMap = new GrassField(10);
        Animal animal1 = new Animal(new Vector2d(2, 2));
        Animal animal2 = new Animal(new Vector2d(2, 2));

        assertDoesNotThrow(() -> grassMap.place(animal1));

        assertThrows(IncorrectPositionException.class,() -> grassMap.place(animal2));
    }

    @Test
    void testMove() {
        WorldMap grassMap = new GrassField(10);

        Vector2d position1 = new Vector2d(1, 3);
        Vector2d position2 = new Vector2d(0, 3);

        Animal animal1 = new Animal(position1);
        Animal animal2 = new Animal(position2);

        try {
            grassMap.place(animal1);
            grassMap.place(animal2);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        grassMap.move(animal1, MoveDirection.RIGHT);
        grassMap.move(animal2, MoveDirection.FORWARD);
        grassMap.move(animal2, MoveDirection.LEFT);
        grassMap.move(animal2, MoveDirection.BACKWARD);


        assertEquals(position1, animal1.getPosition());
        assertEquals(new Vector2d(1, 4), animal2.getPosition());
    }

    @Test
    void testIsOccupied() {
        WorldMap grassMap = new GrassField(10);

        int counter = 0;

        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                if(grassMap.isOccupied(new Vector2d(i,j))) {
                    counter += 1;
                }
            }
        }

        assertEquals(10, counter);
    }

    @Test
    void testObjectAt() {
        WorldMap grassMap = new GrassField(10);

        int counter = 0;

        Vector2d position1 = new Vector2d(11, 11);

        Animal animal1 = new Animal(position1);

        try {
            grassMap.place(animal1);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        for(int i=0; i<12; i++){
            for(int j=0; j<12; j++){
                if(grassMap.objectAt(new Vector2d(i,j)) != null) {
                    counter += 1;
                }
            }
        }
        assertEquals(grassMap.objectAt(position1), animal1);
        assertEquals(11, counter);
    }

    @Test
    void testCanMoveTo() {
        WorldMap grassMap = new GrassField(5);

        Vector2d position1 = new Vector2d(8,8);
        Vector2d position2 = new Vector2d(2,2);

        Animal animal1 = new Animal(position1);
        try {
            grassMap.place(animal1);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        assertFalse(grassMap.canMoveTo(position1));
        assertTrue(grassMap.canMoveTo(position2));
    }
}