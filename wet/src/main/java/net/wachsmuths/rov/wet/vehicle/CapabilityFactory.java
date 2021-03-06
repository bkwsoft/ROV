/**
 * 
 */
package net.wachsmuths.rov.wet.vehicle;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import net.wachsmuths.rov.common.capabilities.Capability;
import net.wachsmuths.rov.common.capabilities.Servo;
import net.wachsmuths.rov.common.capabilities.Motor;
import net.wachsmuths.rov.common.capabilities.Video;

/**
 * @author Generates a Capability Object based on a Configuration Object
 *
 */
public abstract class CapabilityFactory {
  private static Map<Class<? extends AccessoryConfig>, Class<? extends Capability>> clazzMatrix = new HashMap<>();
  
  static {
   clazzMatrix.put(MotorConfig.class, Motor.class);
   clazzMatrix.put(ServoConfig.class, Servo.class);
   clazzMatrix.put(VideoConfig.class, Video.class);
  }
  
  public static Capability getCapability(AccessoryConfig config) {
    Class<? extends Capability> capabilityClazz = clazzMatrix.get(config.getClass());
    
    if (capabilityClazz == null) {
      throw new IllegalArgumentException("Unable to convert configuration class '" + config.getClass().getName() + "' to a capability.");
    }
    
    Capability capability = null;
    
    try {
      capability = capabilityClazz.newInstance();
      BeanUtils.copyProperties(config, capability);
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Error creating capability object.", e);
    }
    
    return capability;
  }
}
