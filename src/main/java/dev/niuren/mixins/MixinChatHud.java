package dev.niuren.mixins;

import dev.niuren.mixininterface.IChatHud;
import dev.niuren.mixininterface.IChatHudLine;
import dev.niuren.systems.modules.Modules;
import dev.niuren.systems.modules.client.Prefix;
import dev.niuren.utils.misc.StringCharacterVisitor;
import dev.niuren.utils.player.ChatUtils;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class MixinChatHud implements IChatHud {
    @Mutable
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Unique
    private int nextId;

    @Shadow
    public abstract void addMessage(Text message);

    @Shadow
    @Final
    private List<ChatHudLine> messages;

    @Override
    public void add(Text message, int id) {
        nextId = id;
        addMessage(message);
        nextId = 0;
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLineVisible(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine) (Object) visibleMessages.get(0)).setId(nextId);
    }

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;ILnet/minecraft/client/gui/hud/MessageIndicator;Z)V", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onAddMessageAfterNewChatHudLine(Text message, MessageSignatureData signature, int ticks, MessageIndicator indicator, boolean refresh, CallbackInfo info) {
        ((IChatHudLine) (Object) messages.get(0)).setId(nextId);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;content()Lnet/minecraft/text/OrderedText;"))
    private OrderedText redirect(ChatHudLine.Visible instance) {
        StringCharacterVisitor visitor = new StringCharacterVisitor();
        instance.content().accept(visitor);
        Prefix prefix = Modules.get().get(Prefix.class);
        if (prefix.isActive() && visitor.result.toString().startsWith("[BetaX] ")) {
            MutableText text = prefix.getPrefix(visitor.result.toString());
            if (ChatUtils.hasModuleInfo(visitor.result.toString())) {
                text.append(ChatUtils.putModule(visitor.result.toString()));
            } else {
                text.append(ChatUtils.asInfoModule(visitor.result.toString()));
            }

            return text.asOrderedText();
        } else {
            return instance.content();
        }
    }
}
