module com.mycompany.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.pathfinder to javafx.fxml;
    exports com.mycompany.pathfinder;
}
