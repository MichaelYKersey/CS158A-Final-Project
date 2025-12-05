package scr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.security.InvalidKeyException;

public class ClientHandler extends Thread {
    Board m_board;
    Socket m_socket;
    DataOutputStream m_output_stream;
    DataInputStream m_input_stream;
    boolean m_move_first;
    
    public ClientHandler(Socket p_socket, Board p_board, boolean p_move_first) throws Exception {
        m_socket = p_socket;
        m_board = p_board;
        m_move_first = p_move_first;
        m_output_stream = new DataOutputStream(m_socket.getOutputStream());
        m_input_stream = new DataInputStream(m_socket.getInputStream());
        m_output_stream.writeInt(TCPPrefixes.ASSIGN_ORDER.ordinal());
        m_output_stream.writeBoolean(m_move_first);
    }

    @Override
    public void run() {
        System.out.println("ThreadStarting");
        try {
        while (!m_socket.isClosed()) {
            System.out.println("Wait for prefix (first:"+m_move_first+")");
            int p = m_input_stream.readInt();
            System.out.println("Receive Prefix:"+p);
            if (p == TCPPrefixes.GET_BOARD.ordinal()) {
                System.out.println("Get Board");
                m_output_stream.writeInt(TCPPrefixes.GET_BOARD_REPLY.ordinal());
                // System.out.println("Wrote Prefix");
                m_output_stream.write(m_board.getAsRaw());
                // System.out.println("Wrote SentBoard");
            } else if (p == TCPPrefixes.SEND_MOVE.ordinal()) {
                System.out.println("Send Move");
                byte move_request = m_input_stream.readByte();
                boolean successful = false;
                if (m_board.isTurn(m_move_first)) {
                    successful = m_board.place(move_request);
                }
                m_output_stream.writeInt(TCPPrefixes.SEND_MOVE_REPLY.ordinal());
                m_output_stream.writeBoolean(successful);
            } else if (p == TCPPrefixes.CLOSE_CONNECTION.ordinal()) {
                System.out.println("Close Connection");
                m_output_stream.close();
                m_input_stream.close();
                m_socket.close();
            } else if (p == TCPPrefixes.WAIT_FOR_OPPONENT.ordinal()) {
                System.out.println("Wait command");
                System.out.println("waiting for opponent (first:"+m_move_first+")");
                while (!m_board.isTurn(m_move_first) && m_board.getWinner() == '.') {
                    sleep(100);
                }
                m_output_stream.writeInt(TCPPrefixes.WAIT_FOR_OPPONENT_REPLY.ordinal());
            } else {
                System.out.println("Invalid TCP Prefix");
                throw new InvalidKeyException(""+p);
            }
            System.out.print(m_board);
            System.out.println("fin_process command (first:"+m_move_first+")");
        }
        System.out.println("Safely Disconnected from Client");
        } catch (Exception e){
            System.out.println("Thead Terminating Due To Error:\n"+e+"\n"+e.getMessage()+"\n");
            e.printStackTrace();
        }
    }
}
