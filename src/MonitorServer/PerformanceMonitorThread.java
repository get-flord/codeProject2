package MonitorServer;

import mpi.Comm;
import mpi.MPIException;

import java.io.IOException;
import java.io.RandomAccessFile;

public class PerformanceMonitorThread extends Thread {
    private double memTotal;
    private double memAvailable;

    Comm comm;
    MonitorServer server;

    PerformanceMonitorThread(MonitorServer server, Comm comm) {
        this.server = server;
        this.comm = comm;
    }

    public void run() {
        while (true) {
            try {
                RandomAccessFile in = new RandomAccessFile("/proc/meminfo", "r");
                String line;
                while ((line = in.readLine()) != null) {
                    line = line.trim().replaceAll(" +", " ");
                    String data[];
                    if (line.matches("^MemTotal.*")) {
                        data = line.split(" ");
                        this.memTotal = Double.valueOf(data[1]);
                    } else if (line.matches("^MemAvailable.*")) {
                        data = line.split(" ");
                        this.memAvailable = Double.valueOf(data[1]);
                    }
                }
                this.server.processPercentageMemoryUsed.put(this.comm.getRank(), (memTotal - memAvailable) / (memTotal));
                Thread.sleep(5000);
                in.seek(0);
            } catch (IOException e) {
                //
            } catch (MPIException e) {
                //
            }
            catch (InterruptedException e) {
                //
            }
        }
    }
}

