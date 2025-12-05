import java.net.Socket;
import java.util.Scanner;

import scr.Board;
import scr.BoardClient;

public class Client {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Socket socket = new Socket("localhost", Server.PORT);
        System.out.println("Connected to server");
        BoardClient bc = new BoardClient(socket);
        System.out.println("You go first:" + bc.movesFirst());
        if (!bc.movesFirst()) {
            System.out.println("Waiting for opponent");
            bc.waitForOpponent();
        }
        System.out.println(bc.getBoard());
        while (!socket.isClosed()) {
            if (checkWinner(bc)) {
                bc.close();
                break;
            }
            System.out.println("Your Turn, enter position index:");
            while (!bc.makeMove(sc.nextInt()-1)) {
                System.out.println("Server Refused Move");
            }
            System.out.println(bc.getBoard());
            if (checkWinner(bc)) {
                bc.close();
                break;
            }
            System.out.println("Waiting for opponent");
            bc.waitForOpponent();
            System.out.println(bc.getBoard());
        }
        System.out.println("Safely Disconnected from server");
    }

    static boolean checkWinner(BoardClient bc) throws Exception {
        char c = bc.getBoard().getWinner();
        if (c == '.') return false;
        if (c == 'T') {
            System.out.println("Game Was A Tie");
        } else if (c == (bc.movesFirst() ? 'X' : 'O')) {
            System.out.println("You Won");
        } else {
            System.out.println("You Lost");
        }
        return true;
    }
}