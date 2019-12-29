package Appointments.View;

import javafx.scene.control.Control;

import java.util.Date;

public class AppointmentsControl extends Control{
    private static final String DEFAULT_STYLE_CLASS = "appointments-view";
    private Date date;

    public AppointmentsControl(Date preset) {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        this.date = preset;
    }

    public AppointmentsControl() {
        this(new Date(System.currentTimeMillis()));
    }

    @Override
    public String getUserAgentStylesheet() {
        return "Appointments/Styles/appointmentsControl.css";
    }

    public Date getDate() {
        return date;
    }
}
