/*
 * $Id: AlarmTypeImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.CORBA.Constant.AlarmTypeConstants;
import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class AlarmTypeImpl {
	/**
	 * Value: {@value}.
	 */
	private static final String ID_RTU_HW_ALARM = AlarmTypeConstants.ID_RTU_HW_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_RTU_SW_ALARM = AlarmTypeConstants.ID_RTU_SW_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_RTU_TEST_ALARM = AlarmTypeConstants.ID_RTU_TEST_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_RTU_TEST_WARNING = AlarmTypeConstants.ID_RTU_TEST_WARNING;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_RTU_HW_ALARM = ID_RTU_HW_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_RTU_SW_ALARM = ID_RTU_SW_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_RTU_TEST_ALARM = ID_RTU_TEST_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_RTU_TEST_WARNING = ID_RTU_TEST_WARNING;

	/**
	 * <code>VARCHAR2(64)</code>, primary key. Currently is <i>not</i> generated
	 * from any sequence and can be one of:
	 * <ul>
	 *     <li>{@link #ID_RTU_HW_ALARM ID_RTU_HW_ALARM};</li>
	 *     <li>{@link #ID_RTU_SW_ALARM ID_RTU_SW_ALARM};</li>
	 *     <li>{@link #ID_RTU_TEST_ALARM ID_RTU_TEST_ALARM};</li>
	 *     <li>{@link #ID_RTU_TEST_WARNING ID_RTU_TEST_WARNING}.</li>
	 * </ul>
	 * As <code>id</code>s must be unique, currently there are only four preset
	 * <code>AlarmType</code>s. The above values are actually
	 * {@linkplain #codename codenames}. In further releases this merged
	 * functionality will be separated between <code>id</code> and
	 * {@link #codename codename}: the further will be generated automatically
	 * from a sequence, the latter will take constant string values.
	 *
	 * @see #codename
	 */
	private String id;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>. Currently, the same
	 * as {@link #id id}.
	 * 
	 * @see #id
	 */
	private String codename;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>. Short description.
	 */
	private String name;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>. Long description.
	 */
	private String description;

	/*
	 * Internal fields.
	 */

	private static Hashtable hashtable = new Hashtable();

	private static AlarmTypeUtilities alarmTypeUtilities;

	static {
		try {
			alarmTypeUtilities
				= AlarmTypeUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("AlarmTypeUtilities"));
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

	AlarmTypeImpl() {
	}

	private AlarmTypeImpl(String id) throws DatabaseAccessException {
		this.id = id;
		codename = alarmTypeUtilities.getCodename(id);
		if (codename.length() == 0)
			codename = null;
		name = alarmTypeUtilities.getName(id);
		if (name.length() == 0)
			name = null;
		description = alarmTypeUtilities.getDescription(id);
		if (description.length() == 0)
			description = null;
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

	public String getDescription() {
		return description;
	}

	public String toString() {
		String returnValue = getName();
		return (returnValue == null) ? "" : returnValue;
	}

	public String getToolTipText() {
		String returnValue = getDescription();
		return (returnValue == null) ? "" : returnValue;
	}

	public static synchronized AlarmTypeImpl AlarmTypeImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (AlarmTypeImpl) (hashtable.get(id));
		return new AlarmTypeImpl(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return alarmTypeUtilities.getIds();
	}

	public static AlarmTypeImpl[] getAlarmTypes() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new String[0];
		}
		ArrayList alarmTypes = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alarmTypes.add(AlarmTypeImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (AlarmTypeImpl[]) (alarmTypes.toArray(new AlarmTypeImpl[alarmTypes.size()]));
	}
}
