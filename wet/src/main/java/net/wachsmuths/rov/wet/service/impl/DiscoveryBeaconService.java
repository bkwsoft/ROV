/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.commons.io.IOUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.RovConstants;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
@Slf4j
public class DiscoveryBeaconService {
  private static final long BEACON_INTERVAL = 10000; // Ten seconds
  private boolean sendBeacon = true;
  
  @Scheduled(fixedRate = BEACON_INTERVAL)
  public void sendDiscoveryBeacon() {
    if (!sendBeacon) {
      return;
    }
    
    log.debug("Sending Discovery Beacon.");
    DatagramSocket socketOut = null;

    try {
      socketOut = new DatagramSocket();
      DatagramPacket data =
          new DatagramPacket(RovConstants.DISCOVERY_BYTES, RovConstants.DISCOVERY_PACKET_SIZE,
              getBroadcastAddrs(), RovConstants.DISCOVERY_BCAST_PORT);
      socketOut.send(data);
    } catch (IOException e) {
      log.error("Error broadcasting Discovery Beacon!", e);
    } finally {
      if (socketOut != null) {
        IOUtils.closeQuietly(socketOut);
      }
    }

    log.debug("Finished Sending Discovery Beacon.");
  }

  @EventListener
  public void controllerConnected(ControllerConnectedEvent event) {
    sendBeacon = false;
  }
  
  @EventListener
  public void controllerDisconnec(ControllerDisconnectedEvent event) {
    sendBeacon = true;
  }

  public InetAddress getBroadcastAddrs() throws SocketException {
    Enumeration<NetworkInterface> nicList = NetworkInterface.getNetworkInterfaces();

    for (; nicList.hasMoreElements();) {
      NetworkInterface nic = nicList.nextElement();
      if (nic.isUp() && !nic.isLoopback()) {
        for (InterfaceAddress ia : nic.getInterfaceAddresses())
          if (ia.getBroadcast() != null) {
            return ia.getBroadcast();
          }
      }
    }

    return null;
  }
}
