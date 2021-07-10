package BookingStory.policy;

import BookingStory.Cargo;
import BookingStory.Voyage;

public interface BookingPolicy {

    public boolean isAllowed(Cargo cargo, Voyage voyage);

}
