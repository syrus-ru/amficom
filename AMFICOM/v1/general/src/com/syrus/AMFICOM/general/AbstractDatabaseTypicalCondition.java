/*
* $Id: AbstractDatabaseTypicalCondition.java,v 1.11 2005/08/28 16:41:33 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.StorableObjectDatabase.CLOSE_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.EQUALS;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.OPEN_BRACKET;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_FROM;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_IN;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_SELECT;
import static com.syrus.AMFICOM.general.StorableObjectDatabase.SQL_WHERE;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.TypicalSort;
import com.syrus.util.EnumUtil;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.11 $, $Date: 2005/08/28 16:41:33 $
 * @author $Author: arseniy $
 * @module general
 */
public abstract class AbstractDatabaseTypicalCondition implements DatabaseStorableObjectCondition {

	protected TypicalCondition condition;
	
	public AbstractDatabaseTypicalCondition(final TypicalCondition delegate) {
		this.condition = delegate;
	}

	protected abstract String getColumnName() throws IllegalObjectEntityException;

	protected abstract String getLinkedColumnName() throws IllegalObjectEntityException;

	protected abstract String getLinkedTableName() throws IllegalObjectEntityException;

	private String getLinkedSubQuery() throws IllegalObjectEntityException {
		switch (this.condition.getType().value()) {
			case TypicalSort._TYPE_ENUM:
				final Enum e = (Enum) this.condition.getValue();
				return StorableObjectWrapper.COLUMN_ID + SQL_IN + OPEN_BRACKET
						+ SQL_SELECT + this.getColumnName()
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
					case OperationSort._OPERATION_IN:
						buffer.append(this.getLinkedSubQuery());
						break;
				}
				break;
			case TypicalSort._TYPE_NUMBER_INT:
			case TypicalSort._TYPE_NUMBER_DOUBLE:
			case TypicalSort._TYPE_NUMBER_LONG:
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS: {
						buffer.append(this.getColumnName());
						buffer.append(StorableObjectDatabase.EQUALS);
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
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
					}
						break;
					case OperationSort._OPERATION_GREAT: {
						buffer.append(this.getColumnName());
						buffer.append(" > ");
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
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
					}
						break;
					case OperationSort._OPERATION_LESS: {
						buffer.append(this.getColumnName());
						buffer.append(" < ");
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
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
					}
						break;
					case OperationSort._OPERATION_GREAT_EQUALS: {
						buffer.append(this.getColumnName());
						buffer.append(" >= ");
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
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
					}
						break;
					case OperationSort._OPERATION_LESS_EQUALS: {
						buffer.append(this.getColumnName());
						buffer.append(" <= ");
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
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
					}
						break;
					case OperationSort._OPERATION_IN_RANGE: {
						buffer.append(StorableObjectDatabase.OPEN_BRACKET);
						buffer.append(this.getColumnName());
						buffer.append(" > ");
						switch (this.condition.getType().value()) {
							case TypicalSort._TYPE_NUMBER_INT:
								buffer.append(this.condition.getFirstInt());
								buffer.append(StorableObjectDatabase.SQL_AND);
								buffer.append(this.getColumnName());
								buffer.append(" < ");
								buffer.append(this.condition.getSecondInt());
								break;
							case TypicalSort._TYPE_NUMBER_DOUBLE:
								buffer.append(this.condition.getFirstDouble());
								buffer.append(StorableObjectDatabase.SQL_AND);
								buffer.append(this.getColumnName());
								buffer.append(" < ");
								buffer.append(this.condition.getSecondDouble());
								break;
							case TypicalSort._TYPE_NUMBER_LONG:
								buffer.append(this.condition.getFirstLong());
								buffer.append(StorableObjectDatabase.SQL_AND);
								buffer.append(this.getColumnName());
								buffer.append(" < ");
								buffer.append(this.condition.getSecondLong());
								break;								
							default:
								Log.errorMessage("AbstractDatabaseTypicalCondition.getSQLQuery | unknown number code "
										+ this.condition.getType().value());
						}
						buffer.append(StorableObjectDatabase.CLOSE_BRACKET);
					}
						break;
					default:
						Log.errorMessage("TypicalCondition.getSQLQuery | unknown operation code "
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_STRING:
				String v = this.condition.getValue().toString();
				buffer.append(this.getColumnName());
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(StorableObjectDatabase.EQUALS);
						buffer.append(StorableObjectDatabase.APOSTROPHE);
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append(StorableObjectDatabase.APOSTROPHE);
						break;
					case OperationSort._OPERATION_SUBSTRING:
						buffer.append(" LIKE ");
						buffer.append(StorableObjectDatabase.APOSTROPHE);
						buffer.append("%");
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append("%");
						buffer.append(StorableObjectDatabase.APOSTROPHE);
						break;
					case OperationSort._OPERATION_REGEXP:
						/* TODO isn't implement */
						break;
					case OperationSort._OPERATION_CI_REGEXP:
						/* TODO isn't implement */
						break;
					default:
						Log.errorMessage("TypicalCondition.getSQLQuery | unknown operation code "
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_DATE:
				buffer.append(this.getColumnName());
				Date date1 = (Date) this.condition.getValue();
				Date date2 = (Date) this.condition.getOtherValue();
				switch (this.condition.getOperation().value()) {
					case OperationSort._OPERATION_EQUALS:
						buffer.append(StorableObjectDatabase.EQUALS);
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_IN_RANGE:
						buffer.append(" >= ");
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						buffer.append(StorableObjectDatabase.SQL_AND);
						buffer.append(this.getColumnName());
						buffer.append(" <= ");
						buffer.append(DatabaseDate.toUpdateSubString(date2));
						break;
					case OperationSort._OPERATION_GREAT:
						buffer.append(" > ");
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_GREAT_EQUALS:
						buffer.append(" >= ");
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_LESS:
						buffer.append(" < ");
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;
					case OperationSort._OPERATION_LESS_EQUALS:
						buffer.append(" <= ");
						buffer.append(DatabaseDate.toUpdateSubString(date1));
						break;						
					default:
						Log.errorMessage("TypicalCondition.getSQLQuery | unknown operation code "
								+ this.condition.getOperation().value());
						break;
				}
				break;
			case TypicalSort._TYPE_BOOLEAN:
				final boolean value = ((Boolean) this.condition.getValue()).booleanValue();
				buffer.append(this.getColumnName());
				switch (this.condition.getOperation().value()) {
				case OperationSort._OPERATION_EQUALS:
					buffer.append(StorableObjectDatabase.EQUALS);
					buffer.append(value ? '1' : '0');
					break;
				default:
					Log.errorMessage("TypicalCondition.getSQLQuery | unknown operation code "
							+ this.condition.getOperation().value());
					break;
				}
				break;
		}
		return buffer.toString();
	}

}
