package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.gui.items.buttons.Button;
import me.alpha432.oyvey.features.modules.client.ClickGui;
import me.alpha432.oyvey.util.ColorUtil;
import me.alpha432.oyvey.util.render.RenderUtil;
import me.alpha432.oyvey.util.render.ScissorUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Component extends Feature {

    private final MinecraftClient mc = MinecraftClient.getInstance();
    protected DrawContext context;

    private final List<Item> items = new ArrayList<>();

    public boolean drag;

    private int x, y, x2, y2;
    private int width = 88;
    private int height = 18;

    private boolean open;
    private boolean hidden = false;

    public Component(String name, int x, int y, boolean open) {
        super(name);
        this.x = x;
        this.y = y;
        this.open = open;
    }

    private void drag(int mouseX, int mouseY) {
        if (!drag) return;
        this.x = this.x2 + mouseX;
        this.y = this.y2 + mouseY;
    }

    public void drawScreen(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        this.context = context;
        drag(mouseX, mouseY);

        float totalItemHeight = open ? getTotalItemHeight() - 2f : 0f;

        int baseColor = ClickGui.getInstance().topColor.getValue().getRGB();
        int finalColor = ClickGui.getInstance().rainbow.getValue()
                ? ColorUtil.rainbow(ClickGui.getInstance().rainbowHue.getValue()).getRGB()
                : baseColor;

        // Заголовок
        context.fill(x, y - 1, x + width, y + height - 6, finalColor);

        // Фон айтемов
        if (open) {
            RenderUtil.rect(context, x, y + 12.5f, x + width,
                    y + height + totalItemHeight, 0x77000000);
        }

        // Имя компонента
        drawString(getName(),
                x + 4,
                y - 3 - OyVeyGui.getClickGui().getTextOffset(),
                Color.WHITE.getRGB());

        // Скрин зона
        ScissorUtil.enable(context, x, 0, x + width,
                mc.getWindow().getScaledHeight());

        if (open) {
            float yOffset = y + height - 3f;

            for (Item item : items) {
                if (item.isHidden()) continue;

                item.setLocation(x + 2f, yOffset);
                item.setWidth(width - 4);

                boolean hovering = item.isHovering(mouseX, mouseY);
                if (hovering) ScissorUtil.disable(context);

                item.drawScreen(context, mouseX, mouseY, partialTicks);

                if (hovering) {
                    ScissorUtil.enable(context, x, 0,
                            x + width,
                            mc.getWindow().getScaledHeight());
                }

                yOffset += item.getHeight() + 1.5f;
            }
        }

        ScissorUtil.disable(context);
    }

    // ==========================
    // MOUSE
    // ==========================

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (mouseButton == 0 && isHovering(mouseX, mouseY)) {
            this.x2 = x - mouseX;
            this.y2 = y - mouseY;

            OyVeyGui.getClickGui().getComponents().forEach(c -> c.drag = false);
            drag = true;
            return;
        }

        if (mouseButton == 1 && isHovering(mouseX, mouseY)) {
            open = !open;
            mc.getSoundManager().play(
                    PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1f)
            );
            return;
        }

        if (!open) return;

        items.forEach(item -> item.mouseClicked(mouseX, mouseY, mouseButton));
    }

    public void mouseReleased(int mouseX, int mouseY, int releaseButton) {
        if (releaseButton == 0) drag = false;

        if (!open) return;
        items.forEach(item -> item.mouseReleased(mouseX, mouseY, releaseButton));
    }

    public void onKeyTyped(char typedChar, int keyCode) {
        if (!open) return;
        items.forEach(item -> item.onKeyTyped(typedChar, keyCode));
    }

    public void onKeyPressed(int key) {
        if (!open) return;
        items.forEach(item -> item.onKeyPressed(key));
    }

    public void addButton(Button button) {
        items.add(button);
    }

    // ==========================
    // DRAW STRING (FIXED)
    // ==========================

    protected void drawString(String text, double x, double y, int color) {

        if (OyVey.fontRenderer != null) {
            // Тень
            OyVey.fontRenderer.drawString(
                    context,
                    text,
                    (int) x + 1,
                    (int) y + 1,
                    0x77000000
            );

            // Текст
            OyVey.fontRenderer.drawString(
                    context,
                    text,
                    (int) x,
                    (int) y,
                    color
            );
        } else {
            context.drawTextWithShadow(
                    mc.textRenderer,
                    text,
                    (int) x,
                    (int) y,
                    color
            );
        }
    }

    // ==========================
    // UTILS
    // ==========================

    public boolean isHovering(int mouseX, int mouseY) {
        return mouseX >= x &&
                mouseX <= x + width &&
                mouseY >= y &&
                mouseY <= y + height - (open ? 2 : 0);
    }

    private float getTotalItemHeight() {
        float total = 0f;
        for (Item item : items)
            total += item.getHeight() + 1.5f;
        return total;
    }

    // ==========================
    // GETTERS / SETTERS
    // ==========================

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public boolean isHidden() { return hidden; }
    public void setHidden(boolean hidden) { this.hidden = hidden; }

    public boolean isOpen() { return open; }
    public List<Item> getItems() { return items; }
}
