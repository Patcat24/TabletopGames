package players.search;

import core.AbstractPlayer;
import core.actions.AbstractAction;
import games.connect4.Connect4ForwardModel;
import games.connect4.Connect4GameParameters;
import games.connect4.Connect4GameState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AlphaBetaPruningTests {

    @Test
    public void connect4SameMoveAndTimings() {

        // the intention here is to run a game of Connect4 from start to finish and confirm that
        // a MaxNSearchPlayer with alphaBetaPruning set to true and false will make the same moves
        // except that the player with pruning should take less time

        // create a game of Connect4
        Connect4GameState gameState = new Connect4GameState(new Connect4GameParameters(), 2);
        Connect4ForwardModel forwardModel = new Connect4ForwardModel();
        forwardModel.setup(gameState);

        // create a MaxNSearchPlayer with alphaBetaPruning set to false
        MaxNSearchParameters paramsOne = new MaxNSearchParameters();
        paramsOne.alphaBetaPruning = false;
        paramsOne.paranoid = true;
        paramsOne.searchDepth = 4;
        AbstractPlayer player1 = new MaxNSearchPlayer(paramsOne);
        player1.setForwardModel(forwardModel);

        // create a MaxNSearchPlayer with alphaBetaPruning set to true
        MaxNSearchParameters paramsTwo = new MaxNSearchParameters();
        paramsTwo.alphaBetaPruning = true;
        paramsTwo.paranoid = true;
        paramsTwo.searchDepth = 4;
        AbstractPlayer player2 = new MaxNSearchPlayer(paramsTwo);
        player2.setForwardModel(forwardModel);

        long playerOneTime = 0;
        long playerTwoTime = 0;
        do {
            // player 1's turn
            long start = System.currentTimeMillis();
            AbstractAction actionOne = player1.getAction(gameState, forwardModel.computeAvailableActions(gameState));
            long timeOne = System.currentTimeMillis() - start;
            AbstractAction actionTwo = player2.getAction(gameState, forwardModel.computeAvailableActions(gameState));
            long timeTwo = System.currentTimeMillis() - start - timeOne;

            System.out.println("Player 1 : " + actionOne.toString() + "\tPlayer 2 : " + actionTwo.toString());
            System.out.println("Player 1 took " + timeOne + "ms, Player 2 took " + timeTwo + "ms");
        //    assertTrue(timeOne > timeTwo);
            assertEquals(actionOne, actionTwo);
            forwardModel.next(gameState, actionOne);

            if (gameState.getGameTick() > 4) {
                // skip the first few moves for JVM warmup
                playerOneTime += timeOne;
                playerTwoTime += timeTwo;
            }
        } while (gameState.isNotTerminal());

        System.out.println("Player 1 took " + playerOneTime + "ms, Player 2 took " + playerTwoTime + "ms");
        assertTrue(playerOneTime > playerTwoTime);
    }

}
