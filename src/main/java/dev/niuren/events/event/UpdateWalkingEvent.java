package dev.niuren.events.event;


import dev.niuren.events.Cancelled;

public class UpdateWalkingEvent extends Cancelled {
    private boolean cancelRotate = false;
    public UpdateWalkingEvent(Stage stage) {
        super();
    }

    public void cancelRotate() {
        this.cancelRotate = true;
    }
    public void setCancelRotate(boolean cancelRotate) {
        this.cancelRotate = cancelRotate;
    }

    public boolean isCancelRotate() {
        return cancelRotate;
    }
}
