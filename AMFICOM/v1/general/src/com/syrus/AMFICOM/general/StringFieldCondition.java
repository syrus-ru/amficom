/*
 * $Id: StringFieldCondition.java,v 1.8 2005/02/07 09:06:23 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.util.Log;
import java.lang.reflect.*;
import java.util.*;

/**
 * 
 * @deprecated use {@link com.syrus.AMFICOM.general.TypicalCondition}
 * 
 * If one needs to write a <code>StringFieldCondition</code> for a certain
 * module (administration, configuration, etc.), the resulting class must meet
 * the following conditions:<ul>
 * <li>It must be a &lt;default&gt; (i. e. package visible) final class, named
 * <code>StringFieldCondition</code> and residing in the appropriate package
 * (e. g.: <code>com.syrus.AMFICOM.administration</code> for administration
 * module). Note that package/module name must be in sync with the appropriate
 * group name from {@link ObjectGroupEntities}, i. e. if group name is
 * &quot;AdministrationGroup&quot;, package name can&apos;t be
 * <code>com.syrus.AMFICOM.administrate</code> or
 * <code>com.syrus.AMFICOM.admin</code>.</li>
 * <li>It must inherit from this class (i. e.
 * {@link StringFieldCondition com.syrus.AMFICOM.general.StringFieldCondition})</li>
 * <li>It must declare <em>the only</em> <code>private</code> constructor with
 * the following code:<pre>
 * private StringFieldCondition(final String string, final Short entityCode, final StringFieldSort sort) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.string = string;
 * 	this.entityCode = entityCode;
 * 	this.sort = sort.value();
 * }</pre></li>
 * <li>It must override {@link #isConditionTrue(Object)}.</li></ul> 
 *
 * @author $Author: bob $
 * @version $Revision: 1.8 $, $Date: 2005/02/07 09:06:23 $
 * @module general_v1
 */
public class StringFieldCondition implements StorableObjectCondition {
	private static final String CREATING_A_DUMMY_CONDITION = "; creating a dummy condition..."; //$NON-NLS-1$

	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: "; //$NON-NLS-1$

	private static final String STRING_FIELD_CONDITION_INIT = "StringFieldCondition.<init>() | "; //$NON-NLS-1$

	private static final String STRING_FIELD_CONDITION_INNER_ONE_IS_CONDITION_TRUE = "StringFieldCondition$1.isConditionTrue() | "; //$NON-NLS-1$

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
		final String className = ObjectGroupEntities.getPackageName(entityCode.shortValue()) + ".StringFieldConditionImpl";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className)
				.getDeclaredConstructor(new Class[]{
					String.class,
					Short.class,
					StringFieldSort.class});
			ctor.setAccessible(true);
			this.delegate = (StringFieldCondition) ctor.newInstance(
				new Object[]{string, entityCode, sort});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ "Class " + className //$NON-NLS-1$
				+ " not found on the classpath" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
				+ className + " doesn't inherit from " //$NON-NLS-1$
				+ StringFieldCondition.class.getName()
				+ CREATING_A_DUMMY_CONDITION,
				Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
				+ className
				+ " doesn't have the constructor expected" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
				+ className + " is abstract" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ INVALID_UNDERLYING_IMPLEMENTATION
				+ "constructor throws an exception in class " //$NON-NLS-1$
				+ className + CREATING_A_DUMMY_CONDITION,
				Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ "Caught an IllegalAccessException" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ "Caught an IllegalArgumentException" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(STRING_FIELD_CONDITION_INIT
				+ "Caught a SecurityException" //$NON-NLS-1$
				+ CREATING_A_DUMMY_CONDITION,
				Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new StringFieldCondition() {
					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(
							STRING_FIELD_CONDITION_INNER_ONE_IS_CONDITION_TRUE
							+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.string = string;
				this.delegate.entityCode = entityCode;
				this.delegate.sort = sort.value();
			}
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
		//Empty constructor used by descendants only.
	}

	/**
	 * @see StorableObjectCondition#getEntityCode()
	 */
	public final Short getEntityCode() {
		return this.delegate.entityCode;
	}
	
	public final StringFieldSort getSort() {
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

	public final String toString() {
		/*
		 * If delegate is not null, this is an instance of top-level
		 * StringFieldCondition from module general (with own fields
		 * empty), otherwise this is an implementation from the
		 * corresponding module.
		 */
		return getClass().getName() + '[' + toString(this.delegate == null ? this : this.delegate) + ']';
	}

	private String sortToString(final StringFieldCondition stringFieldCondition) {
		switch (stringFieldCondition.sort) {
			case StringFieldSort._STRINGSORT_BASE:
				return "StringFieldSort.STRINGSORT_BASE"; //$NON-NLS-1$
			case StringFieldSort._STRINGSORT_INTEGER:
				return "StringFieldSort.STRINGSORT_INTEGER"; //$NON-NLS-1$
			case StringFieldSort._STRINGSORT_USERLOGIN:
				return "StringFieldSort.STRINGSORT_USERLOGIN"; //$NON-NLS-1$
			case StringFieldSort._STRINGSORT_USERNAME:
				return "StringFieldSort.STRINGSORT_USERNAME"; //$NON-NLS-1$
			default:
				return String.valueOf(stringFieldCondition.sort);
		}
	}

	private String toString(final StringFieldCondition stringFieldCondition) {
		return "string = \"" + stringFieldCondition.string + "\"; entityCode = \"" + ObjectEntities.codeToString(stringFieldCondition.entityCode) + "\"; sort = \"" + sortToString(stringFieldCondition) + '"'; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}
