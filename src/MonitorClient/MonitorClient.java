package MonitorClient;

/**
 * Created by cFournierg on 3/22/17.
 */

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * MonitorClient
 */
public class MonitorClient {
    public static final int DEFAULT_PORT = 50000;
    public static final int DEFAULT_SIZE = 1024;
    public DatagramSocket socket;
    public InetAddress serverAddress;
    public int serverPort;

    private MonitorClientGUI clientGUI;

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

            // To be changed
            this.clientGUI = new MonitorClientGUI();
        }
    }

    public void loopForever() throws IOException, CloneNotSupportedException {
        do {
            // Possibly change this to a Scheduler/Thread implementation
            String request = "";
            this.send(request);
            String response;
            response = this.receive();

            // Parse response into data = {hostname, x, y} then pass to GUI
            String[] data = response.split(":");
            this.clientGUI.addDataPoint(data[0], Double.valueOf(data[1]), Double.valueOf(data[2]));
            this.clientGUI.updateChart();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                //
            }
        } while(true);
    }

    public void send(String message) {
        byte[] data = new byte[DEFAULT_SIZE];
        DatagramPacket packet;
        byte[] temp = message.getBytes();
        System.arraycopy(temp, 0, data, 0, temp.length);
        try {
            packet = new DatagramPacket(data, DEFAULT_SIZE);
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
            DatagramPacket packet = new DatagramPacket(new byte[DEFAULT_SIZE], DEFAULT_SIZE);
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
