package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import net.minecraft.world.phys.Vec3;

public class Glide extends Module {
    private final Setting fallSpeed = setting("fallSpeed", 0.05, 0.0, 0.5);

    public Glide() { super("Glide", "Spowalnia opadanie, szybowanie w powietrzu.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        Vec3 v = p.getDeltaMovement();
        if (!p.onGround() && v.y < 0) {
            p.setDeltaMovement(new Vec3(v.x, -fallSpeed.get(), v.z));
            p.resetFallDistance();
        }
    }
}
