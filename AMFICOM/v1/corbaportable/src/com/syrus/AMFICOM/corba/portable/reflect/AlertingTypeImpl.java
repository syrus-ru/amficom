/*
 * $Id: AlertingTypeImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.corba.portable.reflect.common.ObjectResourceImpl;
import com.syrus.util.logging.ErrorHandler;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class AlertingTypeImpl extends AlertingType {
	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EMAIL = ID_EMAIL;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_FAX = ID_FAX;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_LOG = ID_LOG;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_PAGING = ID_PAGING;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_POPUP = ID_POPUP;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SMS = ID_SMS;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_VOICE = ID_VOICE;

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_EMAIL = "По E-mail";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_FAX = "По факсу";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_LOG = "Запись в лог";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_PAGING = "По пейджеру";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_POPUP = "Всплывающим окном";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_SMS = "По SMS";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_VOICE = "голосовое";	

	/**
	 * <code>VARCHAR2(64)</code>, primary key. Currently is <i>not</i> generated
	 * from any sequence and can be one of:
	 * <ul>
	 *     <li>{@link #ID_EMAIL ID_EMAIL};</li>
	 *     <li>{@link #ID_FAX ID_FAX};</li>
	 *     <li>{@link #ID_LOG ID_LOG};</li>
	 *     <li>{@link #ID_PAGING ID_PAGING};</li>
	 *     <li>{@link #ID_POPUP ID_POPUP};</li>
	 *     <li>{@link #ID_SMS ID_SMS};</li>
	 *     <li>{@link #ID_VOICE ID_VOICE}.</li>
	 * </ul>
	 * As <code>id</code>s must be unique, currently there are only seven preset
	 * <code>AlertingType</code>s. The above values are actually
	 * {@linkplain #codename codenames}. In further releases this merged
	 * functionality will be separated between <code>id</code> and
	 * {@link #codename codename}: the further will be generated automatically
	 * from a sequence, the latter will take constant string values.
	 *
	 * @see #codename
	 */
	private Identifier id;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>. Currently, the same
	 * as {@link #id id}.
	 * 
	 * @see #id
	 */
	private String codename;

	/**
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>. Short description,
	 * currently can be one of:
	 * <ul>
	 *     <li>{@link #NAME_EMAIL NAME_EMAIL};</li>
	 *     <li>{@link #NAME_FAX NAME_FAX};</li>
	 *     <li>{@link #NAME_LOG NAME_LOG};</li>
	 *     <li>{@link #NAME_PAGING NAME_PAGING};</li>
	 *     <li>{@link #NAME_POPUP NAME_POPUP};</li>
	 *     <li>{@link #NAME_SMS NAME_SMS};</li>
	 *     <li>{@link #NAME_VOICE NAME_VOICE}.</li>
	 * </ul>
	 */
	private String name;

	/*
	 * Internal fields.
	 */

	public static final AlertingTypeImpl DEFAULT_ALERTING_TYPE;

	private static Hashtable hashtable = new Hashtable();

	private static AlertingTypeUtilities alertingTypeUtilities;

	static {
		try {
			alertingTypeUtilities
				= AlertingTypeUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("AlertingTypeUtilities"));
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

	static {
		AlertingTypeImpl alertingType;
		try {
			alertingType = AlertingTypeImpl(new IdentifierImpl(ID_POPUP));
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			alertingType = null;
		}
		DEFAULT_ALERTING_TYPE = alertingType;
	}

	AlertingTypeImpl() {
	}

	private AlertingTypeImpl(Identifier id) throws DatabaseAccessException {
		this.id = id;
		codename = alertingTypeUtilities.getCodename(id.toString());
		if (codename.length() == 0)
			codename = null;
		name = alertingTypeUtilities.getName(id.toString());
		if (name.length() == 0)
			name = null;
		hashtable.put(id.toString(), this);
	}

	public Identifier getId() {
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

	public static synchronized AlertingTypeImpl AlertingTypeImpl(Identifier id) throws DatabaseAccessException {
		if ((id == null) || (id.toString() == null) || (id.toString().length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (AlertingTypeImpl) (hashtable.get(id));
		return new AlertingTypeImpl(id);
	}

	public static Identifier[] getIds() throws DatabaseAccessException {
		String ids[] = alertingTypeUtilities.getIds();
		Identifier returnValue[] = new Identifier[ids.length];
		for (int i = 0; i < ids.length; i++)
			returnValue[i] = new IdentifierImpl(ids[i]);
		return returnValue;
	}

	public static AlertingTypeImpl[] getAlertingTypes() {
		Identifier ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new Identifier[0];
		}
		ArrayList alertingTypes = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				alertingTypes.add(AlertingTypeImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (AlertingTypeImpl[]) (alertingTypes.toArray(new AlertingTypeImpl[alertingTypes.size()]));
	}
}
