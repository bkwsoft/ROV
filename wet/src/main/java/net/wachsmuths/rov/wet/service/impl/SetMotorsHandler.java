/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.SetMotors;
import net.wachsmuths.rov.wet.service.CommandHandler;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
@Slf4j
public class SetMotorsHandler implements CommandHandler<SetMotors> {

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#execute(net.wachsmuths.rov.common.Command)
   */
  @Override
  public Response execute(SetMotors command) {
    log.debug("Got Set Motors Command!");
    
    return null;
  }

  /* (non-Javadoc)
   * @see net.wachsmuths.rov.service.CommandHandler#getCommandType()
   */
  @Override
  public Class<SetMotors> getCommandType() {
    return SetMotors.class;
  }

}
