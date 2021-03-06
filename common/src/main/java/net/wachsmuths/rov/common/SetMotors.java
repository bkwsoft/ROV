/**
 * 
 */
package net.wachsmuths.rov.common;

import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Brian Wachsmuth
 *
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class SetMotors implements Command {
  private static final long serialVersionUID = -8856009547284313004L;

  private List<MotorPower> powerLevels;
}
