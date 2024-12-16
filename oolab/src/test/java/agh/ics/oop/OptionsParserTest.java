package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {

    @Test
    void checkSingleParse() {
        String[] args = {"f"};

        List<MoveDirection> expected = List.of(MoveDirection.FORWARD);

        try {
            assertEquals(expected, OptionsParser.parseOptions(args));
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    void checkMultipleParses() {
        String[] args = {"f", "b", "l", "r"};

        List<MoveDirection> expected = List.of(MoveDirection.FORWARD, MoveDirection.BACKWARD,
                MoveDirection.LEFT, MoveDirection.RIGHT);

        try {
            assertEquals(expected, OptionsParser.parseOptions(args));
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    void checkRepeatingParses() {
        String[] args = {"f", "f", "b", "f"};

        List<MoveDirection> expected = List.of(MoveDirection.FORWARD, MoveDirection.FORWARD,
                MoveDirection.BACKWARD, MoveDirection.FORWARD);

        try {
            assertEquals(expected, OptionsParser.parseOptions(args));
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

    @Test
    void checkParsingException() {
        String[] args = {"f", "a", "b"};

        assertThrowsExactly(IllegalArgumentException.class, () -> OptionsParser.parseOptions(args));
    }

    @Test
    void checkParsingEmpty() {
        String[] args = {};

        List<MoveDirection> expected = List.of();

        try {
            assertEquals(expected, OptionsParser.parseOptions(args));
        } catch (Exception e) {
            fail("Unexpected Exception");
        }
    }

}