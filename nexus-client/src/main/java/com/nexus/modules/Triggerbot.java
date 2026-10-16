package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.Targets;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public class Triggerbot extends Module {
    private final Setting cooldown = setting("cooldown", 0.95, 0.1, 1.0);
    private final Setting players = setting("players", 1, 0, 1);
    private final Setting mobs = setting("mobs", 1, 0, 1);
    private final Setting animals = setting("animals", 0, 0, 1);
    private final Setting invisible = setting("invisible", 0, 0, 1);

    public Triggerbot() { super("Triggerbot", "Atakuje, gdy celownik jest na przeciwniku.", Category.COMBAT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (mc.screen != null || mc.gameMode == null) return;
        if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof LivingEntity e
                && Targets.valid(e, p, players.bool(), mobs.bool(), animals.bool(), invisible.bool())
                && p.getAttackStrengthScale(0.0f) >= cooldown.get()) {
            mc.gameMode.attack(p, e);
            p.swing(InteractionHand.MAIN_HAND);
        }
    }
}
