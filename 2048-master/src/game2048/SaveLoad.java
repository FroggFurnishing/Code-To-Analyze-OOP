package game2048;

import java.io.*;

public class SaveLoad implements Serializable {
    private static String fileName = "2048.state";

    public static Game load() {
        Game game;
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            SaveLoad state = (SaveLoad) in.readObject();
            game = new Game(state);
        } catch (Exception e) {
            game = null;
        }
        return game;
    }

    public static void save(int score, int highScore, Board board) {
        SaveLoad state = new SaveLoad(score, highScore, board);
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(state);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int score, highScore;
    public Board board;

    private SaveLoad(int score, int highScore, Board board) {
        this.board = board;
        this.score = score;
        this.highScore = highScore;
    }
}
