package com.nexus.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class Targets {
    private Targets() {}

    public static boolean valid(LivingEntity e, Player self, boolean players, boolean hostile,
                                boolean animals, boolean invisible) {
        if (e == self || !e.isAlive() || e instanceof ArmorStand) return false;
        if (!invisible && e.isInvisible()) return false;
        if (e instanceof Player) return players;
        if (e instanceof Enemy) return hostile;
        return animals && e.getType().getCategory() == MobCategory.CREATURE;
    }

    public static Vec3 aimPoint(LivingEntity e) {
        return e.getBoundingBox().getCenter();
    }
}
