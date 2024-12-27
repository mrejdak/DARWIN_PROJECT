package agh.ics.oop.model;


public enum MapDirection {

    //Changed the unit vector functionality a bit

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

    private static final Vector2d[] vectorList = {
            new Vector2d(0, 1),
            new Vector2d(1, 1),
            new Vector2d(1, 0),
            new Vector2d(0, -1),
            new Vector2d(-1, 0)
    };

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

    public MapDirection next() {
        return this;
//        return switch (this) {
//            case NORTH -> EAST;
//            case EAST -> SOUTH;
//            case SOUTH -> WEST;
//            case WEST -> NORTH;
//        };
    }

    public MapDirection previous() {
        return this;
//        return switch (this) {
//            case NORTH -> WEST;
//            case WEST -> SOUTH;
//            case SOUTH -> EAST;
//            case EAST -> NORTH;
//        };
    }

    public Vector2d toUnitVector() {
        return this.unitVector;
    }
}
