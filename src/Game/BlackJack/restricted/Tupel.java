package Game.BlackJack.restricted;

import java.util.Objects;

public class Tupel {
    private int value;
    private int counter;

    public Tupel(int value, int counter) {
        this.value = value;
        this.counter = counter;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tupel)) return false;
        Tupel tupel = (Tupel) o;
        return value == tupel.value &&
                counter == tupel.counter;
    }

    @Override
    public int hashCode() {

        return Objects.hash(value, counter);
    }
}
