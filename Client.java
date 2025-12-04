import java.net.Socket;
import java.util.Scanner;

import scr.BoardClient;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("localhost", Server.PORT);
        System.out.println("Connected to server");
        BoardClient bc = new BoardClient(socket);
        System.out.println(bc.get_board());
        bc.close();
        System.out.println("Safely Disconnected from server");
    }
}