package MonitorServer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Set;

public class MonitorServerThread extends Thread {

    public static final int DEFAULT_PORT = 50000;
    public static final int DEFAULT_PACKET_SIZE = 1024;
    MonitorServer server;
    DatagramSocket socket;
    InetAddress clientAddress;
    DatagramPacket packet;
    int port;

    public MonitorServerThread(DatagramPacket packet, DatagramSocket socket, MonitorServer server) {
        this.server = server;
        this.socket = socket;
        this.packet = packet;
        this.clientAddress = this.packet.getAddress();
        this.port = this.packet.getPort();


    }

    public void run() {
        byte[] response = new byte[DEFAULT_PACKET_SIZE];
        String hostname;
        Double data;
        String message = new String(this.packet.getData());
        if (message.equals("meminfo")) {
            Set<Integer> processes = this.server.processPercentageMemoryUsed.keySet();
            for (int i : processes) {
                hostname = this.server.processHostname.get(i);
                data = this.server.processPercentageMemoryUsed.get(i);
                String temp = hostname + ":" + data;
                byte[] tempBytes = temp.getBytes();
                System.arraycopy(tempBytes, 0, response, 0, tempBytes.length);
                DatagramPacket packet = new DatagramPacket(response, DEFAULT_PACKET_SIZE);
                try {
                    this.socket.connect(clientAddress, port);
                    this.socket.send(packet);
                }
                catch (IOException e) {
                    //
                }
            }
        }
        else {
            throw new IllegalArgumentException("Client must ask for 'meminfo'");
        }
    }
}