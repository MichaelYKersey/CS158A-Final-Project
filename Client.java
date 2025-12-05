import java.net.Socket;
import java.util.Scanner;

import scr.BoardClient;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("localhost", Server.PORT);
        System.out.println("Connected to server");
        BoardClient bc = new BoardClient(socket);
        System.out.println(bc.getBoard());
        while (!socket.isClosed()) {
            if (! bc.makeMove(sc.nextInt())) {
                System.out.println("Server Refused Move");
            } else {
                System.out.println(bc.getBoard());
            }
            if (bc.getBoard().getWinner() != '.') {
                System.out.print(bc.getBoard().getWinner());
                bc.close();
            }
        }
        System.out.println("Safely Disconnected from server");
    }
}