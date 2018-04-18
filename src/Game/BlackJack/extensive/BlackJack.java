package Game.BlackJack.extensive;

import java.util.*;

import Game.BlackJack.*;
import org.apache.commons.math3.util.Combinations;

public class BlackJack implements CardGame {
    //der Bot erfährt nie den Wert der Karten, nur welche er hat und ob er gewinnt
    List<Card> deck;
    Policy policy;
    List<Card> playerCards;
    //List<Card> croupierCards;
    //List<Integer> values = new ArrayList<>();
    HashMap<StateActionPair, List<Integer>> valueList;
    HashMap<StateActionPair, Float> valueFunction;
    List<StateActionPair> stateActionsThisGame;
    private int croupierScore;
    private int playerScore;

    private int reward;
    private float value;
    private int lastHit;


    public BlackJack() {
        deck = new ArrayList<>();
        policy = new Policy();
        playerCards = new ArrayList<>();
        valueList = new HashMap<>();
        valueFunction = new HashMap<>();
        stateActionsThisGame = new ArrayList<>();
    }
    public void setup() {
        setupPolicyAndValuefunction();
    }
    public void play() {
        fillDeck();
        reward=0;
        lastHit=0;
        boolean hit = true;
        playerCards = new ArrayList<>();
        playerCards.add(draw());

        playerScore=playerCards.get(0).getValue();
        Card croupierCard = draw();

        State currentState;
        for (int index = 1; hit && playerScore<=21; index++) {
            //first, player and croupier get 1 card each, then player draws as long as he pleases
            //or he is above 21
            playerCards.add(draw());


            playerScore+=playerCards.get(index).getValue();
            currentState = new State(playerCards, croupierCard);
            if (!policy.contains(currentState)) {
                policy.addState(currentState);
                valueFunction.put(new StateActionPair(currentState, true), 0f);
                valueFunction.put(new StateActionPair(currentState, false), 0f);
                hit = policy.act(currentState);
                stateActionsThisGame.add(new StateActionPair(currentState, hit));
            } else {
                hit = policy.act(currentState);
                stateActionsThisGame.add(new StateActionPair(currentState, hit));
            }
            if (hit) {
                lastHit=playerScore;
            }
        }
        croupierScore = croupierCard.getValue();
        Card newCard;
        while (croupierScore<17) {
            newCard = draw();
            if (newCard.getValue()==11) {
                if (croupierScore + newCard.getValue() > 21) {
                    croupierScore+=1;
                }
            } else {
                croupierScore += newCard.getValue();
            }
        }
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

        List<Integer> helper;
        for (int i=0; i<stateActionsThisGame.size(); i++) {
            //basically policy evaluation here
            helper = new ArrayList<>();
            StateActionPair stateAction = stateActionsThisGame.get(i);
            value = 0;
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

    public Card draw() {
        Random rng = new Random();
        return deck.remove(rng.nextInt(deck.size()));
    }

    private void fillDeck() {
        deck = new ArrayList<>();
        for (int i=0; i<312; i++) {
            switch(i%13) {
                case 0: deck.add(Card.ACE);
                    break;
                case 1: deck.add(Card.TWO);
                    break;
                case 2: deck.add(Card.THREE);
                    break;
                case 3: deck.add(Card.FOUR);
                    break;
                case 4: deck.add(Card.FIVE);
                    break;
                case 5: deck.add(Card.SIX);
                    break;
                case 6: deck.add(Card.SEVEN);
                    break;
                case 7: deck.add(Card.EIGHT);
                    break;
                case 8: deck.add(Card.NINE);
                    break;
                case 9: deck.add(Card.TEN);
                    break;
                case 10: deck.add(Card.JACK);
                    break;
                case 11: deck.add(Card.QUEEN);
                    break;
                case 12: deck.add(Card.KING);
                    break;
            }
        }
    }

    private void setupPolicyAndValuefunction() {
        for (int i = 6; i>1; i--) {
            doCombinations(13, i);
        }
    }

    private void doCombinations(int n, int k) {
        Iterator<int[]> iter;
        List<Card> cards = new ArrayList<>();
        int checkfor21;
        State state;

        for (iter = new Combinations(n, k).iterator(); iter.hasNext();) {
            checkfor21 = 0;
            cards = new ArrayList<>();
            int[] iterArr = iter.next();
            Arrays.sort(iterArr);
            for (int anIterArr : iterArr) {
                if (anIterArr<8) {
                    checkfor21 += (anIterArr+2);
                    cards.add(Card.getCard(anIterArr + 2));
                } else if (anIterArr<12) {
                    checkfor21+=10;
                    cards.add(Card.getCard(10));
                } else {
                    if (checkfor21>11) {
                        checkfor21++;
                        cards.add(Card.getCard(11));
                    } else {
                        checkfor21+=11;
                        cards.add(Card.getCard(11));
                    }
                }
            }
            if (checkfor21<=21) {
                for (int i=2; i<=11; i++) {
                    state = new State(cards, Card.getCard(i));
                    policy.addState(state);
                    valueFunction.put(new StateActionPair(state, true), 0f);
                    valueFunction.put(new StateActionPair(state, false), 0f);
                }
            }
        }
    }

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

}
