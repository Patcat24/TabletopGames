package games.wonders7.actions;

import core.AbstractGameState;
import core.actions.DrawCard;
import core.components.Deck;
import games.wonders7.Wonders7Constants;
import games.wonders7.Wonders7Constants.Resource;
import games.wonders7.Wonders7Constants.TradeSource;
import games.wonders7.Wonders7GameParameters;
import games.wonders7.Wonders7GameState;
import games.wonders7.cards.Wonder7Card;
import utilities.Pair;

import java.util.*;

import static games.wonders7.Wonders7Constants.Resource.Coin;

public class PlayCard extends DrawCard {

    public final String cardName;
    public final int player;
    public final boolean free;

    // Player chooses card to play
    public PlayCard(int player, String cardName, boolean free) {
        super();
        this.cardName = cardName;
        this.player = player;
        this.free = free;
    }


    @Override
    public boolean execute(AbstractGameState gameState) {
        Wonders7GameState wgs = (Wonders7GameState) gameState;

        Deck<Wonder7Card> playerHand = wgs.getPlayerHand(player);
        Map<Resource, Integer> playerResources = wgs.getPlayerResources(player);

        Wonder7Card card = wgs.findCardInHand(player, cardName);

        cardId = card.getComponentID();

        // Removes coins paid for card
        if (!free) {
            Pair<Boolean, List<TradeSource>> buildDetails = card.isPlayable(player, wgs);
            if (!buildDetails.a) {
                throw new AssertionError("Card not playable");
            }
            // first pay direct coin cost
            playerResources.put(Coin, playerResources.get(Coin) - card.constructionCost.getOrDefault(Coin, 0L).intValue());
            // then pay trade costs
            List<TradeSource> tradeSources = buildDetails.b;
            for (TradeSource tradeSource : tradeSources) {
                if (tradeSource.fromPlayer() == -1) {
                    throw new AssertionError("Trade source from player not set");
                } else {
                    // give the supplier the coins
                    int cost = tradeSource.cost();
                    int fromPlayer = tradeSource.fromPlayer();
                    playerResources.put(Coin, playerResources.get(Coin) - cost);
                    wgs.getPlayerResources(fromPlayer).put(Coin, wgs.getPlayerResources(fromPlayer).get(Coin) + cost);
                }
            }
        }

        // Gives player resources produced from card
        Set<Resource> keys = card.resourcesProduced.keySet(); // Gets all the resources the card provides
        for (Resource resource : keys) {  // Goes through all keys for each resource
            int cardValue = card.getNProduced(resource); // Number of resource the card provides
            int playerValue = playerResources.get(resource); // Number of resource the player owns
            playerResources.put(resource, playerValue + cardValue); // Adds the resources provided by the card to the players resource count
        }

        // remove the card from the players hand to the playedDeck
        boolean cardFound = playerHand.remove(card);
        if (!cardFound) {
            throw new AssertionError("Card not found in player hand");
        }
        wgs.getPlayedCards(player).add(card);
        return true;
    }

    @Override
    public String toString() {
        return "Player " + player + " played card " + cardName + (free ? " (free)" : "");
    }

    @Override
    public String getString(AbstractGameState gameState) {
        return toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayCard)) return false;
        if (!super.equals(o)) return false;
        PlayCard playCard = (PlayCard) o;
        return player == playCard.player && free == playCard.free && Objects.equals(cardName, playCard.cardName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), cardName, player, free);
    }

    @Override
    public PlayCard copy() {
        return this;
    }
}
