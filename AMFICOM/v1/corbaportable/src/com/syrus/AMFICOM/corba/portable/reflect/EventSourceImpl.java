/*
 * $Id: EventSourceImpl.java,v 1.1 2004/06/22 12:27:24 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.common.AbstractEventSourceImpl;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.util.*;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public final class EventSourceImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>server</code>&quot; or
	 * &quot;<code>esrc&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>.
	 *
	 * Short description.
	 */
	private String objectSourceName;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>.
	 *
	 * Long description.
	 */
	private String description;

	/*
	 * External references.
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see AbstractEventSourceImpl#id
	 */
	private AbstractEventSourceImpl objectSourceId;
	
	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see EventSourceTypeImpl#id
	 */
	private EventSourceTypeImpl typeId;

	/*
	 * Internal fields.
	 */

	private static Hashtable hashtable = new Hashtable();

	private static EventSourceUtilities eventSourceUtilities;

	static {
		try {
			eventSourceUtilities = EventSourceUtilitiesHelper.narrow(NamingContextExtHelper.narrow(JavaSoftORBUtil.getInstance().getORB().resolve_initial_references("NameService")).resolve_str("EventSourceUtilities"));
		} catch (UserException ue) {
			ue.printStackTrace();
		}
	}

	EventSourceImpl() {
	}

	private EventSourceImpl(String id) throws DatabaseAccessException {
		this.id = id;
		objectSourceName = eventSourceUtilities.getObjectSourceName(id);
		if (objectSourceName.length() == 0)
			objectSourceName = null;
		description = eventSourceUtilities.getDescription(id);
		if (description.length() == 0)
			description = null;
		/**
		 * @todo Invoke the appropriate constructor.
		 */
//		objectSourceId = eventSourceUtilities.getObjectSourceId(id);
		typeId = EventSourceTypeImpl.EventSourceTypeImpl(eventSourceUtilities.getTypeId(id));
		hashtable.put(id, this);
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public String getObjectSourceName() {
		return objectSourceName;
	}

	public AbstractEventSourceImpl getObjectSourceId() {
		return objectSourceId;
	}

	public EventSourceTypeImpl getTypeId() {
		return typeId;
	}

	public String toString() {
		String returnValue = getObjectSourceName();
		return (returnValue == null) ? "" : returnValue;
	}

	public String getToolTipText() {
		String returnValue = getDescription();
		return (returnValue == null) ? "" : returnValue;
	}

	public static synchronized EventSourceImpl EventSourceImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (EventSourceImpl) (hashtable.get(id));
		return new EventSourceImpl(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return eventSourceUtilities.getIds();
	}

	public static EventSourceImpl[] getEventSources() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			dae.printStackTrace();
			ids = new String[0];
		}
		ArrayList eventSources = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				eventSources.add(EventSourceImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (EventSourceImpl[]) (eventSources.toArray(new EventSourceImpl[eventSources.size()]));
	}

	public static String[] getMatchingIds(EventSourceTypeImpl eventSourceType) throws DatabaseAccessException {
		if (eventSourceType == null)
			return new String[0];
		return eventSourceUtilities.getMatchingIds(eventSourceType.getId());
	}

	public static EventSourceImpl[] getMatching(EventSourceTypeImpl eventSourceType) {
		String ids[];
		try {
			ids = getMatchingIds(eventSourceType);
		} catch (DatabaseAccessException dae) {
			dae.printStackTrace();
			ids = new String[0];
		}
		ArrayList eventSources = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				eventSources.add(EventSourceImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (EventSourceImpl[]) (eventSources.toArray(new EventSourceImpl[eventSources.size()]));
	}
}
