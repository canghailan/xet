package cc.whohow.xet.layout;

import com.fasterxml.jackson.databind.JsonNode;

public interface ImageLayoutEngine<CONTEXT, IMAGE> extends LayoutEngine<CONTEXT> {
    IMAGE getImage(CONTEXT context, JsonNode node);

    int getWidth(IMAGE image);

    int getHeight(IMAGE image);
}
