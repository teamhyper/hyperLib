package org.hyperonline.hyperlib.port;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.EnumMap;

/**
 * The {@link PortMapper} class generates a wiring diagram from the robot map. class.
 *
 * @author James Hagborg
 */
public class PortMapper {

  private EnumMap<Port.Type, String[]> portNames = new EnumMap<>(Port.Type.class);
  private Class<?> baseClass;

  /**
   * Generate a wiring diagram from the given class.
   *
   * @param c The RobotMap class containing the entries.
   * @throws DuplicatePortException if there are duplicate ports
   * @throws InvalidPortException if there are invalid ports
   * @throws IOException if there was an IOException from reading or writing an image
   */
  public void mapPorts(Class<?> c)
      throws DuplicatePortException, InvalidPortException, IOException {
    initNames();
    baseClass = c;
    findPorts(c);
    drawMap();
  }

  private void initNames() {
    portNames.put(Port.Type.PWM, new String[10]);
    portNames.put(Port.Type.DIO, new String[10]);
    portNames.put(Port.Type.RELAY, new String[4]);
    portNames.put(Port.Type.ANALOG, new String[4]);
    portNames.put(Port.Type.PCM, new String[8]);
  }

  private void addPort(int number, Port.Type type, String name)
      throws DuplicatePortException, InvalidPortException {
    String[] names = portNames.get(type);
    // for now?
    if (names == null) {
      return;
    }

    if (number < 0 || number >= names.length) {
      throw new InvalidPortException(number, type, name);
    }

    String oldName = names[number];
    if (oldName != null) {
      throw new DuplicatePortException(number, type, oldName, name);
    }

    names[number] = name;
  }

  private void findPorts(Class<?> c) throws DuplicatePortException, InvalidPortException {
    for (Field f : c.getFields()) {
      try {
        Port p = f.getAnnotation(Port.class);
        if (p == null) {
          continue;
        }

        int i = f.getInt(null);

        String fullName = c.getCanonicalName() + "." + f.getName();
        String shortName = fullName.substring(baseClass.getCanonicalName().length() + 1);

        addPort(i, p.type(), shortName);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        // Do nothing, this just means it's not an int or it's not
        // public
      }
    }

    for (Class<?> sub : c.getClasses()) {
      findPorts(sub);
    }
  }

  private void drawMap() throws IOException {
    URL wiring = getClass().getResource("wiring.png");
    if (wiring == null) {
      throw new IOException("Can't find wiring.png!  Check your build properties");
    }

    BufferedImage img = ImageIO.read(wiring);
    Graphics g = img.createGraphics();
    g.setColor(Color.BLACK);

    drawPWM(g);
    drawDIO(g);
    drawRelay(g);
    drawAnalog(g);
    drawPCM(g);

    new File("diagrams").mkdirs();
    ImageIO.write(img, "png", new File("diagrams/wiring.png"));
  }

  private void drawPWM(Graphics g) {
    int p = 218;
    int i = 0;
    for (String s : portNames.get(Port.Type.PWM)) {
      if (s != null) {
        g.drawLine(950, p, 990, p);
        g.drawString(s, 995, p + 4);
        g.drawString("" + i, 950, p - 1);
      }
      p += 25;
      i++;
    }
  }

  private void drawDIO(Graphics g) {
    int p = 222;
    int i = 0;
    for (String s : portNames.get(Port.Type.DIO)) {
      if (s != null) {
        int w = g.getFontMetrics().stringWidth(s);

        g.drawLine(400, p, 440, p);
        g.drawString(s, 395 - w, p + 4);
        g.drawString("" + i, 435, p - 1);
      }
      p += 25;
      i++;
    }
  }

  private void drawRelay(Graphics g) {
    int x = 604;
    int y = 550;
    int i = 0;
    for (String s : portNames.get(Port.Type.RELAY)) {
      if (s != null) {
        int w = g.getFontMetrics().stringWidth(s);

        g.drawLine(x, 520, x, y);
        g.drawLine(550, y, x, y);
        g.drawString(s, 545 - w, y + 4);
        g.drawString("" + i, x - 8, 530);
      }
      x += 25;
      y += 25;
      i++;
    }
  }

  private void drawAnalog(Graphics g) {
    int x = 715;
    int y = 625;
    int i = 0;
    for (String s : portNames.get(Port.Type.ANALOG)) {
      if (s != null) {
        g.drawLine(x, 520, x, y);
        g.drawLine(840, y, x, y);
        g.drawString(s, 845, y + 4);
        g.drawString("" + i, x - 8, 530);
      }
      x += 25;
      y -= 25;
      i++;
    }
  }

  private void drawPCM(Graphics g) {
    for (int i = 0, y = 810; i < 4; i++, y += 33) {
      String s = portNames.get(Port.Type.PCM)[i];
      if (s != null) {
        int w = g.getFontMetrics().stringWidth(s);

        g.drawLine(600, y - 8, 560, y - 8);
        g.drawLine(600, y + 8, 560, y + 8);
        g.drawLine(560, y - 8, 560, y + 8);
        g.drawLine(520, y, 560, y);
        g.drawString(s, 515 - w, y + 4);
        g.drawString("" + i, 550, y - 2);
      }
    }

    for (int i = 4, y = 910; i < 8; i++, y -= 33) {
      String s = portNames.get(Port.Type.PCM)[i];
      if (s != null) {
        g.drawLine(810, y - 8, 850, y - 8);
        g.drawLine(810, y + 8, 850, y + 8);
        g.drawLine(850, y - 8, 850, y + 8);
        g.drawLine(850, y, 890, y);
        g.drawString(s, 895, y + 4);
        g.drawString("" + i, 852, y - 2);
      }
    }
  }
}
