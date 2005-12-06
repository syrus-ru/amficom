/*-
 * $Id: TypicalCondition.java,v 1.62 2005/12/06 09:42:52 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_CI_REGEXP;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_EQUALS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_GREAT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_GREAT_EQUALS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_IN;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_IN_RANGE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_LESS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_LESS_EQUALS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_NOT_EQUALS;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_REGEXP;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort._OPERATION_SUBSTRING;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_BOOLEAN;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_DATE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_ENUM;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_NUMBER_DOUBLE;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_NUMBER_INT;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_NUMBER_LONG;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort._TYPE_STRING;

import java.util.Date;
import java.util.EnumSet;
import java.util.Set;
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
 * @version $Revision: 1.62 $, $Date: 2005/12/06 09:42:52 $
 * @author $Author: bass $
 * @module general
 */
public class TypicalCondition implements StorableObjectCondition {
	private static final long serialVersionUID = -2099200598390912964L;

	protected static final String ERROR_ENTITY_NOT_REGISTERED = "ERROR: Entity not registered for this condition -- ";
	private static final String ERROR_UNKNOWN_TYPE_CODE = "ERROR: Unknown type code: ";
	private static final String ERROR_UNKNOWN_NUMBER_CODE = "ERROR: Unknown number code: ";
	private static final String ERROR_UNKNOWN_OPERATION_CODE = "ERROR: Unknown operation code: ";

	protected int type;

	protected int operation;

	protected Object value;

	protected Object otherValue;

	private static final double PRECISION = 0.0000000001;

	protected double firstDouble;

	protected double secondDouble;

	protected long firstLong;

	protected long secondLong;

	protected int firstInt;

	protected int secondInt;

	protected Short entityCode;

	protected String key;

	public TypicalCondition(final Enum e,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(e, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * 
	 * @param e
	 *            value enum
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_IN}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final Enum e,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.fromEnum(e, operation, entityCode, key);
	}

