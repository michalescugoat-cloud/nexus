package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import net.minecraft.world.phys.Vec3;

public class Spider extends Module {
    public Spider() { super("Spider", "Wspinaczka po scianach jak pajak.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (p.horizontalCollision) {
            Vec3 v = p.getDeltaMovement();
            p.setDeltaMovement(new Vec3(v.x, 0.2, v.z));
        }
    }
}
