package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

public class TCPServer implements Runnable
{
	private class TCPAcceptingThread implements Runnable
	{
	  private TCPServer tcpServer = null;
	  
	  public TCPAcceptingThread(TCPServer tcpServer)
	  {
	    this.tcpServer = tcpServer;
	  }

	  public void run()
	  {
	    while(true)
	    {
	      byte[] kisIDChars = new byte[MAX_KIS_ID_LENGTH];
	      int connectedSocket = this.tcpServer.getConnectedSocket(
	        this.tcpServer.listeningSocket,
	        kisIDChars);
	      
	      if (connectedSocket == -1)
	      {
	        Log.errorMessage("Can't establish connection!");
	        continue;
	      }
	      
	      if (kisIDChars == null)
	      {
		Log.errorMessage("Failed to get KIS_ID kis!");
	      	continue;		
		}
	
	      String gotString =  new String(kisIDChars);
	      System.out.println ("Java got the string: " + gotString + ", length = " + Integer.toString(gotString.length()));
	      Identifier kisID = new Identifier(new String(kisIDChars));
	      
	      TCPServer.kissockets.put(kisID,new Integer(connectedSocket));
	        
	      Transceiver transceiver = new Transceiver(kisID);
	      transceiver.start();
	      this.tcpServer.transceivers.put(kisID, transceiver);
	      Log.errorMessage("Started transceiver for kis '" + kisID.toString() + "'");
	    }
	  }
	}
	
  public static final int MAX_KIS_ID_LENGTH = 32;
  public native int getListeningSocket(byte[] hostName,byte[] serviceName);
  public native int getConnectedSocket(
    int listeningSocket,
    byte[] kis_id);
    
  public native void shutdownServer(int[] serverSockets);  

  protected int listeningSocket = -1;
  protected static Map kissockets = new HashMap();
  protected Map transceivers = null;
  
  private Thread acceptingThread = null;
  private boolean active = true;
  
  public TCPServer(String hostName,String serviceName,Map transceivers)
    throws Exception
  {
    this.transceivers = transceivers;
    Collections.synchronizedMap(kissockets);
    Collections.synchronizedMap(transceivers);    
  
    byte[] serviceNameChars = serviceName.getBytes();
    byte[] hostNameChars = hostName.getBytes();
    
    this.listeningSocket = this.getListeningSocket(hostNameChars,serviceNameChars);
    if (this.listeningSocket <= 0)
    {
        Log.errorMessage("Can't create listening socket for service (port) " +
          serviceName + "!");
        throw new Exception ("Listening socket required for further work!");
    }  
    
    this.acceptingThread = new Thread(new TCPAcceptingThread(this));
    this.acceptingThread.start();
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
      while (this.active)
      {
        Thread.sleep(1000);
      }
      this.acceptingThread.interrupt();
    }
    catch (InterruptedException ie)
    {
      Log.errorMessage("Thread has been interrupted");
    }
  }
  
  public void shutdown()
  {
    this.active = false;
  }
}
