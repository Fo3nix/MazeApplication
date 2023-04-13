package org.maze.mazeapplication;

import javafx.scene.control.ScrollPane;
import org.maze.commons.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class MazeVisualizer extends Application {
    private static final int NODE_SIZE = 10;

    @Override
    public void start(Stage stage) {

        Maze maze = Maze.generateCircularMaze(20);
        Maze.generateMazePaths(maze);
        Scene scene = parseMaze(maze);

        // Set up the stage and show the scene
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    private Scene parseMaze(Maze maze) {
        // Determine the size of the maze
        int minX = maze.getMinX();
        int maxX = maze.getMaxX();
        int minY = maze.getMinY();
        int maxY = maze.getMaxY();

        int width = (maxX - minX + 1) * NODE_SIZE;
        int height = (maxY - minY + 1) * NODE_SIZE;

        // Create a JavaFX pane to hold the maze squares
        ScrollPane scrollPane = new ScrollPane();
        Pane pane = new Pane();
        scrollPane.setContent(pane);
        scrollPane.setPannable(true);

        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        pane.setPrefSize(width, height);

        MazeNode entrance = maze.getEntrance();
        MazeNode exit = maze.getExit();

        // Iterate over each MazeNode and create a corresponding rectangle in the pane
        for(int nodeX = minX; nodeX <= maxX; nodeX++) {
            for(int nodeY = minY; nodeY <= maxY; nodeY++) {
                MazeNode node = maze.getMazeNodeAt(nodeX, nodeY);

                if(node!=null) {
                    int x = (nodeX - minX) * NODE_SIZE;
                    int y = (nodeY - minY) * NODE_SIZE;
                    Rectangle rect = new Rectangle(x, y, NODE_SIZE, NODE_SIZE);
                    if (node.isWall()) {
                        rect.setFill(Color.BLACK);
                    }
                    else if (!node.isWall()){
                        rect.setFill(Color.BEIGE);
                    }
                    else if (node.equals(entrance)) {
                        rect.setFill(Color.GREEN);
                    }
                    else if (node.equals(exit)) {
                        rect.setFill(Color.PINK);
                    }
                    else {
                        rect.setFill(Color.WHITE);
                    }
                    pane.getChildren().add(rect);
                }
            }
        }

        // Create a JavaFX scene using the pane and return it
        Scene scene = new Scene(scrollPane);
        return scene;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
