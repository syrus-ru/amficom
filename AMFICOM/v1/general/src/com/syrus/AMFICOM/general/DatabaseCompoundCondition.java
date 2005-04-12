/*
 * $Id: DatabaseCompoundCondition.java,v 1.6 2005/04/12 16:48:32 arseniy Exp $
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
import com.syrus.util.Log;

/**
 * @version $Revision: 1.6 $, $Date: 2005/04/12 16:48:32 $
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

	private DatabaseStorableObjectCondition reflectDatabaseCondition(StorableObjectCondition condition) {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		String className = condition.getClass().getName();
		int lastPoint = className.lastIndexOf('.');
		String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		try {
			Class clazz = Class.forName(dbClassName);
			Constructor constructor = clazz.getConstructor(new Class[] {condition.getClass()});
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] {condition});
		}
		catch (ClassNotFoundException e) {
			Log.errorException(e);
		}
		catch (SecurityException e) {
			Log.errorException(e);
		}
		catch (NoSuchMethodException e) {
			Log.errorException(e);
		}
		catch (IllegalArgumentException e) {
			Log.errorException(e);
		}
		catch (InstantiationException e) {
			Log.errorException(e);
		}
		catch (IllegalAccessException e) {
			Log.errorException(e);
		}
		catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			}
			else {
				Log.errorException(e);
			}
		}
		return databaseStorableObjectCondition;
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		boolean firstStep = true;
		StringBuffer buffer = new StringBuffer();
		for (Iterator it = this.delegate.getConditions().iterator(); it.hasNext();) {
			final StorableObjectCondition condition = (StorableObjectCondition) it.next();
			final DatabaseStorableObjectCondition databaseStorableObjectCondition = this.reflectDatabaseCondition(condition);
			if (databaseStorableObjectCondition == null)
				return TRUE_CONDITION;

			final String query = databaseStorableObjectCondition.getSQLQuery();
			if (firstStep) {
				firstStep = false;
				buffer.append(StorableObjectDatabase.OPEN_BRACKET);
				buffer.append(query);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
			}
			else {
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
						Log.errorMessage("DatabaseCompoundCondition.getSQLQuery | Unsupported condition sort");

				}
			}
		}
		return buffer.toString();
	}

}
