/*
 * $Id: AlertingMessageUserLinkImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class AlertingMessageUserLinkImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 *
	 * Is generated from a sequence and has the form
	 * &quot;<code>amu&lt;sequence&nbsp;value&gt;</code>&quot; or
	 * &quot;<code>sau&lt;sequence&nbsp;value&gt;</code>&quot;.
	 */
	private String id;

	/*
	 * External references.
	 **************************************************************************/

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see AlertingMessageTextImpl#id
	 */
	private AlertingMessageTextImpl alertingMessageId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see AlertingTypeImpl#id
	 */
	private AlertingTypeImpl alertingTypeId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key, can be <code>null</code>.
	 *
	 * @see EventSourceImpl#id
	 */
	private EventSourceImpl sourceId;

	/**
	 * <code>VARCHAR2(64)</code>, foreign key.
	 *
	 * @see UserImpl#id
	 */
	private UserImpl userId;

	/*
	 * Internal fields.
	 */

	private static Hashtable hashtable = new Hashtable();

	private static AlertingMessageUserLinkUtilities alertingMessageUserLinkUtilities;

	static {
		try {
			alertingMessageUserLinkUtilities
				= AlertingMessageUserLinkUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("AlertingMessageUserLinkUtilities"));
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

	AlertingMessageUserLinkImpl() {
	}

	/**
	 * Constructs a local copy of the database record represented by
	 * <code>id</code>.
	 */
	private AlertingMessageUserLinkImpl(String id) throws DatabaseAccessException {
		this.id = id;
		alertingMessageId = AlertingMessageTextImpl.AlertingMessageTextImpl(alertingMessageUserLinkUtilities.getAlertingMessageId(id));
		alertingTypeId = AlertingTypeImpl.AlertingTypeImpl(new IdentifierImpl(alertingMessageUserLinkUtilities.getAlertingTypeId(id)));
		sourceId = EventSourceImpl.EventSourceImpl(alertingMessageUserLinkUtilities.getSourceId(id));
		userId = UserImpl.UserImpl(new IdentifierImpl(alertingMessageUserLinkUtilities.getUserId(id)));
		hashtable.put(id, this);
	}

	/**
	 * Constructs both a database record and its local copy.
	 */
	private AlertingMessageUserLinkImpl(AlertingMessageTextImpl alertingMessageId, AlertingTypeImpl alertingTypeId, EventSourceImpl sourceId, UserImpl userId) throws DatabaseAccessException {
		id = alertingMessageUserLinkUtilities._new(alertingMessageId.getId(), alertingTypeId.getId().toString(), sourceId.getId(), userId.getId().toString());
		this.alertingMessageId = alertingMessageId;
		this.alertingTypeId = alertingTypeId;
		this.sourceId = sourceId;
		this.userId = userId;
		hashtable.put(id, this);
	}

	public String getId() {
		return id;
	}

	public AlertingMessageTextImpl getAlertingMessageId() {
		return alertingMessageId;
	}

	public void setAlertingMessageId(AlertingMessageTextImpl alertingMessageId) throws DatabaseAccessException {
		alertingMessageUserLinkUtilities.setAlertingMessageId(id, alertingMessageId.getId());
		this.alertingMessageId = alertingMessageId;
	}

	public AlertingTypeImpl getAlertingTypeId() {
		return alertingTypeId;
	}

	public void setAlertingTypeId(AlertingTypeImpl alertingTypeId) throws DatabaseAccessException {
		alertingMessageUserLinkUtilities.setAlertingTypeId(id, alertingTypeId.getId().toString());
		this.alertingTypeId = alertingTypeId;
	}

	public EventSourceImpl getSourceId() {
		return sourceId;
	}

	public UserImpl getUserId() {
		return userId;
	}

	public String toString() {
		return "";
	}

	/**
	 * As this class has no field corresponding to long description, return a
	 * short one.
	 */
	public String getToolTipText() {
		return toString();
	}

	public static synchronized AlertingMessageUserLinkImpl AlertingMessageUserLinkImpl(String id) throws  DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (AlertingMessageUserLinkImpl) (hashtable.get(id));
		return new AlertingMessageUserLinkImpl(id);
	}

	public static synchronized AlertingMessageUserLinkImpl AlertingMessageUserLinkImpl(AlertingMessageTextImpl alertingMessageId, AlertingTypeImpl alertingTypeId, EventSourceImpl sourceId, UserImpl userId) throws DatabaseAccessException {
		String ids[] = alertingMessageUserLinkUtilities.getMatchingIds3(alertingMessageId.getId(), alertingTypeId.getId().toString(), sourceId.getId(), userId.getId().toString());
		if (ids.length == 0)
			return new AlertingMessageUserLinkImpl(alertingMessageId, alertingTypeId, sourceId, userId);
		return AlertingMessageUserLinkImpl(ids[0]);
	}

	public static synchronized void delete(String id) throws DatabaseAccessException {
		AlertingMessageUserLinkImpl alertingMessageUserLink = AlertingMessageUserLinkImpl(id);
		/*
		 * Delete objects dependent on one to be deleted.
		 */
		String ids[] = AlertingImpl.getMatchingIds(alertingMessageUserLink);
		for (int i = 0; i < ids.length; i ++)
			AlertingImpl.delete(ids[i]);

		/**
		 * @todo Remove orphaned alertingMessageUserLinks
		 */

		/*
		 * Delete the object itself.
		 */
		alertingMessageUserLink.id = null;
		alertingMessageUserLink.alertingMessageId = null;
		alertingMessageUserLink.alertingTypeId = null;
		alertingMessageUserLink.sourceId = null;
		alertingMessageUserLink.userId = null;
		hashtable.remove(id);
		alertingMessageUserLinkUtilities.delete(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return alertingMessageUserLinkUtilities.getIds();
	}

	public static AlertingMessageUserLinkImpl[] getAlertingMessageUserLinks() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageUserLinks = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageUserLinks.add(AlertingMessageUserLinkImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageUserLinkImpl[]) (alertingMessageUserLinks.toArray(new AlertingMessageUserLinkImpl[alertingMessageUserLinks.size()]));
	}

	public static String[] getMatchingIds(AlertingMessageTextImpl alertingMessageId, EventSourceImpl sourceId) throws DatabaseAccessException {
		if ((alertingMessageId == null) || (sourceId == null))
			return new String[0];
		return alertingMessageUserLinkUtilities.getMatchingIds1(alertingMessageId.getId(), sourceId.getId());
	}

	public static String[] getMatchingIds(AlertingMessageTextImpl alertingMessageId, EventSourceImpl sourceId, UserImpl userId) throws DatabaseAccessException {
		if ((alertingMessageId == null) || (sourceId == null) || (userId == null))
			return new String[0];
		return alertingMessageUserLinkUtilities.getMatchingIds2(alertingMessageId.getId(), sourceId.getId(), userId.getId().toString());
	}

	public static String[] getMatchingIds(AlertingMessageTextImpl alertingMessageId, AlertingTypeImpl alertingTypeId, EventSourceImpl sourceId, UserImpl userId) throws DatabaseAccessException {
		if ((alertingMessageId == null) || (alertingTypeId == null) || (sourceId == null) || (userId == null))
			return new String[0];
		return alertingMessageUserLinkUtilities.getMatchingIds3(alertingMessageId.getId(), alertingTypeId.getId().toString(), sourceId.getId(), userId.getId().toString());
	}

	public static AlertingMessageUserLinkImpl[] getMatching(AlertingMessageTextImpl alertingMessageId, EventSourceImpl sourceId) {
		String ids[];
		try {	
			ids = getMatchingIds(alertingMessageId, sourceId);
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageUserLinks = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageUserLinks.add(AlertingMessageUserLinkImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageUserLinkImpl[]) (alertingMessageUserLinks.toArray(new AlertingMessageUserLinkImpl[alertingMessageUserLinks.size()]));
	}

	public static AlertingMessageUserLinkImpl[] getMatching(AlertingMessageTextImpl alertingMessageId, EventSourceImpl sourceId, UserImpl userId) {
		String ids[];
		try {
			ids = getMatchingIds(alertingMessageId, sourceId, userId);
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageUserLinks = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageUserLinks.add(AlertingMessageUserLinkImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageUserLinkImpl[]) (alertingMessageUserLinks.toArray(new AlertingMessageUserLinkImpl[alertingMessageUserLinks.size()]));
	}

	public static AlertingMessageUserLinkImpl[] getMatching(AlertingMessageTextImpl alertingMessageId, AlertingTypeImpl alertingTypeId, EventSourceImpl sourceId, UserImpl userId) {
		String ids[];
		try {
			ids = getMatchingIds(alertingMessageId, alertingTypeId, sourceId, userId);
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alertingMessageUserLinks = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingMessageUserLinks.add(AlertingMessageUserLinkImpl(ids[i]));
			} catch (Exception e) {
			}
		return (AlertingMessageUserLinkImpl[]) (alertingMessageUserLinks.toArray(new AlertingMessageUserLinkImpl[alertingMessageUserLinks.size()]));
	}
}
