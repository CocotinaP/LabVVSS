module drinkshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;

    exports drinkshop.repository;
    exports drinkshop.service;
    exports drinkshop.service.validator;

    requires java.base;

    requires org.controlsfx.controls;

    opens drinkshop.ui to javafx.fxml;
    exports drinkshop.ui;

    opens drinkshop.domain to  javafx.base;
    exports drinkshop.domain;
    opens drinkshop.repository to org.mockito;
}