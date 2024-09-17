package dev.niuren.gui;

import net.minecraft.client.gui.DrawContext;

public interface Component {
    void render(DrawContext context, int mouseX, int mouseY);

    void mouseDown(int mouseX, int mouseY, int button);

    void mouseUp(int mouseX, int mouseY);

    void keyPress(int key);
    default void keyTyped(char charW, int key) {}

    void close();
}
