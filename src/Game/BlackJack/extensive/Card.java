package Game.BlackJack.extensive;

import java.util.HashMap;
import java.util.Map;

public enum Card{
    ACE (11),
    TWO (2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);

    private int value;
    private static final Map lookup = new HashMap();

    static {
        //Create reverse lookup hash map
        for(Card card : Card.values())
            lookup.put(card.getValue(), card);
    }

    Card(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
    public static Card getCard(int value) {
        return (Card) lookup.get(value);
    }

}
