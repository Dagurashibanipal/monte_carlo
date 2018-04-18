package Game.BlackJack.gofor21;

import Game.BlackJack.StateActionPair;

import java.util.HashMap;

public class TestGofor21 {
    public static void main (String[] args) {
        HashMap<StateActionPair, Float> valueFunction;
        BlackJack blackjack = new BlackJack();
        blackjack.setup();
        int batchSize=10;


        for (int i=1; i<=1; i++) {
            int winpoints = 0;
            int lastHits = 0;
            System.out.println();
            System.out.println("BATCH " + i);

            for (int j = 0; j < batchSize; j++) {
                blackjack.play();
                int winpointsThisGame = blackjack.getResult();
                winpoints += winpointsThisGame;
                lastHits+=blackjack.getLastHit();

                System.out.println("Spielerpunkte: " + winpointsThisGame);
                System.out.println("Last Hit: " + blackjack.getLastHit());
                System.out.println("Siegeschance: " + winpoints / (float) (j+1));
                System.out.println();


            }
            if (i==1 || i==25 || i==50) {
                valueFunction = blackjack.getValueFunction();
                for (StateActionPair pair : valueFunction.keySet()) {
                    System.out.println("StateAction " + pair.getState() + ", " + pair.getAction() + ": " + valueFunction.get(pair));
                }
            }
            //System.out.println("Points per game : " + winpoints/(float)batchSize);
            //System.out.println("Average Last Hit: " + lastHits/(float)batchSize);
            //System.out.println("States:           " + blackjack.getPolicySize());
        }
    }
}
