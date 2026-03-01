package me.alpha432.oyvey.features.modules.hud;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.Render2DEvent;
import me.alpha432.oyvey.features.modules.client.HudModule;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ArmorHud extends HudModule {

    public ArmorHud() {
        super("ArmorHud", "Displays your armor", 200, 20);
    }

    @Override
    @Subscribe
    public void onRender2D(Render2DEvent e) {
        if (nullCheck()) return;

        DrawContext context = e.getContext();

        int baseX = (int) getX();
        int baseY = (int) getY();

        EquipmentSlot[] slots = {
                EquipmentSlot.HEAD,
                EquipmentSlot.CHEST,
                EquipmentSlot.LEGS,
                EquipmentSlot.FEET
        };

        int spacing = 18;
        int index = 0;

        for (EquipmentSlot slot : slots) {

            ItemStack stack = mc.player.getEquippedStack(slot);
            if (stack.isEmpty()) continue;

            int x = baseX + index * spacing;
            int y = baseY;

            // Рисуем предмет
            context.drawItem(stack, x, y);

            // === ПРОЧНОСТЬ ===
            if (stack.isDamageable()) {

                int max = stack.getMaxDamage();
                int current = max - stack.getDamage();
                int percent = Math.round((current * 100f) / max);

                String text = String.valueOf(percent);

                int textWidth = mc.textRenderer.getWidth(text);

                // Центрируем по предмету
                int textX = x + 8 - textWidth / 2;

                // Ставим снизу как в Meteor
                int textY = y + 16 - mc.textRenderer.fontHeight;

                context.drawTextWithShadow(
                        mc.textRenderer,
                        Text.literal(text),
                        textX,
                        textY,
                        0x00FF00 // зелёный
                );
            }

            index++;
        }

        setWidth(index * spacing);
        setHeight(16);
    }
}
