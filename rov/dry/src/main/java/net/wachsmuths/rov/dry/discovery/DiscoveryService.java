/**
 * 
 */
package net.wachsmuths.rov.dry.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.RovConstants;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
@Slf4j
public class DiscoveryService {
  private ExecutorService executorService;
  private DiscoveryListener listener;
  private ApplicationEventPublisher eventPublisher;

  public DiscoveryService(ExecutorService executorService, ApplicationEventPublisher eventPublisher) {
    this.executorService = executorService;
    this.eventPublisher = eventPublisher;
  }

  @EventListener
  public void handleEvent(ApplicationReadyEvent event) {
    log.debug("Starting Discovery Service");
    startService();
  }

  private void startService() {
    synchronized (this) {
      listener = new DiscoveryListener();
      executorService.execute(listener);
    }
  }
  
  private class DiscoveryListener implements Runnable {
    private static final long SLEEP_TIME = 1000;

    @Override
    public void run() {
      DatagramSocket socketIn = null;
      
      try {
        socketIn = new DatagramSocket(RovConstants.DISCOVERY_BCAST_PORT);

        while (true) {
          DatagramPacket data = new DatagramPacket(new byte[RovConstants.DISCOVERY_PACKET_SIZE], RovConstants.DISCOVERY_PACKET_SIZE);
          socketIn.receive(data);
          
          log.debug("Received beacon packet: " + new String(data.getData(), RovConstants.CHARSET));
          
          if (Arrays.equals(data.getData(), RovConstants.DISCOVERY_BYTES)) {
            //We found a controller!
            eventPublisher.publishEvent(new VehicleDiscoveryEvent(this, data.getAddress()));
            log.debug("Setting vehicle address to " + data.getAddress().toString());
          }

          try {
            Thread.sleep(SLEEP_TIME);
          } catch (InterruptedException e) {
            // DO Nothing and eat the exception
          }
        }
      } catch (IOException e) {
        log.error("Failed to read broadcast message", e);
      } finally {
        if (socketIn != null) {
          IOUtils.closeQuietly(socketIn);
        }
      }
    }
  }
}
