package Game.BlackJack;

import java.util.Objects;

public class StateActionPair {
    private Object state;
    private Boolean action;


    public StateActionPair(Object state, Boolean action) {
        this.state = state;
        this.action = action;
    }

    public Object getState() {
        return state;
    }
    public void setState(Object state) {
        this.state = state;
    }
    public Boolean getAction() {
        return action;
    }
    public void setAction(Boolean action) {
        this.action = action;
    }
/*
    public float getValue() {
        return totalValue/(float) counter;
    }

    public void addValue(float value) {
        totalValue += value;
        counter++;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateActionPair)) return false;
        StateActionPair that = (StateActionPair) o;
        return Objects.equals(state, that.state) &&
                Objects.equals(action, that.action);
    }

    @Override
    public int hashCode() {

        return Objects.hash(state, action);
    }
}
