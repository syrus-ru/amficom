/*
 * $Id: TypicalCondition.java,v 1.1 2005/01/20 15:08:28 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalCondition_Transferable;
import com.syrus.AMFICOM.general.corba.TypicalSort;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/20 15:08:28 $
 * @author $Author: bob $
 * @module general_v1
 */
public class TypicalCondition implements StorableObjectCondition {

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int				type;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int				operation;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Object			value;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Object			otherValue;

	private static final double	PRECISION										= 0.0000000001;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected double			firstDouble;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected double			secondDouble;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected long				firstLong;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected long				secondLong;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int				firstInt;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int				secondInt;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Short				entityCode;

	private TypicalCondition	delegate;

	private static final String	TYPICAL_CONDITION_INIT							= "TypicalCondition.<init>() | ";					//$NON-NLS-1$
	private static final String	CREATING_A_DUMMY_CONDITION						= "; creating a dummy condition...";				//$NON-NLS-1$

	private static final String	INVALID_UNDERLYING_IMPLEMENTATION				= "Invalid underlying implementation: ";			//$NON-NLS-1$

	private static final String	TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE	= "StringFieldCondition$1.isConditionTrue() | ";	//$NON-NLS-1$

	/**
	 * Empty constructor used by descendants only.
	 */
	protected TypicalCondition() {
		// Empty constructor used by descendants only.
	}

