package MonitorServer;

/**
 * Created by cFournierg on 3/22/17.
 */

/**
 * MonitorServer listens for MonitorClient's in order to establish a UDP connection.
 * This connection is used to send performance metrics gathered by the server
 * (and slave processes) to the MonitorClient.
 *
 * Instantiating a MonitorServer will also instantiate a PerformanceMonitor that
 * will collect performance metric data on the local system. Running a MonitorServer
 * will instantiate a MonitorServerThread that will listen for queries from
 * MonitorClients and send back performance metrics.
 */
public class MonitorServer {

}