package Game.BlackJack.restricted;

import java.util.Objects;

public class State {
    private int playerScore;
    private int croupierScore;
    private boolean ace;

    public State(int playerScore, int croupierScore, boolean ace) {
        this.playerScore = playerScore;
        this.croupierScore = croupierScore;
        this.ace = ace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof State)) return false;
        State state = (State) o;
        return playerScore == state.playerScore &&
                croupierScore == state.croupierScore &&
                ace == state.ace;
    }

    @Override
    public int hashCode() {

        return Objects.hash(playerScore, croupierScore, ace);
    }

    @Override
    public String toString() {
        return "State{" +
                "playerScore=" + playerScore +
                ", croupierScore=" + croupierScore +
                ", ace=" + ace +
                '}';
    }
}
