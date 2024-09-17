package dev.niuren.events.event;



public class SendMessageEvent{

    public String message;
    public final String defaultMessage;

    public SendMessageEvent(String message) {
        this.defaultMessage = message;
        this.message = message;
    }
}
