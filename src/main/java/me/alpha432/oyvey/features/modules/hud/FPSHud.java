package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.text.Text;

public class FPSHud extends HudModule {

    private final int blue = 0xFF3399FF;
    private final int white = 0xFFFFFFFF;

    public FPSHud() {
        super("FPS", "Displays your FPS", 5, 5);
    }

    @Subscribe
public void onRender2D(Render2DEvent e) {
    if (mc.player == null) return;

    int fps = mc.getCurrentFps();

    String name = "FPS: ";
    String value = String.valueOf(fps);

    int x = (int) getX();
    int y = (int) getY();

    // Синий заголовок
    e.getContext().drawTextWithShadow(
            mc.textRenderer,
            name,
            x,
            y,
            blue
    );

    // Белое значение
    e.getContext().drawTextWithShadow(
            mc.textRenderer,
            value,
            x + mc.textRenderer.getWidth(name),
            y,
            white
    );
}
}
