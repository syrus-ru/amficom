/*
 * $Id: LinkedIdsConditionImpl.java,v 1.18 2005/03/10 19:36:40 arseniy Exp $
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collection;
import java.util.LinkedList;

import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.18 $, $Date: 2005/03/10 19:36:40 $
 * @author $Author: arseniy $
 * @module measurement_v1
 */
class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short	ANALYSISTYPE_SHORT		= new Short(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
	protected static final Short	EVALUATIONTYPE_SHORT	= new Short(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);	
	protected static final Short	ANALYSIS_SHORT		= new Short(ObjectEntities.ANALYSIS_ENTITY_CODE);
	protected static final Short	EVALUATION_SHORT	= new Short(ObjectEntities.EVALUATION_ENTITY_CODE);
	protected static final Short	MEASUREMENT_SHORT		= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	protected static final Short	RESULT_SHORT			= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	protected static final Short	MEASUREMENTTYPE_SHORT	= new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
	protected static final Short	MS_SHORT				= new Short(ObjectEntities.MS_ENTITY_CODE);

	private LinkedIdsConditionImpl(Collection linkedIds, Short linkedEntityCode, Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}
//
//	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
//		this.linkedIds = Collections.singletonList(identifier);
//		this.entityCode = entityCode;
//	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode} is {@link AnalysisType}for all
	 *         analysesType for criteria ParameterTypes identifier in linkedIds;
	 *         </li>
	 *         <li>if {@link #entityCode} is {@link EvaluationType}for all
	 *         analysesType for threshold ParameterTypes identifier in
	 *         linkedIds;</li>
	 *         <li>if {@link #entityCode} is {@link Analysis} for all
	 *         analysis for parent action identifier in
	 *         </li>
   	 *         <li>if {@link #entityCode} is {@link Evaluation} for all
	 *         evaluation for parent action identifier in
	 *         </li>  
	 *         <li>if {@link #entityCode} is {@link Measurement}for all
	 *         measurements for Test identifier in linkedIds;</li>
	 *         <li>if {@link #entityCode} is {@link Result}for all results for
	 *         Measurement identifier in linkedIds;</li>
	 *         <li>if {@link #entityCode} is {@link MeasurementType}for all
	 *         measurementTypes MeasurementPortType identifier in linkedIds;
	 *         </li>
	 *         <li>if {@link #entityCode} is {@link MeasurementSetup}for all
	 *         measurement setups for MonitoredElement identifier in linkedIds;
	 *         </li>
	 *         </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		Collection params = new LinkedList();
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				AnalysisType analysisType = (AnalysisType) object;
				params.addAll(analysisType.getCriteriaParameterTypes());
				params.addAll(analysisType.getInParameterTypes());
				params.addAll(analysisType.getOutParameterTypes());
				params.addAll(analysisType.getEtalonParameterTypes());
				condition = super.conditionTest(params);
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				EvaluationType evaluationType = (EvaluationType) object;
				params.addAll(evaluationType.getThresholdParameterTypes());
				params.addAll(evaluationType.getInParameterTypes());
				params.addAll(evaluationType.getOutParameterTypes());
				params.addAll(evaluationType.getEtalonParameterTypes());
				condition = super.conditionTest(params);
				break;
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				ModelingType modelingType = (ModelingType)object;
				params.addAll(modelingType.getInParameterTypes());
				params.addAll(modelingType.getOutParameterTypes());
				condition = super.conditionTest(params);
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				Identifier parentActionId = ((Action)object).getParentAction().getId();
				condition = super.conditionTest(parentActionId);
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				Identifier testId = ((Measurement) object).getTestId();
				condition = super.conditionTest(testId);
				/* measurement haven't parent action*/
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE: {
				MeasurementType measurementType = (MeasurementType) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
						condition = super.conditionTest(measurementType.getMeasurementPortTypes());
						break;
				}
				break;
			}
			case ObjectEntities.MS_ENTITY_CODE:
				MeasurementSetup measurementSetup = (MeasurementSetup) object;
				params.addAll(measurementSetup.getMonitoredElementIds());
				condition = super.conditionTest(params);
			case ObjectEntities.RESULT_ENTITY_CODE:
				Identifier actionId = ((Result) object).getAction().getId();
				condition = super.conditionTest(actionId);
				break;
			case ObjectEntities.TEST_ENTITY_CODE:
				Test test = (Test) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.ME_ENTITY_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getId());
						break;
					case ObjectEntities.MEASUREMENTPORT_ENTITY_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getMeasurementPortId());
						break;
					case ObjectEntities.MCM_ENTITY_CODE:
						MeasurementPort measurementPort = (MeasurementPort) ConfigurationStorableObjectPool.getStorableObject(test.getMonitoredElement().getMeasurementPortId(), true);
						KIS kis = (KIS) ConfigurationStorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
						condition = super.conditionTest(kis.getMCMId());
						break;
				}
			default:
				throw new UnsupportedOperationException("entityCode "
						+ ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
		}
		return condition;
	}

	public void setEntityCode(Short entityCode) {
		switch (entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				this.entityCode = ANALYSISTYPE_SHORT;
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				this.entityCode = EVALUATIONTYPE_SHORT;
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				this.entityCode = ANALYSIS_SHORT;
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				this.entityCode = EVALUATION_SHORT;
				break;				
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				this.entityCode = MEASUREMENT_SHORT;
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				this.entityCode = MEASUREMENTTYPE_SHORT;
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				this.entityCode = MS_SHORT;
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				this.entityCode = RESULT_SHORT;
				break;
			default:
				throw new UnsupportedOperationException("entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}

	}

	public boolean isNeedMore(Collection collection) {
		return true;
	}
}
