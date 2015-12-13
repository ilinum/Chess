package me.ilinskiy.chess.game.moves;

import me.ilinskiy.chess.annotations.NotNull;
import me.ilinskiy.chess.chessBoard.Coordinates;

/**
 * Author: Svyatoslav Ilinskiy
 * Date: 8/2/15.
 */
public class Castling extends Move {
    @NotNull
    public final Coordinates initialPosition;
    @NotNull
    public final Coordinates newPosition;
    @NotNull
    public final Coordinates rookNewPosition;
    @NotNull
    public final Coordinates rookInitPosition;


    public Castling(@NotNull Coordinates kingInitPos, @NotNull Coordinates kingNewPos, @NotNull Coordinates rookInitPos,
                    @NotNull Coordinates rookNewPos) {
        initialPosition = kingInitPos;
        newPosition = kingNewPos;
        rookInitPosition = rookInitPos;
        rookNewPosition = rookNewPos;
    }

    public Coordinates getKingInitialPosition() {
        return initialPosition;
    }

    public Coordinates getKingNewPosition() {
        return newPosition;
    }

    public Coordinates getRookInitialPosition() {
        return rookInitPosition;
    }

    public Coordinates getRookNewPosition() {
        return rookNewPosition;
    }

    @NotNull
    @Override
    public Castling copy() {
        return new Castling(getKingInitialPosition(), getKingNewPosition(), getRookInitialPosition(), getRookNewPosition());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Castling) {
            Castling other = (Castling) o;
            boolean kingInitPosEqual = other.getKingInitialPosition().equals(getKingInitialPosition());
            boolean kingNewPosEqual = other.getKingNewPosition().equals(getKingNewPosition());
            boolean rookInitPosEqual = other.rookInitPosition.equals(rookInitPosition);
            boolean rookNewPosEqual = other.rookNewPosition.equals(rookNewPosition);
            return kingInitPosEqual && kingNewPosEqual && rookInitPosEqual && rookNewPosEqual;
        } else if (o instanceof RegularMove){
            RegularMove regularMove = (RegularMove) o;
            return initialPosition.equals(regularMove.initialPosition) && newPosition.equals(regularMove.newPosition);
        }
        return false;
    }

    @Override
    public Coordinates[] getInitialPositions() {
        return new Coordinates[]{getKingInitialPosition(), getRookInitialPosition()};
    }

    @Override
    public Coordinates[] getNewPositions() {
        return new Coordinates[]{getKingNewPosition(), getRookNewPosition()};
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += getKingInitialPosition().hashCode() * 31;
        hash += getKingNewPosition().hashCode() * 15;
        hash += rookInitPosition.hashCode() * 63;
        hash += rookNewPosition.hashCode() * 127;
        return hash;
    }

    @NotNull
    @Override
    public String toString() {
        return "Castling: " +
                "King " + getKingInitialPosition() + " -> " + getKingNewPosition() +
                ", Rook " + getRookInitialPosition() + " -> " + getRookNewPosition();
    }
}