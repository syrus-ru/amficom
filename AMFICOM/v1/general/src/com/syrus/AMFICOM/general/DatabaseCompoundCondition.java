/*
 * $Id: DatabaseCompoundCondition.java,v 1.5 2005/04/02 15:38:17 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.5 $, $Date: 2005/04/02 15:38:17 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class DatabaseCompoundCondition implements DatabaseStorableObjectCondition {

	private CompoundCondition	delegate;

	public DatabaseCompoundCondition(CompoundCondition delegate) {
		this.delegate = delegate;
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

	private DatabaseStorableObjectCondition reflectDatabaseCondition(StorableObjectCondition condition)
			throws IllegalDataException {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		String className = condition.getClass().getName();
		int lastPoint = className.lastIndexOf('.');
		String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		try {
			Class clazz = Class.forName(dbClassName);
			Constructor constructor = clazz.getConstructor(new Class[] { condition.getClass()});
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor
					.newInstance(new Object[] { condition});
		} catch (ClassNotFoundException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Class " + dbClassName //$NON-NLS-1$
					+ " not found on the classpath";
			throw new IllegalDataException(msg, e);
		} catch (SecurityException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		} catch (NoSuchMethodException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Class  " + dbClassName
					+ " haven't constructor (" + className + ")";
			throw new IllegalDataException(msg, e);
		} catch (IllegalArgumentException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Class  " + dbClassName
					+ " haven't constructor (" + className + ")";
			throw new IllegalDataException(msg, e);
		} catch (InstantiationException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		} catch (IllegalAccessException e) {
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Caught " + e.getMessage();
				throw new IllegalDataException(msg, e);
			}
		}
		return databaseStorableObjectCondition;
	}

	public String getSQLQuery() throws IllegalDataException {
		boolean firstCondition = true;
		StringBuffer buffer = new StringBuffer();
		for (Iterator it = this.delegate.getConditions().iterator(); it.hasNext();) {
			StorableObjectCondition condition = (StorableObjectCondition) it.next();
			String query = this.reflectDatabaseCondition(condition).getSQLQuery();
			if (firstCondition) {
				firstCondition = false;
				buffer.append(StorableObjectDatabase.OPEN_BRACKET);
				buffer.append(query);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
			} else {
				switch (this.delegate.getOperation()) {
					case CompoundConditionSort._AND:
						buffer.append(StorableObjectDatabase.SQL_AND);
						buffer.append(StorableObjectDatabase.OPEN_BRACKET);
						buffer.append(query);
						buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						break;
					case CompoundConditionSort._OR:
						buffer.append(StorableObjectDatabase.SQL_OR);
						buffer.append(StorableObjectDatabase.OPEN_BRACKET);
						buffer.append(query);
						buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
						break;
					default:
						throw new IllegalDataException(
														"DatabaseCompoundCondition.getSQLQuery | Unsupported condition sort");

				}
			}
		}
		return buffer.toString();
	}

}
