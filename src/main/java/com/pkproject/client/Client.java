package com.pkproject.client;

import com.pkproject.Main;
import com.pkproject.objects.Question;
import com.pkproject.objects.StateGame;
import com.pkproject.server.Server;
import com.pkproject.server.ServerClientMessage;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Client {
    private static final String HOST = "localhost";
    private ObjectInputStream sInput;		// to read from the socket
    private ObjectOutputStream sOutput;		// to write on the socket
    private Socket clientSocket;
    public boolean connection = false;
    public boolean keepGoing = true;

    private Main main;

    public Client(Main main) {
        this.main = main;
    }
    // we created new client


    public boolean isKeepGoing() {
        return keepGoing;
    }

    public void setKeepGoing(boolean keepGoing) {
        this.keepGoing = keepGoing;
    }

    public boolean connectWithServer() {
        if(!connection)
        {
            // try to connect to the server
            try {
                clientSocket = new Socket(HOST, Server.portServer);
            }
            // if it failed not much I can so
            catch(Exception ec) {
                System.out.println("problem with connect");
                return false;
            }

            /* Creating both Data Stream */
            try
            {
                sInput  = new ObjectInputStream(clientSocket.getInputStream());
                sOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            }

            catch (IOException eIO) {
                System.out.println("Problem with createu Input/Output streams");
                return false;
            }

            // creates the Thread to listen from the server
            new Client.ListenFromServer().start();
            // Send our username to the server this is the only message that we
            // will send as a String. All other messages will be ChatMessage objects
            connection = true;
        }
        // success we inform the caller that it worked
        return connection;
    }

    public void closeConnectWithServer() {
        try {
            if(sInput != null) sInput.close();
        }
        catch(Exception e) {}
        try {
            if(sOutput != null) sOutput.close();
        }
        catch(Exception e) {}
        try{
            if(clientSocket != null) clientSocket.close();
        }
        catch(Exception e) {}
    }

    public boolean turnOnGame() {
        try {
            System.out.println("turn on game");
            sOutput.writeObject(new ServerClientMessage(ServerClientMessage.TURNONQUIZ));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erorr turn on game");
            closeConnectWithServer();
            return false;
        }
        System.out.println(" send object");
        return true;
    }


    public boolean sendResult() {
        ServerClientMessage message = new ServerClientMessage(ServerClientMessage.SAVERESULT);
        message.setQuestions(main.getQuestions());
        try {
            sOutput.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erorr turn on game");
            closeConnectWithServer();
            return false;
        }
        System.out.println(" send object");
        return true;
    }

    public boolean showAllResult() {
        ServerClientMessage message = new ServerClientMessage(ServerClientMessage.CHECKALLRESULT);
        try {
            sOutput.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erorr turn on game");
            closeConnectWithServer();
            return false;
        }
        System.out.println(" send object");
        return true;
    }

    class ListenFromServer extends Thread {
        private ServerClientMessage cm;
        public void run() {
           while(true) {
                try {
                    System.out.println("klient czeka na odpowiedz od serwera");
                    cm = (ServerClientMessage) sInput.readObject();
                    System.out.println("klient dostał  odpowiedz od serwera");
                } catch (EOFException e) {

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                if (cm != null) {
                    switch (cm.getType()) {
                        case ServerClientMessage.TURNONQUIZ:
                            System.out.println("Client: turn on game");
                            main.setQuestions(cm.getQuestions());
                            main.stateGame.setStartQuiz(true);
                            break;
                        case ServerClientMessage.TURNOFFQUIZ:
                            System.out.println("Client: turn off game");
                            main.getStateGame().setEndGame(true);
                            connection = false;
                            break;
                        case ServerClientMessage.CHECKALLRESULT:
                            System.out.println("Client: check all result");
                            main.setPlayers(cm.getPlayers());
                            break;
                    }

                }
         }
        }

    }
}
