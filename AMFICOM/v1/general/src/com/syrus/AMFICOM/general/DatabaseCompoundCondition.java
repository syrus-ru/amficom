/*
 * $Id: DatabaseCompoundCondition.java,v 1.1 2005/02/03 14:56:10 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.general.corba.CompoundCondition_TransferablePackage.CompoundConditionSort;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/03 14:56:10 $
 * @author $Author: bob $
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
//		System.out.println("DatabaseCompoundCondition.reflectDatabaseCondition | dbClassName:" + dbClassName);
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
			String msg = "DatabaseStorableObjectCondition.reflectDatabaseCondition | Caught " + e.getMessage();
			throw new IllegalDataException(msg, e);
		}
		return databaseStorableObjectCondition;
	}

	public String getSQLQuery() throws IllegalDataException {
		StringBuffer buffer = new StringBuffer(StorableObjectDatabase.OPEN_BRACKET);
		StorableObjectCondition firstCondition = this.delegate.getFirstCondition();
		StorableObjectCondition secondCondition = this.delegate.getSecondCondition();
		String query1 = this.reflectDatabaseCondition(firstCondition).getSQLQuery();
		String query2 = this.reflectDatabaseCondition(secondCondition).getSQLQuery();
		switch (this.delegate.getOperation()) {
			case CompoundConditionSort._AND:
				buffer.append(query1);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
				buffer.append(StorableObjectDatabase.SQL_AND);
				buffer.append(StorableObjectDatabase.OPEN_BRACKET);
				buffer.append(query2);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
				break;
			case CompoundConditionSort._OR:
				buffer.append(query1);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
				buffer.append(StorableObjectDatabase.SQL_OR);
				buffer.append(StorableObjectDatabase.OPEN_BRACKET);
				buffer.append(query2);
				buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
				break;
			case CompoundConditionSort._XOR:
				// TODO XOR isn't implement yet
				throw new IllegalDataException("DatabaseCompoundCondition.getSQLQuery | XOR isn't implement yet");

		}
		return buffer.toString();
	}

}
