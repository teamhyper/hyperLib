package org.usfirst.frc.team69.util.port;

/**
 * An exception thrown when you attempt to assign two pieces of hardware to the
 * same port in the robot map.
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

    /**
     * Construct a new {@link DuplicatePortException} with the given parameters
     * 
     * @param port The port number which was duplicated
     * @param type The type of port which was duplicated
     * @param first The name of the first use of this port
     * @param second The name of the second use of this port
     */
    public DuplicatePortException(int port, Port.Type type, String first, String second) {
        this.port = port;
        this.type = type;
        this.first = first;
        this.second = second;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessage() {
        return String.format("%s and %s are both assigned to %s #%d\n",
                first, second, type, port);
    }
}
