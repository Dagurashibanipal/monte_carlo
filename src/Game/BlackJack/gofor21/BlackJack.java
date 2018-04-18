package Game.BlackJack.gofor21;

import Game.BlackJack.Policy;
import Game.BlackJack.StateActionPair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BlackJack {
    HashMap<StateActionPair, List<Integer>> valueList;
    HashMap<StateActionPair, Float> valueFunction;
    List<StateActionPair> stateActionsThisGame;

    private List<Integer> deck;
    private Policy policy;
    private int playerScore;

    private int reward;
    private float value;
    private int lastHit;


    public BlackJack() {
        deck = new ArrayList<>();
        policy = new Policy();
        valueList = new HashMap<>();
        valueFunction = new HashMap<>();
        stateActionsThisGame = new ArrayList<>();
    }

    public void setup() {
        setupPolicyAndValuefunction();
    }

    public void play() {
        playerScore=0;
        //reward = 0;
        value = 0;
        lastHit=0;
        fillDeck();
        boolean hit = true;
        //only makes at most one ace effective
        int playerAceCounter = 0;

        int lastDraw;
        if ((lastDraw=draw())==11) {
            playerAceCounter++;
        }
        playerScore+=lastDraw;
        State currentState;
        //player adds cards
        while (hit && playerScore<39) {
            if ((lastDraw=draw())==11) {
                playerAceCounter++;
            }
            playerScore+=lastDraw;
            if (playerScore>21 && playerAceCounter>0) {
                playerAceCounter--;
                playerScore -=10;
            }
            currentState = new State(playerScore);

            hit = policy.act(currentState);
            stateActionsThisGame.add(new StateActionPair(currentState, hit));

            if (hit) {
                lastHit=playerScore;
            }
        }

        //determine winner
        if (playerScore>21) {
            reward = -50;
        } else {
            reward = playerScore;
        }

        List<Integer> helper;
        for (int i=0; i<stateActionsThisGame.size(); i++) {
            //basically policy evaluation here
            helper = new ArrayList<>();
            value = 0;
            StateActionPair stateAction = stateActionsThisGame.get(i);

            if (!valueList.containsKey(stateAction)) {
                helper.add(reward);
                valueList.put(stateAction, helper);
            } else {
                helper = valueList.get(stateAction);
                helper.add(reward);
                valueList.put(stateAction, helper);
            }

            for (Integer j : helper) {
                value += j;
            }
            valueFunction.put(stateAction, value/(float)helper.size());

            //policy iteration -> greedy
            StateActionPair competitor;

            if (stateAction.getAction()) {
                competitor = new StateActionPair(stateAction.getState(), false);
            } else {
                competitor = new StateActionPair(stateAction.getState(), true);
            }

            if (valueFunction.containsKey(competitor)) {
                if (value > valueFunction.get(competitor)) {
                    policy.addStateActionPair((State) stateAction.getState(), stateAction.getAction());
                } else {
                    policy.addStateActionPair((State) stateAction.getState(), !stateAction.getAction());
                }
            } else {
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

    private void setupPolicyAndValuefunction() {
        State newState;
        //value 50 was picked arbitrarily
        for (int i=1; i<=50; i++) {
            newState = new State(i);
            policy.addState(newState);
            valueFunction.put(new StateActionPair(newState, true), 0f);
            valueFunction.put(new StateActionPair(newState, false), 0f);
            valueList.put(new StateActionPair(newState, true), new ArrayList<>());
            valueList.put(new StateActionPair(newState, false), new ArrayList<>());
        }
    }
    public int getResult() {
        return reward;
    }
    public int getPlayerScore() {
        return playerScore;
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
}
