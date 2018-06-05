package net.simon987.cubotplugin;

import net.simon987.server.assembly.Status;
import net.simon987.server.game.objects.ControllableUnit;
import org.bson.Document;

public class CubotHologram extends CubotHardwareModule {


    /**
     * Hardware ID (Should be unique)
     */
    static final char HWID = 0x0009;

    public static final int DEFAULT_ADDRESS = 9;

    private static final int HOLO_CLEAR = 0;
    private static final int HOLO_DISPLAY_HEX = 1;
    private static final int HOLO_DISPLAY_STRING = 2;
    private static final int HOLO_DISPLAY_DEC = 3;
    private static final int HOLO_DISPLAY_COLOR = 4;

    private static final int STR_MAX_LEN = 8;

    public CubotHologram(Cubot cubot) {
        super(cubot);
    }

    public CubotHologram(Document document, ControllableUnit cubot) {
        super(document, cubot);
    }

    @Override
    public void handleInterrupt(Status status) {

        char a = getCpu().getRegisterSet().getRegister("A").getValue();

        if (a == HOLO_CLEAR) {
            cubot.setHologramMode(Cubot.HologramMode.CLEARED);
        } else if (a == HOLO_DISPLAY_HEX) {
            char b = getCpu().getRegisterSet().getRegister("B").getValue();
            cubot.setHologram(b);
            cubot.setHologramMode(Cubot.HologramMode.HEX);
        } else if (a == HOLO_DISPLAY_STRING) {
            char x = getCpu().getRegisterSet().getRegister("X").getValue();
            //Display zero-terminated string starting at X (max 8 chars)

            StringBuilder holoString = new StringBuilder();

            for (int i = 0; i < STR_MAX_LEN; i++) {

                char nextChar = (char) getCpu().getMemory().get(x + i);

                if (nextChar != 0) {
                    holoString.append((char) getCpu().getMemory().get(x + i));
                } else {
                    break;
                }
            }

            cubot.setHologramString(holoString.toString());
            cubot.setHologramMode(Cubot.HologramMode.STRING);
        } else if (a == HOLO_DISPLAY_DEC) {
            //Display decimal number
            char b = getCpu().getRegisterSet().getRegister("B").getValue();
            cubot.setHologram(b);
            cubot.setHologramMode(Cubot.HologramMode.DEC);

        } else if (a == HOLO_DISPLAY_COLOR) {

            if (cubot.spendEnergy(4)) {
                int b = getCpu().getRegisterSet().getRegister("B").getValue();
                int c = getCpu().getRegisterSet().getRegister("C").getValue();

                cubot.setHologramColor((c | (b << 16))); //B:C
            }
        }

    }

    @Override
    public char getId() {
        return HWID;
    }

}
