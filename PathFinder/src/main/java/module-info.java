module com.mycompany.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.pathfinder to javafx.fxml;
    exports com.mycompany.pathfinder;
}
