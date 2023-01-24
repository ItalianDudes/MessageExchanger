package it.italiandudes.message_exchanger.client.logic;

import it.italiandudes.idl.common.Credential;
import it.italiandudes.idl.common.Peer;
import it.italiandudes.idl.common.RawSerializer;
import it.italiandudes.message_exchanger.MessageExchanger;
import it.italiandudes.message_exchanger.client.Client;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.Socket;

@SuppressWarnings("unused")
public final class ClientAuthenticator {

    //Attributes
    @NotNull private final Credential credentials;
    private final boolean result;
    @Nullable private final Exception exception;

    //Constructors
    public ClientAuthenticator(@NotNull Credential credentials, @NotNull String domain, int port){
        this.credentials = credentials;
        Socket socket;
        try {
            socket = new Socket(domain, port);
        }catch (Exception e) {
            this.exception = e;
            result = false;
            return;
        }
        try {
            RawSerializer.sendString(socket.getOutputStream(), MessageExchanger.Defs.Protocol.PROTOCOL_AUTH);
            RawSerializer.sendObject(socket.getOutputStream(), credentials);
            if(!RawSerializer.receiveBoolean(socket.getInputStream())){
                throw new Exception("Authentication Failed");
            }
        } catch (Exception e){
            this.exception = e;
            result = false;
            return;
        }
        this.exception = null;
        result = true;
        Client.setPeer(new Peer(socket, credentials));
    }

    //Methods
    public boolean getResult(){
        return result;
    }
    @Nullable
    public Exception getException(){
        return exception;
    }
    @NotNull
    public Credential getCredentials(){
        return credentials;
    }

}
