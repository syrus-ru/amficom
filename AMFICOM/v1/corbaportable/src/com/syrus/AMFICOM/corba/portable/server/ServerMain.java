/*
 * $Id: ServerMain.java,v 1.1 2004/06/22 12:27:26 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.server;

import com.syrus.AMFICOM.corba.portable.reflect.*;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.io.IOException;
import org.omg.CORBA.*;
import org.omg.CosNaming.*;
import org.omg.PortableServer.*;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:26 $
 * @author $Author: bass $
 */
final class ServerMain {
	private ServerMain() {
	}

	public static void main(String[] args) throws IOException, UserException {
		ORB orb = JavaSoftORBUtil.getInstance().getORB();

		POA rootPOA = POAHelper.narrow(
			orb.resolve_initial_references("RootPOA"));
		rootPOA.the_POAManager().activate();
		NamingContextExt namingContextExt = NamingContextExtHelper.
			narrow(orb.resolve_initial_references("NameService"));

		AlarmTypeUtilities alarmTypeUtilities = (new AlarmTypeUtilitiesPOATie(
			new AlarmTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alarmTypeUtilitiesPath[]
			= namingContextExt.to_name("AlarmTypeUtilities");
		namingContextExt.rebind(alarmTypeUtilitiesPath, alarmTypeUtilities);

		AlertingUtilities alertingUtilities = (new AlertingUtilitiesPOATie(
			new AlertingUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingUtilitiesPath[]
			= namingContextExt.to_name("AlertingUtilities");
		namingContextExt.rebind(alertingUtilitiesPath, alertingUtilities);

		AlertingMessageTextUtilities alertingMessageTextUtilities = (
			new AlertingMessageTextUtilitiesPOATie(
				new AlertingMessageTextUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingMessageTextUtilitiesPath[]
			= namingContextExt.to_name("AlertingMessageTextUtilities");
		namingContextExt.rebind(alertingMessageTextUtilitiesPath,
			alertingMessageTextUtilities);

		AlertingMessageUserLinkUtilities alertingMessageUserLinkUtilities = (
			new AlertingMessageUserLinkUtilitiesPOATie(
				new AlertingMessageUserLinkUtilitiesImpl(),
				rootPOA))._this(orb);
		NameComponent alertingMessageUserLinkUtilitiesPath[]
			= namingContextExt.to_name("AlertingMessageUserLinkUtilities");
		namingContextExt.rebind(alertingMessageUserLinkUtilitiesPath,
			alertingMessageUserLinkUtilities);

		AlertingTypeUtilities alertingTypeUtilities = (
			new AlertingTypeUtilitiesPOATie(
				new AlertingTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent alertingTypeUtilitiesPath[]
			= namingContextExt.to_name("AlertingTypeUtilities");
		namingContextExt.rebind(alertingTypeUtilitiesPath,
			alertingTypeUtilities);

		EventSourceUtilities eventSourceUtilities = (
			new EventSourceUtilitiesPOATie(
				new EventSourceUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventSourceUtilitiesPath[]
			= namingContextExt.to_name("EventSourceUtilities");
		namingContextExt.rebind(eventSourceUtilitiesPath, eventSourceUtilities);

		EventSourceTypeUtilities eventSourceTypeUtilities = (
			new EventSourceTypeUtilitiesPOATie(
				new EventSourceTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventSourceTypeUtilitiesPath[]
			= namingContextExt.to_name("EventSourceTypeUtilities");
		namingContextExt.rebind(eventSourceTypeUtilitiesPath,
			eventSourceTypeUtilities);

		EventTypeUtilities eventTypeUtilities = (new EventTypeUtilitiesPOATie(
			new EventTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent eventTypeUtilitiesPath[]
			= namingContextExt.to_name("EventTypeUtilities");
		namingContextExt.rebind(eventTypeUtilitiesPath, eventTypeUtilities);

		MessageTypeUtilities messageTypeUtilities = (
			new MessageTypeUtilitiesPOATie(
				new MessageTypeUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent messageTypeUtilitiesPath[]
			= namingContextExt.to_name("MessageTypeUtilities");
		namingContextExt.rebind(messageTypeUtilitiesPath, messageTypeUtilities);

		UserUtilities userUtilities = (new UserUtilitiesPOATie(
			new UserUtilitiesImpl(), rootPOA))._this(orb);
		NameComponent userUtilitiesPath[]
			= namingContextExt.to_name("UserUtilities");
		namingContextExt.rebind(userUtilitiesPath, userUtilities);

		System.out.println(
			"The server is now running. Press any key to terminate it...");
		System.in.read();

		namingContextExt.unbind(alarmTypeUtilitiesPath);
		namingContextExt.unbind(alertingUtilitiesPath);
		namingContextExt.unbind(alertingMessageTextUtilitiesPath);
		namingContextExt.unbind(alertingMessageUserLinkUtilitiesPath);
		namingContextExt.unbind(alertingTypeUtilitiesPath);
		namingContextExt.unbind(eventSourceUtilitiesPath);
		namingContextExt.unbind(eventSourceTypeUtilitiesPath);
		namingContextExt.unbind(eventTypeUtilitiesPath);
		namingContextExt.unbind(messageTypeUtilitiesPath);
		namingContextExt.unbind(userUtilitiesPath);
	}
}
