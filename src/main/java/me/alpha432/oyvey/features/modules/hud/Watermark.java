package me.alpha432.oyvey.features.modules.hud;

import me.alpha432.oyvey.OyVey;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import me.alpha432.oyvey.util.TextUtil;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import com.google.common.eventbus.Subscribe;
import net.minecraft.text.Text; // Важный импорт для 1.21

public class Watermark extends HudModule {
    // Убрал str(), предполагая что это ваш хелпер для String Setting
    public Setting<String> text = register(new Setting<>("Text", OyVey.NAME)); 

    public Watermark() {
        super("Watermark", "Display watermark", 100, 10);
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        // Формируем строку
        String watermarkString = String.format("%s %s", text.getValue(), OyVey.VERSION);

        // В 1.21 рендеринг требует объект Text, а не String
        // Обратите внимание: сигнатура метода может отличаться в зависимости от версии Yarn
        e.getContext().drawTextWithShadow(
            mc.textRenderer, 
            Text.literal(watermarkString), // Превращаем String в Text
            (int) getX(), 
            (int) getY(), 
            -1 // Цвет (белый)
        );

        // Обновляем размеры для Drag & Drop (если он у вас есть)
        setWidth(mc.textRenderer.getWidth(watermarkString));
        setHeight(mc.textRenderer.fontHeight);
    }
}
