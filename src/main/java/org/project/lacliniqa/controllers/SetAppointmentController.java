package org.project.lacliniqa.controllers;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.project.lacliniqa.managers.EventsManager;
import org.project.lacliniqa.models.Appointment;
import org.project.lacliniqa.models.AppointmentType;
import org.project.lacliniqa.models.AvailableDate;
import org.project.lacliniqa.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import static org.project.lacliniqa.globals.constants.EventConstants.*;
import static org.project.lacliniqa.globals.constants.MsgConstants.INVALID_CREDENTIALS_MSG;
import static org.project.lacliniqa.globals.constants.MsgConstants.SAVE_ERROR_MSG;

public class SetAppointmentController {
    public MFXComboBox appointmentTypeBox;
    public MFXTextField appointmentSubtypeField;
    public MFXComboBox appointmentDatetimeBox;
    public Label submitStatusLabel;

    ArrayList<AppointmentType> appointmentTypes;
    ArrayList<AvailableDate> availableDates;

    @FXML
    public void initialize() {
        fetchTypes();
        fetchDates();

        EventsManager.getInstance().registerEvent(FETCH_TYPES_EVENT_ID, this::fetchTypes);
        EventsManager.getInstance().registerEvent(FETCH_DATES_EVENT_ID, this::fetchDates);
    }

    public int fetchTypes() {
        try {
            appointmentTypes = AppointmentType.getTypes();
            for(AppointmentType type: appointmentTypes) {
                appointmentTypeBox.getItems().add(type.getTypename());
            }

            return 0;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    public int fetchDates() {
        try {
            availableDates = AvailableDate.getDates();
            for(AvailableDate date: availableDates) {
                appointmentDatetimeBox.getItems().add(date.getDatetime());
            }

            return 0;
        }
        catch (SQLException exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    public boolean validateCredentials(String type, String subtype, String date) {
        return !type.isEmpty() && !subtype.isEmpty() && !date.isEmpty();
    }

    public void handleSubmitButton() {
        String typeId = appointmentTypes.get(appointmentTypeBox.getSelectedIndex()).getTid();
        String subtype = appointmentSubtypeField.getText();
        String date = appointmentDatetimeBox.getText();

        if(!validateCredentials(typeId, subtype, date)) {
            submitStatusLabel.setText(INVALID_CREDENTIALS_MSG);
            return;
        }

        String userId = User.getCurrentUser().getUid();
        String aid = UUID.randomUUID().toString();

        Appointment appointment = new Appointment(aid, userId, typeId, subtype, date);
        try {
            appointment.saveAppointment();

            EventsManager.getInstance().fireEvent(FETCH_APPOINTMENTS_LIST_EVENT_ID);
            EventsManager.getInstance().fireEvent(GOTO_APPOINTMENTS_EVENT_ID);
        }
        catch (SQLException e) {
            submitStatusLabel.setText(SAVE_ERROR_MSG);
            e.printStackTrace();
        }

        resetInputFields();
    }

    private void resetInputFields() {
        appointmentTypeBox.setText("");
        appointmentSubtypeField.setText("");
        appointmentDatetimeBox.setText("");
    }
}
