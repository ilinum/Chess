package me.ilinskiy.chess.api.game;

import me.ilinskiy.chess.api.Move;
import org.jetbrains.annotations.NotNull;
import me.ilinskiy.chess.api.chessboard.PieceColor;

import java.util.List;
import java.util.Optional;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 3/10/16
 */
public interface Game {
    void makeMove();

    @NotNull
    PieceColor whoseTurnIsIt();

    boolean isGameOver();

    @org.jetbrains.annotations.NotNull
    Optional<PieceColor> getWinner();

    @NotNull
    List<Move> getMovesMade();

    int numberOfMovesMade();
}