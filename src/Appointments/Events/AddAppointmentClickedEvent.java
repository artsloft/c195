package Appointments.Events;

import javafx.event.Event;
import javafx.event.EventType;

import java.util.Date;


public class AddAppointmentClickedEvent extends Event {

    public static final EventType<AddAppointmentClickedEvent> APPOINTMENT_CLICKED_EVENT_EVENT_TYPE = new EventType(ANY, "AddAppointmentEvent");

    private final Date selectedDate;

    public AddAppointmentClickedEvent(Date param) {
        super(APPOINTMENT_CLICKED_EVENT_EVENT_TYPE);
        this.selectedDate = param;
    }

    public Date GetSelectedDate(){
        return this.selectedDate;
    }

    /*public void invokeHandler(AddAppointmentClickedEventHandler handler) {
        handler.onEvent1(param);
    }*/

}