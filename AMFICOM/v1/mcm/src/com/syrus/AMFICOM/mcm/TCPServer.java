
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

import java.net.UnknownServiceException;
import java.util.Map;
import java.util.HashMap;

public class TCPServer extends SleepButWorkThread {

	protected static Map	kissockets		= new HashMap();

	protected int			listeningSocket	= -1;
	private boolean			active			= true;

	public TCPServer(String hostName, String serviceName) throws UnknownServiceException  {
		super(ApplicationProperties.getInt(MeasurementControlModule.KEY_TICK_TIME, MeasurementControlModule.TICK_TIME) * 1000, 
					ApplicationProperties.getInt(MeasurementControlModule.KEY_MAX_FALLS, MAX_FALLS));
		this.listeningSocket = this.getListeningSocket(hostName, serviceName);
		if (this.listeningSocket <= 0) {
			Log.errorMessage("Can't create listening socket for service (port) " + serviceName + "!");
			throw new UnknownServiceException("Listening socket required for further work!");
		}
	}

	public static int getSocketForKisID(Identifier kisId) {
		Integer socket = (Integer) TCPServer.kissockets.get(kisId);
		if (socket == null)
			return -1;
		return socket.intValue();
	}

	private native int getConnectedSocket(int listeningSocket, Object[] object);

	private native int getListeningSocket(String hostName, String serviceName);

	public void run() {
			while (this.active) {
					Log.debugMessage("TCPAcceptingThread.run | ", Log.DEBUGLEVEL05);
					Object[] objects = new Object[1];
					Integer connectedSocket = new Integer(this.getConnectedSocket(this.listeningSocket, objects));
					Log.debugMessage("TCPAcceptingThread.run | got " + ((objects==null)? "null":String.valueOf(objects.length)) + " ", Log.DEBUGLEVEL05);
					
					String id = null;
	
					if (objects == null) {
						Log.errorMessage("Can't get data from connected socket!");
						continue;
					}
	
					for (int i = 0; i < objects.length; i++) {
						if (objects[i] instanceof String) {
							id = (String) objects[i];
						} else {
							if (objects[i] instanceof Integer) {
								connectedSocket = (Integer) objects[i];
							}
						}
					}
	
					if ((connectedSocket == null) || (connectedSocket.intValue() == -1)) {
						Log.errorMessage("Can't establish connection!");
						continue;
					}
	
					if (id == null) {
						Log.errorMessage("Failed to get KIS_ID kis!");
						continue;
					}
	
					Log.debugMessage("Java got the string: " + id + ", length = " + id.length(), Log.DEBUGLEVEL05);
					Identifier kisId = new Identifier(id);
	
					TCPServer.kissockets.put(kisId, connectedSocket);
	
					if (!MeasurementControlModule.transceivers.containsKey(kisId)) {
						Transceiver transceiver = new Transceiver(kisId);
						transceiver.start();
						MeasurementControlModule.transceivers.put(kisId, transceiver);
						Log.debugMessage("Started transceiver for kis '" + kisId.toString() + "'", Log.DEBUGLEVEL05);
					}
		
				try {
					sleep(super.initialTimeToSleep);
				}
				catch (InterruptedException ie) {
					Log.errorException(ie);
				}	
			}			
	}

	public void shutdown() {
		Log.debugMessage("TCPServer.shutdown", Log.DEBUGLEVEL05);
		this.active = false;
	}
	
	
	protected void processFall() {
		Log.errorMessage("TCPServer.processFall | fallCode:" + super.fallCode);
	}

	public native void shutdownServer(int[] serverSockets);

}
