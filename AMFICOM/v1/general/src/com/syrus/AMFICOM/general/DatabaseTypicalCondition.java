/*
 * $Id: DatabaseTypicalCondition.java,v 1.17 2005/10/31 12:30:18 bass Exp $
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
 * @version $Revision: 1.17 $, $Date: 2005/10/31 12:30:18 $
 * @author $Author: bass $
 * @module general
 */
public final class DatabaseTypicalCondition extends AbstractDatabaseTypicalCondition {

	private AbstractDatabaseTypicalCondition delegate;

	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: ";

	private static final String DATABASE_TYPICAL_CONDITION_INIT = "DatabaseTypicalCondition.<init>() | ";	

	public DatabaseTypicalCondition(TypicalCondition condition) {
		super(condition);
		final String className = "com.syrus.AMFICOM."
				+ ObjectGroupEntities.getGroupName(condition.getEntityCode().shortValue()).toLowerCase().replaceAll("group$", "")
				+ ".DatabaseTypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(new Class[] { TypicalCondition.class });
			ctor.setAccessible(true);
			this.delegate = (AbstractDatabaseTypicalCondition) ctor.newInstance(new Object[] { condition });
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Class " + className + " not found on the classpath", Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "class " + className + " doesn't inherit from " + LinkedIdsCondition.class.getName(), Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "class " + className + " doesn't have the constructor expected", Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "class " + className + " is abstract", Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				}
				else {
					assert false : message;
				}
			} else {
				Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class " + className, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException", Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugMessage(iae, Level.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException", Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugMessage(se, Level.SEVERE);
			Log.debugMessage(DATABASE_TYPICAL_CONDITION_INIT + "Caught a SecurityException", Level.SEVERE);
		}
	}

	@Override
	String getColumnName() {
		return this.delegate.getColumnName();
	}

	@Override
	protected String getLinkedColumnName() throws IllegalObjectEntityException {
		return this.delegate.getLinkedColumnName();
	}

	@Override
	protected String getLinkedTableName() throws IllegalObjectEntityException {
		return this.delegate.getLinkedTableName();
	}

	@Override
	public String getSQLQuery() throws IllegalObjectEntityException {
		return this.delegate.getSQLQuery();
	}

	@Override
	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

	/**
	 * This is not supported here and should never be.
	 *
	 * @param key
	 * @see AbstractDatabaseTypicalCondition#isKeySupported(String)
	 */
	@Override
	protected boolean isKeySupported(final String key) {
		throw new UnsupportedOperationException();
	}
}
