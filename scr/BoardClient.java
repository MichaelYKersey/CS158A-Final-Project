package scr;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BoardClient {
    Socket m_socket;
    DataOutputStream m_output_stream;
    DataInputStream m_input_stream;
    boolean m_move_first;

    public BoardClient(Socket p_socket) throws Exception{
        m_socket = p_socket;
        m_output_stream = new DataOutputStream(m_socket.getOutputStream());
        m_input_stream = new DataInputStream(m_socket.getInputStream());
        if (m_input_stream.readInt() != TCPPrefixes.ASSIGN_ORDER.ordinal()) throw new IOException();
        m_move_first = m_input_stream.readBoolean();
    }
    /**
     * @return if the server accepted the move
     */
    public boolean makeMove(int index) throws Exception {
        m_output_stream.writeInt(TCPPrefixes.SEND_MOVE.ordinal());
        m_output_stream.writeByte(index);
        if (m_input_stream.readInt() != TCPPrefixes.SEND_MOVE_REPLY.ordinal()) throw new IOException();
        return m_input_stream.readBoolean();
    }
    public Board getBoard() throws Exception {
        m_output_stream.writeInt(TCPPrefixes.GET_BOARD.ordinal());
        if (m_input_stream.readInt() != TCPPrefixes.GET_BOARD_REPLY.ordinal()) throw new IOException();
        byte[] raw_board = new byte[Board.RAW_SIZE];
        m_input_stream.readFully(raw_board);
        return new Board(raw_board);
    }
    public void close() throws Exception {
        m_output_stream.writeInt(TCPPrefixes.CLOSE_CONNECTION.ordinal());
        m_output_stream.close();
        m_input_stream.close();
        m_socket.close();
    }
}
