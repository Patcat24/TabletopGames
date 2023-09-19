package games.stratego;

import core.AbstractGameState;
import core.AbstractParameters;
import core.components.BoardNode;
import core.components.Component;
import core.components.GridBoard;
import games.GameType;
import games.stratego.components.Piece;
import utilities.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrategoGameState extends AbstractGameState {
    GridBoard gridBoard;

    /**
     * Constructor. Initialises some generic game state variables.
     *
     * @param gameParameters - game parameters.
     */
    public StrategoGameState(AbstractParameters gameParameters, int nPlayers) {
        super(gameParameters, nPlayers);
    }

    @Override
    protected GameType _getGameType() {
        return GameType.Stratego;
    }

    @Override
    protected List<Component> _getAllComponents() {
        return new ArrayList<Component>() {{ add(gridBoard);}};
    }

    @Override
    protected AbstractGameState _copy(int playerId) {
        StrategoGameState s = new StrategoGameState(gameParameters, 2);
        s.gridBoard = gridBoard.emptyCopy();
        Piece.Alliance playerAlliance = null;

        // All piece types that will be hidden for opponent
        ArrayList<Piece.PieceType> pieceTypesHidden = new ArrayList<>();
        if (playerId != -1 && getCoreGameParameters().partialObservable){
            playerAlliance = StrategoConstants.playerMapping.get(playerId);

            for (BoardNode bn: gridBoard.getComponents()) {
                Piece p = (Piece) bn;
                if (p != null && p.getPieceAlliance() != playerAlliance && !p.isPieceKnown()) {
                    pieceTypesHidden.add(p.getPieceType());
                }
            }
        }

        Random random = new Random(gameParameters.getRandomSeed());
        for (BoardNode bn : gridBoard.getComponents()){
            Piece piece = (Piece) bn;
            if (piece != null) {
                Vector2D pos = piece.getPiecePosition();
                if (playerId != -1 && getCoreGameParameters().partialObservable && playerAlliance != piece.getPieceAlliance() && !piece.isPieceKnown()){
                    // Hide type, everything else is known
                    int typeIdx = random.nextInt(pieceTypesHidden.size());
                    Piece.PieceType hiddenPieceType = pieceTypesHidden.get(typeIdx);
                    pieceTypesHidden.remove(typeIdx);
                    s.gridBoard.setElement(pos.getX(), pos.getY(), piece.partialCopy(hiddenPieceType));
                } else{
                    s.gridBoard.setElement(pos.getX(), pos.getY(), piece.copy());
                }
            }
        }
        if (!pieceTypesHidden.isEmpty()) {
            throw new AssertionError("We have a hidden piece that has not been placed on the copied board");
        }
        return s;
    }

    @Override
    protected double _getHeuristicScore(int playerId) {
        return new StrategoHeuristic().evaluateState(this, playerId);
    }

    @Override
    public double getGameScore(int playerId) {
        return playerResults[playerId].value;
    }

    @Override
    protected boolean _equals(Object o) {
        if (o instanceof StrategoGameState) {
            StrategoGameState other = (StrategoGameState) o;
            return gridBoard.equals(other.gridBoard);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return gridBoard.hashCode();
    }

    public GridBoard getGridBoard() {
        return gridBoard;
    }

    @Override
    protected List<Integer> _getUnknownComponentsIds(int playerId) {
        ArrayList<Integer> pieceList = new ArrayList<>();

        for (BoardNode bn : gridBoard.getComponents()){
            Piece piece = (Piece) bn;
            if (piece != null){
                if (playerId != -1){
                    Piece.Alliance playerAlliance = StrategoConstants.playerMapping.get(playerId);
                    if (playerAlliance != piece.getPieceAlliance() && !piece.isPieceKnown()) {
                        pieceList.add(piece.getComponentID());
                    }
                }
            }
        }
        return pieceList;
    }

    public void printToConsole() {
        System.out.println(gridBoard.toString());
    }


}
