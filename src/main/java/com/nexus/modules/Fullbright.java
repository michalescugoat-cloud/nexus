package com.nexus.modules;

import com.nexus.module.Category;
import com.nexus.module.Module;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Fullbright extends Module {
    public Fullbright() { super("Fullbright", "Pelna jasnosc (night vision po stronie klienta).", Category.RENDER); }

    @Override
    public void onTick() {
        mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1000, 0, false, false, false));
    }

    @Override
    protected void onDisable() {
        if (mc.player != null) mc.player.removeEffect(MobEffects.NIGHT_VISION);
    }
}
