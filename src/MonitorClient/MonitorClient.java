package MonitorClient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * MonitorClient is our client class. It sends a request to the server for data
 * every 5s and gives the data to the MonitorClientGUI to display to the user.
 */
public class MonitorClient {
    public static final int DEFAULT_PORT = 50000;
    public static final int DEFAULT_PACKET_SIZE = 1024;
    public DatagramSocket socket;
    public InetAddress serverAddress;
    public int serverPort;

    private MonitorClientGUI clientGUI;

    /**
     * Instantiates a new MonitorClient with args[]. This binds a DatagramSocket
     * to a given address and port (from args[]), and gives us an InetAddress to
     * address the server.
     * @param args - Formatted "<address>:<port>"
     * @throws IOException
     */
    public MonitorClient(String args[]) throws IOException {
        // Extract address and port from args[] in main.
        if (args.length != 1) {
            throw new IllegalArgumentException();
        }
        else {
            String address = args[0];
            int splitIndex = args[0].indexOf(58);
            if (splitIndex != -1) {
                address = args[0].substring(0, splitIndex);
                this.serverPort = Integer.parseInt(args[0].substring(splitIndex + 1, args[0].length()));
            }
            else {
                this.serverPort = DEFAULT_PORT;
            }
            this.serverAddress = InetAddress.getByName(address);
            InetSocketAddress socketAddress = new InetSocketAddress(this.serverAddress, this.serverPort);
            this.socket = new DatagramSocket();
            this.socket.bind(socketAddress);
            System.out.println("Socket bound to " + socketAddress + "." );

            this.clientGUI = new MonitorClientGUI();
        }
    }

    /**
     * Sends requests for data every 5s. Receives data, parses it, and passes it to MonitorClientGUI.
     * @throws IOException
     * @throws CloneNotSupportedException
     */
    public void loopForever() throws IOException, CloneNotSupportedException {
        do {
            LocalTime now = LocalTime.now();
            double x = (double) now.getHour() * 3600 + now.getMinute() * 60 + now.getSecond();
            String request = "";
            this.send(request);
            String response;
            response = this.receive();

            // Parse response into data = {hostname, data} then pass to GUI
            // response should be formated "hostname:data"
            String[] data = response.split(":");
            this.clientGUI.addDataPoint(data[0], x, Double.valueOf(data[1]));
            this.clientGUI.updateChart();

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                //
            }
        } while(true);
    }

    /**
     * Takes a String (message) and converts it to a byte array, then sends the
     * byte array to the server.
     * @param message - message to be sent to the server.
     */
    public void send(String message) {
        byte[] data = new byte[DEFAULT_PACKET_SIZE];
        DatagramPacket packet;
        byte[] temp = message.getBytes();

        System.arraycopy(temp, 0, data, 0, temp.length);
        try {
            packet = new DatagramPacket(data, DEFAULT_PACKET_SIZE);
            this.socket.send(packet);
            // this.socket.close();
            boolean isConnected = this.socket.isConnected();
            System.out.println("Socket still connected(send): " + isConnected);
        } catch (IOException e) {
            //
        }

    }

    public String receive() {
        String message = "";
        try {
            DatagramPacket packet = new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
            this.socket.receive(packet);
            byte[] data = packet.getData();
            message = new String(data);
            message = message.substring(0, packet.getLength());
            //this.socket.close();
            boolean temp = this.socket.isConnected();
            System.out.println("Socket still connected(recv): " + temp);
        } catch (IOException e) {
            //
        }
        return message;
    }

    public static void main(String[] args) {
        try {
            MonitorClient client = new MonitorClient(args);
            client.loopForever();
        }
        catch (Exception e) {
            //
        }
    }
}
