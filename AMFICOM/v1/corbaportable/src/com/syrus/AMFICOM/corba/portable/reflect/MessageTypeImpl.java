/*
 * $Id: MessageTypeImpl.java,v 1.1 2004/06/22 12:27:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.corba.portable.reflect;

import com.syrus.AMFICOM.CORBA.Constant.MessageTypeConstants;
import com.syrus.AMFICOM.corba.portable.common.DatabaseAccessException;
import com.syrus.util.corba.JavaSoftORBUtil;
import java.util.*;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExtHelper;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 12:27:24 $
 * @author $Author: bass $
 */
public final class MessageTypeImpl {
	/**
	 * Value: {@value}.
	 */
	private static final String ID_ALARM = MessageTypeConstants.ID_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_CRITICAL = MessageTypeConstants.ID_CRITICAL;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_DIAGNOSTICS = MessageTypeConstants.ID_DIAGNOSTICS;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_ERROR = MessageTypeConstants.ID_ERROR;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_INFO = MessageTypeConstants.ID_INFO;

	/**
	 * Value: {@value}.
	 */
	private static final String ID_WARNING = MessageTypeConstants.ID_WARNING;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ALARM = ID_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_CRITICAL = ID_CRITICAL;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_DIAGNOSTICS = ID_DIAGNOSTICS;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_ERROR = ID_ERROR;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_INFO = ID_INFO;

	/**
	 * Value: {@value}.
	 */
	private static final String CODENAME_WARNING = ID_WARNING;

	/*
	 * Older idl2java compilers (Oracle 8i and 9i) do not support wstring
	 * constants -- in the form of:
	 * const wstring ws = L"\u0410\u0411\u0412\u0413";
	 */

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_ALARM = "Сигнал тревоги";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_CRITICAL = "Критическая ошибка";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_DIAGNOSTICS = "Диагностическое";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_ERROR = "Ошибка";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_INFO = "Информационное";

	/**
	 * Value: {@value}.
	 */
	private static final String NAME_WARNING = "Предупредительное";

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_ALARM = NAME_ALARM;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_CRITICAL = NAME_CRITICAL;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_DIAGNOSTICS = NAME_DIAGNOSTICS;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_ERROR = NAME_ERROR;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_INFO = NAME_INFO;

	/**
	 * Value: {@value}.
	 */
	private static final String DESCRIPTION_WARNING = NAME_WARNING;

	/**
	 * <code>VARCHAR2(64)</code>, primary key. Currently is <i>not</i> generated
	 * from any sequence and can be one of:
	 * <ul>
	 *     <li>{@link #ID_ALARM ID_ALARM};</li>
	 *     <li>{@link #ID_CRITICAL ID_CRITICAL};</li>
	 *     <li>{@link #ID_DIAGNOSTICS ID_DIAGNOSTICS};</li>
	 *     <li>{@link #ID_ERROR ID_ERROR};</li>
	 *     <li>{@link #ID_INFO ID_INFO};</li>
	 *     <li>{@link #ID_WARNING ID_WARNING}.</li>
	 * </ul>
	 * As <code>id</code>s must be unique, currently there are only six preset
	 * <code>MessageType</code>s. The above values are actually
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
	 * <code>VARCHAR2(64)</code>, can be <code>null</code>. Short description,
	 * currently can be one of:
	 * <ul>
	 *     <li>{@link #NAME_ALARM NAME_ALARM};</li>
	 *     <li>{@link #NAME_CRITICAL NAME_CRITICAL};</li>
	 *     <li>{@link #NAME_DIAGNOSTICS NAME_DIAGNOSTICS};</li>
	 *     <li>{@link #NAME_ERROR NAME_ERROR};</li>
	 *     <li>{@link #NAME_INFO NAME_INFO};</li>
	 *     <li>{@link #NAME_WARNING NAME_WARNING}.</li>
	 * </ul>
	 */
	private String name;

	/**
	 * <code>VARCHAR2(256)</code>, can be <code>null</code>. Long description,
	 * currently the same as {@link #name name}.
	 * 
	 * @see #name
	 */
	private String description;

	/*
	 * Internal fields.
	 */

	public static final MessageTypeImpl DEFAULT_MESSAGE_TYPE;

	private static Hashtable hashtable = new Hashtable();

	private static MessageTypeUtilities messageTypeUtilities;

	static {
		try {
			messageTypeUtilities = MessageTypeUtilitiesHelper.narrow(NamingContextExtHelper.narrow(JavaSoftORBUtil.getInstance().getORB().resolve_initial_references("NameService")).resolve_str("MessageTypeUtilities"));
		} catch (UserException ue) {
			ue.printStackTrace();
		}
	}

	static {
		MessageTypeImpl messageType;
		try {
			messageType = MessageTypeImpl(ID_WARNING);
		} catch (DatabaseAccessException dae) {
			dae.printStackTrace();
			messageType = null;
		}
		DEFAULT_MESSAGE_TYPE = messageType;
	}

	MessageTypeImpl() {
	}

	private MessageTypeImpl(String id) throws DatabaseAccessException {
		this.id = id;
		codename = messageTypeUtilities.getCodename(id);
		if (codename.length() == 0)
			codename = null;
		name = messageTypeUtilities.getName(id);
		if (name.length() == 0)
			name = null;
		description = messageTypeUtilities.getDescription(id);
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

	public static synchronized MessageTypeImpl MessageTypeImpl(String id) throws DatabaseAccessException {
		if ((id == null) || (id.length() == 0))
			return null;
		if (hashtable.containsKey(id))
			return (MessageTypeImpl) (hashtable.get(id));
		return new MessageTypeImpl(id);
	}

	public static String[] getIds() throws DatabaseAccessException {
		return messageTypeUtilities.getIds();
	}

	public static MessageTypeImpl[] getMessageTypes() {
		String ids[];
		try {	
			ids = getIds();
		} catch (DatabaseAccessException dae) {
			dae.printStackTrace();
			ids = new String[0];
		}
		ArrayList messageTypes = new ArrayList();
		for (int i = 0; i < ids.length; i ++)
			try {
				messageTypes.add(MessageTypeImpl(ids[i]));
			} catch (Exception e) {
				;
			}
		return (MessageTypeImpl[]) (messageTypes.toArray(new MessageTypeImpl[messageTypes.size()]));
	}
}
