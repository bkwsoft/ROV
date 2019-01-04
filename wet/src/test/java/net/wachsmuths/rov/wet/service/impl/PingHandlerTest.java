/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.Test;
import org.junit.Before;
import net.wachsmuths.rov.common.Ping;
import net.wachsmuths.rov.common.Pong;
import net.wachsmuths.rov.common.Response;

/**
 * @author Brian Wachsmuth 
 *
 */
public class PingHandlerTest {
  private PingHandler handler;
  
  @Before
  public void setup() {
    handler = new PingHandler();
  }
  
  /**
   * Test method for {@link net.wachsmuths.rov.wet.service.impl.PingHandler#execute(net.wachsmuths.rov.common.Ping)}.
   */
  @Test
  public void testExecute() {
    Response response = handler.execute(new Ping());
    assertThat(response, instanceOf(Pong.class));
  }

  /**
   * Test method for {@link net.wachsmuths.rov.wet.service.impl.PingHandler#getCommandType()}.
   */
  @Test
  public void testGetCommandType() {
    assertThat(handler.getCommandType(), is(equalTo(Ping.class)));
  }

}
