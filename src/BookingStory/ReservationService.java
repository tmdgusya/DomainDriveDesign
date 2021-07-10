package BookingStory;

import BookingStory.policy.OverBookingPolicy;

public class ReservationService {

    OverBookingPolicy overBookingPolicy = new OverBookingPolicy();

    public int makeBooking(Cargo cargo, Voyage voyage) {
        if(!overBookingPolicy.isAllowed(cargo, voyage)) {
            return -1;
        }
        int confirmation = 1;
        voyage.addCargo(confirmation, cargo);
        return confirmation;
    }

}
