/*
 * $Id: ResultSortCondition.java,v 1.5 2004/12/07 10:59:48 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.List;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.ResultSort;
import com.syrus.AMFICOM.measurement.corba.ResultSortCondition_Transferable;

/**
 * @version $Revision: 1.5 $, $Date: 2004/12/07 10:59:48 $
 * @author $Author: bass $
 * @module measurement_v1
 */
public class ResultSortCondition implements StorableObjectCondition {

	private static ResultSortCondition	instance	= null;
	private static boolean				initialized	= false;
	private static Object				lock		= new Object();

	private Short						entityCode	= new Short(ObjectEntities.RESULT_ENTITY_CODE);

	private Identifier					measurementId;
	private ResultSort					resultSort;

	private ResultSortCondition() {
		// empty
	}

	public static ResultSortCondition getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new ResultSortCondition();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}
	
	public ResultSortCondition(ResultSortCondition_Transferable transferable){
		this.setEntityCode(transferable.entity_code);
		this.measurementId = new Identifier(transferable.measurement_id);
		this.resultSort = transferable.sort;
	}

	public ResultSortCondition(Measurement measurement, ResultSort resultSort) {
		this.measurementId = measurement.getId();
		this.resultSort = resultSort;
	}

	public Short getEntityCode() {
		return this.entityCode;
	}

	/**
	 * @return <code>true</code> if {@link #entityCode}is {@link Result}and
	 *         ResultSort is equals given
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof Result) {
			Result result = (Result) object;
			Identifier id = result.getMeasurement().getId();
			ResultSort sort = result.getSort();
			if ((id.equals(this.measurementId)) && (this.resultSort.value() == sort.value())) {
				condition = true;
			}
		}
		return condition;
	}
	
	public boolean isNeedMore(List list) throws ApplicationException {
		return true;
	}

	public void setEntityCode(short entityCode) {
		if (entityCode != ObjectEntities.RESULT_ENTITY_CODE)
		throw new UnsupportedOperationException("entityCode is unknown for this condition");
	}

	public void setEntityCode(Short entityCode) {
		this.setEntityCode(entityCode.shortValue());
	}

	public Object getTransferable() {
		return new ResultSortCondition_Transferable(this.entityCode.shortValue(),
													(Identifier_Transferable) this.measurementId.getTransferable(),
													this.resultSort);
	}

	public Identifier getMeasurementId() {
		return this.measurementId;
	}
	
	public void setMeasurementId(Identifier measurementId) {
		this.measurementId = measurementId;
	}
	
	public ResultSort getResultSort() {
		return this.resultSort;
	}
	
	public void setResultSort(ResultSort resultSort) {
		this.resultSort = resultSort;
	}
}
