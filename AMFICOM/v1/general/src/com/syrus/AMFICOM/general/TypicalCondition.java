/*
 * $Id: TypicalCondition.java,v 1.6 2005/02/04 14:12:24 bob Exp $
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
 * @todo: TODO: WARNING ! UNTESTED YET !
 * 
 * If one needs to write a implementation for <code>TypicalCondition</code>
 * for a certain module (administration, configuration, etc.), the resulting
 * class must meet the following conditions:
 * <ul>
 * <li>It must be a &lt;default&gt; (i. e. package visible) final class, named
 * <code>TypicalConditionImpl</code> and residing in the appropriate package
 * (e. g.: <code>com.syrus.AMFICOM.administration</code> for administration
 * module). Note that package/module name must be in sync with the appropriate
 * group name from {@link ObjectGroupEntities}, i. e. if group name is
 * &quot;AdministrationGroup&quot;, package name can&apos;t be
 * <code>com.syrus.AMFICOM.administrate</code> or
 * <code>com.syrus.AMFICOM.admin</code>.</li>
 * <li>It must inherit from this class (i. e.
 * {@link TypicalCondition TypicalCondition})</li>
 * <li>It must declare <em>the only</em> <code>private</code> constructor
 * with the following code:
 * 
 * <pre>
 * 
 * private TypicalConditionImpl(final int firstInt,
 * 		final int secondInt,
 * 		final OperationSort operation,
 * 		final Short entityCode,
 * 		final String key) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.firstInt = firstInt;
 * 	this.secondInt = secondInt;
 * 	this.type = TypicalSort._TYPE_NUMBER_INT;
 * 	this.operation = operation.value();
 * 	this.entityCode = entityCode;
 * 	this.key = key;
 * }
 * 
 * private TypicalConditionImpl(final long firstLong,
 * 		final long secondLong,
 * 		final OperationSort operation,
 * 		final Short entityCode,
 * 		final String key) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.firstLong = firstLong;
 * 	this.secondLong = secondLong;
 * 	this.type = TypicalSort._TYPE_NUMBER_LONG;
 * 	this.operation = operation.value();
 * 	this.entityCode = entityCode;
 * 	this.key = key;
 * }
 * 
 * private TypicalConditionImpl(final double firstDouble,
 * 		final double secondDouble,
 * 		final OperationSort operation,
 * 		final Short entityCode,
 * 		final String key) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.firstDouble = firstDouble;
 * 	this.secondDouble = secondDouble;
 * 	this.type = TypicalSort._TYPE_NUMBER_DOUBLE;
 * 	this.operation = operation.value();
 * 	this.entityCode = entityCode;
 * 	this.key = key;
 * }
 * 
 * private TypicalConditionImpl(final String value, final OperationSort operation, final Short entityCode, final String key) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.value = value;
 * 	this.type = TypicalSort._TYPE_STRING;
 * 	this.operation = operation.value();
 * 	this.entityCode = entityCode;
 * 	this.key = key;
 * }
 * 
 * private TypicalConditionImpl(final Date firstDate,
 * 		final Date secondDate,
 * 		final OperationSort operation,
 * 		final Short entityCode,
 * 		final String key) {
 * 	super(); // First line must invoke superconstructor w/o parameters.
 * 	this.value = firstDate;
 * 	this.otherValue = secondDate;
 * 	this.type = TypicalSort._TYPE_DATE;
 * 	this.operation = operation.value();
 * 	this.entityCode = entityCode;
 * 	this.key = key;
 * }
 * 
 * 
 * </pre>
 * 
 * </li>
 * 
 * <li>It must override {@link #isConditionTrue(Object)},
 * {@link #isNeedMore(List)}.</li>
 * 
 * <li>Overrided method {@link #isConditionTrue(Object)}get correspond value
 * of object using controller (wrapper) and key, and return result calculated at
 * {@link #parseCondition(Object)}</li>
 * 
 * </ul>
 * 
 * @version $Revision: 1.6 $, $Date: 2005/02/04 14:12:24 $
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

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected String			key;

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

	/**
	 * @param firstInt
	 *            left edge of range or value searching for
	 * @param secondInt
	 *            right edge of range
	 * @param operation
	 *            one of {@link OperationSort.OPERATION_EQUALS},
	 *            {@link OperationSort.OPERATION_GREAT},
	 *            {@link OperationSort.OPERATION_LESS},
	 *            {@link OperationSort.OPERATION_GREAT_EQUALS},
	 *            {@link OperationSort.OPERATION_IN_RANGE}or
	 *            {@link OperationSort.OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { int.class, int.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(firstInt),
					new Integer(secondInt), operation, entityCode, key});
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
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_INT;
				this.delegate.firstInt = firstInt;
				this.delegate.secondInt = secondInt;
				this.delegate.operation = operation.value();
			}
		}
	}

	/**
	 * @param firstLong
	 *            left edge of range or value searching for
	 * @param secondLong
	 *            right edge of range
	 * @param operation
	 *            one of {@link OperationSort.OPERATION_EQUALS},
	 *            {@link OperationSort.OPERATION_GREAT},
	 *            {@link OperationSort.OPERATION_LESS},
	 *            {@link OperationSort.OPERATION_GREAT_EQUALS},
	 *            {@link OperationSort.OPERATION_IN_RANGE}or
	 *            {@link OperationSort.OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { long.class, long.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(firstLong),
					new Long(secondLong), operation, entityCode, key});
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
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_LONG;
				this.delegate.firstLong = firstLong;
				this.delegate.secondLong = secondLong;
				this.delegate.operation = operation.value();
			}
		}
	}

	/**
	 * @param firstDouble
	 *            left edge of range or value searching for
	 * @param secondDouble
	 *            right edge of range
	 * @param operation
	 *            one of {@link OperationSort.OPERATION_EQUALS},
	 *            {@link OperationSort.OPERATION_GREAT},
	 *            {@link OperationSort.OPERATION_LESS},
	 *            {@link OperationSort.OPERATION_GREAT_EQUALS},
	 *            {@link OperationSort.OPERATION_IN_RANGE}or
	 *            {@link OperationSort.OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { double.class, double.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(firstDouble),
					new Double(secondDouble), operation, entityCode, key});
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
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_DOUBLE;
				this.delegate.firstDouble = firstDouble;
				this.delegate.secondDouble = secondDouble;
				this.delegate.operation = operation.value();
			}
		}
	}

	/**
	 * @param value
	 *            value such as substring, regexp
	 * @param operation
	 *            one of {@link OperationSort.OPERATION_EQUALS},
	 *            {@link OperationSort.OPERATION_REGEXP}or
	 *            {@link OperationSort.OPERATION_CI_REGEXP}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final String value, final OperationSort operation, final Short entityCode, final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { String.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { value, operation, entityCode, key});
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
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_STRING;
				this.delegate.value = value;
				this.delegate.otherValue = "";
				this.delegate.operation = operation.value();

			}
		}
	}

	/**
	 * @param firstDate
	 *            start date range
	 * @param secondDate
	 *            end date range or the same object as firstDate if not need (is
	 *            not NULL)
	 * @param operation
	 *            one of {@link OperationSort.OPERATION_EQUALS},
	 *            {@link OperationSort.OPERATION_GREAT},
	 *            {@link OperationSort.OPERATION_LESS},
	 *            {@link OperationSort.OPERATION_GREAT_EQUALS},
	 *            {@link OperationSort.OPERATION_IN_RANGE}or
	 *            {@link OperationSort.OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { Date.class, Date.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { firstDate, secondDate, operation,
					entityCode, key});
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
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_DATE;
				this.delegate.value = firstDate;
				this.delegate.otherValue = secondDate;
				this.delegate.operation = operation.value();

			}
		}
	}

	public TypicalCondition(TypicalCondition_Transferable transferable) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(transferable.entity_code).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		try {
			Constructor ctor;
			switch (transferable.type.value()) {
				case TypicalSort._TYPE_NUMBER_INT:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { int.class, int.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(transferable.value),
							new Integer(transferable.otherValue), transferable.operation,
							new Short(transferable.entity_code), transferable.key});

					break;
				case TypicalSort._TYPE_NUMBER_LONG:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { long.class, long.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(transferable.value),
							new Long(transferable.otherValue), new Integer(transferable.operation.value()),
							new Short(transferable.entity_code), transferable.key});

					break;
				case TypicalSort._TYPE_NUMBER_DOUBLE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { double.class, double.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(transferable.value),
							new Double(transferable.otherValue), new Integer(transferable.operation.value()),
							new Short(transferable.entity_code), transferable.key});

					break;
				case TypicalSort._TYPE_STRING:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { String.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { transferable.value,
							new Integer(transferable.operation.value()), new Short(transferable.entity_code),
							transferable.key});

					break;
				case TypicalSort._TYPE_DATE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { Date.class, Date.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] {
							new Date(Long.parseLong(transferable.value)),
							new Date(Long.parseLong(transferable.otherValue)), transferable.operation,
							new Short(transferable.entity_code), transferable.key});

					break;
				default: {
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
		return this.delegate.entityCode;
	}

	/**
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public final void setEntityCode(final Short entityCode) {
		this.delegate.entityCode = entityCode;
	}

	public String getKey() {
		return this.delegate.key;
	}

	public void setKey(final String key) {
		this.delegate.key = key;
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
		transferable.key = this.key;
		transferable.entity_code = this.entityCode.shortValue();
		transferable.type = TypicalSort.from_int(this.type);
		transferable.operation = OperationSort.from_int(this.operation);
		return transferable;
	}

	/**
	 * only for descendant, do not call it directly
	 * 
	 * @param object
	 * @return
	 */
	protected boolean parseCondition(Object object) {
		boolean result = false;
		switch (this.type) {
			case TypicalSort._TYPE_NUMBER_INT:
			case TypicalSort._TYPE_NUMBER_DOUBLE:
			case TypicalSort._TYPE_NUMBER_LONG:
				int i = 0;
				long l = 0;
				double d = 0.0;
				switch (this.type) {
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
						Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
				}

				switch (this.operation) {
					case OperationSort._OPERATION_EQUALS: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i == this.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l == this.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					case OperationSort._OPERATION_GREAT: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i > this.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = d > this.firstDouble;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l > this.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					case OperationSort._OPERATION_LESS: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i < this.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = d < this.firstDouble;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l < this.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i >= this.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (d > this.firstDouble) || Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l >= this.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					case OperationSort._OPERATION_LESS_EQUALS: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (i <= this.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (d < this.firstDouble) || Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (l <= this.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					case OperationSort._OPERATION_IN_RANGE: {
						switch (this.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								result = (this.firstInt < i && i < this.secondInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								result = (this.firstDouble < d && d < this.secondDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								result = (this.firstLong < l && l < this.secondLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.parseCondition | unknown number code " + this.type);
						}
					}
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code " + this.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_STRING:
				String v = this.value.toString();
				String o = object.toString();

				switch (this.operation) {
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
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code " + this.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_DATE:
				Date date = (Date) object;
				long t = date.getTime();
				long t1 = ((Date) this.value).getTime();
				long t2 = ((Date) this.otherValue).getTime();
				switch (this.operation) {
					case OperationSort._OPERATION_EQUALS:
						result = Math.abs(t - t1) < 1000L;
						break;
					case OperationSort._OPERATION_IN_RANGE:
						result = (t1 <= t && t <= t2);
						break;
					case OperationSort._OPERATION_GREAT:
						result = (t1 < t);
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						result = (t1 < t) || Math.abs(t - t1) < 1000L;
						break;
					case OperationSort._OPERATION_LESS:
						result = (t < t1);
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						result = (t < t1) || Math.abs(t - t1) < 1000L;
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code " + this.operation);
						break;
				}
				break;
		}
		return result;
	}

	public double getFirstDouble() {
		return this.delegate.firstDouble;
	}

	public void setFirstDouble(double firstDouble) {
		this.delegate.firstDouble = firstDouble;
	}

	public int getFirstInt() {
		return this.delegate.firstInt;
	}

	public void setFirstInt(int firstInt) {
		this.delegate.firstInt = firstInt;
	}

	public long getFirstLong() {
		return this.delegate.firstLong;
	}

	public void setFirstLong(long firstLong) {
		this.delegate.firstLong = firstLong;
	}

	public OperationSort getOperation() {
		return OperationSort.from_int(this.delegate.operation);
	}

	public void setOperation(OperationSort operation) {
		this.delegate.operation = operation.value();
	}

	public Object getOtherValue() {
		return this.delegate.otherValue;
	}

	public void setOtherValue(Object otherValue) {
		this.delegate.otherValue = otherValue;
	}

	public double getSecondDouble() {
		return this.delegate.secondDouble;
	}

	public void setSecondDouble(double secondDouble) {
		this.delegate.secondDouble = secondDouble;
	}

	public int getSecondInt() {
		return this.delegate.secondInt;
	}

	public void setSecondInt(int secondInt) {
		this.delegate.secondInt = secondInt;
	}

	public long getSecondLong() {
		return this.delegate.secondLong;
	}

	public void setSecondLong(long secondLong) {
		this.delegate.secondLong = secondLong;
	}

	public TypicalSort getType() {
		return TypicalSort.from_int(this.delegate.type);
	}

	public void setType(TypicalSort type) {
		this.delegate.type = type.value();
	}

	public Object getValue() {
		return this.delegate.value;
	}

	public void setValue(Object value) {
		this.delegate.value = value;
	}
}
