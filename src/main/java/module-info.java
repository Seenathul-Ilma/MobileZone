module lk.ijse.gdse71.mobilezone {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires static lombok;
    requires java.mail;
    requires net.sf.jasperreports.core;

    opens lk.ijse.gdse71.mobilezone.controller to javafx.fxml;
    //opens lk.ijse.gdse71.mobilezone.dto.tm to java.base;
    opens lk.ijse.gdse71.mobilezone.dto.tm to javafx.base;
    exports lk.ijse.gdse71.mobilezone;
}