package agh.ics.oop.model.util;

import agh.ics.oop.model.Vector2d;

public class IncorrectPositionException extends Exception{
    public IncorrectPositionException(Vector2d reason){
        super("Position " + reason + " is not correct");
    }
}
