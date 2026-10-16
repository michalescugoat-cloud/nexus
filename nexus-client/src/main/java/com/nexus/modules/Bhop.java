package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.MoveUtil;
import net.minecraft.world.phys.Vec3;

public class Bhop extends Module {
    private final Setting speed = setting("speed", 1.3, 1.0, 3.0);

    public Bhop() { super("Bhop", "Automatyczne skakanie z przyspieszeniem podczas ruchu.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        double[] dir = MoveUtil.direction(mc);
        if (dir == null) return;
        double s = 0.2873 * speed.get();
        Vec3 v = p.getDeltaMovement();
        double y = p.onGround() ? 0.42 : v.y;
        p.setDeltaMovement(new Vec3(dir[0] * s, y, dir[1] * s));
    }
}
