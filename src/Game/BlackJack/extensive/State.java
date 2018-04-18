package Game.BlackJack.extensive;

import java.util.*;

public class State {
    //maybe use set
    private final List<Card> cards = new ArrayList<>();
    private Card croupierCard;

    public State(List<Card> playerCards, Card croupierCard) {
        this.cards.addAll(playerCards);
        this.croupierCard = croupierCard;
    }
    public State() {
    }

    public List<Card> getCards() {
        return this.cards;
    }

    public Card getCroupierCard() { return this.croupierCard; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        List<Card> a = state.getCards();
        List<Card> b = this.cards;
        Iterator ai = a.iterator();
        Iterator bi = b.iterator();
        if (a.size()!=b.size()) {
            return false;
        }
        while (ai.hasNext()) {
            while (bi.hasNext()) {
                if (((Card) ai.next()).getValue() == ((Card) bi.next()).getValue()) {
                    ai.remove();
                    bi.remove();
                }
            }
        }
        if (a.isEmpty() && b.isEmpty()) {
            return state.getCroupierCard()==this.croupierCard;
        }
        return false;
    }

    @Override
    public int hashCode() {
        List<Integer> list = new ArrayList<>();
        for (Card card : cards) {
            list.add(card.getValue());
        }
        Collections.sort(list);
        return Objects.hash(list, croupierCard);
    }
}
