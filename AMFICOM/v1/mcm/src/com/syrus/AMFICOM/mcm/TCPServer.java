package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class TCPServer implements Runnable
{
  public static final int MAX_KIS_ID_LENGTH = 32;
  public native int getListeningSocket(byte[] serviceName);
  public native int getConnectedSocket(
    int listeningSocket,
    char[] kis_id);
    
  public native void shutdown_server(int[] serverSockets);  

  protected int listeningSocket = -1;
  protected static Map kissockets = new HashMap();
  protected Map transceivers = null;
  
  private Thread acceptingThread = null;
  private boolean active = true;
  
  public TCPServer(String serviceName,Map transceivers)
    throws Exception
  {
    this.transceivers = transceivers;
    Collections.synchronizedMap(kissockets);
    Collections.synchronizedMap(transceivers);    
  
    byte[] serviceNameChars = serviceName.getBytes();
    listeningSocket = this.getListeningSocket(serviceNameChars);
    if (listeningSocket <= 0)
    {
        Log.errorMessage("Can't create listening socket for service (port) " +
          serviceName + "!");
        throw new Exception ("Listening socket required for further work!");
    }  
    
    acceptingThread = new Thread(new TCPAcceptingThread(this));
    acceptingThread.start();
  }

  public static int getSocketForKisID (Identifier kisID)
  {
    Integer socket = (Integer) TCPServer.kissockets.get(kisID);
    if (socket == null)
      return -1;
    return socket.intValue();
  }

  public void run()
  {
    try
    {
      while (active)
      {
        Thread.sleep(1000);
      }
      acceptingThread.interrupt();
    }
    catch (InterruptedException ie)
    {
      Log.errorMessage("Thread has been interrupted");
    }
  }
  
  public void shutdown()
  {
    active = false;
  }
}

class TCPAcceptingThread implements Runnable
{
  private TCPServer tcpServer = null;
  
  private boolean active = true;
  
  public TCPAcceptingThread(TCPServer tcpServer)
  {
    this.tcpServer = tcpServer;
  }

  public void run()
  {
    while(true)
    {
      char[] kisIDChars = new char[TCPServer.MAX_KIS_ID_LENGTH];
      int connectedSocket = tcpServer.getConnectedSocket(
        tcpServer.listeningSocket,
        kisIDChars);
      
      if (connectedSocket == -1)
      {
        Log.errorMessage("Can't establish connection!");
        continue;
      }
      Identifier kisID = new Identifier(new String(kisIDChars));
      
      tcpServer.kissockets.put(kisID,new Integer(connectedSocket));
        
      Transceiver transceiver = new Transceiver(kisID);
      transceiver.start();
      tcpServer.transceivers.put(kisID, transceiver);
      Log.errorMessage("Started transceiver for kis '" + kisID.toString() + "'");
    }
  }
}