/*
 * $Id: DatabaseLinkedIdsCondition.java,v 1.9 2005/06/04 16:56:18 bass Exp $
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
 *
 * Database wrapper for LinkedIdsCondition.
 * Implementation must be have name DatabaseLinkedIdsConditionImpl, extends
 * {@link com.syrus.AMFICOM.general.AbstractDatabaseLinkedIdsCondition} and
 * have constructor
 *
 * <pre>
 * public DatabaseLinkedIdsConditionImpl(LinkedIdsCondition condition) {
 *		super(condition);
 *	}
 * </pre>
 *
 * @version $Revision: 1.9 $, $Date: 2005/06/04 16:56:18 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class DatabaseLinkedIdsCondition extends AbstractDatabaseLinkedIdsCondition {

	private AbstractDatabaseLinkedIdsCondition	delegate;

	private static final String					INVALID_UNDERLYING_IMPLEMENTATION	= "Invalid underlying implementation: ";	

	private static final String					LINKED_IDS_CONDITION_INIT			= "DatabaseLinkedIdsCondition.<init>() | ";

	public DatabaseLinkedIdsCondition(LinkedIdsCondition condition) {
		super(condition);
		final String className = "com.syrus.AMFICOM."
				+ ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "")
				+ ".DatabaseLinkedIdsConditionImpl";
		try {
			Constructor ctor;
			ctor = Class.forName(className).getDeclaredConstructor(new Class[] { LinkedIdsCondition.class});
			ctor.setAccessible(true);
			this.delegate = (AbstractDatabaseLinkedIdsCondition) ctor.newInstance(new Object[] { condition});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
			, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName(), Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
			, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
			, Log.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else		
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class "
					+ className, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException"
			, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException"
			, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException"
			, Log.SEVERE);
		}
	}	

	public String getSQLQuery() throws IllegalObjectEntityException {
		return this.delegate.getSQLQuery();
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

}
