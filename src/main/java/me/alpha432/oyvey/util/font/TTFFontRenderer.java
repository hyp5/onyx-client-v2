package me.alpha432.oyvey.util.font;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.awt.*;
import java.io.InputStream;

public class TTFFontRenderer {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final Font font;

    public TTFFontRenderer(InputStream stream, float size) throws Exception {
        Font base = Font.createFont(Font.TRUETYPE_FONT, stream);
        this.font = base.deriveFont(size);
    }

    public void drawString(DrawContext context, String text, int x, int y, int color) {
        if (text == null) return;
        context.drawTextWithShadow(mc.textRenderer, text, x, y, color);
    }

    public int getWidth(String text) {
        return mc.textRenderer.getWidth(text);
    }

    public int getHeight() {
        return mc.textRenderer.fontHeight;
    }
}
