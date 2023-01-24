package it.italiandudes.message_exchanger.client;

import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import it.italiandudes.message_exchanger.client.javafx.scene.SceneStartup;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public final class Client extends Application {

    //Attributes
    private static Stage stage;

    //JavaFX Start
    @Override
    public void start(Stage stage) {
        Client.stage = stage;
        stage.setTitle(JFXDefs.AppInfo.NAME);
        stage.getIcons().add(JFXDefs.AppInfo.LOGO);
        stage.setScene(SceneStartup.getScene());
        stage.show();
    }

    //Graphic Start Method
    public static int start(String[] args){
        launch(args);
        return 0;
    }

    //NoGui Start Method
    public static int noGuiStart(String[] args){
        return 0;
    }

    //Methods
    @NotNull
    public static Stage getStage(){
        return stage;
    }

}
