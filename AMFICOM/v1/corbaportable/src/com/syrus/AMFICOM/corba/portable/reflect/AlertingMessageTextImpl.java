/*
 * $Id: AlertingMessageTextImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class AlertingMessageTextImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>am&lt;sequence&nbsp;value&gt;</code>&quot; or
	 * &quot;<code>amesg&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>.
	 *
	 * Long description.
	 */
	private String text;

	/*
	 * External references.
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventTypeImpl#id
	 */
	private EventTypeImpl eventTypeId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see MessageTypeImpl#id
	 */
	private MessageTypeImpl messageTypeId;

	/*
	 * Internal fields.
	 */

	public static final String DEFAULT_TEXT = "";

	private static Hashtable hashtable = new Hashtable();

	private static AlertingMessageTextUtilities alertingMessageTextUtilities;

	static {
		try {
			alertingMessageTextUtilities
				= AlertingMessageTextUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("AlertingMessageTextUtilities"));
		} catch (Exception e) {
			/**
			 * @todo In the future, catch UserException and/or
			 *       InvocationTargetException separately.
			 *       In particular, when using JdbcConnection, a
			 *       UserException will be surely thrown.
			 */
			e.printStackTrace();
		}
	}

	AlertingMessageTextImpl() {
	}

	/**
	 * Constructs a local copy of the database record represented by
	 * <code>id</code>.
	 */
	private AlertingMessageTextImpl(String id) throws DatabaseAccessException {
		this.id = id;
		text = alertingMessageTextUtilities.getText(id);
		if (text.length() == 0)
			text = null;
		eventTypeId = EventTypeImpl.EventTypeImpl(alertingMessageTextUtilities.getEventTypeId(id));
		messageTypeId = MessageTypeImpl.MessageTypeImpl(alertingMessageTextUtilities.getMessageTypeId(id));
		hashtable.put(id, this);
	}

	/**
	 * Constructs both a database record and its local copy.
	 */
	private AlertingMessageTextImpl(EventTypeImpl eventTypeId, MessageTypeImpl messageTypeId) throws DatabaseAccessException {
		id = alertingMessageTextUtilities._new(DEFAULT_TEXT, eventTypeId.getId(), messageTypeId.getId());
		text = DEFAULT_TEXT;
		this.eventTypeId = eventTypeId;
		this.messageTypeId = messageTypeId;
		hashtable.put(id, this);
	}

	public String getId() {
		return id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) throws DatabaseAccessException {
		if (text == null)
			text = "";
		alertingMessageTextUtilities.setText(id, text);
		this.text = text;
	}

	public EventTypeImpl getEventTypeId() {
		return eventTypeId;
	}

	public MessageTypeImpl getMessageTypeId() {
		return messageTypeId;
	}

	public void setMessageTypeId(MessageTypeImpl messageTypeId) throws DatabaseAccessException {
		alertingMessageTextUtilities.setMessageTypeId(id, messageTypeId.getId());
		this.messageTypeId = messageTypeId;
	}

	public String toString() {
		String returnValue = getText();
		return (returnValue == null) ? "" : returnValue;
	}

	/**
	 * As this class has no field corresponding to long description, return a
	 * short one.
	 */
	public String getToolTipText() {
		return toString();
	}

	public static synchronized AlertingMessageTextImpl AlertingMessageTextImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (AlertingMessageTextImpl) (hashtable.get(id));
		return new AlertingMessageTextImpl(id);
	}

	public static synchronized AlertingMessageTextImpl AlertingMessageTextImpl(EventTypeImpl eventTypeId, MessageTypeImpl messageTypeId) throws DatabaseAccessException {
		String ids[] = alertingMessageTextUtilities.getMatchingIds2(eventTypeId.getId(), messageTypeId.getId());
		if (ids.length == 0)
			return new AlertingMessageTextImpl(eventTypeId, messageTypeId);
		return AlertingMessageTextImpl(ids[0]);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return alertingMessageTextUtilities.getIds();
	}

	public static AlertingMessageTextImpl[] getAlertingMessageTexts() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageTexts = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageTexts.add(AlertingMessageTextImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageTextImpl[]) (alertingMessageTexts.toArray(new AlertingMessageTextImpl[alertingMessageTexts.size()]));
	}

	public static String[] getMatchingIds(EventTypeImpl eventTypeId) throws DatabaseAccessException {
		if (eventTypeId == null)
			return new String[0];
		return alertingMessageTextUtilities.getMatchingIds1(eventTypeId.getId());
	}

	public static String[] getMatchingIds(EventTypeImpl eventTypeId, MessageTypeImpl messageTypeId) throws DatabaseAccessException {
		if ((eventTypeId == null) || (messageTypeId == null))
			return new String[0];
		return alertingMessageTextUtilities.getMatchingIds2(eventTypeId.getId(), messageTypeId.getId());
	}

	public static AlertingMessageTextImpl[] getMatching(EventTypeImpl eventTypeId) {
		String ids[];
		try {
			ids = getMatchingIds(eventTypeId);
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageTexts = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageTexts.add(AlertingMessageTextImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageTextImpl[]) (alertingMessageTexts.toArray(new AlertingMessageTextImpl[alertingMessageTexts.size()]));
	}

	public static AlertingMessageTextImpl[] getMatching(EventTypeImpl eventTypeId, MessageTypeImpl messageTypeId) {
		String ids[];
		try {
			ids = getMatchingIds(eventTypeId, messageTypeId);
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageTexts = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageTexts.add(AlertingMessageTextImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageTextImpl[]) (alertingMessageTexts.toArray(new AlertingMessageTextImpl[alertingMessageTexts.size()]));
	}
}
