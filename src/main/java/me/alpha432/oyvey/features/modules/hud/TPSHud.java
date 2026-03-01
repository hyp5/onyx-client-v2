package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.text.Text;

public class TPSHud extends HudModule {

    private final int blue = 0xFF3399FF;
    private final int white = 0xFFFFFFFF;

    public TPSHud() {
        super("TPS", "Displays server TPS", 5, 50);
    }

    @Subscribe
public void onRender2D(Render2DEvent e) {
    if (mc.player == null) return;

    double tps = 20.0;

    String name = "TPS: ";
    String value = String.format("%.1f", tps);

    int x = (int) getX();
    int y = (int) getY();

    e.getContext().drawTextWithShadow(
            mc.textRenderer,
            name,
            x,
            y,
            blue
    );

    e.getContext().drawTextWithShadow(
            mc.textRenderer,
            value,
            x + mc.textRenderer.getWidth(name),
            y,
            white
    );
}
}
