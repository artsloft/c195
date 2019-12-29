package Appointments.Controller;

import Appointments.Events.AddAppointmentClickedEvent;
import Appointments.Events.AddAppointmentClickedEventHandler;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import Appointments.View.AppointmentsControl;


public class MainController implements Initializable
{
    /*@FXML
    private AnchorPane anchorPane;*/

    @FXML
    private AnchorPane anchor_pane;

    @FXML
    private AnchorPane bigCalendarPane;

    @FXML
    DatePicker date_picker;

    /*@FXML
    DatePicker bigCalendar;*/

    private StackPane[] calendarCells = new StackPane[35];

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date_picker = new DatePicker();
        date_picker.setVisible( false );
        date_picker.setManaged( false );
        date_picker.applyCss();
        final DatePickerSkin skin = new DatePickerSkin(date_picker);
        anchor_pane.getChildren().add(skin.getPopupContent());

        /*for(int i = 0; i < 7; i++) {
            calendarCells[i] = new StackPane();
            calendarCells[i].setPrefSize(116,116);
            calendarCells[i].setLayoutX(i * 116);
            calendarCells[i].getChildren().add(new Label(String.valueOf(i)));
            calendarCells[i].getChildren().get(0).setLayoutX(58);
            bigCalendarPane.getChildren().add(calendarCells[i]);
        }*/

        final AppointmentsControl appointmentsView = new AppointmentsControl();
        bigCalendarPane.getChildren().add(appointmentsView);

        date_picker.valueProperty().addListener((ov, oldValue, newValue) -> {
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.set(newValue.getYear(), newValue.getMonthValue() - 1, newValue.getDayOfMonth());
            //System.out.println(newValue.getMonthValue());

            System.out.println(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            System.out.println(calendar.get(Calendar.DAY_OF_WEEK));

            /*bigCalendar = new DatePicker();
            bigCalendar.setValue(newValue);
            bigCalendar.setMinSize(614,804);
            bigCalendar.setVisible( false );
            bigCalendar.setManaged( false );
            bigCalendar.applyCss();
            final DatePickerSkin bigSkin = new DatePickerSkin(bigCalendar);
            bigCalendarPane.getChildren().add(bigSkin.getPopupContent());*/

        });

        bigCalendarPane.addEventHandler(AddAppointmentClickedEvent.APPOINTMENT_CLICKED_EVENT_EVENT_TYPE, new AddAppointmentClickedEventHandler());/* {

            @Override
            public void onEvent1(int param0) {
                System.out.println("integer parameter: " + param0);
            }

            @Override
            public void onEvent2(String param0) {
                System.out.println("string parameter: "+param0);
            }
        });*/
    }


}