	public TypicalCondition(final int firstInt,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstInt, firstInt, operation, Short.valueOf(entityCode), key);
	}

	public TypicalCondition(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstInt, secondInt, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * @param firstInt
	 *        left edge of range or value searching for
	 * @param secondInt
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_LESS_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE},
	 *        {@link OperationSort#OPERATION_SUBSTRING},
	 *        {@link OperationSort#OPERATION_REGEXP},
	 *        {@link OperationSort#OPERATION_CI_REGEXP},
	 *        {@link OperationSort#OPERATION_IN}
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
		this.fromIntegers(firstInt, secondInt, operation, entityCode, key);
	}

	public TypicalCondition(final long firstLong,
			final long secondLong,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstLong, secondLong, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * @param firstLong
	 *        left edge of range or value searching for
	 * @param secondLong
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_LESS_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE},
	 *        {@link OperationSort#OPERATION_SUBSTRING},
	 *        {@link OperationSort#OPERATION_REGEXP},
	 *        {@link OperationSort#OPERATION_CI_REGEXP},
	 *        {@link OperationSort#OPERATION_IN}
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
		this.fromLongs(firstLong, secondLong, operation, entityCode, key);
	}

	public TypicalCondition(final double firstDouble,
			final double secondDouble,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstDouble, secondDouble, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * @param firstDouble
	 *        left edge of range or value searching for
	 * @param secondDouble
	 *        right edge of range
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_LESS_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE},
	 *        {@link OperationSort#OPERATION_SUBSTRING},
	 *        {@link OperationSort#OPERATION_REGEXP},
	 *        {@link OperationSort#OPERATION_CI_REGEXP},
	 *        {@link OperationSort#OPERATION_IN}
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
		this.fromDoubles(firstDouble, secondDouble, operation, entityCode, key);
	}

	public TypicalCondition(final String value, final OperationSort operation, final short entityCode, final String key) {
		this(value, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * @param value
	 *            value such as substring, regexp
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_LESS_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE},
	 *        {@link OperationSort#OPERATION_SUBSTRING},
	 *        {@link OperationSort#OPERATION_REGEXP},
	 *        {@link OperationSort#OPERATION_CI_REGEXP},
	 *        {@link OperationSort#OPERATION_IN}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *            key for controller (wrapper)
	 */
	public TypicalCondition(final String value, final OperationSort operation, final Short entityCode, final String key) {
		this.fromString(value, operation, entityCode, key);
	}

	public TypicalCondition(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final short entityCode,
			final String key) {
		this(firstDate, secondDate, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * @param firstDate
	 *        start date range
	 * @param secondDate
	 *        end date range or the same object as firstDate if not need (is not
	 *        NULL)
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS},
	 *        {@link OperationSort#OPERATION_GREAT},
	 *        {@link OperationSort#OPERATION_LESS},
	 *        {@link OperationSort#OPERATION_GREAT_EQUALS},
	 *        {@link OperationSort#OPERATION_LESS_EQUALS},
	 *        {@link OperationSort#OPERATION_IN_RANGE},
	 *        {@link OperationSort#OPERATION_SUBSTRING},
	 *        {@link OperationSort#OPERATION_REGEXP},
	 *        {@link OperationSort#OPERATION_CI_REGEXP},
	 *        {@link OperationSort#OPERATION_IN}
	 * @param entityCode
	 *        code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final Date firstDate,
			final Date secondDate,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.fromDates(firstDate, secondDate, operation, entityCode, key);
	}

	public TypicalCondition(final Boolean value, final OperationSort operation, final short entityCode, final String key) {
		this(value, operation, Short.valueOf(entityCode), key);
	}

	/**
	 * 
	 * @param value
	 *            value boolean
	 * @param operation
	 *        one of {@link OperationSort#OPERATION_EQUALS},
	 *        {@link OperationSort#OPERATION_NOT_EQUALS}
	 * @param entityCode
	 *            code of searching entity
	 * @param key
	 *        key for controller (wrapper)
	 */
	public TypicalCondition(final Boolean value, final OperationSort operation, final Short entityCode, final String key) {
		this.fromBoolean(value, operation, entityCode, key);
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromIntegers(final int firstInt,
			final int secondInt,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.firstInt = firstInt;
		this.secondInt = secondInt;
		this.type = _TYPE_NUMBER_INT;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromLongs(final long firstLong, 
			final long secondLong,
			final OperationSort operation, 
			final Short entityCode,
			final String key) {
		this.firstLong = firstLong;
		this.secondLong = secondLong;
		this.type = _TYPE_NUMBER_LONG;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromDoubles(final double firstDouble,
			final double secondDouble,
			final OperationSort operation, 
			final Short entityCode,
			final String key) {
		this.firstDouble = firstDouble;
		this.secondDouble = secondDouble;
		this.type = _TYPE_NUMBER_DOUBLE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromString(final String value,
			final OperationSort operation, 
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = _TYPE_STRING;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	/**
	 * @param firstDate первое свидание
	 * @param secondDate второе свидание
	 * @param operation
	 * @param entityCode
	 * @param key
	 */
	@SuppressWarnings(value = {"hiding"})
	private void fromDates(final Date firstDate,
				final Date secondDate,
				final OperationSort operation,
				final Short entityCode,
				final String key) {
		this.value = firstDate;
		this.otherValue = secondDate;
		this.type = _TYPE_DATE;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromBoolean(final Boolean value,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = value;
		this.type = _TYPE_BOOLEAN;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	@SuppressWarnings(value = {"hiding"})
	private void fromEnum(final Enum e,
			final OperationSort operation,
			final Short entityCode,
			final String key) {
		this.value = e;
		this.type = _TYPE_ENUM;
		this.operation = operation.value();
		this.entityCode = entityCode;
		this.key = key;
	}

	public TypicalCondition(final IdlTypicalCondition transferable) {
		switch (transferable.sort.value()) {
			case _TYPE_NUMBER_INT:
				this.fromIntegers(Integer.parseInt(transferable.value),
						Integer.parseInt(transferable.otherValue),
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_NUMBER_LONG:
				this.fromLongs(Long.parseLong(transferable.value),
						Long.parseLong(transferable.otherValue),
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_NUMBER_DOUBLE:
				this.fromDoubles(Double.parseDouble(transferable.value),
						Double.parseDouble(transferable.otherValue),
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_STRING:
				this.fromString(transferable.value,
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_DATE:
				this.fromDates(new Date(Long.parseLong(transferable.value)),
						new Date(Long.parseLong(transferable.otherValue)),
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_BOOLEAN:
				this.fromBoolean(Boolean.valueOf(transferable.value),
						transferable.operation,
						Short.valueOf(transferable.entityCode),
						transferable.key);
				break;
			case _TYPE_ENUM:
				try {
					@SuppressWarnings(value = {"unchecked"})
					final Enum e = EnumUtil.valueOf(
							(Class<? extends Enum>) Class.forName(transferable.otherValue),
							Integer.parseInt(transferable.value));
					this.fromEnum(e,
							transferable.operation,
							Short.valueOf(transferable.entityCode),
							transferable.key);
				} catch (final ClassNotFoundException cnfe) {
					throw new IllegalArgumentException("Illegal class name: "
							+ transferable.otherValue);
				}
				break;
			default:
				throw new IllegalArgumentException("TypicalSort unknown; try to recompile IDL interfaces");
		}
	}

	/**
	 * @param storableObject
	 * @see StorableObjectCondition#isConditionTrue(StorableObject)
	 */
	public final boolean isConditionTrue(final StorableObject storableObject) {
		return this.parseCondition(storableObject.getValue(this.key));
	}

	/**
	 * @param identifiables
	 * @see StorableObjectCondition#isNeedMore(Set)
	 */
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		return true;
	}

	/**
	 * @see StorableObjectCondition#getEntityCode()
	 */
	public final Short getEntityCode() {
		return this.entityCode;
	}

	/**
	 * @see StorableObjectCondition#setEntityCode(Short)
	 */
	public final void setEntityCode(final Short entityCode) {
		this.entityCode = entityCode;
	}

	public final String getKey() {
		return this.key;
	}

	public final void setKey(final String key) {
		this.key = key;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	public final IdlStorableObjectCondition getIdlTransferable(final ORB orb) {
		return this.getIdlTransferable();
	}

	public final IdlStorableObjectCondition getIdlTransferable() {
		final IdlTypicalCondition transferable = new IdlTypicalCondition();
		switch (this.type) {
			case _TYPE_NUMBER_INT:
				transferable.value = Integer.toString(this.firstInt);
				transferable.otherValue = Integer.toString(this.secondInt);
				break;
			case _TYPE_NUMBER_DOUBLE:
				transferable.value = Double.toString(this.firstDouble);
				transferable.otherValue = Double.toString(this.secondDouble);
				break;
			case _TYPE_NUMBER_LONG:
				transferable.value = Long.toString(this.firstLong);
				transferable.otherValue = Long.toString(this.secondLong);
				break;
			case _TYPE_STRING:
				/*
				 * Fall through.
				 */
			case _TYPE_BOOLEAN:
				transferable.value = this.value.toString();
				transferable.otherValue = "";
				break;
			case _TYPE_DATE:
				transferable.value = Long.toString(((Date) this.value).getTime());
				transferable.otherValue = Long.toString(((Date) this.otherValue).getTime());
				break;
			case _TYPE_ENUM:
				transferable.value = Integer.toString(EnumUtil.getCode((Enum) this.value));
				transferable.otherValue = this.value.getClass().getName();
				break;
			default:
				Log.errorMessage(ERROR_UNKNOWN_TYPE_CODE + this.type);
				break;
		}
		transferable.key = this.key;
		transferable.entityCode = this.entityCode.shortValue();
		transferable.sort = this.getType();
		transferable.operation = this.getOperation();

		final IdlStorableObjectCondition condition = new IdlStorableObjectCondition();
		condition.typicalCondition(transferable);
		return condition;
	}

	/**
	 * only for descendant, do not call it directly
	 *
	 * @param object
	 */
	private boolean parseCondition(final Object object) {
		boolean result = false;
		switch (this.type) {
			case _TYPE_NUMBER_INT:
			case _TYPE_NUMBER_DOUBLE:
			case _TYPE_NUMBER_LONG:
				int i = 0;
				long l = 0;
				double d = 0.0;
				switch (this.type) {
					case _TYPE_NUMBER_INT:
						if (object instanceof Integer) {
							i = ((Integer) object).intValue();
						} else {
							i = Integer.parseInt(object.toString());
						}
						break;
					case _TYPE_NUMBER_DOUBLE:
						if (object instanceof Double) {
							d = ((Double) object).doubleValue();
						} else {
							d = Double.parseDouble(object.toString());
						}
						break;
					case _TYPE_NUMBER_LONG:
						if (object instanceof Long) {
							l = ((Long) object).longValue();
						} else {
							l = Long.parseLong(object.toString());
						}
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
				}

				switch (this.operation) {
					case _OPERATION_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i == this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case _TYPE_NUMBER_LONG:
								result = (l == this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_NOT_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i != this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = Math.abs(d - this.firstDouble) >= PRECISION;
								break;
							case _TYPE_NUMBER_LONG:
								result = (l != this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_GREAT:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i > this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = (d > this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								result = (l > this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_LESS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i < this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = (d < this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								result = (l < this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_GREAT_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i >= this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = (d > this.firstDouble) || Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case _TYPE_NUMBER_LONG:
								result = (l >= this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_LESS_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (i <= this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = (d < this.firstDouble) || Math.abs(d - this.firstDouble) < PRECISION;
								break;
							case _TYPE_NUMBER_LONG:
								result = (l <= this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					case _OPERATION_IN_RANGE:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								result = (this.firstInt < i && i < this.secondInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								result = (this.firstDouble < d && d < this.secondDouble);
								break;
							case _TYPE_NUMBER_LONG:
								result = (this.firstLong < l && l < this.secondLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
						}
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_STRING:
				String v = this.value.toString();
				String o = object.toString();

				switch (this.operation) {
					case _OPERATION_EQUALS:
						result = v.equals(o);
						break;
					case _OPERATION_NOT_EQUALS:
						result = !v.equals(o);
						break;
					case _OPERATION_SUBSTRING:
						final String vLowerCase = v.toLowerCase();
						final String oLowerCase = o.toLowerCase();
						result = oLowerCase.indexOf(vLowerCase) > -1;
						break;
					case _OPERATION_REGEXP:
						result = o.matches(v);
						break;
					case _OPERATION_CI_REGEXP:
						Pattern p = Pattern.compile(v, Pattern.CASE_INSENSITIVE);
						Matcher m = p.matcher(o);
						result = m.matches();
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_DATE:
				Date date = (Date) object;
				long t = date.getTime();
				long t1 = ((Date) this.value).getTime();
				long t2 = ((Date) this.otherValue).getTime();
				switch (this.operation) {
					case _OPERATION_EQUALS:
						result = Math.abs(t - t1) < 1000L;
						break;
					case _OPERATION_NOT_EQUALS:
						result = Math.abs(t - t1) >= 1000L;
						break;
					case _OPERATION_IN_RANGE:
						result = (t1 <= t && t <= t2);
						break;
					case _OPERATION_GREAT:
						result = (t1 < t);
						break;
					case _OPERATION_GREAT_EQUALS:
						result = (t1 < t) || Math.abs(t - t1) < 1000L;
						break;
					case _OPERATION_LESS:
						result = (t < t1);
						break;
					case _OPERATION_LESS_EQUALS:
						result = (t < t1) || Math.abs(t - t1) < 1000L;
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_BOOLEAN:
				switch (this.operation) {
					case _OPERATION_EQUALS:
						result = ((Boolean) this.value).booleanValue() == ((Boolean) object).booleanValue();
						break;
					case _OPERATION_NOT_EQUALS:
						result = ((Boolean) this.value).booleanValue() != ((Boolean) object).booleanValue();
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_ENUM:
				switch (this.operation) {
					case _OPERATION_EQUALS:
						result = (this.value == object);
						break;
					case _OPERATION_NOT_EQUALS:
						result = (this.value != object);
						break;
					case _OPERATION_IN:
						final EnumSet enumSet = (EnumSet) object;
						result = enumSet.contains(this.value);
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;

		}
		return result;
	}

	public final double getFirstDouble() {
		return this.firstDouble;
	}

	public final void setFirstDouble(final double firstDouble) {
		this.firstDouble = firstDouble;
	}

	public final int getFirstInt() {
		return this.firstInt;
	}

	public final void setFirstInt(final int firstInt) {
		this.firstInt = firstInt;
	}

	public final long getFirstLong() {
		return this.firstLong;
	}

	public final void setFirstLong(final long firstLong) {
		this.firstLong = firstLong;
	}

	public final OperationSort getOperation() {
		return OperationSort.from_int(this.operation);
	}

	public final void setOperation(final OperationSort operation) {
		this.operation = operation.value();
	}

	public final Object getOtherValue() {
		return this.otherValue;
	}

	public final void setOtherValue(final Object otherValue) {
		this.otherValue = otherValue;
	}

	public final double getSecondDouble() {
		return this.secondDouble;
	}

	public final void setSecondDouble(final double secondDouble) {
		this.secondDouble = secondDouble;
	}

	public final int getSecondInt() {
		return this.secondInt;
	}

	public final void setSecondInt(final int secondInt) {
		this.secondInt = secondInt;
	}

	public final long getSecondLong() {
		return this.secondLong;
	}

	public final void setSecondLong(final long secondLong) {
		this.secondLong = secondLong;
	}

	public final TypicalSort getType() {
		return TypicalSort.from_int(this.type);
	}

	public final void setType(final TypicalSort sort) {
		this.type = sort.value();
	}

	public final Object getValue() {
		return this.value;
	}

	public final void setValue(final Object value) {
		this.value = value;
	}
	
	@Override
	public final String toString() {
		final StringBuffer buffer = new StringBuffer(TypicalCondition.class.getSimpleName());
		buffer.append(", field: ");
		buffer.append(this.key);
		switch (this.type) {
			case _TYPE_NUMBER_INT:
			case _TYPE_NUMBER_DOUBLE:
			case _TYPE_NUMBER_LONG:
				switch (this.operation) {
					case _OPERATION_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] == ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] == ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] == ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_NOT_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] != ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] != ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] != ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_GREAT:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] > ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] > ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] > ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_LESS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] < ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] < ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] < ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_GREAT_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] >= ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] >= ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] >= ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_LESS_EQUALS:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] <= ");
								buffer.append(this.firstInt);
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] <= ");
								buffer.append(this.firstDouble);
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] <= ");
								buffer.append(this.firstLong);
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					case _OPERATION_IN_RANGE:
						switch (this.type) {
							case _TYPE_NUMBER_INT:
								buffer.append(" [int] in (");
								buffer.append(this.firstInt);
								buffer.append(", ");
								buffer.append(this.secondInt);
								buffer.append(")");
								break;
							case _TYPE_NUMBER_DOUBLE:
								buffer.append(" [double] in (");
								buffer.append(this.firstDouble);
								buffer.append(", ");
								buffer.append(this.secondDouble);
								buffer.append(")");
								break;
							case _TYPE_NUMBER_LONG:
								buffer.append(" [long] in (");
								buffer.append(this.firstLong);
								buffer.append(", ");
								buffer.append(this.secondLong);
								buffer.append(")");
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE + this.type);
								break;
						}
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_STRING:
				final String v = this.value.toString();
				buffer.append(" [string] ");
				switch (this.operation) {
					case _OPERATION_EQUALS:
						buffer.append("equals ");
						break;
					case _OPERATION_NOT_EQUALS:
						buffer.append("not equals ");
						break;
					case _OPERATION_SUBSTRING:
						buffer.append("is substring of ");
						break;
					case _OPERATION_REGEXP:
						buffer.append("is match regexp ");
						break;
					case _OPERATION_CI_REGEXP:
						buffer.append("is match case insencetive regexp ");
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				buffer.append(APOSTROPHE);
				buffer.append(v);
				buffer.append(APOSTROPHE);
				break;
			case _TYPE_DATE:
				buffer.append(" [date] ");
				switch (this.operation) {
					case _OPERATION_IN_RANGE:
						buffer.append(" between ");
						buffer.append(this.value);
						buffer.append(" and ");
						buffer.append(this.otherValue);
						break;
					case _OPERATION_EQUALS:
						buffer.append(" equals ");
						buffer.append(this.value);
						break;
					case _OPERATION_NOT_EQUALS:
						buffer.append(" not equals ");
						buffer.append(this.value);
						break;
					case _OPERATION_GREAT:
						buffer.append(" after ");
						buffer.append(this.value);
						break;
					case _OPERATION_GREAT_EQUALS:
						buffer.append(" after or equals ");
						buffer.append(this.value);
						break;
					case _OPERATION_LESS:
						buffer.append(" before ");
						buffer.append(this.value);
						break;
					case _OPERATION_LESS_EQUALS:
						buffer.append(" before or equals ");
						buffer.append(this.value);
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_BOOLEAN:
				switch (this.operation) {
					case _OPERATION_EQUALS:
						buffer.append(" [boolean] is ");
						buffer.append(this.value);
						break;
					case _OPERATION_NOT_EQUALS:
						buffer.append(" [boolean] is not ");
						buffer.append(this.value);
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			case _TYPE_ENUM:
				switch (this.operation) {
					case _OPERATION_EQUALS:
						buffer.append(" [enum] is ");
						buffer.append(this.value);
						break;
					case _OPERATION_NOT_EQUALS:
						buffer.append(" [enum] not is ");
						buffer.append(this.value);
						break;
					case _OPERATION_IN:
						buffer.append(" [enum] is in ");
						buffer.append(this.value);
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE + this.operation);
						break;
				}
				break;
			default:
				Log.errorMessage(ERROR_UNKNOWN_TYPE_CODE + this.type);
		}
		return buffer.toString();
	}
}
