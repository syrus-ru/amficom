/*
 * $Id: DatabaseLinkedIdsCondition.java,v 1.14 2005/10/30 15:20:42 bass Exp $
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
 * @version $Revision: 1.14 $, $Date: 2005/10/30 15:20:42 $
 * @author $Author: bass $
 * @module general
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
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
			, Level.WARNING);
		} catch (ClassCastException cce) {
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName(), Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
			, Level.WARNING);
		} catch (InstantiationException ie) {
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
			, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else		
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class "
					+ className, Level.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			assert Log.debugMessage(iae, Level.SEVERE);
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalAccessException"
			, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			assert Log.debugMessage(iae, Level.SEVERE);
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught an IllegalArgumentException"
			, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			assert Log.debugMessage(se, Level.SEVERE);
			assert Log.debugMessage(LINKED_IDS_CONDITION_INIT + "Caught a SecurityException"
			, Level.SEVERE);
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
