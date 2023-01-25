package it.italiandudes.message_exchanger;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.message_exchanger.client.Client;
import it.italiandudes.message_exchanger.server.Server;

import java.util.Arrays;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public final class MessageExchanger {

    //Main Method
    public static void main(String[] args) {

        if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.TEST_START))){
            return;
        }

        int exitCode;

        //Check if the user wants to run the app even if the Logger initialization fails
        boolean logOnDefaultStreamIfLoggerFail = Arrays.stream(args).
                anyMatch(Predicate.isEqual(Defs.LaunchArgs.LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL));
        if(logOnDefaultStreamIfLoggerFail)
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL)).
                    toArray(String[]::new);

        try{ //Attempt to instantiate the Logger
            if(!Logger.init()){
                System.err.println("An unknown error has occurred during Logger initialization.");
                if(!logOnDefaultStreamIfLoggerFail)
                    System.exit(Defs.ReturnCodes.LOGGER_INIT_ERROR);
            }
            Runtime.getRuntime().addShutdownHook(new Thread(Logger::close));
        }catch (Exception e) {
            System.err.println("An exception has occurred during Logger initialization.");
            e.printStackTrace();
            if(!logOnDefaultStreamIfLoggerFail)
                System.exit(Defs.ReturnCodes.LOGGER_INIT_ERROR);
        }

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Logger.log(e));

        if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_SERVER))){ //Start the server
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_SERVER)).
                    toArray(String[]::new);
            exitCode = Server.start(args);
        }else if(Arrays.stream(args).anyMatch(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP))){ //Start the client in textual-mode
            args = Arrays.stream(args).
                    filter(Predicate.isEqual(Defs.LaunchArgs.START_TEXTUAL_APP)).
                    toArray(String[]::new);
            exitCode = Client.noGuiStart(args);
        }else{ //Start the client in graphic-mode
            exitCode = Client.start(args);
        }

        Logger.log("Application terminating with code: "+exitCode);

        System.exit(exitCode);
    }

    //App Generic Constants
    public static class Defs {

        public static final String JAR_POSITION = MessageExchanger.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1);


        //Launch Arguments
        public static final class LaunchArgs {
            public static final String LOG_ON_DEFAULT_STREAM_IF_LOGGER_INIT_FAIL = "-LogOnDefaultStreamIfLoggerInitFail";
            public static final String START_SERVER = "-server";
            public static final String START_TEXTUAL_APP = "-nogui";
            public static final String TEST_START = "-test";
        }

        //Return Codes
        public static final class ReturnCodes {

            //<0 -> Pre-Launch Errors
            //>0 -> Post-Launch Errors

            //Pre Launch Codes
            public static final int LOGGER_INIT_ERROR = -100;
            public static final int SERVER_START_FAIL = -81;

            //Post-Launch Codes
            public static final int CLIENT_CONNECTION_ATTEMPT_FAIL = 91;
            public static final int CLIENT_COM_FAIL = 107;
            public static final int CLIENT_AUTH_FAIL = 185;
        }

        //Server Defs
        public static final class Server {
            public static final String SERVER_DIR = "server/";
            public static final String CONFIG_FILE_PATH = SERVER_DIR+"server.cfg";
            public static final String AUTH_USERS_PATH = SERVER_DIR+"authUsers.csv";
            public static final int DEFAULT_PORT = 45800;

            public static final class Commands {
                public static final String STOP = "stop";
                public static final String MESSAGE = "msg";
                public static final String MESSAGE_SET = "set";
                public static final String MESSAGE_GET = "get";
                public static final String PORT = "port";
                public static final String PORT_GET = "get";
                public static final String PORT_SET = "set";
                public static final String HELP = "help";
                public static final String REGISTER = "register";
                public static final String UNREGISTER = "unregister";
            }
        }

        //Protocol Strings
        public static final class Protocol {
            public static final String PROTOCOL_AUTH = "auth";
            public static final String PROTOCOL_GET = "get";
            public static final String PROTOCOL_DISC = "disc";
        }

    }

}
