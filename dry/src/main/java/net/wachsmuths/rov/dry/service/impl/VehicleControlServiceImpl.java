/**
 * 
 */
package net.wachsmuths.rov.dry.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.VehicleCapabilities;
import net.wachsmuths.rov.dry.discovery.VehicleDiscoveryEvent;
import net.wachsmuths.rov.dry.service.VehicleControlService;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
@Slf4j
public class VehicleControlServiceImpl implements VehicleControlService {
  private Map<UUID, ControllHandler> handlers = new HashMap<>(2);
  
  /* (non-Javadoc)
   * @see net.wachsmuths.rov.dry.service.VehicleControlService#activeConnections()
   */
  @Override
  public boolean activeConnections() {
    return handlers.size() > 0;
  }

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.dry.service.VehicleControlService#sendCommand(int, net.wachsmuths.rov.common.Command)
   */
  @Override
  public Response sendCommand(int vehicleId, Command command) {
    ControllHandler handler = handlers.get(vehicleId);
    
    if (handler == null) {
      throw new IllegalArgumentException("No vehicle with ID " + vehicleId + " is attatched to the controller.");
    }
    
    try {
      return handler.sendCommand(command);
    } catch (ClassNotFoundException | IOException e) {
      throw new RuntimeException("Error sending vehicle command.", e);
    }
  }

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.dry.service.VehicleControlService#getAttatchedVehicles()
   */
  @Override
  public List<VehicleCapabilities> getAttatchedVehicles() {
    List<VehicleCapabilities> capabilities = new ArrayList<>(handlers.size());
    
    for (ControllHandler handler : handlers.values()) {
      capabilities.add(handler.getVehicleCapabilities());
    }
    
    return capabilities;
  }
  
  @EventListener
  public void handleVehicleDiscovery(VehicleDiscoveryEvent event) {
    log.info("Handling vehicle attatchment.");
    try {
      ControllHandler handler = new ControllHandler(event.getVehicleAddress());
      
      handlers.put(handler.getId(), handler);
    } catch (ClassNotFoundException | IOException e) {
      log.error("Error opening communications to vehicle", e);
    }
  }
}
