package me.alpha432.oyvey.features.gui;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.Feature;
import me.alpha432.oyvey.features.gui.items.Item;
import me.alpha432.oyvey.features.gui.items.buttons.ModuleButton;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;

public class OyVeyGui extends Screen {
    private static OyVeyGui INSTANCE;
    private static Color colorClipboard = null;

    static {
        INSTANCE = new OyVeyGui();
    }

    private final ArrayList<Component> components = new ArrayList<>();

    public OyVeyGui() {
        super(Text.literal("OyVey"));
        setInstance();
        load();
    }

    public static OyVeyGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OyVeyGui();
        }
        return INSTANCE;
    }

    public static OyVeyGui getClickGui() {
        return OyVeyGui.getInstance();
    }

    private void setInstance() {
        INSTANCE = this;
    }

    private void load() {
    this.components.clear();

    int screenWidth = net.minecraft.client.MinecraftClient.getInstance()
            .getWindow().getScaledWidth();

    int startY = 4;
    int panelWidth = 88;      // ширина панели
    int panelHeader = 14;     // высота заголовка
    int spacing = 6;          // расстояние между панелями

    // Список категорий без HUD
    ArrayList<Module.Category> categories = new ArrayList<>();
    for (Module.Category category : OyVey.moduleManager.getCategories()) {
        if (category != Module.Category.HUD) {
            categories.add(category);
        }
    }

    // Сколько колонок помещается
    int columns = Math.max(1, screenWidth / (panelWidth + spacing));

    // Центровка
    int totalWidth = columns * (panelWidth + spacing);
    int startX = (screenWidth - totalWidth) / 2;

    int x = startX;
    int y = startY;
    int column = 0;

    for (Module.Category category : categories) {

        Component panel = new Component(category.getName(), x, y, true);

        OyVey.moduleManager.stream()
                .filter(m -> m.getCategory() == category && !m.hidden)
                .map(ModuleButton::new)
                .forEach(panel::addButton);

        this.components.add(panel);

        column++;
        if (column >= columns) {
            column = 0;
            x = startX;
            y += panelHeader + 120; // расстояние между строками
        } else {
            x += panelWidth + spacing;
        }
    }

    this.components.forEach(component ->
            component.getItems().sort(Comparator.comparing(Feature::getName))
    );
}
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Item.context = context;
        context.fill(0, 0, context.getScaledWindowWidth(), context.getScaledWindowHeight(), new Color(0, 0, 0, 120).hashCode());
        this.components.forEach(components -> components.drawScreen(context, mouseX, mouseY, delta));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int clickedButton) {
        this.components.forEach(components -> components.mouseClicked((int) mouseX, (int) mouseY, clickedButton));
        return super.mouseClicked(mouseX, mouseY, clickedButton);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int releaseButton) {
        this.components.forEach(components -> components.mouseReleased((int) mouseX, (int) mouseY, releaseButton));
        return super.mouseReleased(mouseX, mouseY, releaseButton);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (verticalAmount < 0) {
            this.components.forEach(component -> component.setY(component.getY() - 10));
        } else if (verticalAmount > 0) {
            this.components.forEach(component -> component.setY(component.getY() + 10));
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.components.forEach(component -> component.onKeyPressed(keyCode));
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        this.components.forEach(component -> component.onKeyTyped(chr, modifiers));
        return super.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }//ignore 1.21.8 blur thing

    public final ArrayList<Component> getComponents() {
        return this.components;
    }

    public int getTextOffset() {
        return -6;
    }

    public static Color getColorClipboard() {
        return colorClipboard;
    }

    public static void setColorClipboard(Color color) {
        colorClipboard = color;
    }
}
