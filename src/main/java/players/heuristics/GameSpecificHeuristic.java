package players.heuristics;

import core.AbstractGameState;
import core.interfaces.IStateHeuristic;

public class GameSpecificHeuristic implements IStateHeuristic {

    /**
     * This cares only about the raw game score
     *
     * @param gs       - game state to evaluate and score.
     * @param playerId - player id
     * @return
     */
    @Override
    public double evaluateState(AbstractGameState gs, int playerId) {
        return gs.getGameScore(playerId);
    }

    @Override
    public double minValue() {
        return Double.NEGATIVE_INFINITY;
    }
    @Override
    public double maxValue() {
        return Double.POSITIVE_INFINITY;
    }
}
