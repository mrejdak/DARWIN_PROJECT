package agh.ics.oop.model;

import agh.ics.oop.model.util.IncorrectPositionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangularMapTest {
    @Test
    void testPlace() {
        WorldMap rectangularMap = new RectangularMap(4, 4);
        Animal animal1 = new Animal(new Vector2d(2, 2), 1,10);
        Animal animal2 = new Animal(new Vector2d(2, 2), 1,10);

        assertDoesNotThrow(() -> rectangularMap.place(animal1));

        assertThrowsExactly(IncorrectPositionException.class, () -> rectangularMap.place(animal2));
    }

    @Test
    void testMove() {
        WorldMap rectangularMap = new RectangularMap(5,5);

        Vector2d position1 = new Vector2d(3, 3);
        Vector2d position2 = new Vector2d(0, 3);

        Animal animal1 = new Animal(position1, 1,10);
        Animal animal2 = new Animal(position2, 1,10);

        try {
            rectangularMap.place(animal1);
            rectangularMap.place(animal2);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

//        rectangularMap.move(animal1, MoveDirection.FORWARD);
//        rectangularMap.move(animal1, MoveDirection.FORWARD);
//
//        rectangularMap.move(animal2, MoveDirection.FORWARD);
//        rectangularMap.move(animal2, MoveDirection.LEFT);
//        rectangularMap.move(animal2, MoveDirection.BACKWARD);


        assertEquals(new Vector2d(3,4), animal1.getPosition());
        assertEquals(new Vector2d(1, 4), animal2.getPosition());
    }

    @Test
    void testIsOccupied() {
        WorldMap rectangularMap = new RectangularMap(4,4);

        Vector2d position1 = new Vector2d(3, 3);
        Vector2d position2 = new Vector2d(0, 3);

        Animal animal1 = new Animal(position1, 1,10);

        try {
            rectangularMap.place(animal1);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        assertTrue(rectangularMap.isOccupied(position1));
        assertFalse(rectangularMap.isOccupied(position2));
    }

    @Test
    void testObjectAt() {
        WorldMap rectangularMap = new RectangularMap(15, 15);

        Vector2d position1 = new Vector2d(11, 11);
        Vector2d position2 = new Vector2d(12, 13);

        Animal animal1 = new Animal(position1, 1,10);

        try {
            rectangularMap.place(animal1);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        assertEquals(rectangularMap.objectAt(position1), animal1);
        assertNull(rectangularMap.objectAt(position2));
    }

    @Test
    void testCanMoveTo() {
        WorldMap rectangularMap = new RectangularMap(5, 5);

        Vector2d position3 = new Vector2d(8,8);
        Vector2d position1 = new Vector2d(4,4);
        Vector2d position2 = new Vector2d(2,2);

        Animal animal1 = new Animal(position1, 1,10);
        try {
            rectangularMap.place(animal1);
        } catch (IncorrectPositionException e) {
            fail("Unexpected Incorrect Animal Position");
        }

        assertFalse(rectangularMap.canMoveTo(position1));
        assertTrue(rectangularMap.canMoveTo(position2));
        assertFalse(rectangularMap.canMoveTo(position3));
    }
}