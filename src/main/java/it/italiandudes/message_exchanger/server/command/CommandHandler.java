package it.italiandudes.message_exchanger.server.command;

import it.italiandudes.idl.common.Credential;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.idl.common.StringHandler;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.MessageExchanger.Defs.Server.Commands;
import it.italiandudes.message_exchanger.server.Server;
import it.italiandudes.message_exchanger.server.list.AuthorizedUserList;

public final class CommandHandler {

    public static void handleCommand(String command){

        String[] splitCommand = StringHandler.parseString(command);

        switch (splitCommand[0]) {

            case Commands.MESSAGE:
                if(splitCommand.length<2){
                    Logger.log("Command syntax error");
                    return;
                }
                if(splitCommand[1].equals(Commands.MESSAGE_GET)){
                    Logger.log("Message: \""+ Server.getMessage()+"\"");
                }else if(splitCommand[1].equals(Commands.MESSAGE_SET)){
                    if(splitCommand.length > 2) {
                        StringBuilder msgBuilder = new StringBuilder();
                        for(int i=2;i<splitCommand.length;i++){
                            msgBuilder.append(splitCommand[i]);
                        }
                        Logger.log("Message changed successfully");
                        Server.setMessage(msgBuilder.toString());
                    }else{
                        Logger.log("Command syntax error");
                    }
                }else{
                    Logger.log("Command syntax error");
                }
                break;

            case Commands.HELP:
                printHelpMenu();
                break;

            case Commands.PORT:
                if(splitCommand.length<2){
                    Logger.log("Command syntax error");
                    return;
                }
                if(splitCommand[1].equals(Commands.PORT_GET)){
                    Logger.log("Port: "+ Server.getPort());
                }else if(splitCommand[1].equals(Commands.PORT_SET)){
                    if(splitCommand.length > 2) {
                        try {
                            Server.setPort(Integer.parseInt(splitCommand[2]));
                            Logger.log("Port changed successfully");
                        }catch (NumberFormatException exception){
                            Logger.log("Command syntax error");
                        }
                    }else{
                        Logger.log("Command syntax error");
                    }
                }else{
                    Logger.log("Command syntax error");
                }
                break;

            case Commands.REGISTER:
                if(splitCommand.length<3){
                    Logger.log("Command syntax error");
                    return;
                }
                Credential credentials = new Credential(splitCommand[1], splitCommand[2].toLowerCase(), false);
                if(AuthorizedUserList.getUser(credentials.getUsername())!=null){
                    Logger.log("User already exists");
                }else{
                    AuthorizedUserList.addUser(credentials);
                    Logger.log("User registered successfully");
                }
                break;

            case Commands.UNREGISTER:
                if(splitCommand.length<2){
                    Logger.log("Command syntax error");
                    return;
                }
                for(int i=1;i<splitCommand.length;i++){
                    if(AuthorizedUserList.removeUser(splitCommand[i])){
                        Logger.log("User \""+splitCommand[i]+"\" unregistered successfully");
                    }else{
                        Logger.log("User \""+splitCommand[i]+"\" it's not registered");
                    }
                }
                break;

            default:
                Logger.log("Unknown command, type 'help' to get command list");
                break;

        }

    }

    private static void printHelpMenu(){

        Logger.log("- "+Commands.PORT+":");
        Logger.log("\t- Subcommands: "+Commands.PORT_GET+", "+Commands.PORT_SET+" <value>");
        Logger.log("\t- Example: "+Commands.PORT+" "+Commands.PORT_GET);
        Logger.log("\t- Example: "+Commands.PORT+" "+Commands.PORT_SET+" "+ MessageExchanger.Defs.Server.DEFAULT_PORT);

        Logger.log("\n");

        Logger.log("- "+Commands.MESSAGE+":");
        Logger.log("\t- Subcommands: "+Commands.MESSAGE_GET+", "+Commands.MESSAGE_SET+" <message>");
        Logger.log("\t- Example: "+Commands.MESSAGE+" "+Commands.MESSAGE_GET);
        Logger.log("\t- Example: "+Commands.MESSAGE+" "+Commands.MESSAGE_SET+" Hello World!");

        Logger.log("- "+Commands.REGISTER+":");
        Logger.log("\t- Syntax: "+Commands.REGISTER+" <username> <sha512password>");

        Logger.log("- "+Commands.UNREGISTER+":");
        Logger.log("\t- Syntax: "+Commands.UNREGISTER+" <username> [username1] ... [usernameN]");

        Logger.log("\n");

    }

}
