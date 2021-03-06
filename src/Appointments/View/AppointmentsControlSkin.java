package Appointments.View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

import Appointments.Events.AddAppointmentClickedEvent;

public class AppointmentsControlSkin  extends SkinBase<AppointmentsControl> {
    private final Date date;
    private final Label month;
    private final BorderPane content;
    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMMM yyyy");

    private static class CalendarCell extends StackPane {

        private final Date date;

        public CalendarCell(Date day, String text) {
            this.date = day;
            Label label = new Label(text);
            getChildren().add(label);
        }

        public Date getDate() {
            return date;
        }
    }

    public AppointmentsControlSkin(AppointmentsControl appointmentsControl) {
        super(appointmentsControl);
        // this date is the selected date
        date = appointmentsControl.getDate();
        final DatePickerPane calendarPane = new DatePickerPane(date);


        month = new Label(simpleDateFormat.format(calendarPane.getShownMonth()));
        HBox hbox = new HBox();

        // create the navigation Buttons
        Button yearBack = new Button("<<");
        yearBack.setOnAction(ae -> {
            calendarPane.forward(-12);
        });
        Button monthBack = new Button("<");
        monthBack.setOnAction(ae -> {
            calendarPane.forward(-1);
        });
        Button monthForward = new Button(">");
        monthForward.setOnAction(ae -> {
            calendarPane.forward(1);
        });
        Button yearForward = new Button(">>");
        yearForward.setOnAction(ae -> {
            calendarPane.forward(12);
        });

        // center the label and make it grab all free space
        HBox.setHgrow(month, Priority.ALWAYS);
        month.setMaxWidth(Double.MAX_VALUE);
        month.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(yearBack, monthBack, month, monthForward, yearForward);

        // use a BorderPane to Layout the view
        content = new BorderPane();
        getChildren().add(content);
        content.setTop(hbox);
        content.setCenter(calendarPane);
    }


    class DatePickerPane extends Region {

        private final Date selectedDate;
        private final Calendar cal;
        private CalendarCell selectedDayCell;
        // this is used to format the day cells
        private final SimpleDateFormat sdf = new SimpleDateFormat("d");
        // empty cell header of weak-of-year row
        private final CalendarCell woyCell = new CalendarCell(new Date(), "");
        private int rows, columns;//default

        public DatePickerPane(Date date) {
            setPrefSize(804, 604);
            woyCell.getStyleClass().add("week-of-year-cell");
            setPadding(new Insets(5, 0, 5, 0));
            this.columns = 7;
            this.rows = 5;

            // use a copy of Date, because it's mutable
            // we'll helperDate it through the month
            cal = Calendar.getInstance();
            Date helperDate = new Date(date.getTime());
            cal.setTime(helperDate);

            // the selectedDate is the date we will change, when a date is picked
            selectedDate = date;
            refresh();
        }

        /**
         Move forward the specified number of Months, move backward by using
         negative numbers

         @param i
         */
        public void forward(int i) {

            cal.add(Calendar.MONTH, i);
            month.setText(simpleDateFormat.format(cal.getTime()));
            refresh();
        }

