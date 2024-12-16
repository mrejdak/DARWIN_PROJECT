package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import agh.ics.oop.model.MoveDirection;

public class OptionsParser {
    public static List<MoveDirection> parseOptions(String[] directions) {

        List<MoveDirection> moveDirections = new ArrayList<>();

        for (String arg : directions) {
            switch (arg) {
                case "f", "forward" -> moveDirections.add(MoveDirection.FORWARD);
                case "b", "backward" -> moveDirections.add(MoveDirection.BACKWARD);
                case "r", "right" -> moveDirections.add(MoveDirection.RIGHT);
                case "l", "left" -> moveDirections.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not a legal move specification");
            }
        }
        return moveDirections;
    }
}
