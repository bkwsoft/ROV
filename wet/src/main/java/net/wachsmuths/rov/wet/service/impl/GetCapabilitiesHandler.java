/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import org.springframework.stereotype.Service;
import net.wachsmuths.rov.common.GetCapabilities;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.VehicleCapabilities;
import net.wachsmuths.rov.wet.service.CommandHandler;
import net.wachsmuths.rov.wet.vehicle.AccessoryConfig;
import net.wachsmuths.rov.wet.vehicle.CapabilityFactory;
import net.wachsmuths.rov.wet.vehicle.VehicleConfiguration;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
public class GetCapabilitiesHandler implements CommandHandler<GetCapabilities> {
  private VehicleConfiguration vehicleConfiguration;
  private VehicleCapabilities capabilities = null;
  
  public GetCapabilitiesHandler(VehicleConfiguration vehicleConfiguration) {
    this.vehicleConfiguration = vehicleConfiguration;
  }
  
  private VehicleCapabilities getCapabilities() {
    //Lazy load and cache the vehicle capabilities
    if (capabilities == null) {
      capabilities = new VehicleCapabilities();
      
      capabilities.setId(vehicleConfiguration.getId());
      capabilities.setName(vehicleConfiguration.getName());
      
      for (AccessoryConfig config : vehicleConfiguration.getAllConfiguration()) {
        capabilities.getCapabilities().add(CapabilityFactory.getCapability(config));
      }
    }
    
    return capabilities;
  }
  
  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#execute(net.wachsmuths.rov.common.Command)
   */
  @Override
  public Response execute(GetCapabilities command) {
    return getCapabilities();
  }

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#getCommandType()
   */
  @Override
  public Class<GetCapabilities> getCommandType() {
    return GetCapabilities.class;
  }
}
