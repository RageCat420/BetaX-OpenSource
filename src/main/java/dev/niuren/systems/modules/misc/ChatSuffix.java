package dev.niuren.systems.modules.misc;


import dev.niuren.events.event.SendMessageEvent;
import dev.niuren.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;

@Module.Info(name = "ChatSuffix", chineseName = "聊天后缀", category = Module.Category.Misc, description = "fuck")

public class ChatSuffix extends Module {

	public ChatSuffix() {

	}
	public static final String BetaXSuffix = "\uD835\uDC69\uD835\uDC86\uD835\uDC95\uD835\uDC82\uD835\uDC7F";
	@EventHandler
	public void onSendMessage(SendMessageEvent event) {
		if (nullCheck()) return;
		String message = event.message;

		if (message.startsWith("/") || message.startsWith(".")) {
			return;
		}

        message = message + " " + BetaXSuffix;

		event.message = message;
	}
	public enum Modes {
		BetaXSuffix,

	}
	public String getSuffix() {
		return null;
	}
}
