/**
 * 
 */
package net.wachsmuths.rov.dry.service;

import java.util.List;
import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.VehicleCapabilities;

/**
 * @author Brian Wachsmuth
 *
 */
public interface VehicleControlService {
  boolean activeConnections();
  
  Response sendCommand(int vehicleId, Command command);
  
  List<VehicleCapabilities> getAttatchedVehicles();
}
