module org.project.lacliniqa {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires java.sql;
    requires MaterialFX;
    requires java.prefs;


    opens org.project.lacliniqa to javafx.fxml;
    exports org.project.lacliniqa;
    exports org.project.lacliniqa.controllers;
    opens org.project.lacliniqa.controllers to javafx.fxml;
}