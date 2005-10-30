/*-
 * $Id: Foo.java,v 1.1 2005/10/30 14:49:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.OFF;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import com.syrus.AMFICOM.eventv2.corba.MessageReceiver;
import com.syrus.AMFICOM.eventv2.corba.MessageReceiverHelper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExt;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExtHelper;
import com.syrus.util.Application;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/30 14:49:11 $
 * @module leserver
 */
final class Foo {
	public static void main(String[] args) throws UserException, InterruptedException {
		Application.init("foo");
		final ORB orb = ORB.init(new String[]{"-ORBInitialHost", "aldan",
				"-ORBInitialPort", "1050",
				"-ORBDebug", "none",
				"-ORBDebugLevel", "0"}, null);
		if (orb instanceof com.sun.corba.se.spi.orb.ORB) {
			final com.sun.corba.se.spi.orb.ORB sunOrb = (com.sun.corba.se.spi.orb.ORB) orb;
//			sunOrb.getLogger("rpc").setLevel(OFF);
//			sunOrb.getLogger("rpc.encoding").setLevel(OFF);
//			sunOrb.getLogger("rpc.presentation").setLevel(OFF);
//			sunOrb.getLogger("rpc.protocol").setLevel(OFF);
			sunOrb.getLogger("rpc.transport").setLevel(OFF);
//			sunOrb.getLogger("oa").setLevel(OFF);
//			sunOrb.getLogger("oa.invocation").setLevel(OFF);
//			sunOrb.getLogger("oa.ior").setLevel(OFF);
//			sunOrb.getLogger("oa.lifecycle").setLevel(OFF);
//			sunOrb.getLogger("orb").setLevel(OFF);
//			sunOrb.getLogger("orb.lifecycle").setLevel(OFF);
//			sunOrb.getLogger("orb.resolver").setLevel(OFF);
//			sunOrb.getLogger("util").setLevel(OFF);
		}
		final Thread orbThread = new Thread("ORB") {
			@Override
			public void run() {
				orb.run();
			}
		};
		orbThread.start();

		final NamingContextExt namingCtx = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
		
		for (final Object object : getObjects(namingCtx)) {
			if (object._is_a(MessageReceiverExtHelper.id())) {
				Log.debugMessage("Object: " + object + "is a valid MessageReceiverExt; continuing", FINEST);
				final MessageReceiverExt messageReceiverExt = MessageReceiverExtHelper.narrow(object);
				Log.debugMessage("Message will be delivered to: "
						+ Identifier.valueOf(messageReceiverExt.getSystemUserId()),
						FINEST);
				
				if (object._is_a(MessageReceiverHelper.id())) {
					final MessageReceiver messageReceiver = MessageReceiverHelper.narrow(object);
				} else {
					/*
					 * Everyone who implements MessageReceiverExt,
					 * must also implement MessageReceiver.
					 */
					assert false;
				}
			} else {
				Log.debugMessage("Object: " + object + "is not a MessageReceiverExt; skipping", FINEST);
			}
		}

		orb.shutdown(true);
		orbThread.join();
	}

	private static List<Object> getObjects(final NamingContextExt namingCtx) throws UserException {
		List<Object> objects = null;

		final BindingListHolder bindingList = new BindingListHolder();
		namingCtx.list(Integer.MAX_VALUE, bindingList, new BindingIteratorHolder());
		for (final Binding binding : bindingList.value) {
			if (objects == null) {
				objects = new LinkedList<Object>();
			}

			final NameComponent[] path = binding.binding_name;
			final String string = namingCtx.to_string(path);
			final Object object = namingCtx.resolve(path);
			try {
				if (object._non_existent()) {
					Log.debugMessage("Object: "
							+ string + " is non-existent; skipping",
							FINEST);
					continue;
				}
			} catch (final COMM_FAILURE cf) {
				Log.debugMessage("Object: "
						+ string + " is non-existent; skipping",
						FINEST);
				continue;
			}

			if (binding.binding_type == BindingType.ncontext) {
				Log.debugMessage("Traversing into context: " + string, FINEST);
				objects.addAll(getObjects(NamingContextExtHelper.narrow(object)));
			} else {
				Log.debugMessage("Object found: " + string, FINEST);
				objects.add(object);
			}
		}

		return objects == null ? Collections.<Object>emptyList() : objects;
	}
}
