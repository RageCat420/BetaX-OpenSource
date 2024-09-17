package dev.niuren.events;


public class Cancelled {
    private boolean cancelled = false;


    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        cancelled = true;
    }
    public enum Stage{
        Pre, Post
    }
    public Cancelled() {
        this.cancelled = false;

    }
    public Stage getStage() {
        return null;
    }
}
