/*
 * $Id: EventTypeImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.CORBA.Constant.EventTypeConstants;
import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class EventTypeImpl {
	/**
	 * Value: {@value}.
	 */
	private static final String ID_ALARM_CHANGE_EVENT = EventTypeConstants.ID_ALARM_CHANGE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_ALARM_EVENT = EventTypeConstants.ID_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_ALERT_EVENT = EventTypeConstants.ID_ALERT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_ANALYSE_EVENT = EventTypeConstants.ID_ANALYSE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_CHANGE_EVENT = EventTypeConstants.ID_CHANGE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_CONNECT_EVENT = EventTypeConstants.ID_CONNECT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_CONNECT_PORT_EVENT = EventTypeConstants.ID_CONNECT_PORT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_DISCONNECT_EVENT = EventTypeConstants.ID_DISCONNECT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_DISCONNECT_PORT_EVENT = EventTypeConstants.ID_DISCONNECT_PORT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_EVALUATE_EVENT = EventTypeConstants.ID_EVALUATE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_EVALUATION_ALARM_EVENT = EventTypeConstants.ID_EVALUATION_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_EVALUATION_WARNING_EVENT = EventTypeConstants.ID_EVALUATION_WARNING_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_HW_ALARM_EVENT = EventTypeConstants.ID_HW_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_LOGIN_EVENT = EventTypeConstants.ID_LOGIN_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_LOGOFF_EVENT = EventTypeConstants.ID_LOGOFF_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_READ_EVENT = EventTypeConstants.ID_READ_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_RESULT_EVENT = EventTypeConstants.ID_RESULT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_SW_ALARM_EVENT = EventTypeConstants.ID_SW_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_SWITCH_OFF_EVENT = EventTypeConstants.ID_SWITCH_OFF_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_SWITCH_ON_EVENT = EventTypeConstants.ID_SWITCH_ON_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_TEST_ALARM_EVENT = EventTypeConstants.ID_TEST_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_TEST_WARNING_EVENT = EventTypeConstants.ID_TEST_WARNING_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_TIMER_EVENT = EventTypeConstants.ID_TIMER_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_WRITE_EVENT = EventTypeConstants.ID_WRITE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ALARM_CHANGE_EVENT = ID_ALARM_CHANGE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ALARM_EVENT = ID_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ALERT_EVENT = ID_ALERT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ANALYSE_EVENT = ID_ANALYSE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CHANGE_EVENT = ID_CHANGE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CONNECT_EVENT = ID_CONNECT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CONNECT_PORT_EVENT = ID_CONNECT_PORT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_DISCONNECT_EVENT = ID_DISCONNECT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_DISCONNECT_PORT_EVENT = ID_DISCONNECT_PORT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EVALUATE_EVENT = ID_EVALUATE_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EVALUATION_ALARM_EVENT = ID_EVALUATION_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EVALUATION_WARNING_EVENT = ID_EVALUATION_WARNING_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_HW_ALARM_EVENT = ID_HW_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_LOGIN_EVENT = ID_LOGIN_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_LOGOFF_EVENT = ID_LOGOFF_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_READ_EVENT = ID_READ_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_RESULT_EVENT = ID_RESULT_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SW_ALARM_EVENT = ID_SW_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SWITCH_OFF_EVENT = ID_SWITCH_OFF_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SWITCH_ON_EVENT = ID_SWITCH_ON_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_TEST_ALARM_EVENT = ID_TEST_ALARM_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_TEST_WARNING_EVENT = ID_TEST_WARNING_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_TIMER_EVENT = ID_TIMER_EVENT;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_WRITE_EVENT = ID_WRITE_EVENT;

	/**
	 * <code>VARCHAR2(64)</code>, primary key. Currently is <i>not</i> generated
	 * from any sequence and can be one of:
	 * <ul>
	 *     <li>{@link #ID_ALARM_CHANGE_EVENT ID_ALARM_CHANGE_EVENT};</li>
	 *     <li>{@link #ID_ALARM_EVENT ID_ALARM_EVENT};</li>
	 *     <li>{@link #ID_ALERT_EVENT ID_ALERT_EVENT};</li>
	 *     <li>{@link #ID_ANALYSE_EVENT ID_ANALYSE_EVENT};</li>
	 *     <li>{@link #ID_CHANGE_EVENT ID_CHANGE_EVENT};</li>
	 *     <li>{@link #ID_CONNECT_EVENT ID_CONNECT_EVENT};</li>
	 *     <li>{@link #ID_CONNECT_PORT_EVENT ID_CONNECT_PORT_EVENT};</li>
	 *     <li>{@link #ID_DISCONNECT_EVENT ID_DISCONNECT_EVENT};</li>
	 *     <li>{@link #ID_DISCONNECT_PORT_EVENT ID_DISCONNECT_PORT_EVENT};</li>
	 *     <li>{@link #ID_EVALUATE_EVENT ID_EVALUATE_EVENT};</li>
	 *     <li>{@link #ID_EVALUATION_ALARM_EVENT ID_EVALUATION_ALARM_EVENT};</li>
	 *     <li>{@link #ID_EVALUATION_WARNING_EVENT ID_EVALUATION_WARNING_EVENT};</li>
	 *     <li>{@link #ID_HW_ALARM_EVENT ID_HW_ALARM_EVENT};</li>
	 *     <li>{@link #ID_LOGIN_EVENT ID_LOGIN_EVENT};</li>
	 *     <li>{@link #ID_LOGOFF_EVENT ID_LOGOFF_EVENT};</li>
	 *     <li>{@link #ID_READ_EVENT ID_READ_EVENT};</li>
	 *     <li>{@link #ID_RESULT_EVENT ID_RESULT_EVENT};</li>
	 *     <li>{@link #ID_SW_ALARM_EVENT ID_SW_ALARM_EVENT};</li>
	 *     <li>{@link #ID_SWITCH_OFF_EVENT ID_SWITCH_OFF_EVENT};</li>
	 *     <li>{@link #ID_SWITCH_ON_EVENT ID_SWITCH_ON_EVENT};</li>
	 *     <li>{@link #ID_TEST_ALARM_EVENT ID_TEST_ALARM_EVENT};</li>
	 *     <li>{@link #ID_TEST_WARNING_EVENT ID_TEST_WARNING_EVENT};</li>
	 *     <li>{@link #ID_TIMER_EVENT ID_TIMER_EVENT};</li>
	 *     <li>{@link #ID_WRITE_EVENT ID_WRITE_EVENT}.</li>
	 * </ul>
	 * As <code>id</code>s must be unique, currently there are only twenty four
	 * preset <code>EventType</code>s. The above values are actually
	 * {@linkplain #codename codenames}. In further releases this merged
	 * functionality will be separated between <code>id</code> and
	 * {@link #codename codename}: the further will be generated automatically
	 * from a sequence, the latter will take constant string values.
	 *
	 * @see #codename
	 */
	private String id;

	/**
	 * <code>VARCHAR2(64)</code>. Currently, the same as {@link #id id}.
	 *
	 * @see #id
	 */
	private String codename;
	
	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>.
	 *
	 * Short description.
	 */
	private String name;

	/*
	 * Internal fields.
	 */

	private static Hashtable hashtable = new Hashtable();

	private static EventTypeUtilities eventTypeUtilities;

	static {
		try {
			eventTypeUtilities
				= EventTypeUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("EventTypeUtilities"));
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

	EventTypeImpl() {
	}

	private EventTypeImpl(String id) throws DatabaseAccessException {
		this.id = id;
		codename = eventTypeUtilities.getCodename(id);
		if (codename.length() == 0)
			codename = null;
		name = eventTypeUtilities.getName(id);
		if (name.length() == 0)
			name = null;
		hashtable.put(id, this);
	}

	public String getId() {
		return id;
	}

	public String getCodename() {
		return codename;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		String returnValue = getName();
		return (returnValue == null) ? "" : returnValue;
	}

	/**
	 * As this class has no field corresponding to long description, return a
	 * short one.
	 */
	public String getToolTipText() {
		return toString();
	}

	public static synchronized EventTypeImpl EventTypeImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (EventTypeImpl) (hashtable.get(id));
		return new EventTypeImpl(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return eventTypeUtilities.getIds();
	}

	public static EventTypeImpl[] getEventTypes() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList eventTypes = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				eventTypes.add(EventTypeImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (EventTypeImpl[]) (eventTypes.toArray(new EventTypeImpl[eventTypes.size()]));
	}

	public static EventTypeImpl getSelectedEventType(EventSourceImpl eventSource, AlarmTypeImpl alarmType) {
		if ((eventSource == null) || (alarmType == null))
			return null;
		try {
			return EventTypeImpl.EventTypeImpl(eventTypeUtilities.getSelectedEventType1(eventSource.getId(), alarmType.getId()));
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			return null;
		}
	}

	public static EventTypeImpl getSelectedEventType(EventSourceTypeImpl eventSourceType, AlarmTypeImpl alarmType) {
		if ((eventSourceType == null) || (alarmType == null))
			return null;
		try {
			return EventTypeImpl.EventTypeImpl(eventTypeUtilities.getSelectedEventType2(eventSourceType.getId(), alarmType.getId()));
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			return null;
		}
	}
}
