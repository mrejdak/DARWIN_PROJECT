package agh.ics.oop.presenter;

import agh.ics.oop.Simulation;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.model.*;
import agh.ics.oop.model.util.Boundary;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.Collection;
import java.util.List;

import static agh.ics.oop.OptionsParser.parseOptions;

public class SimulationPresenter implements MapChangeListener {

    @FXML
    private Label infoLabel;
    @FXML
    private TextField moveListField;
    @FXML
    private Label moveDescription;
    @FXML
    private GridPane mapGrid;

    private int xMin;
    private int yMin;
    private int xMax;
    private int yMax;
    private int mapWidth;
    private int mapHeight;
    private WorldMap worldMap;
    private static final int CELL_WIDTH = 30;
    private static final int CELL_HEIGHT = 30;


    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }


    private void drawMap() {
        clearGrid();
        updateBoundaries();
        gridColumns();
        gridRows();
        addElements();

        infoLabel.setText(worldMap.toString());
    }


    private void updateBoundaries(){
        Boundary bounds =  worldMap.getCurrentBounds();
        xMin = bounds.lowerLeft().getX();
        yMin = bounds.lowerLeft().getY();
        xMax = bounds.upperRight().getX();
        yMax = bounds.upperRight().getY();
        mapWidth = xMax - xMin + 1;
        mapHeight = yMax - yMin + 1;
        System.out.println(bounds.upperRight());
        System.out.println(bounds.lowerLeft());
        System.out.println(mapWidth);
        System.out.println(mapHeight);
    }


    private void gridColumns(){
        mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
        for(int i=0; i<mapWidth; i++){
            Label label = new Label(Integer.toString(i + xMin));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getColumnConstraints().add(new ColumnConstraints(CELL_WIDTH));
            mapGrid.add(label, i+1, 0);
        }
    }

    private void gridRows(){
        mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
        for(int i=0; i<mapHeight; i++){
            Label label = new Label(Integer.toString(yMax - i));
            GridPane.setHalignment(label, HPos.CENTER);
            mapGrid.getRowConstraints().add(new RowConstraints(CELL_HEIGHT));
            mapGrid.add(label, 0, i+1);
        }
    }

    private void addElements(){

        Collection<WorldElement> elements = worldMap.getElements();
        for (WorldElement element : elements) {
            Vector2d pos = element.getPosition();
            Label elementLabel = new Label(worldMap.objectAt(pos).toString());
            mapGrid.add(elementLabel, pos.getX() - xMin + 1, yMax - pos.getY() + 1);
            GridPane.setHalignment(elementLabel, HPos.CENTER);
        }
    }


    private void clearGrid() {
        mapGrid.getChildren().retainAll(mapGrid.getChildren().get(0)); // hack to retain visible grid lines
        mapGrid.getColumnConstraints().clear();
        mapGrid.getRowConstraints().clear();
    }


    @Override
    public void mapChanged(WorldMap worldMap, String message) {
        Platform.runLater(() -> {
            drawMap();
            moveDescription.setText(message);
        });
    }


    @FXML
    public void onSimulationStartClicked(){
        try {
            String moveInput = moveListField.getText();
            List<MoveDirection> directions = parseOptions(moveInput.split(" "));
            List<Vector2d> positions = List.of(new Vector2d(2, 2), new Vector2d(3, 4));
            AbstractWorldMap grassMap = new GrassField(10);
            grassMap.addObserver(this);
            this.setWorldMap(grassMap);

            Simulation simulation = new Simulation(positions, grassMap);
            SimulationEngine engine = new SimulationEngine(List.of(simulation));
            engine.runAsync();
        }
        catch (IllegalArgumentException e){
            System.out.println("Error: " + e.getMessage());
        }

    }
}
