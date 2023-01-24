package it.italiandudes.message_exchanger.client.javafx;

import it.italiandudes.message_exchanger.MessageExchanger;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;

@SuppressWarnings("unused")
public final class JFXDefs {

    //App Info
    public static final class AppInfo {
        public static final String NAME = "Message Exchanger";
        public static final Image LOGO = new Image(JFXDefs.Resource.get(JFXDefs.Resource.Image.IMAGE_LOGO).toString());
    }

    //CSS Functions
    public static final class CSS {}

    //Resource Locations
    public static final class Resource {

        //Resource Getter
        public static URL get(@NotNull final String resourceConst){
            return Objects.requireNonNull(MessageExchanger.class.getResource(resourceConst));
        }

        //FXML Location
        public static final class FXML {
            private static final String FXML_DIR = "/fxml/";
            public static final String FXML_STARTUP = FXML_DIR + "SceneStartup.fxml";
            public static final String FXML_LOADING = FXML_DIR+"SceneLoading.fxml";
        }

        //GIF Location
        public static final class GIF {
            private static final String GIF_DIR = "/gif/";
            public static final String GIF_LOADING = GIF_DIR+"loading.gif";
        }

        //Image Location
        public static final class Image {
            private static final String IMAGE_DIR = "/image/";
            public static final String IMAGE_LOGO = IMAGE_DIR+"app-logo.png";
            public static final String IMAGE_FILE_EXPLORER = IMAGE_DIR+"file-explorer.png";
        }

    }

}
