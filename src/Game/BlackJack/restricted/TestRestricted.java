package Game.BlackJack.restricted;

import Game.BlackJack.StateActionPair;

import java.util.HashMap;

public class TestRestricted {
    public static void main (String[] args) {
        BlackJack blackjack = new BlackJack();
        HashMap<StateActionPair, Float> valueFunction;
        blackjack.setup();
        int batchSize=2000;

        for (int i=1; i<=10; i++) {
            int winpoints = 0;
            int drawpoints = 0;
            int lastHits = 0;
            System.out.println();
            System.out.println("BATCH " + i);

            for (int j = 0; j < batchSize; j++) {
                blackjack.play();
                //String result = "ERROR";
                switch (blackjack.getResult()) {
                    case -1:
                        //result = "LOSS";
                        break;
                    case 0:
                        //result = "DRAW";
                        drawpoints++;
                        break;
                    case 1:
                        //result = "WIN";
                        winpoints++;
                        break;
                }
                lastHits+=blackjack.getLastHit();
/*
                System.out.println(result);
                System.out.println("Spielerpunkte   : " + blackjack.getPlayerScore());
                System.out.println("Croupierpunkte  : " + blackjack.getCroupierScore());
                System.out.println("Last Hit        : " + blackjack.getLastHit());
                System.out.println("Reward          : " + blackjack.getResult());
                System.out.println();
*/

            }
            if (i==1 || i==10 || i==40) {
                valueFunction = blackjack.getValueFunction();
                for (StateActionPair pair : valueFunction.keySet()) {
                    System.out.println("StateAction " + pair.getState() + ", " + pair.getAction() + ": " + valueFunction.get(pair));
                }
            }

            System.out.println("Win Percentage  : " + winpoints/(float)batchSize);
            System.out.println("Draw Percentage : " + drawpoints/(float)batchSize);
            System.out.println("Average Last Hit: " + lastHits/(float)batchSize);
            System.out.println("States:           " + blackjack.getPolicySize());
        }
        System.out.println();
        System.out.println(blackjack.getPolicy());
    }
}
