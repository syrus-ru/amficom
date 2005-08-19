/*-
* $Id: XMLTypicalCondition.java,v 1.1 2005/08/19 14:02:46 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Set;

import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.util.Log;



/**
 * @version $Revision: 1.1 $, $Date: 2005/08/19 14:02:46 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module general
 */
public class XMLTypicalCondition extends XMLStorableObjectCondition<TypicalCondition> {
	
	@SuppressWarnings("unused")
	private XMLTypicalCondition(final TypicalCondition condition,
	                            final StorableObjectXMLDriver driver) {
		super(condition, driver);
	}

	@Override
	public Set<Identifier> getIdsByCondition() throws IllegalDataException {
		final StringBuffer buffer = new StringBuffer(super.getBaseQuery());
		buffer.append('/');
		buffer.append(super.condition.getKey());
		final int operationCode = super.condition.getOperation().value();
		switch(operationCode){
		case OperationSort._OPERATION_EQUALS: 
			buffer.append("[text()='");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			break;
		case OperationSort._OPERATION_GREAT: 
			buffer.append("[text()>'");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			break;
		case OperationSort._OPERATION_LESS: 
			buffer.append("[text()<'");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			break;
		case OperationSort._OPERATION_GREAT_EQUALS: 
			buffer.append("[text()>='");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			break;
		case OperationSort._OPERATION_LESS_EQUALS: 
			buffer.append("[text()<='");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			break;
		case OperationSort._OPERATION_IN_RANGE: 
			buffer.append("[text()>'");
			buffer.append(super.getValue(super.condition.getValue()));
			buffer.append("']");
			buffer.append("[text()<'");
			buffer.append(super.getValue(super.condition.getOtherValue()));
			buffer.append("']");
			break;
		default:
			Log.errorMessage("TypicalCondition.toString | unknown operation code " + operationCode);
			break;
		}
		return super.getIdsByCondition(buffer.toString(), true);
	}
}

