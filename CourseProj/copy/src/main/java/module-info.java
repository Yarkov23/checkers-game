module com.example.checkergame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.checkergame to javafx.fxml;
    exports com.example.checkergame;
}