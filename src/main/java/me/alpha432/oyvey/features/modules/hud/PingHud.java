package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.text.Text;

public class PingHud extends HudModule {

    private final int blue = 0xFF3399FF;
    private final int white = 0xFFFFFFFF;

    public PingHud() {
        super("Ping", "Displays your ping", 5, 20);
    }

    @Subscribe
public void onRender2D(Render2DEvent e) {
    if (mc.player == null || mc.getNetworkHandler() == null) return;

    PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
    int ping = entry != null ? entry.getLatency() : 0;

    String name = "Ping: ";
    String value = ping + " ms";

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
