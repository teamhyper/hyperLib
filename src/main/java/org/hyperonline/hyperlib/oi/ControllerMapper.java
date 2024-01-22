package org.hyperonline.hyperlib.oi;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * The {@link ControllerMapper} class generates diagrams from the operator interface.
 *
 * @author James Hagborg
 */
public class ControllerMapper {
  /**
   * Draws diagrams for a list of joysticks, and saves them to file.
   *
   * @param data A list of {@link ControllerData} objects describing the oi
   * @throws IOException If there is an error reading or writing the diagrams
   */
  public static void drawMap(List<ControllerData> data) throws IOException {
    for (ControllerData js : data) {
      drawSubMap(js);
    }
  }

  private static void drawSubMap(ControllerData joystick) throws IOException {
    BufferedImage img = null;

    switch (joystick.type()) {
      case LOGITECH_2_AXIS:
        img = draw2Axis(joystick);
        break;
      case LOGITECH_3_AXIS:
        img = draw3Axis(joystick);
        break;
      default:
        System.err.printf(
            "On joystick %s: type %s is not recognized, so no diagram",
            joystick.name(), joystick.type());
    }

    // img is null if new enum types are added
    if (img != null) {
      new File("diagrams").mkdirs();
      String filename = String.format("diagrams/joystick%d.png", joystick.port());
      ImageIO.write(img, "png", new File(filename));
    }
  }

  private static void drawInfo(Graphics g, ControllerData js) {
    String s = String.format("Joystick #%d: %s", js.port(), js.name());
    final int w = g.getFontMetrics().stringWidth(s);
    g.drawString(s, 50, 50);
    g.drawRect(45, 30, w + 10, 25);
  }

  private static BufferedImage draw2Axis(ControllerData js) throws IOException {
    BufferedImage img =
        ImageIO.read(ControllerMapper.class.getResource("sc_mapping_helper_2_axis.jpg"));
    Graphics g = img.createGraphics();
    g.setColor(Color.BLACK);

    g.setFont(new Font("Arial", Font.PLAIN, 16));
    drawInfo(g, js);

    int[] x = {300, 280, 297, 136, 449, 22, 10, 166, 357, 520, 520};
    int[] y = {44, 208, 125, 125, 125, 412, 510, 643, 643, 375, 454};
    int[] used = new int[x.length];

    for (ButtonData b : js.buttons()) {
      int size = b.port() == 1 ? 12 : 16;
      g.setFont(new Font("Arial", Font.PLAIN, size));

      int i = b.port() - 1;
      g.drawString(b.description(), x[i], y[i] + used[i] * size);
      used[i]++;
    }

    return img;
  }

  private static BufferedImage draw3Axis(ControllerData js) throws IOException {
    BufferedImage img = ImageIO.read(ControllerMapper.class.getResource("sc_mapping_helper.jpg"));
    Graphics g = img.createGraphics();
    g.setColor(Color.BLACK);

    g.setFont(new Font("Arial", Font.PLAIN, 16));
    drawInfo(g, js);

    int[] x = {634, 206, 450, 810, 419, 826, 28, 206, 28, 206, 28, 206};
    int[] y = {354, 436, 296, 296, 223, 223, 562, 562, 635, 635, 708, 708};
    int[] used = new int[x.length];

    for (ButtonData b : js.buttons()) {
      int size = b.port() == 1 ? 12 : 16;

      g.setFont(new Font("Arial", Font.PLAIN, size));

      int i = b.port() - 1;
      g.drawString(b.description(), x[i], y[i] + size);
      used[i]++;
    }

    return img;
  }
}
