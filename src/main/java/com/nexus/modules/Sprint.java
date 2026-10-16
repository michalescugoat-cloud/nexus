package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;

public class Sprint extends Module {
    public Sprint() { super("Sprint", "Automatycznie sprintuje podczas chodzenia do przodu.", Category.MOVEMENT); }

    @Override
    public void onTick() {
        var p = mc.player;
        if (mc.options.keyUp.isDown() && !p.horizontalCollision && !p.isUsingItem()
                && p.getFoodData().getFoodLevel() > 6) {
            p.setSprinting(true);
        }
    }
}
