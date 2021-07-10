package BookingStory;

import BookingStory.policy.BookingPolicy;
import BookingStory.policy.OverBookingPolicy;

public class ReservationService {

    BookingPolicy overBookingPolicy = new OverBookingPolicy();

    public int makeBooking(Cargo cargo, Voyage voyage) {
        if(!overBookingPolicy.isAllowed(cargo, voyage)) {
            return -1;
        }
        int confirmation = 1;
        voyage.addCargo(confirmation, cargo);
        return confirmation;
    }

}
