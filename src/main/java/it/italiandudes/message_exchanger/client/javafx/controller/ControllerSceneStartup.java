package it.italiandudes.message_exchanger.client.javafx.controller;

import it.italiandudes.idl.common.Credential;
import it.italiandudes.message_exchanger.client.Client;
import it.italiandudes.message_exchanger.client.javafx.alert.ErrorAlert;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneLoading;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneMessageView;
import it.italiandudes.message_exchanger.client.logic.ClientAuthenticator;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

@SuppressWarnings("unused")
public final class ControllerSceneStartup {

    //Attributes

    //Graphic Elements
    @FXML private AnchorPane mainPane;
    @FXML private TextField domainField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    //Initialize
    @FXML
    private void initialize() {
        Client.getStage().setResizable(false);
    }

    //EDT Methods
    @FXML
    private void connectToServer(){
        Scene thisScene = Client.getStage().getScene();
        Client.getStage().setScene(SceneLoading.getScene());
        Service<Void> connectToServerService = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() {
                        String[] domainAndPort = domainField.getText().split(":");
                        if(domainAndPort.length != 2) {
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore di Compilazione dei campi", "Il campo \"dominio\" non rispetta la sintassi. Rispettare il formato dominio:porta");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }
                        String domain = domainAndPort[0];
                        String strPort = domainAndPort[1];
                        int port;
                        try{
                            port = Integer.parseInt(strPort);
                            if(port<1 || port>65535) throw new Exception();
                        }catch (Exception e){
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore di Compilazione dei campi", "Il campo \"dominio\" non rispetta la sintassi. Assicurarsi che la porta sia un numero intero compreso tra 1 e 65535");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        if(usernameField.getText()==null || usernameField.getText().equals("")){
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore di Compilazione dei campi", "Il campo \"username\" non può essere vuoto");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        if(passwordField.getText()==null || passwordField.getText().equals("")){
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore di Compilazione dei campi", "Il campo \"password\" non può essere vuoto");
                                Client.getStage().setScene(thisScene);
                            });
                            return null;
                        }

                        ClientAuthenticator authenticator = new ClientAuthenticator(new Credential(usernameField.getText(), passwordField.getText()), domain, port);

                        if(authenticator.getException()!=null){
                            Platform.runLater(() -> {
                                new ErrorAlert("Errore", "Errore durante la connessione col server", authenticator.getException().getMessage());
                                Client.getStage().setScene(thisScene);
                            });
                        }else{
                            Platform.runLater(() -> Client.getStage().setScene(SceneMessageView.getScene()));
                        }

                        return null;
                    }
                };
            }
        };
        connectToServerService.start();
    }

}
