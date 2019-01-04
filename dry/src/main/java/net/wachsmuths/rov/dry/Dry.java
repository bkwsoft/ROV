/**
 * 
 */
package net.wachsmuths.rov.dry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Brian Wachsmuth
 *
 */
@SpringBootApplication
public class Dry {

  public static void main(String[] args) {
      SpringApplication.run(Dry.class, args);
  }
}
