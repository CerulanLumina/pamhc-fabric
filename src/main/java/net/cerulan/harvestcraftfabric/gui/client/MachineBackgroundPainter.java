package net.cerulan.harvestcraftfabric.gui.client;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.util.Identifier;

public class MachineBackgroundPainter implements BackgroundPainter {

    private int leftPadding, rightPadding, topPadding, bottomPadding;
    private final Identifier texture;

    public MachineBackgroundPainter(Identifier texture) {
        this.texture = texture;
    }

    public MachineBackgroundPainter setPadding(int leftPadding, int rightPadding, int topPadding, int bottomPadding) {
        this.leftPadding = leftPadding;
        this.rightPadding = rightPadding;
        this.topPadding = topPadding;
        this.bottomPadding = bottomPadding;
        return this;
    }

    @Override
    public void paintBackground(int left, int top, WWidget panel) {
        int width = panel.getWidth() + this.leftPadding + this.rightPadding;
        int height = panel.getHeight() + this.topPadding + this.bottomPadding;
        left -= this.leftPadding;
        top -= this.topPadding;

        ScreenDrawing.texturedRect(left, top, width, height, this.texture, 0.0F, 0.0F, 0.6875f, 0.6875f, -1);
    }
}
