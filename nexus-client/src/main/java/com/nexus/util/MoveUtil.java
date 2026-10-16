package com.nexus.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public final class MoveUtil {
    private MoveUtil() {}

    /** Zwraca znormalizowany kierunek ruchu {x, z} wg klawiszy i obrotu gracza, albo null gdy stoi. */
    public static double[] direction(Minecraft mc) {
        var o = mc.options;
        LocalPlayer p = mc.player;
        double fwd = (o.keyUp.isDown() ? 1 : 0) - (o.keyDown.isDown() ? 1 : 0);
        double left = (o.keyLeft.isDown() ? 1 : 0) - (o.keyRight.isDown() ? 1 : 0);
        if (fwd == 0 && left == 0) return null;
        double yaw = Math.toRadians(p.getYRot());
        double dx = -Math.sin(yaw) * fwd + Math.cos(yaw) * left;
        double dz = Math.cos(yaw) * fwd + Math.sin(yaw) * left;
        double len = Math.sqrt(dx * dx + dz * dz);
        return new double[]{dx / len, dz / len};
    }
}
