package com.nexus.util;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class Rotations {
    private Rotations() {}

    public static float yawTo(LocalPlayer p, Vec3 t) {
        Vec3 eye = p.getEyePosition();
        return (float) Math.toDegrees(Math.atan2(-(t.x - eye.x), t.z - eye.z));
    }

    public static float pitchTo(LocalPlayer p, Vec3 t) {
        Vec3 eye = p.getEyePosition();
        double dx = t.x - eye.x, dy = t.y - eye.y, dz = t.z - eye.z;
        return (float) -Math.toDegrees(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)));
    }

    /** Kat (w stopniach) miedzy obecnym spojrzeniem a celem. */
    public static double diff(LocalPlayer p, Vec3 t) {
        double dy = Mth.wrapDegrees(yawTo(p, t) - p.getYRot());
        double dp = pitchTo(p, t) - p.getXRot();
        return Math.sqrt(dy * dy + dp * dp);
    }

    /** Obraca gracza w strone celu o max 'speed' stopni na tick. Zwraca pozostaly kat. */
    public static double toward(LocalPlayer p, Vec3 t, double speed) {
        float dyaw = Mth.wrapDegrees(yawTo(p, t) - p.getYRot());
        float dpitch = pitchTo(p, t) - p.getXRot();
        float s = (float) speed;
        p.setYRot(p.getYRot() + Mth.clamp(dyaw, -s, s));
        p.setXRot(Mth.clamp(p.getXRot() + Mth.clamp(dpitch, -s, s), -90f, 90f));
        return diff(p, t);
    }
}
