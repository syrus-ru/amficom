/*
 * $Id: DatabaseCompoundCondition.java,v 1.14.4.1.2.1 2006/04/18 17:06:09 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_AND;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_OR;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort._AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort._OR;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.14.4.1.2.1 $, $Date: 2006/04/18 17:06:09 $
 * @author $Author: arseniy $
 * @module general
 */
public final class DatabaseCompoundCondition implements DatabaseStorableObjectCondition {

	private CompoundCondition delegate;

	DatabaseCompoundCondition(final CompoundCondition delegate) {
		this.delegate = delegate;
	}

	public Short getEntityCode() {
		return this.delegate.getEntityCode();
	}

	private DatabaseStorableObjectCondition reflectDatabaseCondition(final StorableObjectCondition condition) {
		DatabaseStorableObjectCondition databaseStorableObjectCondition = null;
		final String className = condition.getClass().getName();
		final int lastPoint = className.lastIndexOf('.');
		final String dbClassName = className.substring(0, lastPoint + 1) + "Database" + className.substring(lastPoint + 1);
		try {
			final Class<?> clazz = Class.forName(dbClassName);
			final Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { condition.getClass() });
			constructor.setAccessible(true);
			databaseStorableObjectCondition = (DatabaseStorableObjectCondition) constructor.newInstance(new Object[] { condition });
		} catch (ClassNotFoundException e) {
			Log.errorMessage(e);
		} catch (SecurityException e) {
			Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			Log.errorMessage(e);
		} catch (IllegalArgumentException e) {
			Log.errorMessage(e);
		} catch (InstantiationException e) {
			Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				} else {
					assert false : message;
				}
			} else {
				Log.errorMessage(e);
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
				Log.errorMessage("ERROR: Cannot reflect database condition -- returning default");
				return FALSE_CONDITION;
			}

			final String query = databaseStorableObjectCondition.getSQLQuery();
			if (firstStep) {
				firstStep = false;
				buffer.append(OPEN_BRACKET);
				buffer.append(query);
				buffer.append(CLOSE_BRACKET);
			} else {
				switch (this.delegate.getOperation()) {
					case _AND:
						buffer.append(SQL_AND);
						buffer.append(OPEN_BRACKET);
						buffer.append(query);
						buffer.append(CLOSE_BRACKET);
						break;
					case _OR:
						buffer.append(SQL_OR);
						buffer.append(OPEN_BRACKET);
						buffer.append(query);
						buffer.append(CLOSE_BRACKET);
						break;
					default:
						Log.errorMessage("Unsupported condition sort");

				}
			}
		}
		return buffer.toString();
	}

}
