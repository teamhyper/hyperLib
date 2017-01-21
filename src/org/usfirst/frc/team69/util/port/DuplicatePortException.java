package org.usfirst.frc.team69.util.port;

/**
 * An exception thrown when you attempt to assign two pieces of hardware to the
 * same port in {@link RobotMap}.
 * 
 * @author James Hagborg
 *
 */
@SuppressWarnings("serial")
public class DuplicatePortException extends Exception {

    private int port;
    private Port.Type type;
    private String first;
    private String second;

    public int getNumber() { return port; }
    public Port.Type getType() { return type; }
    public String getFirst() { return first; }
    public String getSecond() { return second; }

    public DuplicatePortException(int port, Port.Type type, String first, String second) {
        this.port = port;
        this.type = type;
        this.first = first;
        this.second = second;
    }

    @Override
    public String getMessage() {
        return String.format("%s and %s are both assigned to %s #%d\n",
                first, second, type, port);
    }
}
