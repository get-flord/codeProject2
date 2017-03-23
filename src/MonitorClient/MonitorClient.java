package MonitorClient;

/**
 * Created by cFournierg on 3/22/17.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

/**
 * MonitorClient handles the UDP connection to MonitorServer. At fixed periods
 * the MonitorClient should poll MonitorServer for performance metrics to display
 * through MonitorClientGUI.
 *
 * Instantiating MonitorClient should also instantiate a new MonitorClientGUI.
 * Data collection will be looped to poll data from the server,
 * and deliver that information to the user through the MonitorClientGUI.
 */
public class MonitorClient {
    public static final int DEFAULT_PORT = 50000;
    public DatagramSocket socket;
    public DatagramPacket packet;

    private MonitorClientGUI clientGUI;

    public MonitorClient(String args[]) throws IOException {
        // Extract address and port from args[] in main.
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        else {
            int port;
            String address = args[0];
            int splitIndex = args[0].indexOf(58);
            if (splitIndex != -1) {
                address = args[0].substring(0, splitIndex);
                port = Integer.parseInt(args[0].substring(splitIndex + 1, args[0].length()));
            }
            else {
                port = 50000;
            }
            InetAddress inetAddress = InetAddress.getByName(address);
            InetSocketAddress socketAddress = new InetSocketAddress(inetAddress, port);
            this.socket = new DatagramSocket();
            this.socket.bind(socketAddress);
            System.out.println("Socket bound to " + socketAddress + "." );
        }
    }

    public void loopForever() throws IOException {
        while(true) {

        }
    }
}
