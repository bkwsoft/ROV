/**
 * 
 */
package net.wachsmuths.rov.dry.service.impl;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.GetCapabilities;
import net.wachsmuths.rov.common.Response;
import net.wachsmuths.rov.common.RovConstants;
import net.wachsmuths.rov.common.VehicleCapabilities;

/**
 * @author Brian Wachsmuth
 *
 */
@Slf4j
public class ControllHandler implements Closeable {
  private VehicleCapabilities capabilities;
  private Socket vehicleSocket;
  private ObjectOutputStream out;
  private ObjectInputStream in;
  
  public ControllHandler(InetAddress vehicleAddress) throws IOException, ClassNotFoundException {
    vehicleSocket = new Socket(vehicleAddress, RovConstants.ROV_PORT);
    out = new ObjectOutputStream(vehicleSocket.getOutputStream());
    in = new ObjectInputStream(vehicleSocket.getInputStream());
    
    //Get Capabilities
    capabilities = (VehicleCapabilities) sendCommand(new GetCapabilities());
  }
  
  public Response sendCommand(Command command) throws IOException, ClassNotFoundException {
    log.debug("Sending command: " + command.getClass().getSimpleName());
    out.writeObject(command);
    out.flush();
    
    Response response = (Response) in.readObject();
    log.debug("Got command response: " + response.toString());
    
    return response;
    
  }
  
  public VehicleCapabilities getVehicleCapabilities() {
    return capabilities;
  }
  
  public UUID getId() {
    return capabilities.getId();
  }

  @Override
  public void close() throws IOException {
    IOUtils.closeQuietly(in);
    IOUtils.closeQuietly(out);
    IOUtils.closeQuietly(vehicleSocket);
  }
}
