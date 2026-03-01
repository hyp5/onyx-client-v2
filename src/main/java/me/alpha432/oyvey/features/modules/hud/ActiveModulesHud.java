package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.Module;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ActiveModulesHud extends HudModule {

    public Setting<Boolean> shadow = register(new Setting<>("Shadow", true));
    public Setting<Double> scale = register(new Setting<>("Scale", 1.0));

    private final List<Module> modules = new ArrayList<>();
    private final int blueColor = 0xFF3399FF; // синий цвет

    // Смещения от правого верхнего угла
    private int offsetX = 5;
    private int offsetY = 5;

    public ActiveModulesHud() {
        super("ActiveModules", "Displays active modules (right-aligned, blue)", 0, 0);
    }

    @Subscribe
    public void onRender2D(Render2DEvent e) {
    if (mc.player == null) return;

    // Получаем реально активные модули, исключаем HudModule
    modules.clear();
    for (Module m : OyVey.moduleManager.getModules()) {
        if (m.isEnabled() && !m.hidden && !(m instanceof HudModule)) modules.add(m);
    }

    // Сортируем от самого длинного названия к самому короткому
    modules.sort((m1, m2) -> mc.textRenderer.getWidth(m2.getName()) - mc.textRenderer.getWidth(m1.getName()));

    int yOffset = offsetY;
    int screenWidth = mc.getWindow().getScaledWidth();

    int maxWidth = 0;
    for (Module m : modules) {
        String text = m.getName();
        int textWidth = mc.textRenderer.getWidth(text);
        if (textWidth > maxWidth) maxWidth = textWidth;

        // Правый верхний угол + смещение
        int xPos = screenWidth - offsetX - textWidth;

        e.getContext().drawTextWithShadow(
                mc.textRenderer,
                Text.literal(text),
                xPos,
                yOffset,
                blueColor
        );

        yOffset += (int) (mc.textRenderer.fontHeight * scale.getValue());
    }

    // Если нужно для Drag & Drop, можно хранить размеры локально
    int hudWidth = maxWidth;
    int hudHeight = (int) (modules.size() * mc.textRenderer.fontHeight * scale.getValue());
}

    // Метод для drag (можно привязать к mouseDragged)
    public void drag(int mouseX, int mouseY) {
        offsetX = mc.getWindow().getScaledWidth() - mouseX;
        offsetY = mouseY;
    }
}
