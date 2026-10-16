package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.Rotations;
import com.nexus.util.Targets;
import net.minecraft.world.entity.LivingEntity;

public class AimAssist extends Module {
    private final Setting range = setting("range", 5.0, 1.0, 8.0);
    private final Setting speed = setting("speed", 8, 1, 60);
    private final Setting fov = setting("fov", 90, 10, 360);
    private final Setting onlyClick = setting("onlyClick", 1, 0, 1);
    private final Setting players = setting("players", 1, 0, 1);
    private final Setting mobs = setting("mobs", 0, 0, 1);
    private final Setting animals = setting("animals", 0, 0, 1);

    public AimAssist() { super("AimAssist", "Plynnie doprowadza celownik do najblizszego celu.", Category.COMBAT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (mc.screen != null) return;
        if (onlyClick.bool() && !mc.options.keyAttack.isDown()) return;

        LivingEntity best = null;
        double bestAngle = Double.MAX_VALUE;
        for (LivingEntity e : mc.level.getEntitiesOfClass(LivingEntity.class, p.getBoundingBox().inflate(range.get()))) {
            if (!Targets.valid(e, p, players.bool(), mobs.bool(), animals.bool(), false)) continue;
            if (p.distanceTo(e) > range.get() || !p.hasLineOfSight(e)) continue;
            double a = Rotations.diff(p, Targets.aimPoint(e));
            if (a > fov.get() / 2) continue;
            if (a < bestAngle) { bestAngle = a; best = e; }
        }
        if (best != null) Rotations.toward(p, Targets.aimPoint(best), speed.get());
    }
}
