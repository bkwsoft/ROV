/**
 * 
 */
package net.wachsmuths.rov.wet.service.impl;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import net.wachsmuths.rov.common.Command;
import net.wachsmuths.rov.common.RovConstants;
import net.wachsmuths.rov.wet.service.CommandProcessorService;

/**
 * @author Brian Wachsmuth
 *
 */
@Service
@Slf4j
public class TcpConnectionService {
  public static final long IDLE_TIME = 1000;  //One second
  
  private CommandProcessorService commandProcessorService;
  private ConnectionServer connectionServer;
  private ApplicationEventPublisher eventPublisher;
  
  public TcpConnectionService(CommandProcessorService commandProcessorService,ApplicationEventPublisher eventPublisher) {
    this.commandProcessorService = commandProcessorService;
    this.eventPublisher = eventPublisher;
    connectionServer = new ConnectionServer();
  }

  @EventListener
  public void startServer(ApplicationReadyEvent event) {
    log.debug("Starting TcpConnectionService server.");
    connectionServer.start();
  }

  
  private class ConnectionServer extends Thread {
    private ServerSocket listener;

    @Override
    public void run() {
      try {
        this.listener = new ServerSocket(RovConstants.ROV_PORT);
      } catch (IOException e) {
        log.error("Failed to start connection server.", e);
        return;
      }
      
      while (true) {
        try {
          new ConnectionHandler(listener.accept()).start();
          eventPublisher.publishEvent(new ControllerConnectedEvent(this));
        } catch (IOException e) {
          log.error("Error starting a new connection.", e);
        }
        
        try {
          sleep(IDLE_TIME);
        } catch (InterruptedException e) {
          break;
        }
      }
    }
  }
  
  private class ConnectionHandler extends Thread {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    public ConnectionHandler(Socket clientSocket) throws IOException {
      log.debug("Received new connection from client: " + clientSocket.getInetAddress());
      this.socket = clientSocket;
      this.out = new ObjectOutputStream(clientSocket.getOutputStream());
      this.in= new ObjectInputStream(clientSocket.getInputStream());
    }

    @Override
    public void run() {
      while (socket.isConnected()) {
        try {
          Command command = (Command) in.readObject();
          out.writeObject(commandProcessorService.handleCommand(command));
        } catch (EOFException e) {
          //Out channel has closed.  Break out of the loop.
          eventPublisher.publishEvent(new ControllerDisconnectedEvent(this));
          break;
        } catch (ClassNotFoundException | IOException e) {
          log.error("Error handling command stream.", e);
        }
        
        try {
          sleep(IDLE_TIME);
        } catch (InterruptedException e) {
          break;
        }
      }
      
      IOUtils.closeQuietly(in);
      IOUtils.closeQuietly(out);
      IOUtils.closeQuietly(socket);
    }
  }
}
