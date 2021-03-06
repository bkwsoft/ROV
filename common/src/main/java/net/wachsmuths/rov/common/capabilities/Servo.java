/**
 * 
 */
package net.wachsmuths.rov.common.capabilities;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Brian Wachsmuth
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class Servo extends AbstractCapability {
  private static final long serialVersionUID = 7930072440063621979L;

}
