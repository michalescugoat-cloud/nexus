package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.MoveUtil;
import net.minecraft.world.phys.Vec3;

public class Speed extends Module {
    private final Setting multiplier = setting("multiplier", 1.6, 1.0, 5.0);

    public Speed() { super("Speed", "Zwieksza predkosc poruszania sie.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        double[] dir = MoveUtil.direction(mc);
        if (dir == null) return;
        double s = 0.2873 * multiplier.get();
        Vec3 v = p.getDeltaMovement();
        p.setDeltaMovement(new Vec3(dir[0] * s, v.y, dir[1] * s));
    }
}
