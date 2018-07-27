package cc.whohow.xet;

import cc.whohow.xet.engine.image.ImageXetEngine;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class TestXet {
    @Test
    public void testEngine() throws Exception {
        ImageXetEngine engine = new ImageXetEngine();
        long t1 = System.currentTimeMillis();
        BufferedImage image1 = engine.process(new File("test.xml").toURI().toURL());
        long t2 = System.currentTimeMillis();
        BufferedImage image2 = engine.process(new File("test.xml").toURI().toURL());
        long t3 = System.currentTimeMillis();
        ImageIO.write(image1, "jpg", new File("temp-output1.jpg"));
        ImageIO.write(image2, "jpg", new File("temp-output2.jpg"));
        System.out.println(t2 - t1);
        System.out.println(t3 - t2);
    }

    public void testContext() {
        
    }
}
