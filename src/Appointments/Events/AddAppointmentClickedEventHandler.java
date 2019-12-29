package Appointments.Events;


import javafx.animation.Animation;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Light;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.List;

public /*abstract*/ class AddAppointmentClickedEventHandler implements EventHandler<AddAppointmentClickedEvent> {

    /*public abstract void onEvent1(Date param0);

    public abstract void onEvent2(String param0);*/

    @Override
    public void handle(AddAppointmentClickedEvent event) {
        //event.invokeHandler(this);
        Rectangle rect = new Rectangle(450, 250, Color.CORAL);
        //rect.se(Color.CORAL);

        TranslateTransition animation = createAnimation(rect);

        animation.pause();
        //((Node)event.getTarget()).setEffect(new GaussianBlur());

        VBox pauseRoot = new VBox(5);
        pauseRoot.getStylesheets().add("Appointments/Styles/Style.css");
        pauseRoot.getChildren().add(new Label("Add Appointment"/*event.GetSelectedDate().toString()*/));
        GridPane appointmentDetails = new GridPane();
        /****************   Appointment Description ******************/
        Pane appointmentDesc = new Pane();
        appointmentDesc.setStyle("-fx-pref-height: 200;-fx-pref-width: 225");
        appointmentDesc.getChildren().add(0, new Label("Customer Name"));
        appointmentDesc.getChildren().get(0).setTranslateY(0);
        appointmentDesc.getChildren().add(1, new ComboBox<String>(FXCollections.observableArrayList("text1", "text2", "text3")));
        appointmentDesc.getChildren().get(1).setTranslateY(22);
        appointmentDesc.getChildren().get(1).idProperty().setValue("CustomerName");
        appointmentDesc.getChildren().add(2, new Label("Appointment Type"));
        appointmentDesc.getChildren().get(2).setTranslateY(52);
        appointmentDesc.getChildren().add(3, new ComboBox<String>(FXCollections.observableArrayList("In Person", "Phone Call", "Webex")));
        appointmentDesc.getChildren().get(3).setTranslateY(72);
        appointmentDesc.getChildren().add(4, new Label("Location Details"));
        appointmentDesc.getChildren().get(4).setTranslateY(100);
        appointmentDesc.getChildren().add(5, new TextArea());
        appointmentDesc.getChildren().get(5).setTranslateY(120);
        /****************   Appointment Timing     ******************/
        Pane appointmentTiming = new Pane();
        appointmentTiming.setStyle("-fx-pref-height: 200;-fx-pref-width: 225");
        appointmentTiming.getChildren().add(0, new Label("Appointment Date"));
        appointmentTiming.getChildren().get(0).setTranslateY(0);
        appointmentTiming.getChildren().add(1, new DatePicker());
        appointmentTiming.getChildren().get(1).setTranslateY(22);
        appointmentTiming.getChildren().add(2, new Label("Appointment Time"));
        appointmentTiming.getChildren().get(2).setTranslateY(52);
        appointmentTiming.getChildren().add(3, new ComboBox<String>(FXCollections.observableArrayList("3PM", "4PM", "5PM")));
        appointmentTiming.getChildren().get(3).setTranslateY(72);
        /****************   Save & Cancel     ******************/
        Pane buttonsArea = new Pane();
        buttonsArea.setStyle("-fx-pref-height: 50;-fx-pref-width: 450;");
        buttonsArea.getChildren().add(0, new Button("Save"));
        buttonsArea.getChildren().add(1, new Button("Cancel"));
        /****************   Put together grid     ******************/
        appointmentDetails.add(appointmentDesc, 0, 0, 1, 1);
        appointmentDetails.add(appointmentTiming, 1, 0, 1, 1);
        appointmentDetails.add(buttonsArea, 0, 1, 2, 1);
        GridPane.setHalignment(buttonsArea, HPos.CENTER);
        pauseRoot.getChildren().add(appointmentDetails);
        /****************   Window     ******************/
        pauseRoot.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");
        pauseRoot.setAlignment(Pos.CENTER);
        pauseRoot.setPadding(new Insets(20));

        Button resume = new Button("Cancel");
        pauseRoot.getChildren().add(resume);

        Stage popupStage = new Stage(/*StageStyle.TRANSPARENT*/);
        popupStage.initOwner(((Node)event.getTarget()).getScene().getWindow());
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(new Scene(pauseRoot, Color.TRANSPARENT));


        resume.setOnAction(rs -> {
            ((Node)event.getTarget()).setEffect(null);
            animation.play();
            popupStage.hide();
        });

        popupStage.show();
    }

    private TranslateTransition createAnimation(Rectangle rect) {
        TranslateTransition animation = new TranslateTransition(Duration.seconds(1), rect);
        animation.setByX(400);
        animation.setCycleCount(Animation.INDEFINITE);
        animation.setAutoReverse(true);
        animation.play();
        return animation;
    }
}