package Game.BlackJack.restricted;

import Game.BlackJack.Policy;
import Game.BlackJack.StateActionPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlackJack {
    HashMap<StateActionPair, Tupel> valueList;
    HashMap<StateActionPair, Float> valueFunction;
    List<StateActionPair> stateActionsThisGame;

    private List<Integer> deck;
    private Policy policy;
    private int playerScore;
    private int croupierScore;

    private int reward;
    //private float value;
    private int lastHit;


    public BlackJack() {
        deck = new ArrayList<>();
        policy = new Policy();
        valueList = new HashMap<>();
        valueFunction = new HashMap<>();
        stateActionsThisGame = new ArrayList<>();
    }

    public void setup() {
        //setupPolicyAndValuefunction();
    }

    public void play() {
        playerScore=0;
        croupierScore=0;
        reward = 0;
        fillDeck();
        lastHit=0;
        boolean hit = true;
        //only makes at most one ace effective
        boolean playerAce = false;
        boolean croupierAce = false;

        int lastDraw;
        if ((lastDraw=draw())==11) {
            playerAce=true;
        }
        playerScore+=lastDraw;
        if ((lastDraw=draw())==11) {
            croupierAce=true;
        }
        croupierScore+=lastDraw;
        State currentState;

        //player adds cards
        while (hit) {
            if ((lastDraw=draw())==11) {
                playerAce=true;
            }
            playerScore+=lastDraw;
            if (playerScore>21 && playerAce) {
                playerAce=false;
                playerScore -=10;
            }
            currentState = new State(playerScore, croupierScore, playerAce);

            if (!policy.contains(currentState)) {
                if (playerScore<20) {
                    policy.addStateActionPair(currentState, true);
                } else {
                    policy.addStateActionPair(currentState, false);
                }

            }
            hit = policy.act(currentState);
            stateActionsThisGame.add(new StateActionPair(currentState, hit));

            if (hit) {
                lastHit=playerScore;
            }
        }
        //croupier adds cards
        while (croupierScore<17) {
            if ((lastDraw=draw())==11) {
                croupierAce=true;
            }
            croupierScore+=lastDraw;
        }
        if (croupierScore>21 && croupierAce) {
            croupierScore -=10;
        }
        //determine winner
        /*
        if (croupierScore>21) {
            reward = 1;
        } else if (playerScore>21) {
            reward = -1;
        } else {
            if (playerScore>croupierScore) {
                reward = 1;
            } else if (playerScore==croupierScore) {
                reward = 0;
            } else {
                reward = -1;
            }
        }
*/
        if (playerScore>croupierScore) {
            if (playerScore>21) {
                reward = -1;
            }
            if (croupierScore>21) {
                reward = 1;
            }
        }
        if (playerScore==croupierScore) {
            reward = 0;
        }
        if (playerScore<croupierScore){
            reward = -1;
            if (croupierScore>21) {
                reward = 1;
            }

        }

        Tupel helper;
        for (int i=0; i<stateActionsThisGame.size(); i++) {
            //basically policy evaluation here


            StateActionPair stateAction = stateActionsThisGame.get(i);

            if (!valueList.containsKey(stateAction)) {
                helper = new Tupel(reward, 1);
                valueList.put(stateAction, helper);
            } else {
                helper = valueList.get(stateAction);
                helper.setValue(helper.getValue()+reward);
                helper.setCounter(helper.getCounter()+1);
                valueList.put(stateAction, helper);
            }

            valueFunction.put(stateAction, valueList.get(stateAction).getValue()/(float)valueList.get(stateAction).getCounter());

            //policy iteration -> greedy
            StateActionPair competitor;

            if (stateAction.getAction()) {
                competitor = new StateActionPair(stateAction.getState(), false);
            } else {
                competitor = new StateActionPair(stateAction.getState(), true);
            }

            if (valueFunction.containsKey(competitor)) {
                if (valueList.get(stateAction).getValue() > valueFunction.get(competitor)) {
                    policy.addStateActionPair((State) stateAction.getState(), stateAction.getAction());
                } else {
                    policy.addStateActionPair((State) stateAction.getState(), !stateAction.getAction());
                }
            } else {
                policy.addStateActionPair((State) stateAction.getState(), stateAction.getAction());
                valueFunction.put(competitor, 0f);
            }
        }
    }

    private int draw() {
        Random rng = new Random();
        return deck.remove(rng.nextInt(deck.size()));
    }

    private void fillDeck() {
        for (int i = 0; i<312; i++) {
            if (i%13<8) {
                deck.add((i%13)+2);
            } else if (i%13<12) {
                deck.add(10);
            } else {
                deck.add(11);
            }

        }
    }
/*
    private void setupPolicyAndValuefunction() {
        State newState;
        //21+10 possible scores for player...
        for (int i=1; i<=32; i++) {
            //times 9 (2,3..11) for every possible score of the croupier
            for (int j =2; j<=11;j++) {
                newState = new State(i, j);
                policy.addState(newState);
                valueFunction.put(new StateActionPair(newState, true), 0f);
                valueFunction.put(new StateActionPair(newState, false), 0f);
                valueList.put(new StateActionPair(newState, true), new ArrayList<>());
                valueList.put(new StateActionPair(newState, false), new ArrayList<>());
            }
        }
    }
    */
    public int getResult() {
        return reward;
    }
    public int getPlayerScore() {
        return playerScore;
    }
    public int getCroupierScore() {
        return croupierScore;
    }
    public int getLastHit() {
        return lastHit;
    }
    public int getPolicySize() {
        return policy.getPolicySize();
    }
    public HashMap<StateActionPair, Float> getValueFunction() {
        return valueFunction;
    }
    public Policy getPolicy() {
        return policy;
    }
}
