/*
 * $Id: DatabaseLinkedIdsCondition.java,v 1.1 2005/02/03 14:56:10 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/03 14:56:10 $
 * @author $Author: bob $
 * @module general_v1
 */
public class DatabaseLinkedIdsCondition extends AbstractDatabaseLinkedIdsCondition {

	private AbstractDatabaseLinkedIdsCondition	delegate;

	private static final String					INVALID_UNDERLYING_IMPLEMENTATION	= "Invalid underlying implementation: ";	//$NON-NLS-1$

	private static final String					LINKED_IDS_CONDITION_INIT			= "DatabaseLinkedIdsCondition.<init>() | "; //$NON-NLS-1$

	public DatabaseLinkedIdsCondition(LinkedIdsCondition condition) {
		super(condition);
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "") + ".DatabaseLinkedIdsConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		final String conditionClassName = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "") + ".LinkedIdsConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Class conditionClass = Class.forName(conditionClassName);
			Constructor ctor;
			ctor = Class.forName(className).getDeclaredConstructor(new Class[] { LinkedIdsCondition.class});
			ctor.setAccessible(true);
			this.delegate = (AbstractDatabaseLinkedIdsCondition) ctor.newInstance(new Object[] { condition});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
			, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ LinkedIdsCondition.class.getName(), Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
			, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
			, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
			, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
			, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
			, Log.SEVERE);
		}
	}

	protected String getColumnName() {
		return this.delegate.getColumnName();
	}

	public String getSQLQuery() throws IllegalDataException {
		return super.getSQLQuery();
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

}
