module com.mycompany.pathfinder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires io.github.cdimascio.dotenv.java;

    opens com.mycompany.pathfinder to javafx.fxml;
    exports com.mycompany.pathfinder;
}
