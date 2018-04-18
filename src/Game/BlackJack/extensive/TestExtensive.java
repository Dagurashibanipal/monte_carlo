package Game.BlackJack.extensive;

import Game.BlackJack.extensive.BlackJack;

public class TestExtensive {
    public static void main (String[] args) {
        BlackJack blackjack = new BlackJack();
        blackjack.setup();
        for (int i=1; i<=50; i++) {
            int winpoints = 0;
            int drawpoints = 0;
            int lastHits = 0;
            System.out.println();
            System.out.println("BATCH " + i);

            for (int j = 0; j < 100; j++) {
                blackjack.play();
                String result = "ERROR";
                switch (blackjack.getResult()) {
                    case -1:
                        result = "LOSS";
                        break;
                    case 0:
                        result = "DRAW";
                        drawpoints++;
                        break;
                    case 1:
                        result = "WIN";
                        winpoints++;
                        break;
                }
                lastHits+=blackjack.getLastHit();
                /*
                System.out.println(result);
                System.out.println("Spielerpunkte: " + blackjack.getPlayerScore());
                System.out.println("Croupierpunkte: " + blackjack.getCroupierScore());
                System.out.println("Last Hit: " + blackjack.getLastHit());
                System.out.println("Siegeschance: " + winpoints / (float) (j+1));
                System.out.println();
                */
            }
            System.out.println("Win Ratio  : " + winpoints/(float)100);
            System.out.println("Draw Percentage : " + drawpoints/(float)100);
            System.out.println("Average Last Hit: " + lastHits/(float)100);
            //System.out.println("States:           " + blackjack.getPolicy().size());
        }
    }
}
