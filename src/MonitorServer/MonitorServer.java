package MonitorServer;

import mpi.Comm;
import mpi.MPI;
import mpi.MPIException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.concurrent.ScheduledExecutorService;

public class MonitorServer {
    public static final int DEFAULT_PORT = 50000;
    public DatagramSocket serverSocket = new DatagramSocket();
    public InetSocketAddress serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), DEFAULT_PORT);

    Comm comm;
    String hostname;
    HashMap<Integer, String> processHostname = new HashMap<>();
    HashMap<Integer, Double> processPercentageMemoryUsed = new HashMap<>();

    private int DEFAULT_PACKET_SIZE = 1024;

    public MonitorServer() throws Exception {
        this.serverSocket.bind(serverAddress);
        this.hostname = InetAddress.getLocalHost().getHostName();
        this.comm = MPI.COMM_WORLD;

        // From in-class example
        int size = 20;
        int np = this.comm.getSize();
        int rank = this.comm.getRank();
        char data[] = new char[size * np];

        char hostnameChar[] = this.hostname.toCharArray();
        for (int i = 0; i < size; i++)
            data[i] = ' ';
        System.arraycopy(hostnameChar, 0, data, 0, Math.min(size, hostnameChar.length));

        this.comm.gather(data, size, MPI.CHAR, 0);

        if (rank == 0) {
            for (int i = 0; i < np; i++) {
                hostnameChar = new char[size];
                System.arraycopy(data, i * size, hostnameChar, 0, size);
                this.processHostname.put(i, new String(hostnameChar));
            }
        }
    }

    public void loopForever() throws IOException, MPIException {
        new Thread(new PerformanceMonitorThread(this, this.comm));
        if (comm.getRank() == 0) {
            DatagramPacket packet = new DatagramPacket(new byte[DEFAULT_PACKET_SIZE], DEFAULT_PACKET_SIZE);
            while (true) {
                this.serverSocket.receive(packet);
                new Thread(new MonitorServerThread(packet, this.serverSocket, this)).start();
            }
        }
    }

    public static void main(String[] args) {
        try {
            MPI.Init(args);
            MonitorServer server = new MonitorServer();
            server.loopForever();
            MPI.Finalize();
        } catch (Exception e) {
            //
        }
    }
}