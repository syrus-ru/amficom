/*
 * $Id: DatabaseCompoundCondition.java,v 1.13 2005/10/30 15:20:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.13 $, $Date: 2005/10/30 15:20:42 $
 * @author $Author: bass $
 * @module general
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
		} catch (ClassNotFoundException e) {
			assert Log.errorMessage(e);
		} catch (SecurityException e) {
			assert Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			assert Log.errorMessage(e);
		} catch (IllegalArgumentException e) {
			assert Log.errorMessage(e);
		} catch (InstantiationException e) {
			assert Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			assert Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				assert Log.errorMessage(e);
			}
		}
		return databaseStorableObjectCondition;
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		boolean firstStep = true;
		final StringBuffer buffer = new StringBuffer();
		for (final StorableObjectCondition condition : this.delegate.getConditions()) {
			final DatabaseStorableObjectCondition databaseStorableObjectCondition = this.reflectDatabaseCondition(condition);
			if (databaseStorableObjectCondition == null) {
				assert Log.errorMessage("ERROR: Cannot reflect database condition -- returning default");
				return FALSE_CONDITION;
			}

			final String query = databaseStorableObjectCondition.getSQLQuery();
			if (firstStep) {
				firstStep = false;
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
						assert Log.errorMessage("Unsupported condition sort");

				}
			}
		}
		return buffer.toString();
	}

}
