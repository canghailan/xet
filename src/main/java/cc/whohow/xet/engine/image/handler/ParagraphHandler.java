package cc.whohow.xet.engine.image.handler;

import cc.whohow.xet.box.CharactersBox;
import cc.whohow.xet.box.ParagraphBox;
import cc.whohow.xet.engine.image.AWTParagraphLayoutEngine;
import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.function.BiConsumer;

public class ParagraphHandler implements BiConsumer<ImageXetContext, Element> {
    @Override
    public void accept(ImageXetContext context, Element element) {
        ObjectNode style = context.getComputedStyle(element);
        ParagraphBox paragraphBox = new ParagraphBox();
        paragraphBox.setText(element.getTextContent());
        paragraphBox.setX(context.getX(style));
        paragraphBox.setY(context.getY(style));
        paragraphBox.setWidth(context.getWidth(style));
        paragraphBox.setHeight(context.getHeight(style));
        paragraphBox.setColor(style.path("color").asText());
        paragraphBox.setFontFamily(style.path("font-family").asText());
        paragraphBox.setFontSize(style.path("font-size").asInt(-1));
        paragraphBox.setTextAlign(style.path("text-align").textValue());
        paragraphBox.setLineHeight(style.path("line-height").asInt(-1));

        Graphics g = context.getGraphics();
        g.setFont(context.getFont(paragraphBox.getFontFamily(), paragraphBox.getFontSize()));
        g.setColor(context.getColor(paragraphBox.getColor()));

        AWTParagraphLayoutEngine layoutEngine = new AWTParagraphLayoutEngine();
        layoutEngine.setFontMetrics(g.getFontMetrics());
        layoutEngine.layout(paragraphBox);

        for (CharactersBox charactersBox : paragraphBox.getCharactersBoxes()) {
            g.drawString(charactersBox.getText(), charactersBox.getX(), charactersBox.getY());
        }
    }
}
