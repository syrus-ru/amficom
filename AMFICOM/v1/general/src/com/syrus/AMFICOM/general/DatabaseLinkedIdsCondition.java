/*
 * $Id: DatabaseLinkedIdsCondition.java,v 1.15.4.1.2.1 2006/04/18 17:06:09 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

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
 * @version $Revision: 1.15.4.1.2.1 $, $Date: 2006/04/18 17:06:09 $
 * @author $Author: arseniy $
 * @module general
 */
public final class DatabaseLinkedIdsCondition extends AbstractDatabaseLinkedIdsCondition {
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: ";
	private static final String LINKED_IDS_CONDITION_INIT = "DatabaseLinkedIdsCondition.<init>() | ";

	private AbstractDatabaseLinkedIdsCondition delegate;

	DatabaseLinkedIdsCondition(final LinkedIdsCondition condition) {
		super(condition);
		final String className = "com.syrus.AMFICOM."
				+ ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "")
				+ ".DatabaseLinkedIdsConditionImpl";
		try {
			final Constructor<?> ctor = Class.forName(className).getDeclaredConstructor(new Class[] { LinkedIdsCondition.class });
			ctor.setAccessible(true);
			this.delegate = (AbstractDatabaseLinkedIdsCondition) ctor.newInstance(new Object[] { condition });
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className + " not found on the classpath", Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT
					+ INVALID_UNDERLYING_IMPLEMENTATION + "class " + className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName(), Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT
					+ INVALID_UNDERLYING_IMPLEMENTATION + "class " + className + " doesn't have the constructor expected",
					Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(LINKED_IDS_CONDITION_INIT
					+ INVALID_UNDERLYING_IMPLEMENTATION + "class " + className + " is abstract", Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				} else {
					assert false : message;
				}
			} else
				Log.debugMessage(LINKED_IDS_CONDITION_INIT
						+ INVALID_UNDERLYING_IMPLEMENTATION + "constructor throws an exception in class " + className,
						Level.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException", Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException", Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugMessage(se, Level.SEVERE);
			Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException", Level.SEVERE);
		}
	}	

	public String getSQLQuery() throws IllegalObjectEntityException {
		return this.delegate.getSQLQuery();
	}

	@Override
	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

}
