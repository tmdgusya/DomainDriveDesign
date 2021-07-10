package BookingStory.policy;

import BookingStory.Cargo;
import BookingStory.Voyage;

public class OverBookingPolicy implements BookingPolicy {
    @Override
    public boolean isAllowed(Cargo cargo, Voyage voyage) {
        return cargo.size() + voyage.currentRemainContainerSize() > Voyage.MAX_CONTAIN_SIZE;
    }
}