        private void refresh() {
            super.getChildren().clear();
            this.rows = 5; // most of the time 5 rows are ok
            // save a copy to reset the date after our loop
            Date copy = new Date(cal.getTime().getTime());

            // empty cell header of weak-of-year row
            super.getChildren().add(woyCell);

            // Display a styleable row of localized weekday symbols
            DateFormatSymbols symbols = new DateFormatSymbols();
            String[] dayNames = symbols.getShortWeekdays();

            // @TODO use static constants to access weekdays, I suspect we
            // get problems with localization otherwise ( Day 1 = Sunday/ Monday in
            // different timezones
            for (int i = 1; i < 8; i++) { // array starts with an empty field
                CalendarCell calendarCell = new CalendarCell(cal.getTime(), dayNames[i]);
                calendarCell.getStyleClass().add("weekday-cell");
                super.getChildren().add(calendarCell);
            }

            // find out which month we're displaying
            cal.set(Calendar.DAY_OF_MONTH, 1);
            final int month = cal.get(Calendar.MONTH);

            int weekday = cal.get(Calendar.DAY_OF_WEEK);

            // if the first day is a sunday we need to rewind 7 days otherwise the
            // code below would only start with the second week. There might be
            // better ways of doing this...
            if (weekday != Calendar.SUNDAY) {
                // it might be possible, that we need to add a row at the end as well...

                Calendar check = Calendar.getInstance();
                check.setTime(new Date(cal.getTime().getTime()));
                int lastDate = check.getActualMaximum(Calendar.DATE);
                check.set(Calendar.DATE, lastDate);
                if ((lastDate + weekday) > 36) {
                    rows = 6;
                }

                cal.add(Calendar.DATE, -7);

            }
            cal.set(Calendar.DAY_OF_WEEK, 1);



            // used to identify and style the cell with the selected date;
            Calendar testSelected = Calendar.getInstance();
            testSelected.setTime(selectedDate);

            for (int i = 0; i < (rows); i++) {

                // first column shows the week of year
                CalendarCell calendarCell = new CalendarCell(cal.getTime(), "" + cal.get(Calendar.WEEK_OF_YEAR));
                calendarCell.getStyleClass().add("week-of-year-cell");
                super.getChildren().add(calendarCell);

                // loop through current week
                for (int j = 0; j < columns; j++) {
                    String formatted = sdf.format(cal.getTime());
                    final CalendarCell dayCell = new CalendarCell(cal.getTime(), formatted);
                    dayCell.getStyleClass().add("calendar-cell");
                    if (cal.get(Calendar.MONTH) != month) {
                        dayCell.getStyleClass().add("calendar-cell-inactive");
                    } else {
                        if (isSameDay(testSelected, cal)) {
                            dayCell.getStyleClass().add("calendar-cell-selected");
                            selectedDayCell = dayCell;
                        }
                        if (isToday(cal)) {
                            dayCell.getStyleClass().add("calendar-cell-today");
                        }

                    }

                    dayCell.setOnMouseClicked(mc -> {
                        if (selectedDayCell != null) {
                            selectedDayCell.getStyleClass().add("calendar-cell");
                            selectedDayCell.getStyleClass().remove("calendar-cell-selected");
                        }
                        selectedDate.setTime(dayCell.getDate().getTime());
                        dayCell.getStyleClass().remove("calendar-cell");
                        dayCell.getStyleClass().add("calendar-cell-selected");
                        selectedDayCell = dayCell;
                        Calendar checkMonth = Calendar.getInstance();
                        checkMonth.setTime(dayCell.getDate());

                        if (checkMonth.get(Calendar.MONTH) != month) {
                            forward(checkMonth.get(Calendar.MONTH) - month);
                        }
                    });

                    dayCell.setOnContextMenuRequested(cm -> {
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem menuItem1 = new MenuItem("Create Appointment");
                        menuItem1.setOnAction(ae -> {
                            dayCell.fireEvent(new AddAppointmentClickedEvent(dayCell.getDate()));
                        });
                        contextMenu.getItems().add(menuItem1);
                        contextMenu.show(dayCell, cm.getScreenX(), cm.getScreenY());
                    });

                    // grow the hovered cell in size
                    dayCell.setOnMouseEntered(me -> {
                        dayCell.setScaleX(1.1);
                        dayCell.setScaleY(1.1);
                    });

                    dayCell.setOnMouseExited(me -> {
                        dayCell.setScaleX(1);
                        dayCell.setScaleY(1);
                    });

                    super.getChildren().add(dayCell);
                    cal.add(Calendar.DATE, 1);  // number of days to add
                }
            }
            cal.setTime(copy);
        }

        /**
         Overriden, don't add Children directly

         @return unmodifieable List
         */
        @Override
        protected ObservableList getChildren() {
            return FXCollections.unmodifiableObservableList(super.getChildren());
        }

        /**
         get the current month our calendar displays. Should always give you the
         correct one, even if some days of other mnths are also displayed

         @return
         */
        public Date getShownMonth() {
            return cal.getTime();
        }

        @Override
        protected void layoutChildren() {
            ObservableList<Node> children = getChildren();
            double width = getWidth();
            double height = getHeight();

            double cellWidth = width * 0.13;
            double cellHeight = height * 0.18;
            double headerHeight = height * 0.05;
            double weekColWidth = width * 0.09;

            for (int i = 0; i < (rows + 1); i++) {
                for (int j = 0; j < (columns + 1); j++) {
                    if (children.size() <= ((i * (columns + 1)) + j)) {
                        break;
                    }
                    Node get = children.get((i * (columns + 1)) + j);
                    if(i == 0)
                        if(j == 0)
                            layoutInArea(get, j * weekColWidth, i * headerHeight, weekColWidth, headerHeight, 0.0d, HPos.LEFT, VPos.TOP);
                        else
                            layoutInArea(get, weekColWidth + ((j - 1) * cellWidth), i * headerHeight, cellWidth, headerHeight, 0.0d, HPos.LEFT, VPos.TOP);
                    else
                        if(j == 0)
                            layoutInArea(get, j * weekColWidth, headerHeight + ((i-1) * cellHeight), cellWidth, cellHeight, 0.0d, HPos.LEFT, VPos.TOP);
                        else
                            layoutInArea(get, weekColWidth + ((j - 1) * cellWidth), headerHeight + ((i-1) * cellHeight), cellWidth, cellHeight, 0.0d, HPos.LEFT, VPos.TOP);
                }

            }
        }
    }
    // utility methods

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    private static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }


}
