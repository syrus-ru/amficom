/*
 * $Id: RISDConnectionInfo.java,v 1.4 2004/09/25 19:31:05 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General;

import com.syrus.AMFICOM.CORBA.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2004/09/25 19:31:05 $
 * @module generalclient_v1
 */
public final class RISDConnectionInfo extends ConnectionInterface {
	private boolean connected;

	/**
	 * Shadows {@link ConnectionInterface#instance}
	 */
	private static RISDConnectionInfo instance = null;

	/**
	 * @deprecated Use {@link #getServer()} instead.
	 */
	public AMFICOM server;

	private String serverName;

	private Map serverMap = null;

	/**
	 * @deprecated In the future, this constructor will be declared private.
	 *             Use {@link #getInstance()} instead.
	 */
	public RISDConnectionInfo() {
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(final boolean connected) throws UserException, SystemException {
		try {
		if (this.connected == connected)
			return;
		if (connected)
			synchronized (this) {
				if (connected) {
					if (serverMap == null)
						serverMap = new HashMap();
					try {
						NamingContextExt rootCtx = NamingContextExtHelper.narrow(JavaSoftORBUtil.getInstance().getORB().resolve_initial_references("NameService"));
						BindingListHolder rootBindingListHolder = new BindingListHolder();
						BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
						rootCtx.list(Integer.MAX_VALUE, rootBindingListHolder, bindingIteratorHolder);
						Binding rootBindingList[] = rootBindingListHolder.value;
						for (int i = 0; i < rootBindingList.length; i++) {
							Binding rootBinding = rootBindingList[i];
							NameComponent rootNameComponent[] = rootBinding.binding_name;
							if (rootBinding.binding_type.value() != BindingType._ncontext)
								continue;
							NamingContextExt childCtx = NamingContextExtHelper.narrow(rootCtx.resolve(rootNameComponent));
							BindingListHolder childBindingListHolder = new BindingListHolder();
							childCtx.list(Integer.MAX_VALUE, childBindingListHolder, bindingIteratorHolder);
							Binding childBindingList[] = childBindingListHolder.value;
							Map featureMap = new HashMap();
							for (int j = 0; j < childBindingList.length; j++) {
								Binding childBinding = childBindingList[j];
								NameComponent childNameComponent[] = childBinding.binding_name;
								if (childBinding.binding_type.value() != BindingType._nobject)
									continue;
								featureMap.put(rootCtx.to_string(childNameComponent), childCtx.resolve(childNameComponent));
							}
							serverMap.put(rootCtx.to_string(rootNameComponent).replace('_', '.'), featureMap);
						}
					} catch (UserException ue) {
						serverMap.clear();
						throw ue;
					} catch (SystemException se) {
						serverMap.clear();
						throw se;
					} catch (Exception e) {
						serverMap.clear();
						e.printStackTrace();
					}
					/**
					 * @todo Define an exception to be thrown if
					 *       zarro servers found.
					 */
					assert (!serverMap.isEmpty()) : "No servers found!";
					for (Iterator serverIterator = serverMap.keySet().iterator(); serverIterator.hasNext();) {
						String serverName = (String) (serverIterator.next());
						Map featureMap = (Map) (serverMap.get(serverName));
						AMFICOM server = AMFICOMHelper.narrow((org.omg.CORBA.Object) (featureMap.get("Amficom"))); 
						
						System.err.println(serverName);
						for (Iterator featureIterator = featureMap.keySet().iterator(); featureIterator.hasNext();)
							System.err.println('\t' + ((String) (featureIterator.next())));
						
						/*
						 * Currently, take the very first server from the list.
						 */
						if (this.server == null)
							this.server = server;
						if (this.serverName == null)
							this.serverName = serverName;
					}
				}
			}
		else
			synchronized (this) {
				if (!connected) {
					if (serverMap != null)
						serverMap.clear();
					server = null;
					serverName = null;
				}
			}
		this.connected = connected;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static ConnectionInterface getInstance() {
		if (instance == null)
			synchronized (RISDConnectionInfo.class) {
				if (instance == null)
					instance = new RISDConnectionInfo();
			}
		if (ConnectionInterface.instance == null)
			synchronized (ConnectionInterface.class) {
				if (ConnectionInterface.instance == null)
					ConnectionInterface.instance = instance;
			}
		return instance;
	}

	public AMFICOM getServer() {
		return server;
	}

	public String getServerName() {
		return serverName;
	}

	/**
	 * @deprecated
	 */
	public static final String DEFAULT_objectName = "/AMFICOM/AMFICOM";

	/**
	 * @deprecated
	 */
	public static final String DEFAULT_user = "AMFICOM";

	/**
	 * @deprecated
	 */
	public static final String DEFAULT_password = "amficom";

	/**
	 * @deprecated
	 */
	public static final String DEFAULT_SUBCONTEXT_NAME = ":default";

	/**
	 * @deprecated
	 */
	public long ConnectTime;

	/**
	 * @deprecated
	 */
	public int sessions;

	/**
	 * @deprecated Does nothing.
	 */
	public void initialize() {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void SetDefaults() {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getServicePrefix() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setServicePrefix(String servicePrefix) {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getTCPport() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setTCPport(String TCPport) {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getSID() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setSID(String SID) {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getObjectName() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setObjectName(String objectName) {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getUser() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setUser(String user) {
	}

	/**
	 * @deprecated Does nothing.
	 */
	public String getPassword() {
		return "";
	}

	/**
	 * @deprecated Does nothing.
	 */
	public void setPassword(String password) {
	}
}
