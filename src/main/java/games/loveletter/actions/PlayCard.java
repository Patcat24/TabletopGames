package games.loveletter.actions;

import core.AbstractGameState;
import core.actions.AbstractAction;
import core.components.Deck;
import core.components.PartialObservableDeck;
import games.loveletter.LoveLetterGameState;
import games.loveletter.cards.LoveLetterCard;

import java.util.Objects;

public class PlayCard extends AbstractAction {
    protected final int playerID;
    final int targetPlayer;
    protected final LoveLetterCard.CardType cardType;
    final LoveLetterCard.CardType targetCardType;
    final LoveLetterCard.CardType forcedCountessCardType;
    final boolean canExecuteEffect;
    final boolean discard;

    public PlayCard(int playerID, boolean discard) {
        this.cardType = null;
        this.playerID = playerID;
        this.targetPlayer = -1;
        this.targetCardType = null;
        this.forcedCountessCardType = null;
        this.canExecuteEffect = false;
        this.discard = discard;
    }

    public PlayCard(int playerID, boolean discard, int targetPlayer) {
        this.cardType = null;
        this.playerID = playerID;
        this.targetPlayer = targetPlayer;
        this.targetCardType = null;
        this.forcedCountessCardType = null;
        this.canExecuteEffect = false;
        this.discard = discard;
    }

    public PlayCard(int playerID, boolean discard, LoveLetterCard.CardType targetCardType) {
        this.cardType = null;
        this.playerID = playerID;
        this.targetPlayer = -1;
        this.targetCardType = targetCardType;
        this.forcedCountessCardType = null;
        this.canExecuteEffect = false;
        this.discard = discard;
    }

    public PlayCard(LoveLetterCard.CardType cardType, int playerID, int targetPlayer, LoveLetterCard.CardType targetCardType, LoveLetterCard.CardType forcedCountessCardType, boolean canExecuteEffect, boolean discard) {
        this.cardType = cardType;
        this.playerID = playerID;
        this.targetPlayer = targetPlayer;
        this.targetCardType = targetCardType;
        this.forcedCountessCardType = forcedCountessCardType;
        this.canExecuteEffect = canExecuteEffect;
        this.discard = discard;
    }

    @Override
    public boolean execute(AbstractGameState gs) {
        LoveLetterGameState llgs = (LoveLetterGameState) gs;

        if (discard) {
            PartialObservableDeck<LoveLetterCard> from = llgs.getPlayerHandCards().get(playerID);
            Deck<LoveLetterCard> to = llgs.getPlayerDiscardCards().get(playerID);
            LoveLetterCard card = null;
            // Find card by type
            for (LoveLetterCard c : from.getComponents()) {
                if (c.cardType == cardType) {
                    card = c;
                    break;
                }
            }
            if (card != null) {
                // Discard card
                from.remove(card);
                to.add(card);
            } else {
                throw new AssertionError("No card in hand matching the required type");
            }
        }

        // Execute card effect
        if (canExecuteEffect) // If not, just discard the card, it's ok
            return _execute(llgs);

        return true;
    }

    @Override
    public PlayCard copy() {
        return this;
    }

    protected boolean _execute(LoveLetterGameState llgs) {return false;}
    protected String _toString() {return "";}

    @Override
    public String toString() {
        if (!canExecuteEffect) return cardType + " - no effect";
        else return _toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayCard)) return false;
        PlayCard playCard = (PlayCard) o;
        return playerID == playCard.playerID && targetPlayer == playCard.targetPlayer && canExecuteEffect == playCard.canExecuteEffect && discard == playCard.discard && cardType == playCard.cardType && targetCardType == playCard.targetCardType && forcedCountessCardType == playCard.forcedCountessCardType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerID, targetPlayer, cardType, targetCardType, forcedCountessCardType, canExecuteEffect, discard);
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getTargetPlayer() {
        return targetPlayer;
    }

    public LoveLetterCard.CardType getCardType() {
        return cardType;
    }

    public LoveLetterCard.CardType getForcedCountessCardType() {
        return forcedCountessCardType;
    }

    public LoveLetterCard.CardType getTargetCardType() {
        return targetCardType;
    }

    public boolean canExecuteEffect() {
        return canExecuteEffect;
    }

    public boolean isDiscard() {
        return discard;
    }
}
