package BookingStory;

import java.util.HashMap;
import java.util.Map;

public class Voyage {

    public static final int MAX_CONTAIN_SIZE = 10;

    private final Map<Integer, Cargo> containers = new HashMap<>();

    public void addCargo(int confirmation, Cargo cargo) {
        containers.put(confirmation, cargo);
    }

    public int currentRemainContainerSize() {
        return MAX_CONTAIN_SIZE - containers.size();
    }

}
