package it.italiandudes.message_exchanger.client.javafx.scene;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.message_exchanger.client.javafx.JFXDefs;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

@SuppressWarnings("unused")
public final class SceneStartup {

    //Scene Generator
    public static Scene getScene(){
        try {
            return new Scene(FXMLLoader.load(JFXDefs.Resource.get(JFXDefs.Resource.FXML.FXML_STARTUP)));
        }catch (IOException e){
            Logger.log(e);
            return null;
        }
    }

}
