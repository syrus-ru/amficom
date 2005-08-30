/*
 * $Id: TypicalCondition.java,v 1.47 2005/08/30 19:26:59 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.EnumUtil;
import com.syrus.util.Log;

/**
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
 * <li>It must override {@link #isConditionTrue(StorableObject)},
 * {@link #isNeedMore(java.util.Set)}.</li>
 *
 * <li>Overrided method {@link #isConditionTrue(StorableObject)}get correspond value
 * of object using controller (wrapper) and key, and return result calculated at
 * {@link #parseCondition(Object)}</li>
 *
 * </ul>
 *
 * @version $Revision: 1.47 $, $Date: 2005/08/30 19:26:59 $
 * @author $Author: arseniy $
 * @module general
 */
public class TypicalCondition implements StorableObjectCondition {

	protected static final String ENTITY_NOT_REGISTERED = "Entity not registered for this condition -- ";

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int type;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int operation;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Object value;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Object otherValue;

	private static final double PRECISION = 0.0000000001;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected double firstDouble;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected double secondDouble;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected long firstLong;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected long secondLong;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int firstInt;
	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected int secondInt;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected Short entityCode;

	/**
	 * Field is used by descendants only, and never directly.
	 */
	protected String key;

	private TypicalCondition delegate;

	private static final String TYPICAL_CONDITION_INIT = "TypicalCondition.<init>() | ";
	private static final String CREATING_A_DUMMY_CONDITION = "; creating a dummy condition...";
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = "Invalid underlying implementation: ";
	private static final String TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE = "StringFieldCondition$1.isConditionTrue() | ";
	private static final String TYPICAL_CONDITION_INNER_ONE_IS_NEED_MORE = "StringFieldCondition$1.isNeedMore() | ";

	/**
	 * Empty constructor used by descendants only.
	 */
	protected TypicalCondition() {
		// Empty constructor used by descendants only.
	}

