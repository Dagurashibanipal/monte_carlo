package Game.BlackJack.gofor21;

import java.util.Objects;

public class State {
    private int playerScore;

    public State(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return playerScore == state.playerScore;
    }

    @Override
    public int hashCode() {

        return Objects.hash(playerScore);
    }

    @Override
    public String toString() {
        if (playerScore<10) {
            return " " + playerScore;
        }
        return "" + playerScore;
    }
}

