/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import net.wachsmuths.rov.common.GetCapabilities;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.VehicleCapabilities;
import net.wachsmuths.rov.common.capabilities.Capability;
import net.wachsmuths.rov.wet.vehicle.AccessoryConfig;
import net.wachsmuths.rov.wet.vehicle.MotorConfig;
import net.wachsmuths.rov.wet.vehicle.ServoConfig;
import net.wachsmuths.rov.wet.vehicle.VehicleConfiguration;

/**
 * @author Brian Wachsmuth
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(GetCapabilitiesHandler.class)
public class GetCapabilitiesHandlerTest {
  private static final UUID TEST_UUID = UUID.randomUUID();
  
  @MockBean
  VehicleConfiguration vehicleConfiguration;
  
  @Autowired
  GetCapabilitiesHandler handler;

  /**
   * Test method for {@link net.wachsmuths.rov.wet.service.impl.GetCapabilitiesHandler#execute(net.wachsmuths.rov.common.GetCapabilities)}.
   */
  @Test
  public void testExecute() {
    when(vehicleConfiguration.getId()).thenReturn(TEST_UUID);
    when(vehicleConfiguration.getName()).thenReturn("Test Vehicle");
    
    List<AccessoryConfig> accessories = new ArrayList<>();
    accessories.add(new MotorConfig());
    accessories.add(new ServoConfig());
    
    when(vehicleConfiguration.getAllConfiguration()).thenReturn(accessories);
    
    
    Response response = handler.execute(new GetCapabilities());
    
    assertThat(response, instanceOf(VehicleCapabilities.class));
    
    VehicleCapabilities vehicleCapabilities = (VehicleCapabilities) response;
    assertThat(vehicleCapabilities.getId(), is(TEST_UUID));
    assertThat(vehicleCapabilities.getName(), is("Test Vehicle"));
    
    List<Capability> capabilities = vehicleCapabilities.getCapabilities();
    assertThat(capabilities, instanceOf(ArrayList.class));
    assertThat(capabilities.size(), is(2));
  }

}
