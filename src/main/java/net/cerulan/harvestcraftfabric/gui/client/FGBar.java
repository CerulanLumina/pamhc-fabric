package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WBar;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;

public class FGBar extends WBar {

    private final Vec2f minUV, maxUV;

    public FGBar(Identifier bg, Identifier bar, int field, int maxfield) {
        this(bg, bar, field, maxfield, Direction.RIGHT, new Vec2f(0f, 0f), new Vec2f(1f, 1f));
    }

    public FGBar(Identifier bg, Identifier bar, int field, int maxfield, WBar.Direction direction) {
        this(bg, bar, field, maxfield, direction, new Vec2f(0f, 0f), new Vec2f(1f, 1f));
    }

    public FGBar(Identifier bg, Identifier bar, int field, int maxfield, WBar.Direction direction, Vec2f minUV, Vec2f maxUV) {
        super(bg, bar, field, maxfield, direction);
        this.minUV = minUV;
        this.maxUV = maxUV;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        float percent = properties.get(field) / (float) properties.get(max);
        if (percent < 0) percent = 0f;
        if (percent > 1) percent = 1f;

        int barMax = getWidth();
        if (direction == Direction.DOWN || direction == Direction.UP) barMax = getHeight();
        percent = ((int) (percent * barMax)) / (float) barMax; //Quantize to bar size

        int barSize = (int) (barMax * percent);
        if (barSize <= 0) return;

        float minU = minUV.x;
        float minV = minUV.y;
        float maxU = maxUV.x;
        float maxV = maxUV.y;

        switch(direction) { //anonymous blocks in this switch statement are to sandbox variables
            case UP: {
                int top = y + getHeight();
                top -= barSize;
                if (bar!=null) {
                    ScreenDrawing.texturedRect(x, top, getWidth(), barSize, bar, minU, (1 - percent) * (maxV - minV) + minV, maxU, 1, 0xFFFFFFFF);
                } else {
                    ScreenDrawing.coloredRect(x, top, getWidth(), barSize,  ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
                break;
            }
            case RIGHT: {
                if (bar!=null) {
                    ScreenDrawing.texturedRect(x, y, barSize, getHeight(), bar, minU, minV, percent * (maxU - minU) + minU, maxV, 0xFFFFFFFF);
                } else {
                    ScreenDrawing.coloredRect(x, y, barSize, getHeight(), ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
                break;
            }
            case DOWN: {
                if (bar!=null) {
                    ScreenDrawing.texturedRect(x, y, getWidth(), barSize, bar, minU, minV, maxU, percent * (maxV - minV) + minV, 0xFFFFFFFF);
                } else {
                    ScreenDrawing.coloredRect(x, y, getWidth(), barSize, ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
                break;
            }
            case LEFT: {
                int left = x + getWidth();
                left -= barSize;
                if (bar!=null) {
                    ScreenDrawing.texturedRect(left, y, barSize, getHeight(), bar, (1 - percent) * (maxU - minU) + minU, minV, maxU, maxV, 0xFFFFFFFF);
                } else {
                    ScreenDrawing.coloredRect(left, y, barSize, getHeight(), ScreenDrawing.colorAtOpacity(0xFFFFFF, 0.5f));
                }
                break;
            }
        }
    }
}
