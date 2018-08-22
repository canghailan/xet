package cc.whohow.xet.engine.image;

import cc.whohow.xet.engine.awt.AWTXetContext;
import cc.whohow.xet.layout.LayoutEngine;
import cc.whohow.xet.render.RenderEngine;

import java.awt.*;

public class ImageXetContext extends AWTXetContext {
    protected LayoutEngine<ImageXetContext> layoutEngine;
    protected RenderEngine<ImageXetContext> renderEngine;

    public ImageXetContext(Graphics2D graphics) {
        super(graphics);
    }

    public void setLayoutEngine(LayoutEngine<ImageXetContext> layoutEngine) {
        this.layoutEngine = layoutEngine;
    }

    public void setRenderEngine(RenderEngine<ImageXetContext> renderEngine) {
        this.renderEngine = renderEngine;
    }

    @Override
    public void layout() {
        layoutEngine.layout(this, getRenderTree());
    }

    @Override
    public void render() {
        renderEngine.render(this, getRenderTree());
    }
}
