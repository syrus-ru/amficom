
package com.syrus.AMFICOM.mcm;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.util.Log;
import java.util.Map;
import java.util.HashMap;

public class TCPServer implements Runnable {

	private class TCPAcceptingThread implements Runnable {

		private TCPServer	tcpServer	= null;

		public TCPAcceptingThread(TCPServer tcpServer) {
			this.tcpServer = tcpServer;
		}

		public void run() {
			while (true) {
				//byte[] kisIDChars = new byte[MAX_KIS_ID_LENGTH];
				Object[] objects = this.tcpServer.getConnectedSocket(this.tcpServer.listeningSocket);
				
				Integer connectedSocket = null;
				String id = null;
				
				if (objects == null){
					Log.errorMessage("Can't get data from connected socket!");
					continue;
				}
				
				for(int i=0;i<objects.length;i++){
					if (objects[i] instanceof String){
						id = (String)objects[i];
					} else{
						if (objects[i] instanceof Integer){
							connectedSocket = (Integer)objects[i];
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
				
				Log.debugMessage("Java got the string: " + id + ", length = "
						+ id.length(), Log.DEBUGLEVEL05);
				Identifier kisId = new Identifier(id);

				TCPServer.kissockets.put(kisId, connectedSocket);

				if (!this.tcpServer.transceivers.containsKey(kisId)){
					Transceiver transceiver = new Transceiver(kisId);
					transceiver.start();
					this.tcpServer.transceivers.put(kisId, transceiver);
					Log.debugMessage("Started transceiver for kis '" + kisId.toString() + "'", Log.DEBUGLEVEL05);
				}
			}
		}
	}

	protected static Map	kissockets			= new HashMap();

	protected int			listeningSocket		= -1;
	protected Map			transceivers		= null;

	private Thread			acceptingThread		= null;
	private boolean			active				= true;

	public TCPServer(String hostName, String serviceName, Map transceivers) throws Exception {
		this.transceivers = transceivers;

		this.listeningSocket = this.getListeningSocket(hostName, serviceName);
		if (this.listeningSocket <= 0) {
			Log.errorMessage("Can't create listening socket for service (port) " + serviceName + "!");
			throw new Exception("Listening socket required for further work!");
		}

		this.acceptingThread = new Thread(new TCPAcceptingThread(this));
		this.acceptingThread.start();
	}

	public static int getSocketForKisID(Identifier kisId) {
		Integer socket = (Integer) TCPServer.kissockets.get(kisId);
		if (socket == null)
			return -1;
		return socket.intValue();
	}

	public native Object[] getConnectedSocket(int listeningSocket);

	public native int getListeningSocket(String hostName, String serviceName);

	public void run() {
		try {
			while (this.active) {
				Thread.sleep(1000);
			}
			this.acceptingThread.interrupt();
		} catch (InterruptedException ie) {
			Log.errorMessage("Thread has been interrupted");
		}
	}

	public void shutdown() {
		this.active = false;
	}

	public native void shutdownServer(int[] serverSockets);

}
