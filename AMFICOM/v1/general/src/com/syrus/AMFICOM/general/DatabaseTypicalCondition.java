/*
 * $Id: DatabaseTypicalCondition.java,v 1.9 2005/06/21 12:43:48 bass Exp $
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
 * Database wrapper for TypicalCondition. Implementation must be have name
 * DatabaseTypicalConditionImpl, extends
 * {@link com.syrus.AMFICOM.general.AbstractDatabaseTypicalCondition} and have
 * constructor
 *
 * <pre>
 *
 * public DatabaseTypicalConditionImpl(TypicalCondition condition) {
 * 	super(condition);
 * }
 * </pre>
 *
 * @version $Revision: 1.9 $, $Date: 2005/06/21 12:43:48 $
 * @author $Author: bass $
 * @module general_v1
 */
public final class DatabaseTypicalCondition extends AbstractDatabaseTypicalCondition {

	private AbstractDatabaseTypicalCondition	delegate;

	private static final String					INVALID_UNDERLYING_IMPLEMENTATION	= "Invalid underlying implementation: ";	

	private static final String					DATABASE_TYPICAL_CONDITION_INIT		= "DatabaseTypicalCondition.<init>() | ";	

	public DatabaseTypicalCondition(TypicalCondition condition) {
		super(condition);
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "") + ".DatabaseTypicalConditionImpl";
		try {
			Constructor ctor;
			ctor = Class.forName(className).getDeclaredConstructor(new Class[] { TypicalCondition.class});
			ctor.setAccessible(true);
			this.delegate = (AbstractDatabaseTypicalCondition) ctor.newInstance(new Object[] { condition});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
			, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ LinkedIdsCondition.class.getName(), Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
			, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
			, Log.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
			, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
			, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught a SecurityException"
			, Log.SEVERE);
		}
	}

	@Override
	protected String getColumnName() throws IllegalObjectEntityException {
		return this.delegate.getColumnName();
	}

	@Override
	public String getSQLQuery() throws IllegalObjectEntityException {
		return this.delegate.getSQLQuery();
	}

	@Override
	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

}
