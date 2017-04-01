package MonitorServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class MonitorServerThread extends Thread {

    MonitorServer server;
    DatagramSocket socket;
    InetAddress serverAddress;
    int port;

    public MonitorServerThread(int port, InetAddress serverAddress, DatagramSocket socket, MonitorServer server) {
        this.server = server;
        this.port = port;
        this.socket = socket;
        this.serverAddress = serverAddress;

    }

    private final ScheduledExecutorService schedule = Executors.newScheduledThreadPool(1);

    public void run() {
        final Runnable sendData = new Runnable() {
            @Override
            public void run() {

            }
        };
        final ScheduledFuture<?> sendDataHandler = schedule.scheduleAtFixedRate(sendData, 0, 5, TimeUnit.SECONDS);
    }
}