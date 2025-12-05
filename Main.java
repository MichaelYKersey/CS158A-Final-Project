import scr.Board;

public class Main {
    public static void main(String[] args) throws Exception{
        char c = 'A';
        byte b = (byte) c;
        char c2 = (char) b;
        System.out.println(c2);
        
        Board board = new Board("XOXOXOXOX".toCharArray(),(byte)9,'X');
        System.out.println(board);
        byte[] raw = board.getAsRaw();
        for (int i=0; i< raw.length; i++) {
            System.out.print((char)raw[i]+",");
        }
        System.err.println();
        System.out.println(new Board(board.getAsRaw()));
    }
}
