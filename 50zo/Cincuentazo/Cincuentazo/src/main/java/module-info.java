module com.cincuentazo {
    requires javafx.controls;
    requires javafx.fxml;

    exports com.cincuentazo;
    opens com.cincuentazo.controller to javafx.fxml;
    opens com.cincuentazo.model to javafx.base;
}
