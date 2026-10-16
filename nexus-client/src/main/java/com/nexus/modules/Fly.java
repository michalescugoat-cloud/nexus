package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import com.nexus.util.MoveUtil;
import net.minecraft.world.phys.Vec3;

public class Fly extends Module {
    private final Setting speed = setting("speed", 0.6, 0.1, 5.0);

    public Fly() { super("Fly", "Latanie: spacja w gore, shift w dol, WASD w kierunku patrzenia.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        double s = speed.get();
        double y = mc.options.keyJump.isDown() ? s : mc.options.keyShift.isDown() ? -s : 0;
        double[] dir = MoveUtil.direction(mc);
        double x = dir == null ? 0 : dir[0] * s;
        double z = dir == null ? 0 : dir[1] * s;
        p.setDeltaMovement(new Vec3(x, y, z));
        p.resetFallDistance();
    }
}
