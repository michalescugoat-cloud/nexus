package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

public class KillAura extends Module {
    private final Setting range = setting("range", 3.5, 1.0, 6.0);
    private final Setting players = setting("players", 1, 0, 1);
    private final Setting mobs = setting("mobs", 1, 0, 1);

    public KillAura() { super("KillAura", "Automatycznie atakuje pobliskie cele.", Category.COMBAT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (mc.gameMode == null || p.getAttackStrengthScale(0.0f) < 1.0f) return;

        LivingEntity best = null;
        double bestDist = Double.MAX_VALUE;
        for (LivingEntity e : mc.level.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(range.get()))) {
            if (e == p || !e.isAlive() || e instanceof ArmorStand) continue;
            boolean valid = (e instanceof Player && players.bool()) || (e instanceof Enemy && mobs.bool());
            if (!valid) continue;
            double d = p.distanceTo(e);
            if (d > range.get() || !p.hasLineOfSight(e)) continue;
            if (d < bestDist) { bestDist = d; best = e; }
        }
        if (best != null) {
            mc.gameMode.attack(p, best);
            p.swing(InteractionHand.MAIN_HAND);
        }
    }
}
