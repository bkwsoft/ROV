/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import org.springframework.context.ApplicationEvent;

/**
 * @author Brian Wachsmuth
 *
 */
public class ControllerDisconnectedEvent extends ApplicationEvent {

  public ControllerDisconnectedEvent(Object source) {
    super(source);
  }
}
