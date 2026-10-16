package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.Rotations;
import com.nexus.util.Targets;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;

public class KillAura extends Module {
    private final Setting range = setting("range", 3.5, 1.0, 6.0);
    private final Setting rotate = setting("rotate", 1, 0, 1);
    private final Setting speed = setting("rotSpeed", 40, 1, 180);
    private final Setting fov = setting("fov", 360, 10, 360);
    private final Setting cooldown = setting("cooldown", 0.95, 0.1, 1.0);
    private final Setting priority = setting("priority", 0, 0, 1); // 0 = dystans, 1 = najmniej HP
    private final Setting players = setting("players", 1, 0, 1);
    private final Setting mobs = setting("mobs", 1, 0, 1);
    private final Setting animals = setting("animals", 0, 0, 1);
    private final Setting invisible = setting("invisible", 0, 0, 1);
    private final Setting walls = setting("walls", 0, 0, 1);

    public KillAura() { super("KillAura", "Automatycznie celuje i atakuje pobliskie cele.", Category.COMBAT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (mc.gameMode == null) return;

        LivingEntity best = null;
        double bestScore = Double.MAX_VALUE;
        for (LivingEntity e : mc.level.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(range.get()))) {
            if (!Targets.valid(e, p, players.bool(), mobs.bool(), animals.bool(), invisible.bool())) continue;
            double d = p.distanceTo(e);
            if (d > range.get()) continue;
            if (!walls.bool() && !p.hasLineOfSight(e)) continue;
            if (fov.get() < 360 && Rotations.diff(p, Targets.aimPoint(e)) > fov.get() / 2) continue;
            double score = priority.get() >= 0.5 ? e.getHealth() : d;
            if (score < bestScore) { bestScore = score; best = e; }
        }
        if (best == null) return;

        if (rotate.bool()) {
            double left = Rotations.toward(p, Targets.aimPoint(best), speed.get());
            if (left > 8.0) return; // jeszcze nie wycelowany
        }
        if (p.getAttackStrengthScale(0.0f) < cooldown.get()) return;

        mc.gameMode.attack(p, best);
        p.swing(InteractionHand.MAIN_HAND);
    }
}
