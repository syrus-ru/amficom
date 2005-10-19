/*-
 * $Id: PopupNotificationEventProcessor.java,v 1.1 2005/10/19 13:39:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.leserver;

import static com.syrus.AMFICOM.eventv2.EventType.NOTIFICATION;
import static java.util.logging.Level.SEVERE;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
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
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/10/19 13:39:20 $
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

		if (notificationEvent instanceof PopupNotificationEvent) {
			@SuppressWarnings(value = {"unused"})
			final PopupNotificationEvent popupNotificationEvent = (PopupNotificationEvent) notificationEvent;

			try {
				Log.debugMessage("PopupNotificationEventProcessor.processEvent() | A new event has arrived", SEVERE);
				final ORB orb = LEServerSessionEnvironment.getInstance()
						.getLEServerServantManager()
						.getCORBAServer().getOrb();
				final NamingContextExt namingCtx = NamingContextExtHelper.narrow(orb.resolve_initial_references("NameService"));
				BindingListHolder bindingList = new BindingListHolder();
				BindingIteratorHolder bindingIteratorHolder = new BindingIteratorHolder();
				namingCtx.list(Integer.MAX_VALUE, bindingList, bindingIteratorHolder);
				for (final Binding binding : bindingList.value) {
					final NameComponent path[] = binding.binding_name;
					final BindingType bindingType = binding.binding_type;
					final Object object = namingCtx.resolve(path);
					final String string = namingCtx.to_string(path);
					Log.debugMessage("PopupNotificationEventProcessor.processEvent() | Binding type: "
							+ (bindingType == BindingType.nobject ? "object" : "context")
							+ "; object: " + object
							+ "; interface: " + object._get_interface_def()
							+ "; string rep: " + string,
							SEVERE);
				}
				Log.debugMessage("PopupNotificationEventProcessor.processEvent() | Exiting...", SEVERE);
			} catch (final Throwable t) {
				Log.debugException(t, SEVERE);
			}
		}
	}
}
