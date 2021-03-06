/**
 * 
 */
package net.wachsmuths.rov.wet.vehicle;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import net.wachsmuths.rov.common.capabilities.Capability;
import net.wachsmuths.rov.common.capabilities.Motor;
import net.wachsmuths.rov.common.capabilities.MotorOrientation;
import net.wachsmuths.rov.common.capabilities.MotorType;
import net.wachsmuths.rov.common.capabilities.Servo;
import net.wachsmuths.rov.common.capabilities.Video;

/**
 * @author Brian Wachsmuth
 *
 */
public class CapabilityFactoryTest {

  /**
   * Test method for {@link net.wachsmuths.rov.wet.vehicle.CapabilityFactory#getCapability(net.wachsmuths.rov.wet.vehicle.AccessoryConfig)}.
   */
  @Test
  public void testGetCapabilityMotor() {
    MotorConfig config = new MotorConfig();
    config.setId(100);
    config.setMotorOrientation(MotorOrientation.Y_AXIS);
    config.setMotorType(MotorType.TRACK);
    config.setName("Test1");
    config.setPwmPort(5);
    
    Capability capability = CapabilityFactory.getCapability(config);
    
    assertThat(capability, instanceOf(Motor.class));
    Motor motor = (Motor) capability;
    assertThat(motor.getId(), is(config.getId()));
    assertThat(motor.getMotorOrientation(), is(config.getMotorOrientation()));
    assertThat(motor.getMotorType(), is(config.getMotorType()));
    assertThat(motor.getName(), is(config.getName()));
  }

  /**
   * Test method for {@link net.wachsmuths.rov.wet.vehicle.CapabilityFactory#getCapability(net.wachsmuths.rov.wet.vehicle.AccessoryConfig)}.
   */
  @Test
  public void testGetCapabilityServo() {
    ServoConfig config = new ServoConfig();
    config.setId(100);
    config.setName("Test1");
    config.setPwmPort(5);
    
    Capability capability = CapabilityFactory.getCapability(config);
    
    assertThat(capability, instanceOf(Servo.class));
    Servo servo = (Servo) capability;
    assertThat(servo.getId(), is(config.getId()));
    assertThat(servo.getName(), is(config.getName()));
  }

  /**
   * Test method for {@link net.wachsmuths.rov.wet.vehicle.CapabilityFactory#getCapability(net.wachsmuths.rov.wet.vehicle.AccessoryConfig)}.
   */
  @Test
  public void testGetCapabilityVideo() {
    VideoConfig config = new VideoConfig();
    config.setId(100);
    config.setName("Test1");
    config.setVideoStreamAddress("localhost:12345");
    
    Capability capability = CapabilityFactory.getCapability(config);
    
    assertThat(capability, instanceOf(Video.class));
    Video video = (Video) capability;
    assertThat(video.getId(), is(config.getId()));
    assertThat(video.getName(), is(config.getName()));
    assertThat(video.getVideoStreamAddress(), is(config.getVideoStreamAddress()));
  }

  /**
   * Test method for {@link net.wachsmuths.rov.wet.vehicle.CapabilityFactory#getCapability(net.wachsmuths.rov.wet.vehicle.AccessoryConfig)}.
   */
  @Test(expected=IllegalArgumentException.class)
  public void testGetCapabilityInvalid() {
    AccessoryConfig config = new AccessoryConfig() {
      
      @Override
      public String getName() {
        // TODO Auto-generated method stub
        return null;
      }
      
      @Override
      public int getId() {
        // TODO Auto-generated method stub
        return 0;
      }
    };
    
    CapabilityFactory.getCapability(config);
  }
}
