/*
 * $Id: ServerMain.java,v 1.1.2.2 2004/10/20 10:51:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.server;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.corba.portable.reflect.*;
import com.syrus.AMFICOM.server.measurement.AmficomRtuImpl;
import com.syrus.AMFICOM.server.object.AmficomImpl;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.io.*;
import java.net.InetAddress;
import java.util.Properties;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.AlreadyBound;
import org.omg.PortableServer.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1.2.2 $, $Date: 2004/10/20 10:51:42 $
 * @module corbaportable_v1
 */
final class ServerMain {
	private ServerMain() {
	}

	public static void main(String args[]) throws IOException, UserException {
		ORB orb = JavaSoftORBUtil.getInstance().getORB();

		POA rootPOA = POAHelper.narrow(
			orb.resolve_initial_references("RootPOA"));
		rootPOA.the_POAManager().activate();
		NamingContextExt rootNamingCtx = NamingContextExtHelper.
			narrow(orb.resolve_initial_references("NameService"));
			
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream("server.properties"));
		} catch (FileNotFoundException fnfe) {
			;
		}
		final String hostName = properties.getProperty(
			"hostName",
			InetAddress.getLocalHost().getCanonicalHostName())
			.replaceAll("\\.", "_");
		
		NameComponent childPath[] = rootNamingCtx.to_name(hostName);
		
		NamingContextExt childNamingCtx;
		try {
			childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx.bind_new_context(childPath));
		} catch (AlreadyBound ab) {
			childNamingCtx = NamingContextExtHelper.narrow(rootNamingCtx.resolve_str(hostName));
		}

		AlarmTypeUtilities alarmTypeUtilities = (new AlarmTypeUtilitiesPOATie(
			new AlarmTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alarmTypeUtilitiesPath[]
			= rootNamingCtx.to_name("AlarmTypeUtilities");
		childNamingCtx.rebind(alarmTypeUtilitiesPath, alarmTypeUtilities);

		AlertingUtilities alertingUtilities = (new AlertingUtilitiesPOATie(
			new AlertingUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingUtilitiesPath[]
			= rootNamingCtx.to_name("AlertingUtilities");
		childNamingCtx.rebind(alertingUtilitiesPath, alertingUtilities);

		AlertingMessageTextUtilities alertingMessageTextUtilities = (
			new AlertingMessageTextUtilitiesPOATie(
				new AlertingMessageTextUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingMessageTextUtilitiesPath[]
			= rootNamingCtx.to_name("AlertingMessageTextUtilities");
		childNamingCtx.rebind(alertingMessageTextUtilitiesPath,
			alertingMessageTextUtilities);

		AlertingMessageUserLinkUtilities alertingMessageUserLinkUtilities = (
			new AlertingMessageUserLinkUtilitiesPOATie(
				new AlertingMessageUserLinkUtilitiesImpl(),
				rootPOA))._this(orb);
		NameComponent alertingMessageUserLinkUtilitiesPath[]
			= rootNamingCtx.to_name("AlertingMessageUserLinkUtilities");
		childNamingCtx.rebind(alertingMessageUserLinkUtilitiesPath,
			alertingMessageUserLinkUtilities);

		AlertingTypeUtilities alertingTypeUtilities = (
			new AlertingTypeUtilitiesPOATie(
				new AlertingTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingTypeUtilitiesPath[]
			= rootNamingCtx.to_name("AlertingTypeUtilities");
		childNamingCtx.rebind(alertingTypeUtilitiesPath,
			alertingTypeUtilities);

		EventSourceUtilities eventSourceUtilities = (
			new EventSourceUtilitiesPOATie(
				new EventSourceUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventSourceUtilitiesPath[]
			= rootNamingCtx.to_name("EventSourceUtilities");
		childNamingCtx.rebind(eventSourceUtilitiesPath, eventSourceUtilities);

		EventSourceTypeUtilities eventSourceTypeUtilities = (
			new EventSourceTypeUtilitiesPOATie(
				new EventSourceTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventSourceTypeUtilitiesPath[]
			= rootNamingCtx.to_name("EventSourceTypeUtilities");
		childNamingCtx.rebind(eventSourceTypeUtilitiesPath,
			eventSourceTypeUtilities);

		EventTypeUtilities eventTypeUtilities = (new EventTypeUtilitiesPOATie(
			new EventTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventTypeUtilitiesPath[]
			= rootNamingCtx.to_name("EventTypeUtilities");
		childNamingCtx.rebind(eventTypeUtilitiesPath, eventTypeUtilities);

		MessageTypeUtilities messageTypeUtilities = (
			new MessageTypeUtilitiesPOATie(
				new MessageTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent messageTypeUtilitiesPath[]
			= rootNamingCtx.to_name("MessageTypeUtilities");
		childNamingCtx.rebind(messageTypeUtilitiesPath, messageTypeUtilities);

		UserUtilities userUtilities = (new UserUtilitiesPOATie(
			new UserUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent userUtilitiesPath[]
			= rootNamingCtx.to_name("UserUtilities");
		childNamingCtx.rebind(userUtilitiesPath, userUtilities);

		AMFICOM amficom = (new AMFICOMPOATie(
			new AmficomImpl(), rootPOA))._this(orb);
		NameComponent amficomPath[]
			= rootNamingCtx.to_name("Amficom");
		childNamingCtx.rebind(amficomPath, amficom);

		AMFICOMKIS amficomRtu = (new AMFICOMKISPOATie(
			new AmficomRtuImpl(), rootPOA))._this(orb);
		NameComponent amficomRtuPath[]
			= rootNamingCtx.to_name("AmficomRtu");
		childNamingCtx.rebind(amficomRtuPath, amficomRtu);

		System.err.println(
			"The server is now running. Press enter to terminate it...");
		System.in.read();

		childNamingCtx.unbind(alarmTypeUtilitiesPath);
		childNamingCtx.unbind(alertingUtilitiesPath);
		childNamingCtx.unbind(alertingMessageTextUtilitiesPath);
		childNamingCtx.unbind(alertingMessageUserLinkUtilitiesPath);
		childNamingCtx.unbind(alertingTypeUtilitiesPath);
		childNamingCtx.unbind(eventSourceUtilitiesPath);
		childNamingCtx.unbind(eventSourceTypeUtilitiesPath);
		childNamingCtx.unbind(eventTypeUtilitiesPath);
		childNamingCtx.unbind(messageTypeUtilitiesPath);
		childNamingCtx.unbind(userUtilitiesPath);
		childNamingCtx.unbind(amficomPath);
		childNamingCtx.unbind(amficomRtuPath);

		/*
		 * Do not unbind as this path may be shared with other servers
		 * (say, CmServer).
		 */
//		rootNamingCtx.unbind(childPath);
	}
}
