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
    public ClientHandler(Socket p_socket, boolean p_go_first) throws Exception {
        m_socket = p_socket;
        m_move_first = p_go_first;
        m_output_stream = new DataOutputStream(m_socket.getOutputStream());
        m_input_stream = new DataInputStream(m_socket.getInputStream());
        m_output_stream.writeInt(TCPPrefixes.ASSIGN_ORDER.ordinal());
        m_output_stream.writeBoolean(m_move_first);
    }

    @Override
    public void run() {
        try {
        while (!m_socket.isClosed()) {
            int p = m_input_stream.readInt();
            if (p == TCPPrefixes.GET_BOARD.ordinal()) {
                m_output_stream.writeInt(TCPPrefixes.GET_BOARD_REPLY.ordinal());
                m_output_stream.write(m_board.getAsRaw());
            } else if (p == TCPPrefixes.SEND_MOVE.ordinal()) {
                boolean successful = false;
                if (m_board.is_turn(m_move_first)) {
                    successful = m_board.place(m_input_stream.readByte());
                }
                m_output_stream.writeInt(TCPPrefixes.SEND_MOVE_REPLY.ordinal());
                m_output_stream.writeBoolean(successful);
            } else if (p == TCPPrefixes.CLOSE_CONNECTION.ordinal()) {
                m_output_stream.close();
                m_input_stream.close();
                m_socket.close();
            } else {
                throw new InvalidKeyException();
            }
        }
        } catch (Exception e){
            System.out.println("Thead Terminating Due To Error"+e);
        }
        
    }
}
