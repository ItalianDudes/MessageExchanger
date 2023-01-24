package it.italiandudes.message_exchanger.client.javafx.controller;

import it.italiandudes.message_exchanger.client.Client;
import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class ControllerSceneLoading {

    //Attributes
    private static final Image loadingImage = new Image(JFXDefs.Resource.get(JFXDefs.Resource.GIF.GIF_LOADING).toString());

    //Graphic Elements
    @FXML private ImageView loadingView;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
        loadingView.setImage(loadingImage);
    }

}
