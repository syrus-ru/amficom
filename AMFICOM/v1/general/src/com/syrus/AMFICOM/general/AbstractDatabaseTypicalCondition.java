/*
* $Id: AbstractDatabaseTypicalCondition.java,v 1.2 2005/02/04 14:09:18 bob Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Date;

import com.syrus.AMFICOM.general.corba.OperationSort;
import com.syrus.AMFICOM.general.corba.TypicalSort;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseDate;
import com.syrus.util.database.DatabaseString;


/**
 * @version $Revision: 1.2 $, $Date: 2005/02/04 14:09:18 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class AbstractDatabaseTypicalCondition implements DatabaseStorableObjectCondition {

	protected TypicalCondition condition;
	
	public AbstractDatabaseTypicalCondition(TypicalCondition delegate) {
		this.condition = delegate;
	}

	protected abstract String getColumnName();
	
	public Short getEntityCode() {
		return this.condition.getEntityCode();
	}
	
	public String getSQLQuery() throws IllegalDataException{
		StringBuffer buffer = new StringBuffer();
		switch (this.condition.getType().value()) {
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
						buffer.append(StorableObjectDatabase.APOSTOPHE);
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append(StorableObjectDatabase.APOSTOPHE);
						break;
					case OperationSort._OPERATION_SUBSTRING:
						buffer.append(" LIKE ");
						buffer.append(StorableObjectDatabase.APOSTOPHE);
						buffer.append("%");
						buffer.append(DatabaseString.toQuerySubString(v));
						buffer.append("%");
						buffer.append(StorableObjectDatabase.APOSTOPHE);
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
		}
		return buffer.toString();
	}

}
