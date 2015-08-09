package me.ilinskiy.chess.game;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Board;
import me.ilinskiy.chess.chessBoard.PieceColor;
import me.ilinskiy.chess.chessBoard.PieceType;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/17/15
 */
public interface Player {

    @NotNull
    Move getMove(@NotNull Board b);

    @NotNull
    PieceColor getPlayerColor();

    @NotNull
    PieceType getPieceTypeForPromotedPawn();
}
