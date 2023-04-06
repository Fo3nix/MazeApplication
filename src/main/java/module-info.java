module org.maze.mazeapplication {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.maze.mazeapplication to javafx.fxml;
    exports org.maze.mazeapplication;
}