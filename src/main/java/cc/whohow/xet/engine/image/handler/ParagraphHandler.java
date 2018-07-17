package cc.whohow.xet.engine.image.handler;

import cc.whohow.xet.box.CharactersBox;
import cc.whohow.xet.box.TextBox;
import cc.whohow.xet.engine.image.AWTChineseTextLayoutEngine;
import cc.whohow.xet.engine.image.ImageXetContext;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.w3c.dom.Element;

import java.awt.*;
import java.util.function.BiConsumer;

public class ParagraphHandler implements BiConsumer<ImageXetContext, Element> {
    @Override
    public void accept(ImageXetContext context, Element element) {
        ObjectNode style = context.getComputedStyle(element);
        TextBox textBox = new TextBox();
        textBox.setText(element.getTextContent());
        textBox.setX(context.getX(style));
        textBox.setY(context.getY(style));
        textBox.setWidth(context.getWidth(style));
        textBox.setHeight(context.getHeight(style));
        textBox.setColor(style.path("color").asText());
        textBox.setFontFamily(style.path("font-family").asText());
        textBox.setFontSize(style.path("font-size").asInt(-1));
        textBox.setTextAlign(style.path("text-align").textValue());
        textBox.setLineHeight(style.path("line-height").asInt(-1));

        Graphics g = context.getGraphics();
        g.setFont(context.getFont(textBox.getFontFamily(), textBox.getFontSize()));
        g.setColor(context.getColor(textBox.getColor()));

        AWTChineseTextLayoutEngine textLayoutEngine = new AWTChineseTextLayoutEngine();
        textLayoutEngine.setFontMetrics(g.getFontMetrics());
        textLayoutEngine.layout(textBox);

        for (CharactersBox charactersBox : textBox.getCharactersBoxes()) {
            g.drawString(charactersBox.getText(), charactersBox.getX(), charactersBox.getY());
        }
    }
}
