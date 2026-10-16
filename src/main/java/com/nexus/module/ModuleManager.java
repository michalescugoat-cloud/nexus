package com.nexus.module;

import com.nexus.NexusClient;
import com.nexus.modules.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new Sprint());
        modules.add(new Fly());
        modules.add(new Speed());
        modules.add(new Spider());
        modules.add(new KillAura());
        modules.add(new Fullbright());
        modules.add(new ESP());
    }

    public List<Module> all() { return modules; }

    public Module get(String name) {
        for (Module m : modules) if (m.name.equalsIgnoreCase(name)) return m;
        return null;
    }

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;
        for (Module m : modules) {
            if (!m.isEnabled()) continue;
            try {
                m.onTick();
            } catch (Throwable t) {
                NexusClient.LOG.error("Blad w module {} - wylaczam", m.name, t);
                m.setEnabled(false);
            }
        }
    }
}