	public TypicalCondition(final int firstInt, final int secondInt, final int operation, final Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { int.class, int.class, int.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(firstInt),
					new Integer(secondInt), new Integer(operation), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_INT;
				this.delegate.firstInt = firstInt;
				this.delegate.secondInt = secondInt;
				this.delegate.operation = operation;
			}
		}
	}

	public TypicalCondition(long firstLong, long secondLong, int operation, Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { long.class, long.class, int.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(firstLong),
					new Long(secondLong), new Integer(operation), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_LONG;
				this.delegate.firstLong = firstLong;
				this.delegate.secondLong = secondLong;
				this.delegate.operation = operation;
			}
		}
	}

	public TypicalCondition(double firstDouble, double secondDouble, int operation, Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { double.class, double.class, int.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(firstDouble),
					new Double(secondDouble), new Integer(operation), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_DOUBLE;
				this.delegate.firstDouble = firstDouble;
				this.delegate.secondDouble = secondDouble;
				this.delegate.operation = operation;
			}
		}
	}

	public TypicalCondition(String value, int operation, Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { String.class, int.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { value, new Integer(operation),
					entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_STRING;
				this.delegate.value = value;
				this.delegate.operation = operation;

			}
		}
	}

	public TypicalCondition(Date firstDate, Date secondDate, int operation, Short entityCode) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { Date.class, Date.class, int.class, Short.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { firstDate, secondDate,
					new Integer(operation), entityCode});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_DATE;
				this.delegate.value = firstDate;
				this.delegate.otherValue = secondDate;
				this.delegate.operation = operation;

			}
		}
	}

	public TypicalCondition(TypicalCondition_Transferable transferable) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(transferable.entity_code).toLowerCase().replaceAll("group$", "") + ".TypicalCondition"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor; 
			switch(transferable.type.value()) {
				case TypicalSort._TYPE_NUMBER_INT:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { int.class, int.class, int.class, Short.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(transferable.value), new Integer(transferable.otherValue),
							new Integer(transferable.operation.value()), new Short(transferable.entity_code)});
					
					break;
				case TypicalSort._TYPE_NUMBER_LONG:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { long.class, long.class, int.class, Short.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(transferable.value), new Long(transferable.otherValue),
							new Integer(transferable.operation.value()), new Short(transferable.entity_code)});
	
					break;
				case TypicalSort._TYPE_NUMBER_DOUBLE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { double.class, double.class, int.class, Short.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(transferable.value), new Double(transferable.otherValue),
							new Integer(transferable.operation.value()), new Short(transferable.entity_code)});
	
					break;
				case TypicalSort._TYPE_STRING:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { String.class, int.class, Short.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { transferable.value, 
							new Integer(transferable.operation.value()), new Short(transferable.entity_code)});
	
					break;
				case TypicalSort._TYPE_DATE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { Date.class, Date.class, int.class, Short.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Date(Long.parseLong(transferable.value)), new Date(Long.parseLong(transferable.otherValue)),
							new Integer(transferable.operation.value()), new Short(transferable.entity_code)});
	
					break;
				default:
					{
						if (this.delegate == null) {
							this.delegate = new TypicalCondition() {

								public boolean isConditionTrue(final Object object) throws ApplicationException {
									Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
											+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
										Log.WARNING);
									return false;
								}
							};
						}
					}	
			}
			
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className //$NON-NLS-1$
					+ " not found on the classpath" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't inherit from " //$NON-NLS-1$
					+ StringFieldCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " doesn't have the constructor expected" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class " //$NON-NLS-1$
					+ className + " is abstract" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (InvocationTargetException ite) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
					+ "constructor throws an exception in class " //$NON-NLS-1$
					+ className + CREATING_A_DUMMY_CONDITION, Log.WARNING);
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Log.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException" //$NON-NLS-1$
					+ CREATING_A_DUMMY_CONDITION, Log.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = new TypicalCondition() {

					public boolean isConditionTrue(final Object object) throws ApplicationException {
						Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
								+ "This is a dummy condition; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
						return false;
					}
				};

			}
		}
	}
	                        
	/**
	 * Must be overridden by descendants, or a {@link NullPointerException}will
	 * occur.
	 * 
	 * @param object
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isConditionTrue(Object)
	 */
	public boolean isConditionTrue(final Object object) throws ApplicationException {
		return this.delegate.isConditionTrue(object);
	}

	/**
	 * @param list
	 * @throws ApplicationException
	 * @see StorableObjectCondition#isNeedMore(List)
	 */
	public boolean isNeedMore(List list) throws ApplicationException {
		return this.delegate.isNeedMore(list);
	}

	/**
	 * @see StorableObjectCondition#getEntityCode()
	 */
	public final Short getEntityCode() {
		return this.delegate.delegate.entityCode;
	}

	/**
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public final void setEntityCode(final Short entityCode) {
		this.delegate.entityCode = entityCode;
	}

	public Object getTransferable() {
		TypicalCondition_Transferable transferable = new TypicalCondition_Transferable();
		switch (this.delegate.type) {
			case TypicalSort._TYPE_NUMBER_INT:
				transferable.value = Integer.toString(this.firstInt);
				transferable.otherValue = Integer.toString(this.secondInt);
				break;
			case TypicalSort._TYPE_NUMBER_DOUBLE:
				transferable.value = Double.toString(this.firstDouble);
				transferable.otherValue = Double.toString(this.secondDouble);
				break;
			case TypicalSort._TYPE_NUMBER_LONG:
				transferable.value = Long.toString(this.firstLong);
				transferable.otherValue = Long.toString(this.secondLong);
				break;
			case TypicalSort._TYPE_STRING:
				transferable.value = this.value.toString();
				transferable.otherValue = "";
				break;
			case TypicalSort._TYPE_DATE:
				transferable.value = Long.toString(((Date) this.value).getTime());
				transferable.otherValue = Long.toString(((Date) this.otherValue).getTime());
				break;
			default:
				Log.errorMessage("TypicalCondition.parseCondition | unknown type code " + this.delegate.type);
				break;

		}
		transferable.entity_code = this.entityCode.shortValue();
		transferable.type = TypicalSort.from_int(this.type);
		transferable.operation = OperationSort.from_int(this.operation);
		return transferable;
	}

	protected boolean parseCondition(Object object) {
		boolean result = false;
		switch (this.delegate.type) {
			case TypicalSort._TYPE_NUMBER_INT:
			case TypicalSort._TYPE_NUMBER_DOUBLE:
			case TypicalSort._TYPE_NUMBER_LONG:
				int i = 0;
				long l = 0;
				double d = 0.0;
				switch (this.delegate.type) {
					case TypicalSort._TYPE_NUMBER_INT: {
						if (object instanceof Integer)
							i = ((Integer) object).intValue();
						else
							i = Integer.parseInt(object.toString());
					}
						break;
					case TypicalSort._TYPE_NUMBER_DOUBLE: {
						if (object instanceof Double)
							d = ((Double) object).doubleValue();
						else
							d = Double.parseDouble(object.toString());
					}

						break;
					case TypicalSort._TYPE_NUMBER_LONG: {
						if (object instanceof Long)
							l = ((Long) object).longValue();
						else
							l = Long.parseLong(object.toString());
					}
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
								+ this.delegate.type);
				}

				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i == this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = Math.abs(d - this.delegate.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l == this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					case OperationSort._OPERATION_GREAT: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i > this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = d > this.delegate.firstDouble;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l > this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					case OperationSort._OPERATION_LESS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i < this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = d < this.delegate.firstDouble;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l < this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i >= this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (d > this.delegate.firstDouble)
										|| Math.abs(d - this.delegate.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l >= this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					case OperationSort._OPERATION_LESS_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i <= this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (d < this.delegate.firstDouble)
										|| Math.abs(d - this.delegate.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l <= this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					case OperationSort._OPERATION_IN_RANGE: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (this.delegate.firstInt < i && i < this.delegate.secondInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (this.delegate.firstDouble < d && d < this.delegate.secondDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (this.delegate.firstLong < l && l < this.delegate.secondLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code "
										+ this.delegate.type);
						}
					}
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code "
								+ this.delegate.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_STRING:
				String v = this.delegate.value.toString();
				String o = object.toString();

				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS:
						result = v.equals(o);
						break;
					case OperationSort._OPERATION_SUBSTRING:
						result = o.indexOf(v) > -1;
						break;
					case OperationSort._OPERATION_REGEXP:
						result = o.matches(v);
						break;
					case OperationSort._OPERATION_CI_REGEXP:
						Pattern p = Pattern.compile(v, Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(o);
						result = m.matches();
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code "
								+ this.delegate.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_DATE:
				Date date = (Date) object;
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS:
						result = date.equals(this.delegate.value);
						break;
					case OperationSort._OPERATION_IN_RANGE:
						long t = date.getTime();
						long t1 = ((Date) this.delegate.value).getTime();
						long t2 = ((Date) this.delegate.otherValue).getTime();
						result = (t1 <= t && t <= t2);
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code "
								+ this.delegate.operation);
						break;
				}
				break;
		}
		return result;
	}
}
