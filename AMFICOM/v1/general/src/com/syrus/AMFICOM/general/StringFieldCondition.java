/*
 * $Id: StringFieldCondition.java,v 1.1 2004/12/08 08:49:35 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.Log;
import java.util.*;

/**
 * @todo Describe class properties and methods that need to be overridden.
 *
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/12/08 08:49:35 $
 * @module general_v1
 */
public class StringFieldCondition implements StorableObjectCondition {
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Short entityCode;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int sort;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected String string;

	private StringFieldCondition delegate;

	public StringFieldCondition(final String string, final short entityCode) {
		this(string, new Short(entityCode));
	}

	public StringFieldCondition(final String string, final short entityCode, final StringFieldSort sort) {
		this(string, new Short(entityCode), sort);
	}

	public StringFieldCondition(final String string, final Short entityCode) {
		this(string, entityCode, StringFieldSort.STRINGSORT_BASE);
	}

	public StringFieldCondition(final String string, final Short entityCode, final StringFieldSort sort) {
		try {
			final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".StringFieldCondition";
			Class clazz = Class.forName(className);
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage("WARNING1", Log.DEBUGLEVEL05);
			this.delegate = new StringFieldCondition() {
				public boolean isConditionTrue(final Object object)
						throws ApplicationException {
					Log.debugMessage("WARNING2", Log.DEBUGLEVEL05);
					return false;
				}
			};
			this.delegate.string = string;
			this.delegate.entityCode = entityCode;
			this.delegate.sort = sort.value();
		}
	}

	public StringFieldCondition(final StringFieldCondition_Transferable stringFieldCondition) {
		this(stringFieldCondition.field_string,
			stringFieldCondition.entity_code, 
			stringFieldCondition.sort);
	}

	/**
	 * Empty constructor used by descendants only.
	 */
	protected StringFieldCondition() {
	}

	/**
	 * @see StorableObjectCondition#getEntityCode()
	 */
	public final Short getEntityCode() {
		return this.delegate.entityCode;
	}
	
	public final StringFieldSort getSort(){
		return StringFieldSort.from_int(this.delegate.sort);
	}
	
	public final String getString() {
		return this.delegate.string;
	}

	/**
	 * Creates a new object on every invocation.
	 *
	 * @see StorableObjectCondition#getTransferable()
	 */
	public final Object getTransferable() {
		return new StringFieldCondition_Transferable(
			this.delegate.entityCode.shortValue(),
			this.delegate.string,
			StringFieldSort.from_int(this.delegate.sort));
	}

	/**
	 * Must be overridden by descendants, or a {@link NullPointerException}
	 * will occur.
	 *
	 * @param object
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isConditionTrue(Object)
	 */
	public boolean isConditionTrue(final Object object)
			throws ApplicationException {
		return this.delegate.isConditionTrue(object);
	}

	/**
	 * @param list
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isNeedMore(List)
	 */
	public final boolean isNeedMore(final List list) throws ApplicationException {
		if (list != null)
			for (Iterator iterator = list.iterator(); iterator.hasNext();)
				if (isConditionTrue(iterator.next()))
					return false;
		return true;
	}

	public final void setEntityCode(final short entityCode) {
		setEntityCode(new Short(entityCode));
	}

	/**
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public final void setEntityCode(final Short entityCode) {
		this.delegate.entityCode = entityCode;
	}
	
	public final void setSort(final StringFieldSort sort){
		this.delegate.sort = sort.value();
	}
	
	public final void setString(final String string) {
		this.delegate.string = string;
	}
}
