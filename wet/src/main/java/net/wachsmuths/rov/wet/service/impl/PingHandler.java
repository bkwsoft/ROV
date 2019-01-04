/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import org.springframework.stereotype.Service;
import net.wachsmuths.rov.common.Ping;
import net.wachsmuths.rov.common.Pong;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.wet.service.CommandHandler;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
public class PingHandler implements CommandHandler<Ping> {

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#execute(net.wachsmuths.rov.common.Command)
   */
  @Override
  public Response execute(Ping command) {
    return new Pong();
  }

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#getCommandType()
   */
  @Override
  public Class<Ping> getCommandType() {
    return Ping.class;
  }

}
