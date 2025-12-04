package scr;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.zip.DataFormatException;

public class Board {
    public static final int RAW_SIZE = 11;
    private char[] m_board = ".".repeat(9).toCharArray(); 
    private byte m_turns = 0;
    private char m_winner = '.';
    int players = 0;
    
    public Board() {};
    public Board(char[] p_board, byte p_turns, char p_winner)  throws Exception {
        if (m_board.length != 9) throw new DataFormatException();
        m_board = p_board;
        m_turns = p_turns;
        m_winner = p_winner;
    }
    public Board(byte[] raw) throws Exception {
        if (raw.length != RAW_SIZE) throw new DataFormatException();
        char[] board = new char[9];
        for (int i=0; i<board.length; i++) {
            board[i] = (char) raw[i];
        }
        this(board,raw[9],(char)raw[10]);
    }
    public byte[] getAsRaw() {
        byte[] raw = new byte[RAW_SIZE];
        for (int i=0; i<m_board.length; i++) {
            raw[i] = (byte) raw[i];
        }
        raw[9] = m_turns;
        raw[10] = (byte)m_winner;
        return raw;
    }

    /**
     * @param index index to place turn at
     * @return if move was successful
     */
    public boolean place(byte index) {
        if (m_winner != '.') {
            System.err.println("game over can't place");
        }
        if (m_board[index] != '.' || index >= m_board.length) {
            return false;
        }
        m_board[index] = (m_turns%2 == 0 ? 'X' : 'O');
        m_turns++;
        return true;
    }
    /**
     * @return winner (T=tie,.=TBD)
     */
    public char updateWinner() {
        m_winner = calcWinner();
        return m_winner;
    }
    public char calcWinner() {
        for (int i=0; i<3; i++) {
            //check row
            if (m_board[i*3] == m_board[i*3+1] && m_board[i*3] == m_board[i*3+2]) return m_board[i];
            //check col
            if (m_board[i] == m_board[i+3] && m_board[i] == m_board[i+6]) return m_board[i];
        }
        //check diagonals
        if (m_board[0] == m_board[4] && m_board[0] == m_board[8]) return m_board[0];
        if (m_board[2] == m_board[4] && m_board[2] == m_board[6]) return m_board[2];
        if (m_turns >= 9) return 'T';
        return '.';
    }
    @Override
    public String toString() {
        String str = "";
        str += m_board[0] + "|" + m_board[1] + "|" + m_board[2] + "\n";
        str += "-----" + "\n";
        str += m_board[3] + "|" + m_board[4] + "|" + m_board[5] + "\n";
        str += "-----" + "\n";
        str += m_board[6] + "|" + m_board[7] + "|" + m_board[8] + "\n";
        return str;
    }
    public boolean isTurn(boolean move_first) {
        if (m_turns >= 9) return false;
        return m_turns%2 == (move_first ? 0 : 1);
    }
}
