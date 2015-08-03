package me.ilinskiy.chess.chessBoard;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.game.Move;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 7/18/15
 */
public final class Board {
    private final ImmutableBoard inner;

    public Board() {

        inner = new ImmutableBoard();
    }

    Board(ImmutableBoard b) {
        inner = b;
    }

    @NotNull
    public ChessElement getPieceAt(@NotNull Coordinates c) {
        return inner.getPieceAt(c);
    }

    public void setPieceAt(@NotNull Coordinates pos, @NotNull ChessElement element) {
        inner.setPieceAt(pos, element);
    }

    public void setPieceAccordingToMove(@NotNull Move move) {
        ChessElement initPosPiece = inner.getPieceAt(move.getInitialPosition());
        if (initPosPiece instanceof EmptyCell) {
            throw new IllegalArgumentException("Cannot move an empty cell!");
        }
        inner.setPieceAt(move.getInitialPosition(), EmptyCell.INSTANCE);
        inner.setPieceAt(move.getNewPosition(), initPosPiece);
    }

    public void movePiece(@NotNull Move m) {
        inner.movePiece(m);
    }

    public void paint() {
        inner.paint(inner.getGraphics());
    }

    public ImmutableBoard getInner() {
        return inner;
    }

    @Override
    public String toString() {
        return inner.toString();
    }
}