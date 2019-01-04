/**
 * 
 */
package net.wachsmuths.rov.wet.vehicle;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.wachsmuths.rov.common.capabilities.Video;

/**
 * @author Brian Wachsmuth
 *
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class VideoConfig extends Video implements AccessoryConfig {

}
