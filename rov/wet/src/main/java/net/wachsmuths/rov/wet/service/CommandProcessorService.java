/**
 * 
 */
package net.wachsmuths.rov.wet.service;

import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.Response;

/**
 * @author Brian Wachsmuth 
 *
 */
public interface CommandProcessorService {
  Response handleCommand(Command command);
}
