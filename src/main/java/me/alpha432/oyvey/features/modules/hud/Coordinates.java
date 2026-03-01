package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import me.alpha432.oyvey.features.settings.Setting;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import java.awt.Color;

public class Coordinates extends HudModule {

    public Setting<Boolean> nether = register(new Setting<>("NetherCoords", true));
    public Setting<Boolean> shadow = register(new Setting<>("Shadow", true));

    public Coordinates() {
        super("Coordinates", "Display coordinates and compass", 100, 40);
    }

    private enum Direction {
        N("N"), E("E"), S("S"), W("W");

        private final String label;
        Direction(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        if (nullCheck()) return;

        int x = mc.player.getBlockX();
        int y = mc.player.getBlockY();
        int z = mc.player.getBlockZ();

        Identifier dimension = mc.player.getWorld().getRegistryKey().getValue();
        boolean inNether = dimension.getPath().equals("the_nether");

        int baseX = (int) getX();
        int baseY = (int) getY();

        // Основной текст с координатами
        String main = "XYZ: " + x + ", " + y + ", " + z;
        e.getContext().drawTextWithShadow(
                mc.textRenderer,
                Text.literal(main),
                baseX,
                baseY,
                Color.WHITE.getRGB()
        );

        int height = mc.textRenderer.fontHeight;

        // Дополнительные координаты Overworld/Nether
        if (nether.getValue()) {
            String second;
            if (inNether) {
                second = "Overworld: " + (x * 8) + ", " + (z * 8);
            } else {
                second = "Nether: " + (x / 8) + ", " + (z / 8);
            }

            e.getContext().drawTextWithShadow(
                    mc.textRenderer,
                    Text.literal(second),
                    baseX,
                    baseY + height + 2,
                    0xAAAAFF
            );

            setHeight(height * 2 + 2);
            setWidth(Math.max(
                    mc.textRenderer.getWidth(main),
                    mc.textRenderer.getWidth(second)
            ));
        } else {
            setHeight(height);
            setWidth(mc.textRenderer.getWidth(main));
        }

        // Компас
        double yaw = Math.toRadians(MathHelper.wrapDegrees(mc.player.getYaw()));
        double compassScale = 40; // радиус "компаса"
        int centerX = baseX + (int)(getWidth() / 2.0f);
        int centerY = baseY - 20; // над координатами

        for (Direction dir : Direction.values()) {
            double angle = yaw + dir.ordinal() * Math.PI / 2;
            int dx = (int) (Math.sin(angle) * compassScale);
            int dy = (int) (-Math.cos(angle) * compassScale); // - чтобы Y шло вниз

            int color = dir == Direction.N ? 0x2255FF : 0xAAAAAA; // синий для N, серый для остальных
            e.getContext().drawTextWithShadow(
                    mc.textRenderer,
                    Text.literal(dir.getLabel()),
                    centerX + dx - mc.textRenderer.getWidth(dir.getLabel()) / 2,
                    centerY + dy - mc.textRenderer.fontHeight / 2,
                    color
            );
        }
    }
}
