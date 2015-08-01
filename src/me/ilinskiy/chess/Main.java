package me.ilinskiy.chess;

import me.ilinskiy.chess.chessBoard.ImmutableBoard;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.game.Game;
import me.ilinskiy.chess.game.Player;
import me.ilinskiy.chess.game.UserPlayer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static final int INIT_HEIGHT_AND_WIDTH = 62 * ImmutableBoard.BOARD_SIZE; //approx 500
    //public static final int MIN_HEIGHT_AND_WIDTH = 30 * ImmutableBoard.BOARD_SIZE;

    public static void main(String[] args) {
        JFrame game = new JFrame();
        game.setTitle("Chess");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Player p1 = new UserPlayer(PieceColor.White);
        Player p2 = new UserPlayer(PieceColor.Black);
        game.setLayout(new BorderLayout());
        game.setSize(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH);
//        game.setMinimumSize(new Dimension(MIN_HEIGHT_AND_WIDTH, MIN_HEIGHT_AND_WIDTH));
//        game.setPreferredSize(new Dimension(INIT_HEIGHT_AND_WIDTH, INIT_HEIGHT_AND_WIDTH));
        game.setResizable(false);
        Game g = new Game(p1, p2, game);
        game.setLocationRelativeTo(null);
        game.setVisible(true);
        while (!g.isGameOver()) {
            try {
                g.makeMove();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}