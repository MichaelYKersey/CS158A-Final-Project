import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import scr.Board;
import scr.ClientHandler;

public class Server {
    public static final int PORT = 5000;
    public static void main(String[] args) throws Exception {
        ServerSocket ss = new ServerSocket(PORT);
        Board room = new Board();
        int order = 0;
        while (true) {
            Socket socket = ss.accept();
            System.out.println("Client connected");
            new ClientHandler(socket,room).start();
            if (room.updateConnected(0) == 2) {
                room = new Board();
            }
        }
    }
}
