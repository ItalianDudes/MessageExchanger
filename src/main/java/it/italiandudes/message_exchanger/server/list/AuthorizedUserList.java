package it.italiandudes.message_exchanger.server.list;

import it.italiandudes.idl.common.Credential;
import it.italiandudes.idl.common.Logger;
import it.italiandudes.message_exchanger.MessageExchanger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings({"unused", "UnusedReturnValue"})
public final class AuthorizedUserList {

    //List
    private static final ArrayList<Credential> userList = readAuthorizedUsers();

    //Methods
    public static boolean addUser(Credential newCredential){
        for(Credential credential : userList){
            if(credential.equals(newCredential)){
                return false;
            }
        }
        return userList.add(newCredential);
    }
    public static boolean removeUser(String username){
        Credential user = getUser(username);
        if(user == null) return false;
        return userList.remove(user);
    }
    public static boolean isAuthorized(Credential credential){
        return getUser(credential.getUsername())!=null;
    }
    public static Credential getUser(String username){
        for(Credential credential : userList){
            if(credential.getUsername().equals(username)){
                return credential;
            }
        }
        return null;
    }
    public static void clearUserList(){
        userList.clear();
    }
    private static ArrayList<Credential> readAuthorizedUsers(){
        ArrayList<Credential> authUsers = new ArrayList<>();

        File serverDir = new File(MessageExchanger.Defs.Server.SERVER_DIR);
        if(!serverDir.exists() || !serverDir.isDirectory()){
            //noinspection ResultOfMethodCallIgnored
            serverDir.mkdir();
        }

        Scanner inFile;
        try{
            inFile = new Scanner(new File(MessageExchanger.Defs.Server.AUTH_USERS_PATH));
        }catch (FileNotFoundException e){
            return authUsers;
        }

        try {
            while (inFile.hasNext()) {
                String line = inFile.nextLine();
                if (!line.equals("")) {
                    String[] splitLine = line.split(";");
                    authUsers.add(new Credential(splitLine[0], splitLine[1], false));
                }
            }
        }catch (Exception ignored){}

        return authUsers;
    }
    public static void updateAuthorizedUsers(){
        File authUsersFile = new File(MessageExchanger.Defs.Server.AUTH_USERS_PATH);
        BufferedWriter outFile;
        try{
            outFile = new BufferedWriter(new FileWriter(authUsersFile));
        }catch (Exception e){
            Logger.log("Can't write on authorized users file.");
            return;
        }
        try{
            for(Credential credential : userList) {
                outFile.append(credential.getUsername()).append(';').append(credential.getPassword()).append('\n');
            }
            outFile.flush();
            outFile.close();
        }catch (Exception e){
            Logger.log("Error on auth users list writing.");
            try{
                outFile.close();
            }catch (Exception ignored){}
        }
    }

}
