module Lab5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.prefs;
    requires java.desktop;

    exports Client;
    opens Controller to javafx.fxml;

}