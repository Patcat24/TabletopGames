package games.explodingkittens.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import games.explodingkittens.ExplodingKittensGameState;
import games.explodingkittens.cards.ExplodingKittensCard;

public class Skip extends AbstractAction {

    @Override
    public boolean execute(AbstractGameState gs) {
        ExplodingKittensGameState state = (ExplodingKittensGameState) gs;
        state.setInPlay(ExplodingKittensCard.CardType.SKIP, state.getCurrentPlayer());
        state.setSkip(true);
        return true;
    }

    @Override
    public Skip copy() {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Skip;
    }

    @Override
    public int hashCode() {
        return 3402;
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }
    @Override
    public String toString() {
        return "Skip draw";
    }
}