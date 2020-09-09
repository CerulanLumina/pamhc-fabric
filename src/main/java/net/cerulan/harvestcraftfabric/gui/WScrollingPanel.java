package net.cerulan.harvestcraftfabric.gui;

import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;

public class WScrollingPanel extends WScrollPanel {
    /**
     * Creates a vertically scrolling panel.
     *
     * @param widget the viewed widget
     */
    public WScrollingPanel(WWidget widget) {
        super(widget);
    }

    @Override
    public void onMouseScroll(int x, int y, double amount) {
        WScrollBar bar = this.verticalScrollBar;
        bar.setValue(bar.getValue() + (-(int)amount) * 10);
    }
}
