package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.text.Text;

public class SpeedHud extends HudModule {

    private final int blue = 0xFF3399FF;
    private final int white = 0xFFFFFFFF;

    public SpeedHud() {
        super("Speed", "Displays your movement speed", 5, 35);
    }

    @Subscribe
public void onRender2D(Render2DEvent e) {
    if (mc.player == null) return;

    double dx = mc.player.getX() - mc.player.lastX;
    double dz = mc.player.getZ() - mc.player.lastZ;

    double speed = Math.sqrt(dx * dx + dz * dz) * 20.0;

    String name = "Speed: ";
    String value = String.format("%.2f", speed);

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
