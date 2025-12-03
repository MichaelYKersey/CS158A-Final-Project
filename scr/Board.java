package scr;
public class Board {
    private char[] m_board = ".".repeat(9).toCharArray(); 
    private int turns = 0;
    private char winner = '.';
    
    /**
     * @param index index to place turn at
     * @return if game is over
     */
    public boolean place(int index) {
        if (winner != '.') {
            System.err.println("game over can't place");
        }
        if (m_board[index] != '.' || index >= m_board.length) {
            throw new IllegalArgumentException();
        }
        m_board[index] = (turns%2 == 0 ? 'X' : 'O');
        turns++;
        return update_winner() != '.';
    }
    /**
     * @return winner (T=tie,.=TBD)
     */
    public char update_winner() {
        winner = calc_winner();
        return winner;
    }
    public char calc_winner() {
        for (int i=0; i<3; i++) {
            //check row
            if (m_board[i*3] == m_board[i*3+1] && m_board[i*3] == m_board[i*3+2]) return m_board[i];
            //check col
            if (m_board[i] == m_board[i+3] && m_board[i] == m_board[i+6]) return m_board[i];
        }
        //check diagonals
        if (m_board[0] == m_board[4] && m_board[0] == m_board[8]) return m_board[0];
        if (m_board[2] == m_board[4] && m_board[2] == m_board[6]) return m_board[2];
        if (turns >= 9) return 'T';
        return '.';
    }
    @Override
    public String toString() {
        String str = "";
        str += m_board[0] + "|" + m_board[1] + "|" + m_board[2];
        str += "-----";
        str += m_board[3] + "|" + m_board[4] + "|" + m_board[5];
        str += "-----";
        str += m_board[6] + "|" + m_board[7] + "|" + m_board[8];
        return str;
    }
}
