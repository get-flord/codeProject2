package MonitorServer;

import mpi.Comm;
import mpi.MPI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

// AHHH
public class MonitorServer {
    public static final int DEFAULT_PORT = 50000;
    public DatagramSocket serverSocket = new DatagramSocket();
    public InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT);

    Comm comm;
    String hostname;
    HashMap<Integer, String> processHostname = new HashMap<>();
    HashMap<Integer, Double> processPercentageMemoryUsed = new HashMap<>();

    private int DEFAULT_SIZE = 1024;

    public MonitorServer() throws Exception {
        this.serverSocket.bind(serverAddress);
        this.hostname = InetAddress.getLocalHost().getHostName();
        this.comm = MPI.COMM_WORLD;
    }

    public void loopForever() throws IOException {
        while(true) {

        }
    }

    public static void main(String[] args) {
        try {
            MonitorServer server = new MonitorServer();
            server.loopForever();
        }
        catch (Exception e) {
            //
        }
    }
}
