package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

import java.util.ArrayList;

public class Earth extends AbstractWorldMap{

    public Earth(int width, int height){
        super(width, height);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.followsVertically(bounds.lowerLeft()) && position.precedesVertically(bounds.upperRight());
    }
}
