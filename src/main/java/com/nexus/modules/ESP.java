package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ESP extends Module {
    public ESP() { super("ESP", "Podswietla zywe byty (widoczne przez sciany).", Category.RENDER); }

    @Override
    public void onTick() {
        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof LivingEntity && e != mc.player) e.setGlowingTag(true);
        }
    }

    @Override
    protected void onDisable() {
        if (mc.level == null) return;
        for (Entity e : mc.level.entitiesForRendering()) {
            if (e instanceof LivingEntity && e != mc.player) e.setGlowingTag(false);
        }
    }
}
