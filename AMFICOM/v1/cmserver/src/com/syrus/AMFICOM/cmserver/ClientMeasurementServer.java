/*
 * $Id: ClientMeasurementServer.java,v 1.1 2004/09/20 09:42:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.cmserver.corba.CMServerPOATie;
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.Application;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.net.InetAddress;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.PortableServer.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/20 09:42:33 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public class ClientMeasurementServer extends SleepButWorkThread {

	public static final String	APPLICATION_NAME	= "cmserver";

	public static final String	KEY_TICK_TIME		= "TickTime";
	public static final String	KEY_MAX_FALLS		= "MaxFalls";

	public static final int		TICK_TIME		= 5;

	/* CORBA server */
	private static CORBAServer	corbaServer;

	public ClientMeasurementServer() {
		super(ApplicationProperties.getInt(KEY_TICK_TIME, TICK_TIME) * 1000, ApplicationProperties
				.getInt(KEY_MAX_FALLS, MAX_FALLS));
	}

	public static void main(String[] args) {
		Application.init(APPLICATION_NAME);
		startup();
	}

	private static void startup() {
		/* Create CORBA server with servant(s) */
		activateCORBAServer();
	}

	private static void activateCORBAServer() {
		/* Create local CORBA server end activate servant */
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			final String hostName = InetAddress.getLocalHost().getCanonicalHostName()
					.replaceAll("\\.", "_");

			NameComponent childPath[] = rootNamingCtx.to_name(hostName);

			NamingContextExt childNamingCtx;
			try {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.bind_new_context(childPath));
			} catch (AlreadyBound ab) {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx.resolve_str(hostName));
			}

			CMServer server = (new CMServerPOATie(new CMServerImpl(), rootPOA))._this(orb);
			NameComponent serverPath[] = rootNamingCtx.to_name("CMServer");
			childNamingCtx.rebind(serverPath, server);
			
			childNamingCtx.unbind(serverPath);
			rootNamingCtx.unbind(childPath);
			
		} catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}
	}
	
	private static void stopCORBAServer(){
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			final String hostName = InetAddress.getLocalHost().getCanonicalHostName()
					.replaceAll("\\.", "_");

			NameComponent childPath[] = rootNamingCtx.to_name(hostName);

			NamingContextExt childNamingCtx;
			try {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx
						.bind_new_context(childPath));
			} catch (AlreadyBound ab) {
				childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx.resolve_str(hostName));
			}
			
			NameComponent serverPath[] = rootNamingCtx.to_name("CMServer");
			
			childNamingCtx.unbind(serverPath);
			rootNamingCtx.unbind(childPath);
			
		} catch (Exception e) {
			Log.errorException(e);
			DatabaseConnection.closeConnection();
			System.exit(-1);
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
		super.clearFalls();
	}
}
