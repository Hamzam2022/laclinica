package org.project.lacliniqa.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import org.project.lacliniqa.managers.EventsManager;
import org.project.lacliniqa.models.User;

import java.util.prefs.Preferences;

import static org.project.lacliniqa.globals.constants.EventConstants.LOGOUT_CMPLT_EVENT_ID;
import static org.project.lacliniqa.globals.constants.PrefConstants.PREFS_SAVE_USER_UID;

public class LogoutController {
    public MFXButton logoutButton;

    public void handleLogoutButton() {
        User.getCurrentUser().unsetUser();

        Preferences prefs = Preferences.systemNodeForPackage(AppController.class);
        prefs.remove(PREFS_SAVE_USER_UID);

        EventsManager.getInstance().fireEvent(LOGOUT_CMPLT_EVENT_ID);
    }
}
