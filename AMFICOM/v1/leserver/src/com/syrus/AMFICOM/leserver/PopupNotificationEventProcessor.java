/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.3 2005/10/30 14:49:11 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.FINEST;
import static java.util.logging.Level.SEVERE;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.omg.CORBA.COMM_FAILURE;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.Binding;
import org.omg.CosNaming.BindingIteratorHolder;
import org.omg.CosNaming.BindingListHolder;
import org.omg.CosNaming.BindingType;
import org.omg.CosNaming.NameComponent;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;

import com.syrus.AMFICOM.eventv2.Event;
import com.syrus.AMFICOM.eventv2.EventType;
import com.syrus.AMFICOM.eventv2.NotificationEvent;
import com.syrus.AMFICOM.eventv2.PopupNotificationEvent;
import com.syrus.AMFICOM.eventv2.corba.MessageReceiver;
import com.syrus.AMFICOM.eventv2.corba.MessageReceiverHelper;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExt;
import com.syrus.AMFICOM.leserver.corba.MessageReceiverExtHelper;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2005/10/30 14:49:11 $
 * @module leserver
 */
final class PopupNotificationEventProcessor implements EventProcessor {
	/**
	 * @see EventProcessor#getEventType()
	 */
	public EventType getEventType() {
		return NOTIFICATION;
	}

	/**
	 * @param event
	 * @throws EventProcessingException
	 * @see EventProcessor#processEvent(Event)
	 */
	public void processEvent(final Event event) throws EventProcessingException {
		final NotificationEvent notificationEvent = (NotificationEvent) event;

		if (!(notificationEvent instanceof PopupNotificationEvent)) {
			return;
		}

		final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) notificationEvent;

		try {
			Log.debugMessage("A new event has arrived", SEVERE);
			final Identifier targetUserId = popupNotificationEvent.getTargetUserId();

			final ORB orb = LEServerSessionEnvironment.getInstance()
					.getLEServerServantManager()
					.getCORBAServer().getOrb();
			final NamingContextExt namingCtx = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));

			for (final Object object : getObjects(namingCtx)) {
				try {
					if (!object._is_a(MessageReceiverExtHelper.id())) {
						Log.debugMessage("Object: " + object
								+ " is not a MessageReceiverExt; skipping",
								FINEST);
						continue;
					}
					
					Log.debugMessage("Object: " + object
							+ " is a valid MessageReceiverExt; continuing",
							FINEST);
					final MessageReceiverExt messageReceiverExt = MessageReceiverExtHelper.narrow(object);
					final Identifier loggedUserId = Identifier.valueOf(messageReceiverExt.getSystemUserId());
					if (!targetUserId.equals(loggedUserId)) {
						Log.debugMessage("Message is for: " + targetUserId
								+ "; while the user currently logged in is: "
								+ loggedUserId + "; skipping",
								FINEST);
						continue;
					}
					Log.debugMessage("Message will be delivered to: "
							+ targetUserId,
							FINEST);
					
					if (object._is_a(MessageReceiverHelper.id())) {
						final MessageReceiver messageReceiver = MessageReceiverHelper.narrow(object);
						messageReceiver.receiveMessages(popupNotificationEvent.getTransferable(orb));
					} else {
						/*
						 * Everyone who implements MessageReceiverExt,
						 * must also implement MessageReceiver.
						 */
						assert false;
					}
				} catch (final SystemException se) {
					Log.debugMessage(se, SEVERE);
				}
			}				

			Log.debugMessage("Exiting...", SEVERE);
		} catch (final Throwable t) {
			Log.debugMessage(t, SEVERE);
		}
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
