/*
 * $Id: UserImpl.java,v 1.2 2004/09/25 18:06:32 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.corba.portable.reflect.common.*;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2004/09/25 18:06:32 $
 * @module corbaportable_v1
 */
public final class UserImpl extends AbstractEventSourceImpl {
	/**
	 * <code>VARCHAR2(64)</code>, primary key.
	 */
	private Identifier id;

	/**
	 * <code>VARVHAR2(64)</code>.
	 */
	private String type;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String login;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String name;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String operationalId;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String opertatorId;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String organizationId;

	/**
	 * <code>VARVHAR2(64)</code>, can be <code>null</code>.
	 */
	private String subscriberId;

	/*
	 * Internal fields.
	 */

	private static Hashtable hashtable = new Hashtable();

	private static UserUtilities userUtilities;

	static {
		try {
			userUtilities
				= UserUtilitiesHelper
				.narrow(ObjectResourceImpl.getObject("UserUtilities"));
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

	UserImpl() {
	}

	private UserImpl(Identifier id) throws DatabaseAccessException {
		this.id = id;
		type = userUtilities.getType(id.toString());
		if (type.length() == 0)
			type = null;
		login = userUtilities.getLogin(id.toString());
		if (login.length() == 0)
			login = null;
		name = userUtilities.getName(id.toString());
		if (name.length() == 0)
			name = null;
		operationalId = userUtilities.getOperationalId(id.toString());
		if (operationalId.length() == 0)
			operationalId = null;
		opertatorId = userUtilities.getOperatorId(id.toString());
		if (opertatorId.length() == 0)
			opertatorId = null;
		organizationId = userUtilities.getOrganizationId(id.toString());
		if (organizationId.length() == 0)
			organizationId = null;
		subscriberId = userUtilities.getSubscriberId(id.toString());
		if (subscriberId.length() == 0)
			subscriberId = null;
		hashtable.put(id, this);
	}

	/**
	 * Getter for property id.
	 *
	 * @return Value of property id.
	 */
	public Identifier getId() {
		return id;
	}
	
	/**
	 * Getter for property type.
	 *
	 * @return Value of property type.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Getter for property login.
	 *
	 * @return Value of property login.
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Getter for property name.
	 *
	 * @return Value of property name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for property operationalId.
	 *
	 * @return Value of property operationalId.
	 */
	public String getOperationalId() {
		return operationalId;
	}
	
	/**
	 * Getter for property opertatorId.
	 *
	 * @return Value of property opertatorId.
	 */
	public String getOpertatorId() {
		return opertatorId;
	}
	
	/**
	 * Getter for property organizationId.
	 *
	 * @return Value of property organizationId.
	 */
	public String getOrganizationId() {
		return organizationId;
	}
	
	/**
	 * Getter for property subscriberId.
	 *
	 * @return Value of property subscriberId.
	 */
	public String getSubscriberId() {
		return subscriberId;
	}

	public String toString() {
		String returnValue = getLogin();
		return (returnValue == null) ? "" : returnValue;
	}

	public String getToolTipText() {
		String returnValue = getName();
		return (returnValue == null) ? "" : returnValue;
	}

	public static synchronized UserImpl UserImpl(Identifier id) throws DatabaseAccessException {
		if ((id == null) || (id.toString() == null) || (id.toString().length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (UserImpl) (hashtable.get(id));
		return new UserImpl(id);
	}

	public static Identifier[] getIds() throws DatabaseAccessException {
		String ids[] = userUtilities.getIds();
		Identifier returnValue[] = new Identifier[ids.length];
		for (int i = 0; i < ids.length; i++)
			returnValue[i] = new IdentifierImpl(ids[i]);
		return returnValue;
	}

	public static UserImpl[] getUsers() {
		Identifier ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			ErrorHandler.getInstance().error(ObjectResourceImpl.unbox(dae));
			ids = new Identifier[0];
		}
		ArrayList users = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				users.add(UserImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (UserImpl[]) (users.toArray(new UserImpl[users.size()]));
	}
}
