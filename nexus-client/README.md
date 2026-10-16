# Nexus Client (Fabric, Minecraft 1.21.11)

Klient narzedziowy (utility) w stylu Meteor / Wurst / Catlean. Sterowanie komendami `/nx`.

## Budowanie
1. Zainstaluj JDK 21 i IntelliJ IDEA.
2. Otworz ten folder w IntelliJ (File > Open) i poczekaj na import Gradle.
3. W panelu Gradle: Tasks > build > build (albo `./gradlew build`, jesli masz wrapper).
4. Gotowy plik: `build/libs/nexus-client-1.0.0.jar` (NIE ten z `-sources`).
5. Wrzuc jar do `.minecraft/mods` razem z Fabric API. Wymagany Fabric Loader 0.18.1+.

## GUI
Nacisnij **Right Shift** w grze (klawisz zmienisz w Opcje > Sterowanie > Inne).
LPM na module = wlacz/wylacz, PPM = rozwin opcje, klik na pasek opcji = ustaw wartosc, ESC = zamknij.

## Komendy
- `/nx list` - lista modulow
- `/nx toggle <modul>` - wlacz/wylacz
- `/nx info <modul>` - opis i opcje
- `/nx set <modul> <opcja> <wartosc>` - np. `/nx set Fly speed 1.5`

## Moduly
Sprint, Fly, Speed, Spider, Bhop, Glide, KillAura (z rotacjami), Triggerbot, AimAssist, Fullbright, ESP
