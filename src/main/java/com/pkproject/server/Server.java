package com.pkproject.server;

import com.pkproject.objects.Player;
import com.pkproject.objects.Question;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Server {
    private List<Player> players;
    private static int uniqueConnectIdWithClient;
    // lista, trzymajaca w sobie wszystkich polaczonych uzytkownikow
    private ArrayList<ClientThread> al;
    // numer portu ktory sluzy do polaczenia
    public static int portServer = 1111;
    // wlacz/wylacz server dla konkretnego uzytkownika
    private boolean keepGoing;

    //we store
    public Server() {
        // inicjalizacja arrayList dla listy clientow
        al = new ArrayList<ClientThread>();
        players = new ArrayList<Player>();
    }

    private void startServer() throws IOException {
        keepGoing = true;
        ServerSocket serverSocket = new ServerSocket(portServer);

        while(keepGoing) {
            System.out.println("port server=" + portServer);

            Socket socket = serverSocket.accept();  	// accept connection
            // jezeli przerwiemy oczekiwanie
            if(!keepGoing)
                break;
            // utworzenie watku nowego clienta
            Server.ClientThread t = new Server.ClientThread(socket);
            // zapisanei go do listy
            al.add(t);
            // wlaczenie watku dla nowego clienta

            t.start();
        }

        //close all connect
        serverSocket.close();
        for(int i = 0; i < al.size(); ++i) {
            Server.ClientThread tc = al.get(i);
            try {
                tc.sInput.close();
                tc.sOutput.close();
                tc.socket.close();
            }
            catch(IOException ioE) {
            }
        }

    }


    //check which one user is disconnect
    synchronized void remove(int id) {
        // poszukaj w liscie ktory z uzytkownikow sie odlaczyl od servera
        for(int i = 0; i < al.size(); ++i) {
            Server.ClientThread ct = al.get(i);
            // jesli go znajdziesz, usun go
            if(ct.id == id) {
                al.remove(i);
                return;
            }
        }
    }



    class ClientThread extends Thread {
        DataBase dataBase;
        private Socket socket;
        private ObjectInputStream sInput;
        private ObjectOutputStream sOutput;
        int id;
        ServerClientMessage cm;
        boolean keepGoing = true;

        ClientThread(Socket socket) {
            dataBase = new DataBase();
            // przypisanie unikalnego id
            id = ++uniqueConnectIdWithClient;
            this.socket = socket;
            //utworzenie data streams
            System.out.println("Watek probuje utworzyc obiekt Input/Output Streams");
            try
            {
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput  = new ObjectInputStream(socket.getInputStream());
            }
            catch (IOException e) {
                return;
            }
        }
        public void run() {
            Player player = new Player();

            boolean keepGoing2 = true;

            while (keepGoing2) {
                try {
                    System.out.println("watek czeka na klienta");
                    cm = (ServerClientMessage) sInput.readObject();
                    System.out.print("serwer otrzymal wiadomosc od klienta" + cm.getType());
                } catch (IOException e) {
                    System.out.println("Problem z odczytaniem wiadomosci od uzytkownika: ");
                    keepGoing2 = false;
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    System.out.println("Problem z odczytaniem wiadomosci od uzytkownika: ");
                    keepGoing2 = false;
                    e.printStackTrace();
                }
                switch(cm.getType()) {
                    case ServerClientMessage.TURNONQUIZ:
                        System.out.println("serwer turn on quiz");
                        cm.setQuestions(dataBase.getQuestion());
                        try {
                            sOutput.writeObject(cm);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("send");
                        break;
                    case ServerClientMessage.TURNOFFQUIZ:
                        writeMsg(new ServerClientMessage(ServerClientMessage.TURNOFFQUIZ));
                        keepGoing2 = false;
                        break;
                    case ServerClientMessage.SAVERESULT:
                        List<Question> questions = cm.getQuestions();
                        int countResult = 0;
                        for(int i = 0; i < questions.size(); i++) {
                            if(questions.get(i).checkAnswer()) countResult++;
                        }

                        player.setResult(countResult);
                        players.add(player);
                        System.out.println("your result is: " + countResult);
                        writeMsg(new ServerClientMessage(ServerClientMessage.CHECKALLRESULT));
                        break;

                    case ServerClientMessage.CHECKALLRESULT:
                        ServerClientMessage message = new ServerClientMessage(ServerClientMessage.CHECKALLRESULT);
                        message.setPlayers(players);
                        writeMsg(message);
                        break;
                }
            }

            remove(id);
            close();
        }

        public void checkAnswer() {

        }

        private void writeMsg(ServerClientMessage serverClientMessage) {
            if(!socket.isConnected()) {
                close();
            }
            try {
                sOutput.writeObject(serverClientMessage);
            }
            catch(IOException e) {
                keepGoing = false;
                System.out.println("Problem z wyslaniem wiadomosci przez server");
            }
        }


        private void close() {
            // probuj zamknac polaczenie
            try {
                if(sOutput != null) sOutput.close();
            }
            catch(Exception e) {}
            try {
                if(sInput != null) sInput.close();
            }
            catch(Exception e) {};
            try {
                if(socket != null) socket.close();
            }
            catch (Exception e) {}
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.startServer();
    }

}