	public TypicalCondition(final Enum e,
			final OperationSort operation,
			final short entityCode,
			final String key) {

		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { Enum.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { e,
					operation,
					new Short(entityCode),
					key });
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = new Short(entityCode);
				this.delegate.type = TypicalSort._TYPE_ENUM;
				this.delegate.value = e;
				this.delegate.operation = operation.value();
			}
		}
	
	}
	
	public TypicalCondition(final int firstInt,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstInt, firstInt, operation, new Short(entityCode), key);
	}
	
	public TypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstInt, secondInt, operation, new Short(entityCode), key);
	}

	/**
	 * @param firstInt
	 *        left edge of range or value searching for
	 * @param secondInt
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE}or
	 *        {@link OperationSort#OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *        code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { int.class, int.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(firstInt),
					new Integer(secondInt), operation, entityCode, key});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_INT;
				this.delegate.firstInt = firstInt;
				this.delegate.secondInt = secondInt;
				this.delegate.operation = operation.value();
			}
		}
	}

	public TypicalCondition(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstLong, secondLong, operation, new Short(entityCode), key);
	}

	/**
	 * @param firstLong
	 *        left edge of range or value searching for
	 * @param secondLong
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE}or
	 *        {@link OperationSort#OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *        code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { long.class, long.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(firstLong),
					new Long(secondLong), operation, entityCode, key});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_LONG;
				this.delegate.firstLong = firstLong;
				this.delegate.secondLong = secondLong;
				this.delegate.operation = operation.value();
			}
		}
	}

	public TypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstDouble, secondDouble, operation, new Short(entityCode), key);
	}

	/**
	 * @param firstDouble
	 *        left edge of range or value searching for
	 * @param secondDouble
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE}or
	 *        {@link OperationSort#OPERATION_LESS_EQUALS}
	 * @param entityCode
	 *        code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { double.class, double.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(firstDouble),
					new Double(secondDouble), operation, entityCode, key});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_NUMBER_DOUBLE;
				this.delegate.firstDouble = firstDouble;
				this.delegate.secondDouble = secondDouble;
				this.delegate.operation = operation.value();
			}
		}
	}

	public TypicalCondition(final String value, final OperationSort operation, final short entityCode, final String key) {
		this(value, operation, new Short(entityCode), key);
	}

	/**
	 * @param value
	 *            value such as substring, regexp
	 * @param operation
	 *            one of {@link OperationSort#OPERATION_EQUALS},
	 *            {@link OperationSort#OPERATION_REGEXP}or
	 *            {@link OperationSort#OPERATION_CI_REGEXP}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final String value, final OperationSort operation, final Short entityCode, final String key) {
		final String className = ObjectGroupEntities.getPackageName(entityCode.shortValue()) + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(String.class, OperationSort.class, Short.class, String.class);
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(value, operation, entityCode, key);
		} catch (final ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (final IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (final IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (final SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_STRING;
				this.delegate.value = value;
				this.delegate.otherValue = "";
				this.delegate.operation = operation.value();

			}
		}
	}

	public TypicalCondition(final Date firstDate,
	            			final Date secondDate,
	            			final OperationSort operation,
	            			final short entityCode,
	            			final String key) {
		this(firstDate, secondDate, operation, new Short(entityCode), key);
	}

	/**
	 * @param firstDate
	 *            start date range
	 * @param secondDate
	 *            end date range or the same object as firstDate if not need (is
	 *            not NULL)
	 * @param operation
	 *            one of {@link OperationSort#OPERATION_EQUALS},
	 *            {@link OperationSort#OPERATION_GREAT},
	 *            {@link OperationSort#OPERATION_LESS},
	 *            {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *            {@link OperationSort#OPERATION_IN_RANGE}or
	 *            {@link OperationSort#OPERATION_LESS_EQUALS}
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
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(entityCode.shortValue()).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(
				new Class[] { Date.class, Date.class, OperationSort.class, Short.class, String.class});
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { firstDate, secondDate == null ? firstDate : secondDate, operation,
					entityCode, key});
		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_DATE;
				this.delegate.value = firstDate;
				this.delegate.otherValue = secondDate;
				this.delegate.operation = operation.value();

			}
		}
	}

	public TypicalCondition(final Boolean value, final OperationSort operation, final short entityCode, final String key) {
		this(value, operation, new Short(entityCode), key);
	}

	/**
	 * @param operation only {@link OperationSort#OPERATION_EQUALS} is
	 *        currently supported.
	 */
	public TypicalCondition(final Boolean value, final OperationSort operation, final Short entityCode, final String key) {
		final String className = ObjectGroupEntities.getPackageName(entityCode.shortValue()) + ".TypicalConditionImpl";
		try {
			final Constructor ctor = Class.forName(className).getDeclaredConstructor(Boolean.class, OperationSort.class, Short.class, String.class);
			ctor.setAccessible(true);
			this.delegate = (TypicalCondition) ctor.newInstance(value, operation, entityCode, key);
		} catch (final ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (final IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (final IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (final SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
				this.delegate.key = key;
				this.delegate.entityCode = entityCode;
				this.delegate.type = TypicalSort._TYPE_BOOLEAN;
				this.delegate.value = value;
				this.delegate.otherValue = "";
				this.delegate.operation = operation.value();

			}
		}
	}

	@SuppressWarnings("unchecked")
	public TypicalCondition(final IdlTypicalCondition transferable) {
		final String className = "com.syrus.AMFICOM." + ObjectGroupEntities.getGroupName(transferable.entityCode).toLowerCase().replaceAll("group$", "") + ".TypicalConditionImpl";
		Log.debugMessage(TYPICAL_CONDITION_INIT + "Try reflect class " + className, Level.INFO);
		try {
			Constructor ctor;
			switch (transferable.sort.value()) {
				case TypicalSort._TYPE_NUMBER_INT:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { int.class, int.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Integer(transferable.value),
							new Integer(transferable.otherValue), transferable.operation,
							new Short(transferable.entityCode), transferable.key});

					break;
				case TypicalSort._TYPE_NUMBER_LONG:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { long.class, long.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Long(transferable.value),
							new Long(transferable.otherValue), transferable.operation,
							new Short(transferable.entityCode), transferable.key});

					break;
				case TypicalSort._TYPE_NUMBER_DOUBLE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { double.class, double.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] { new Double(transferable.value),
							new Double(transferable.otherValue), transferable.operation,
							new Short(transferable.entityCode), transferable.key});

					break;
				case TypicalSort._TYPE_STRING:
					ctor = Class.forName(className).getDeclaredConstructor(
							String.class, OperationSort.class, Short.class, String.class);
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(transferable.value,
							transferable.operation, new Short(transferable.entityCode),
							transferable.key);

					break;
				case TypicalSort._TYPE_DATE:
					ctor = Class.forName(className).getDeclaredConstructor(
						new Class[] { Date.class, Date.class, OperationSort.class, Short.class, String.class});
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(new Object[] {
							new Date(Long.parseLong(transferable.value)),
							new Date(Long.parseLong(transferable.otherValue)), transferable.operation,
							new Short(transferable.entityCode), transferable.key});

					break;
				case TypicalSort._TYPE_BOOLEAN:
					ctor = Class.forName(className).getDeclaredConstructor(
							Boolean.class, OperationSort.class, Short.class, String.class);
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(Boolean.valueOf(transferable.value),
							transferable.operation, new Short(transferable.entityCode),
							transferable.key);
				case TypicalSort._TYPE_ENUM:
					ctor = Class.forName(className).getDeclaredConstructor(Enum.class, OperationSort.class, Short.class, String.class);
					ctor.setAccessible(true);
					this.delegate = (TypicalCondition) ctor.newInstance(EnumUtil.reflectFromInt((Class<? extends Enum>) Class.forName(transferable.otherValue),
							Integer.parseInt(transferable.value)),
							transferable.operation,
							new Short(transferable.entityCode),
							transferable.key);

					break;
					//XXX
				default: {
					if (this.delegate == null) {
						this.delegate = createDummyCondition();
					}
				}
			}

		} catch (ClassNotFoundException cnfe) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Class " + className
					+ " not found on the classpath"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (ClassCastException cce) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't inherit from "
					+ TypicalCondition.class.getName() + CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (NoSuchMethodException nsme) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " doesn't have the constructor expected"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InstantiationException ie) {
			Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ className + " is abstract"
					+ CREATING_A_DUMMY_CONDITION, Level.WARNING);
		} catch (InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else {
				Log.debugMessage(TYPICAL_CONDITION_INIT + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
					+ className + CREATING_A_DUMMY_CONDITION, Level.WARNING);
			}
		} catch (IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalAccessException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.debugException(iae, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught an IllegalArgumentException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} catch (SecurityException se) {
			/*
			 * Never.
			 */
			Log.debugException(se, Level.SEVERE);
			Log.debugMessage(TYPICAL_CONDITION_INIT + "Caught a SecurityException"
					+ CREATING_A_DUMMY_CONDITION, Level.SEVERE);
		} finally {
			if (this.delegate == null) {
				this.delegate = createDummyCondition();
			}
		}
	}

	private static TypicalCondition createDummyCondition() {
		return new TypicalCondition() {

			@Override
			public boolean isConditionTrue(final StorableObject storableObject) {
				Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_CONDITION_TRUE
						+ "Object: " + storableObject.toString() + "; "
						+ "This is a dummy condition; evaluation result is always false...",
					Level.WARNING);
				return false;
			}

			@Override
			public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
				Log.debugMessage(TYPICAL_CONDITION_INNER_ONE_IS_NEED_MORE
						+ "Object: " + storableObjects + "; "
						+ "This is a dummy condition; evaluation result is always false...",
					Level.WARNING);
				return false;
			}
		};
	}

	/**
	 * Must be overridden by descendants, or a {@link NullPointerException}will
	 * occur.
	 *
	 * @param storableObject
	 * @see StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		return this.delegate.isConditionTrue(storableObject);
	}

	/**
	 * @param storableObjects
	 * @see StorableObjectCondition#isNeedMore(Set)
	 * @todo Write implementation of this method
	 */
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		//return this.delegate.isNeedMore(storableObjects);
		return true;
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

	public final String getKey() {
		return this.delegate.key;
	}

	public final void setKey(final String key) {
		this.delegate.key = key;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public final IdlStorableObjectCondition getTransferable(final ORB orb) {
		return this.getTransferable();
	}

	public final IdlStorableObjectCondition getTransferable() {
		IdlTypicalCondition transferable = new IdlTypicalCondition();
		switch (this.delegate.type) {
			case TypicalSort._TYPE_NUMBER_INT:
				transferable.value = Integer.toString(this.delegate.firstInt);
				transferable.otherValue = Integer.toString(this.delegate.secondInt);
				break;
			case TypicalSort._TYPE_NUMBER_DOUBLE:
				transferable.value = Double.toString(this.delegate.firstDouble);
				transferable.otherValue = Double.toString(this.delegate.secondDouble);
				break;
			case TypicalSort._TYPE_NUMBER_LONG:
				transferable.value = Long.toString(this.delegate.firstLong);
				transferable.otherValue = Long.toString(this.delegate.secondLong);
				break;
			case TypicalSort._TYPE_STRING:
				/*
				 * Fall through.
				 */
			case TypicalSort._TYPE_BOOLEAN:
				transferable.value = this.delegate.value.toString();
				transferable.otherValue = "";
				break;
			case TypicalSort._TYPE_DATE:
				transferable.value = Long.toString(((Date) this.delegate.value).getTime());
				transferable.otherValue = Long.toString(((Date) this.delegate.otherValue).getTime());
				break;
			case TypicalSort._TYPE_ENUM:
				transferable.value = Integer.toString(EnumUtil.getCode((Enum) this.delegate.value));
				transferable.otherValue = this.delegate.value.getClass().getName();
				break;
			default:
				Log.errorMessage("TypicalCondition.parseCondition | unknown type code " + this.delegate.type);
				break;

		}
		transferable.key = this.delegate.key;
		transferable.entityCode = this.delegate.entityCode.shortValue();
		transferable.sort = TypicalSort.from_int(this.delegate.type);
		transferable.operation = OperationSort.from_int(this.delegate.operation);

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.typicalCondition(transferable);
		return condition;
	}

	/**
	 * only for descendant, do not call it directly
	 *
	 * @param object
	 */
	protected final boolean parseCondition(final Object object) {
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
			case TypicalSort._TYPE_BOOLEAN:
				switch (this.operation) {
					case OperationSort._OPERATION_EQUALS:
						result = ((Boolean) this.value).booleanValue() ==
								((Boolean) object).booleanValue();
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code " + this.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_ENUM:
				switch (this.operation) {
					case OperationSort._OPERATION_EQUALS:
						result = (this.value == object);
						break;
					case OperationSort._OPERATION_IN:
						final EnumSet enumSet = (EnumSet) object;
						result = enumSet.contains(this.value);
						break;
					default:
						Log.errorMessage("TypicalCondition.parseCondition | unknown operation code " + this.operation);
						break;
				}
				break;

		}
		return result;
	}

	public final double getFirstDouble() {
		return this.delegate.firstDouble;
	}

	public final void setFirstDouble(final double firstDouble) {
		this.delegate.firstDouble = firstDouble;
	}

	public final int getFirstInt() {
		return this.delegate.firstInt;
	}

	public final void setFirstInt(final int firstInt) {
		this.delegate.firstInt = firstInt;
	}

	public final long getFirstLong() {
		return this.delegate.firstLong;
	}

	public final void setFirstLong(final long firstLong) {
		this.delegate.firstLong = firstLong;
	}

	public final OperationSort getOperation() {
		return OperationSort.from_int(this.delegate.operation);
	}

	public final void setOperation(final OperationSort operation) {
		this.delegate.operation = operation.value();
	}

	public final Object getOtherValue() {
		return this.delegate.otherValue;
	}

	public final void setOtherValue(final Object otherValue) {
		this.delegate.otherValue = otherValue;
	}

	public final double getSecondDouble() {
		return this.delegate.secondDouble;
	}

	public final void setSecondDouble(final double secondDouble) {
		this.delegate.secondDouble = secondDouble;
	}

	public final int getSecondInt() {
		return this.delegate.secondInt;
	}

	public final void setSecondInt(final int secondInt) {
		this.delegate.secondInt = secondInt;
	}

	public final long getSecondLong() {
		return this.delegate.secondLong;
	}

	public final void setSecondLong(final long secondLong) {
		this.delegate.secondLong = secondLong;
	}

	public final TypicalSort getType() {
		return TypicalSort.from_int(this.delegate.type);
	}

	public final void setType(final TypicalSort sort) {
		this.delegate.type = sort.value();
	}

	public final Object getValue() {
		return this.delegate.value;
	}

	public final void setValue(final Object value) {
		this.delegate.value = value;
	}
	
	@Override
	public String toString() {
		final StringBuffer buffer = new StringBuffer(TypicalCondition.class.getSimpleName());
		buffer.append(", field: ");
		buffer.append(this.delegate.key);
		switch (this.delegate.type) {
			case TypicalSort._TYPE_NUMBER_INT:
			case TypicalSort._TYPE_NUMBER_DOUBLE:
			case TypicalSort._TYPE_NUMBER_LONG:
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] == ");
								buffer.append(this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] == ");
								buffer.append(this.delegate.firstDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] == ");
								buffer.append(this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					case OperationSort._OPERATION_GREAT: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] > ");
								buffer.append(this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] > ");
								buffer.append(this.delegate.firstDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] > ");
								buffer.append(this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					case OperationSort._OPERATION_LESS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] < ");
								buffer.append(this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] < ");
								buffer.append(this.delegate.firstDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] < ");
								buffer.append(this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] >= ");
								buffer.append(this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] >= ");
								buffer.append(this.delegate.firstDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] >= ");
								buffer.append(this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					case OperationSort._OPERATION_LESS_EQUALS: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] <= ");
								buffer.append(this.delegate.firstInt);
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] <= ");
								buffer.append(this.delegate.firstDouble);
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] <= ");
								buffer.append(this.delegate.firstLong);
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					case OperationSort._OPERATION_IN_RANGE: {
						switch (this.delegate.type) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(" [int] in (");
								buffer.append(this.delegate.firstInt);
								buffer.append(", ");
								buffer.append(this.delegate.secondInt);
								buffer.append(")");
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] in (");
								buffer.append(this.delegate.firstDouble);
								buffer.append(", ");
								buffer.append(this.delegate.secondDouble);
								buffer.append(")");
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(" [long] in (");
								buffer.append(this.delegate.firstLong);
								buffer.append(", ");
								buffer.append(this.delegate.secondLong);
								buffer.append(")");
								break;
							default:
								Log.errorMessage("TypicalCondition.toString | unknown number code " + this.delegate.type);
								break;
						}
					}
						break;
					default:
						Log.errorMessage("TypicalCondition.toString | unknown operation code " + this.delegate.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_STRING:
				final String v = this.delegate.value.toString();
				buffer.append(" [string] ");
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append("equals ");
						break;
					case OperationSort._OPERATION_SUBSTRING:
						buffer.append("is substring of ");
						break;
					case OperationSort._OPERATION_REGEXP:
						buffer.append("is match regexp ");
						break;
					case OperationSort._OPERATION_CI_REGEXP:
						buffer.append("is match case insencetive regexp ");
						break;
					default:
						Log.errorMessage("TypicalCondition.toString | unknown operation code " + this.delegate.operation);
						break;
				}
				buffer.append(APOSTROPHE);
				buffer.append(v);
				buffer.append(APOSTROPHE);
				break;
			case TypicalSort._TYPE_DATE:
				buffer.append(" [date] ");
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_IN_RANGE:
						buffer.append(" between ");
						buffer.append(this.delegate.value);
						buffer.append(" and ");
						buffer.append(this.delegate.otherValue);
						break;
					case OperationSort._OPERATION_EQUALS:
						buffer.append(" equals ");
						buffer.append(this.delegate.value);
						break;						
					case OperationSort._OPERATION_GREAT:
						buffer.append(" after ");
						buffer.append(this.delegate.value);
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						buffer.append(" after or equals ");
						buffer.append(this.delegate.value);
						break;
					case OperationSort._OPERATION_LESS:
						buffer.append(" before ");
						buffer.append(this.delegate.value);
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						buffer.append(" before or equals ");
						buffer.append(this.delegate.value);
						break;
					default:
						Log.errorMessage("TypicalCondition.toString | unknown operation code " + this.delegate.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_BOOLEAN:
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(" [boolean] is ");
						buffer.append(this.delegate.value);
						break;
					default:
						Log.errorMessage("TypicalCondition.toString | unknown operation code " + this.delegate.operation);
						break;
				}
				break;
			case TypicalSort._TYPE_ENUM:
				switch (this.delegate.operation) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(" [enum] is ");
						buffer.append(this.delegate.value);
						break;
					case OperationSort._OPERATION_IN:
						buffer.append(" [enum] is in ");
						buffer.append(this.delegate.value);
						break;
					default:
						Log.errorMessage("TypicalCondition.toString | unknown operation code " + this.delegate.operation);
						break;
				}
				break;
		}
		return buffer.toString();
	}
}
