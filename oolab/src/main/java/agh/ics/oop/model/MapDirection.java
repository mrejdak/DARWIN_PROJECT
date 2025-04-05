package agh.ics.oop.model;


public enum MapDirection {

    NORTH(new Vector2d(0, 1)),
    NORTH_EAST(new Vector2d(1, 1)),
    EAST(new Vector2d(1, 0)),
    SOUTH_EAST(new Vector2d(1,-1)),
    SOUTH(new Vector2d(0, -1)),
    SOUTH_WEST(new Vector2d(-1,-1)),
    WEST(new Vector2d(-1, 0)),
    NORTH_WEST(new Vector2d(-1,1));

    private final Vector2d unitVector;
    MapDirection(Vector2d unitVector){
        this.unitVector = unitVector;
    }


    @Override
    public String toString() {
        return switch(this) {
            case NORTH -> "North";
            case NORTH_EAST -> "North east";
            case EAST -> "East";
            case SOUTH_EAST -> "South east";
            case SOUTH -> "South";
            case SOUTH_WEST -> "South west";
            case WEST -> "West";
            case NORTH_WEST -> "North west";
        };
    }


    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}
