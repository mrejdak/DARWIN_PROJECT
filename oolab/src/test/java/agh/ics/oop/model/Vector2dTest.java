package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {

    @Test
    void isToStringWorking() {
        Vector2d v = new Vector2d(2, 3);

        assertEquals("(2, 3)", v.toString());
        assertNotEquals("2, 3", v.toString());
    }

    @Test
    void isVectorWithSmallerCoordinatesPreceding() {
        Vector2d precededVector = new Vector2d(2, 3);
        Vector2d precedingVector = new Vector2d(1, 2);
        Vector2d otherVector = new Vector2d(3, 1);

        assertTrue(precededVector.precedes(precededVector));
        assertTrue(precedingVector.precedes(precededVector));
        assertFalse(otherVector.precedes(precedingVector));
        assertFalse(precededVector.precedes(precedingVector));
    }

    @Test
    void isVectorWithBiggerCoordinatesFollowing() {
        Vector2d followingVector = new Vector2d(2, 3);
        Vector2d followedVector = new Vector2d(1, 2);
        Vector2d otherVector = new Vector2d(3, 1);

        assertTrue(followingVector.follows(followingVector));
        assertTrue(followingVector.follows(followedVector));
        assertFalse(otherVector.follows(followedVector));
        assertFalse(followedVector.follows(followingVector));
    }

    @Test
    void doVectorsCoordinatesGetAdded() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(3, 4);
        assertEquals(v3, v1.add(v2));
        assertNotEquals(v1, v2.add(v3));
    }

    @Test
    void doVectorsCoordinatesGetSubtracted() {
        Vector2d v1 = new Vector2d(1, 1);
        Vector2d v2 = new Vector2d(2, 3);
        Vector2d v3 = new Vector2d(-1, -2);
        assertEquals(v3, v1.subtract(v2));
        assertNotEquals(v2, v1.subtract(v3));
    }

    @Test
    void willPointBeUpperRight() {
        Vector2d v1 = new Vector2d(1, 0);
        Vector2d v2 = new Vector2d(0, 2);
        Vector2d v3 = new Vector2d(1, 2);

        assertEquals(v3, v1.upperRight(v2));
        assertNotEquals(v2, v1.upperRight(v3));
    }

    @Test
    void willPointBeLowerLeft() {
        Vector2d v1 = new Vector2d(1, 0);
        Vector2d v2 = new Vector2d(0, 2);
        Vector2d v3 = new Vector2d(0, 0);

        assertEquals(v1.lowerLeft(v2), v3);
        assertNotEquals(v1.lowerLeft(v3), v2);
    }

    @Test
    void arePointsOpposite() {
        Vector2d v1 = new Vector2d(-1, 2);
        Vector2d v2 = new Vector2d(1, -2);

        assertEquals(v1, v2.opposite());
        assertNotEquals(v1.opposite(), v2.opposite());
    }

    @Test
    void checkEquality() {
        Vector2d v1 = new Vector2d(1, 2);
        Vector2d v2 = new Vector2d(1, 2);
        Vector2d v3 = new Vector2d(2, 2);
        Integer not_v1 = null;
        String not_v2 = "słabo";

        assertEquals(v1, v2);
        assertNotEquals(v1, v3);
        assertNotEquals(v1, not_v1);
        assertNotEquals(v2, not_v2);
    }
}