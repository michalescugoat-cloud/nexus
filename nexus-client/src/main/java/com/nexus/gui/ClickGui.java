package com.nexus.gui;

import com.nexus.NexusClient;
import com.nexus.module.Category;
import com.nexus.module.Module;
import com.nexus.module.Setting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/** Proste ClickGUI: panele kategorii. LPM = wlacz/wylacz modul, PPM = rozwin opcje, klik na pasek = ustaw wartosc. */
public class ClickGui extends Screen {
    private static final int PW = 120, HEADER_H = 16, ROW_H = 14, SET_H = 13, GAP = 10;
    private static final int BG = 0xFF181825, BG_HOVER = 0xFF262637, SET_BG = 0xFF11111B;
    private static final int HEADER = 0xFF2B2B45, ACCENT = 0xFF7C5CFF, ACCENT_DIM = 0xFF4A3A99;
    private static final int TEXT = 0xFFFFFFFF, TEXT_DIM = 0xFFAAAAAA;

    private static final Set<Module> EXPANDED = new HashSet<>();

    private interface Action { void run(int button, double mx, Hit hit); }

    private record Hit(int x, int y, int w, int h, Action action) {
        boolean contains(double mx, double my) { return mx >= x && mx < x + w && my >= y && my < y + h; }
    }

    private final List<Hit> hits = new ArrayList<>();

    public ClickGui() { super(Component.literal("Nexus")); }

    @Override
    public boolean isPauseScreen() { return false; }

    @Override
    public void render(GuiGraphics g, int mx, int my, float delta) {
        hits.clear();
        g.fill(0, 0, width, height, 0x90000000);

        int x = GAP;
        for (Category cat : Category.values()) {
            List<Module> mods = NexusClient.modules.all().stream().filter(m -> m.category == cat).toList();
            if (mods.isEmpty()) continue;

            int y = GAP;
            g.fill(x, y, x + PW, y + HEADER_H, HEADER);
            g.fill(x, y + HEADER_H - 1, x + PW, y + HEADER_H, ACCENT);
            g.drawString(font, cat.name().charAt(0) + cat.name().substring(1).toLowerCase(), x + 5, y + 4, TEXT);
            y += HEADER_H;

            for (Module m : mods) {
                boolean hover = mx >= x && mx < x + PW && my >= y && my < y + ROW_H;
                int bg = m.isEnabled() ? (hover ? ACCENT : ACCENT_DIM) : (hover ? BG_HOVER : BG);
                g.fill(x, y, x + PW, y + ROW_H, bg);
                g.drawString(font, m.name, x + 5, y + 3, m.isEnabled() ? TEXT : TEXT_DIM);
                if (!m.settings.isEmpty()) {
                    g.drawString(font, EXPANDED.contains(m) ? "-" : "+", x + PW - 10, y + 3, TEXT_DIM);
                }
                final Module mod = m;
                hits.add(new Hit(x, y, PW, ROW_H, (button, cx, h) -> {
                    if (button == 0) mod.toggle();
                    else if (button == 1 && !mod.settings.isEmpty()) {
                        if (!EXPANDED.remove(mod)) EXPANDED.add(mod);
                    }
                }));
                y += ROW_H;

                if (EXPANDED.contains(m)) {
                    for (Setting s : m.settings) {
                        g.fill(x, y, x + PW, y + SET_H, SET_BG);
                        String label;
                        if (s.isBool()) {
                            if (s.bool()) g.fill(x, y, x + PW, y + SET_H, ACCENT_DIM);
                            label = s.name + ": " + (s.bool() ? "ON" : "OFF");
                        } else {
                            double ratio = (s.get() - s.min) / (s.max - s.min);
                            g.fill(x, y, x + (int) (PW * ratio), y + SET_H, ACCENT_DIM);
                            label = s.name + ": " + String.format(Locale.ROOT, "%.2f", s.get());
                        }
                        g.drawString(font, label, x + 8, y + 3, TEXT);
                        final Setting set = s;
                        hits.add(new Hit(x, y, PW, SET_H, (button, cx, h) -> {
                            if (set.isBool()) {
                                set.set(set.bool() ? 0 : 1);
                            } else {
                                double r = Math.max(0, Math.min(1, (cx - h.x()) / h.w()));
                                double v = set.min + r * (set.max - set.min);
                                set.set(Math.round(v * 20.0) / 20.0);
                            }
                        }));
                        y += SET_H;
                    }
                }
            }
            x += PW + GAP;
        }

        g.drawString(font, "Nexus Client  |  LPM: wlacz/wylacz  |  PPM: opcje  |  klik na pasek: ustaw wartosc  |  ESC: zamknij",
            GAP, height - 14, TEXT_DIM);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        for (int i = hits.size() - 1; i >= 0; i--) {
            Hit h = hits.get(i);
            if (h.contains(event.x(), event.y())) {
                h.action().run(event.button(), event.x(), h);
                return true;
            }
        }
        return super.mouseClicked(event, doubleClick);
    }
}
