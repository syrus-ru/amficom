/*
 * $Id: EventSourceTypeImpl.java,v 1.1 2004/06/22 12:27:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.util.*;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 * 
 * @todo make local object updating from database possible
 * @todo implement dummy object creation when no database connection
 */
public final class EventSourceTypeImpl {
	/**
	 * Value: {@value}.
	 */
	private static final String ID_CLIENT_SOURCE = "clientsource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_EQUIPMENT_SOURCE = "equipmentsource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_ISM_SOURCE = "ismsource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_KIS_SOURCE = "KISsource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_NET_SOURCE = "netsource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_SERVER_SOURCE = "serversource";

	/**
	 * Value: {@value}.
	 */
	private static final String ID_USER_SOURCE = "usersource";

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CLIENT_SOURCE = ID_CLIENT_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_EQUIPMENT_SOURCE = ID_EQUIPMENT_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ISM_SOURCE = ID_ISM_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_KIS_SOURCE = ID_KIS_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_NET_SOURCE = ID_NET_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_SERVER_SOURCE = ID_SERVER_SOURCE;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_USER_SOURCE = ID_USER_SOURCE;

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_CLIENTS = "CLIENTS";

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_EQUIPMENTS = "EQUIPMENTS";

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_ISMMAPCONTEXTS = "ISMMAPCONTEXTS";

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_MAPCONTEXTS = "MAPCONTEXTS";

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_SERVERS = "SERVERS";

	/**
	 * Value: {@value}.
	 */
	public static final String SOURCE_TABLE_NAME_USERS = "USERS";

	/**
	 * <code>VARCHAR2(64)</code>, primary key. Currently is <i>not</i> generated
	 * from any sequence and can be one of:
	 * <ul>
	 *     <li>{@link #ID_CLIENT_SOURCE ID_CLIENT_SOURCE};</li>
	 *     <li>{@link #ID_EQUIPMENT_SOURCE ID_EQUIPMENT_SOURCE};</li>
	 *     <li>{@link #ID_ISM_SOURCE ID_ISM_SOURCE};</li>
	 *     <li>{@link #ID_KIS_SOURCE ID_KIS_SOURCE};</li>
	 *     <li>{@link #ID_NET_SOURCE ID_NET_SOURCE};</li>
	 *     <li>{@link #ID_SERVER_SOURCE ID_SERVER_SOURCE};</li>
	 *     <li>{@link #ID_USER_SOURCE ID_USER_SOURCE}.</li>
	 * </ul>
	 * As <code>id</code>s must be unique, currently there are only seven preset
	 * <code>EventSourceType</code>s. The above values are actually
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
	 * <code>VARCHAR2(64)</code>. Specifies the class that specializes (extends)
	 * {@link com.syrus.AMFICOM.corba.portable.reflect.common.AbstractEventSourceImpl AbstractEventSourceImpl}.
	 * Currently can be one of:
	 * <ul>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.ClientImpl ClientImpl};</li>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.EquipmentImpl EquipmentImpl};</li>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.ISMMapContextImpl ISMMapContextImpl};</li>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.MapContextImpl MapContextImpl};</li>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.ServerImpl ServerImpl};</li>
	 *     <li>{@link com.syrus.AMFICOM.corba.portable.reflect.UserImpl UserImpl}.</li>
	 * </ul>
	 * When a new {@link EventSourceImpl EventSourceImpl} is created, this class is
	 * instantiated.
	 * 
	 * @see com.syrus.AMFICOM.corba.portable.reflect.common.AbstractEventSourceImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.ClientImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.EquipmentImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.ISMMapContextImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.MapContextImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.ServerImpl
	 * @see com.syrus.AMFICOM.corba.portable.reflect.UserImpl
	 */
	private Class sourceTableName;

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

	private static EventSourceTypeUtilities eventSourceTypeUtilities;
	
	static {
		try {
			eventSourceTypeUtilities = EventSourceTypeUtilitiesHelper.narrow(NamingContextExtHelper.narrow(JavaSoftORBUtil.getInstance().getORB().resolve_initial_references("NameService")).resolve_str("EventSourceTypeUtilities"));
		} catch (UserException ue) {
			ue.printStackTrace();
		}
	}

	EventSourceTypeImpl() {
	}

	private EventSourceTypeImpl(String id) throws DatabaseAccessException {
		this.id = id;
		codename = eventSourceTypeUtilities.getCodename(id);
		if (codename.length() == 0)
			codename = null;
		try {
			sourceTableName = Class.forName(eventSourceTypeUtilities.getSourceTableName(id));
		} catch (ClassNotFoundException cnfe) {
			;
		}
		name = eventSourceTypeUtilities.getName(id);
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

	public Class getSourceTableName() {
		return sourceTableName;
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

	public static synchronized EventSourceTypeImpl EventSourceTypeImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (EventSourceTypeImpl) (hashtable.get(id));
		return new EventSourceTypeImpl(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return eventSourceTypeUtilities.getIds();
	}

	public static EventSourceTypeImpl[] getEventSourceTypes() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			dae.printStackTrace();
			ids = new String[0];
		}
		ArrayList eventSourceTypes = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				eventSourceTypes.add(EventSourceTypeImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (EventSourceTypeImpl[]) (eventSourceTypes.toArray(new EventSourceTypeImpl[eventSourceTypes.size()]));
	}
}
