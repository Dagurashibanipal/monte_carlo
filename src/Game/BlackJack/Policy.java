package Game.BlackJack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class Policy {
    //true=hit  false=stick
    private HashMap<Object, Boolean> policy;

    public Policy() {
        this.policy = new HashMap<>();
    }

    public void addState(Object state) {
        Random rng = new Random();
        Boolean boo = rng.nextBoolean();
        policy.put(state, boo);
    }
    public void addStateActionPair(Object state, boolean action) {
        policy.put(state, action);
    }
    public Boolean act(Object state) {
        Random rng = new Random();
        Boolean helper;
        if (!policy.containsKey(state)) {
            addState(state);
        }
        if (rng.nextDouble()<0.975) {
            helper = policy.get(state);
        } else {
            helper = rng.nextBoolean();
        }
        return helper;
    }

    public HashMap<Object, Boolean> getPolicy() {
        return policy;
    }
    public int getPolicySize() {
        return policy.size();
    }
    public Boolean contains(Object state) {
        return policy.containsKey(state);
    }

    @Override
    public String toString() {
        String string = "";
        Iterator it = policy.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            string += "State: " + pair.getKey()+ "\t Action: " + pair.getValue() + "\n";
        }
        return string;
    }
}
