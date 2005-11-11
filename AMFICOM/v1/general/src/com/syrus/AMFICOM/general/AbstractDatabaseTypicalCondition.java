/*
* $Id: AbstractDatabaseTypicalCondition.java,v 1.23 2005/11/11 09:15:16 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.APOSTROPHE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.GREAT_THAN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.GREAT_THAN_OR_EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.LESS_THAN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.LESS_THAN_OR_EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.NOT_EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_AND;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_LIKE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_PATTERN_CHARACTERS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FUNCTION_UPPER;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.EnumUtil;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.23 $, $Date: 2005/11/11 09:15:16 $
 * @author $Author: arseniy $
 * @module general
 */
public abstract class AbstractDatabaseTypicalCondition implements DatabaseStorableObjectCondition {
	private static final String ERROR_UNKNOWN_NUMBER_CODE = "ERROR: Unknown number code: ";
	private static final String ERROR_UNKNOWN_OPERATION_CODE = "ERROR: Unknown operation code: ";

	protected TypicalCondition condition;
	
	public AbstractDatabaseTypicalCondition(final TypicalCondition delegate) {
		this.condition = delegate;
	}

	String getColumnName() {
		final String key = this.condition.getKey().intern();
		assert this.isKeySupported(key) : "Entity '"
				+ ObjectEntities.codeToString(this.condition.getEntityCode())
				+ "' and key '" + key + "' are not supported.";
		return key;
	}

	protected abstract String getLinkedThisColumnName() throws IllegalObjectEntityException;

	protected abstract String getLinkedColumnName() throws IllegalObjectEntityException;

	protected abstract String getLinkedTableName() throws IllegalObjectEntityException;

	private final String getLinkedSubQuery() throws IllegalObjectEntityException {
		switch (this.condition.getType().value()) {
			case TypicalSort._TYPE_ENUM:
				final Enum e = (Enum) this.condition.getValue();
				return StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET
						+ SQL_SELECT + this.getLinkedThisColumnName()
						+ SQL_FROM + this.getLinkedTableName()
						+ SQL_WHERE + this.getLinkedColumnName() + EQUALS + EnumUtil.getCode(e)
						+ CLOSE_BRACKET;
			default:
				Log.errorMessage("Illegal type of condition: " + this.condition.getType().value());
				return DatabaseStorableObjectCondition.FALSE_CONDITION;
		}
	}

	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}

	public String getSQLQuery() throws IllegalObjectEntityException {
		final StringBuffer buffer = new StringBuffer();
		switch (this.condition.getType().value()) {
			case TypicalSort._TYPE_ENUM:
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(EQUALS);
						buffer.append(EnumUtil.getCode((Enum) this.condition.getValue()));
						break;
					case OperationSort._OPERATION_NOT_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(NOT_EQUALS);
						buffer.append(EnumUtil.getCode((Enum) this.condition.getValue()));
						break;
					case OperationSort._OPERATION_IN:
						buffer.append(this.getLinkedSubQuery());
						break;
				}
				break;
			case TypicalSort._TYPE_NUMBER_INT:
			case TypicalSort._TYPE_NUMBER_DOUBLE:
			case TypicalSort._TYPE_NUMBER_LONG:
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(EQUALS);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_NOT_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(NOT_EQUALS);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_GREAT:
						buffer.append(this.getColumnName());
						buffer.append(GREAT_THAN);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_LESS:
						buffer.append(this.getColumnName());
						buffer.append(LESS_THAN);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(GREAT_THAN_OR_EQUALS);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						buffer.append(this.getColumnName());
						buffer.append(LESS_THAN_OR_EQUALS);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;								
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
						}
						break;
					case OperationSort._OPERATION_IN_RANGE:
						buffer.append(OPEN_BRACKET);
						buffer.append(this.getColumnName());
						buffer.append(GREAT_THAN);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
								buffer.append(Long.MIN_VALUE);
						}
						buffer.append(SQL_AND);
						buffer.append(this.getColumnName());
						buffer.append(LESS_THAN);
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getSecondInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getSecondDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getSecondLong());
								break;
							default:
								Log.errorMessage(ERROR_UNKNOWN_NUMBER_CODE
										+ this.condition.getType().value());
								buffer.append(Long.MAX_VALUE);
						}
						buffer.append(CLOSE_BRACKET);
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_STRING:
				final String v = this.condition.getValue().toString();
				int operation = this.condition.getOperation().value();
				if(operation == OperationSort._OPERATION_SUBSTRING) {
					buffer.append(SQL_FUNCTION_UPPER);
					buffer.append(OPEN_BRACKET);
					buffer.append(this.getColumnName());
					buffer.append(CLOSE_BRACKET);
				} else {
					buffer.append(this.getColumnName());
				}
				switch (operation) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(EQUALS);
						buffer.append(APOSTROPHE);
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append(APOSTROPHE);
						break;
					case OperationSort._OPERATION_NOT_EQUALS:
						buffer.append(NOT_EQUALS);
						buffer.append(APOSTROPHE);
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append(APOSTROPHE);
						break;
					case OperationSort._OPERATION_SUBSTRING:
						buffer.append(SQL_LIKE);
						buffer.append(SQL_FUNCTION_UPPER);
						buffer.append(OPEN_BRACKET);
						buffer.append(APOSTROPHE);
						buffer.append(SQL_PATTERN_CHARACTERS);
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append(SQL_PATTERN_CHARACTERS);
						buffer.append(APOSTROPHE);
						buffer.append(CLOSE_BRACKET);
						break;
					case OperationSort._OPERATION_REGEXP:
						/* TODO isn't implement */
						break;
					case OperationSort._OPERATION_CI_REGEXP:
						/* TODO isn't implement */
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_DATE:
				buffer.append(this.getColumnName());
				final Date date1 = (Date) this.condition.getValue();
				final Date date2 = (Date) this.condition.getOtherValue();
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_NOT_EQUALS:
						buffer.append(NOT_EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_IN_RANGE:
						buffer.append(GREAT_THAN_OR_EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						buffer.append(SQL_AND);
						buffer.append(this.getColumnName());
						buffer.append(LESS_THAN_OR_EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date2));
						break;
					case OperationSort._OPERATION_GREAT:
						buffer.append(GREAT_THAN);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						buffer.append(GREAT_THAN_OR_EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_LESS:
						buffer.append(LESS_THAN);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						buffer.append(LESS_THAN_OR_EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;						
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_BOOLEAN:
				final boolean value = ((Boolean) this.condition.getValue()).booleanValue();
				buffer.append(this.getColumnName());
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(EQUALS);
						buffer.append(value ? '1' : '0');
						break;
					case OperationSort._OPERATION_NOT_EQUALS:
						buffer.append(NOT_EQUALS);
						buffer.append(value ? '1' : '0');
						break;
					default:
						Log.errorMessage(ERROR_UNKNOWN_OPERATION_CODE
								+ this.condition.getOperation().value());
						break;
				}
				break;
		}
		return buffer.toString();
	}

	protected abstract boolean isKeySupported(final String key);
}
