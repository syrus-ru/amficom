/*
 * $Id: LinkedIdsConditionImpl.java,v 1.30 2005/04/02 17:36:14 arseniy Exp $
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.HashSet;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.30 $, $Date: 2005/04/02 17:36:14 $
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

	private LinkedIdsConditionImpl(java.util.Set linkedIds, Short linkedEntityCode, Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

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
	public boolean isConditionTrue(final Object object) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				AnalysisType analysisType = (AnalysisType) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						java.util.Set params = new HashSet();
						params.addAll(analysisType.getCriteriaParameterTypes());
						params.addAll(analysisType.getInParameterTypes());
						params.addAll(analysisType.getOutParameterTypes());
						params.addAll(analysisType.getEtalonParameterTypes());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						condition = super.conditionTest(analysisType.getMeasurementTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				EvaluationType evaluationType = (EvaluationType) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						java.util.Set params = new HashSet();
						params.addAll(evaluationType.getThresholdParameterTypes());
						params.addAll(evaluationType.getInParameterTypes());
						params.addAll(evaluationType.getOutParameterTypes());
						params.addAll(evaluationType.getEtalonParameterTypes());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						condition = super.conditionTest(evaluationType.getMeasurementTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MODELINGTYPE_ENTITY_CODE:
				ModelingType modelingType = (ModelingType)object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						java.util.Set params = new HashSet();
						params.addAll(modelingType.getInParameterTypes());
						params.addAll(modelingType.getOutParameterTypes());
						condition = super.conditionTest(params);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				Action action = (Action) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENT_ENTITY_CODE:
						Identifier parentActionId = action.getParentAction().getId();
						condition = super.conditionTest(parentActionId);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				Measurement measurement = (Measurement) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.TEST_ENTITY_CODE:
						condition = super.conditionTest(measurement.getTestId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				/* measurement haven't parent action*/
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE: {
				MeasurementType measurementType = (MeasurementType) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE:
						condition = super.conditionTest(measurementType.getMeasurementPortTypes());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			}
			case ObjectEntities.MS_ENTITY_CODE:
				MeasurementSetup measurementSetup = (MeasurementSetup) object;
				switch (this.linkedEntityCode) {
					case ObjectEntities.ME_ENTITY_CODE:
						java.util.Set params = new HashSet();
						params.addAll(measurementSetup.getMonitoredElementIds());
						condition = super.conditionTest(params);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				Result result = (Result) object;
				if (this.linkedEntityCode == ObjectEntities.MEASUREMENT_ENTITY_CODE
						|| this.linkedEntityCode == ObjectEntities.ANALYSIS_ENTITY_CODE
						|| this.linkedEntityCode == ObjectEntities.EVALUATION_ENTITY_CODE
						|| this.linkedEntityCode == ObjectEntities.MODELING_ENTITY_CODE) {
					for (Action a = result.getAction(); a != null; a = a.getParentAction()) {
						if (super.conditionTest(a.getId())) {
							condition = true;
							break;
						}
					}
				}
				else
					throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
							+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
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
						condition = super.conditionTest(test.getMCMId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			default:
				throw new IllegalObjectEntityException(ENTITY_CODE_NOT_REGISTERED + this.entityCode
						+ ", " + ObjectEntities.codeToString(this.entityCode),
						IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}
		return condition;
	}

	public void setEntityCode(final Short entityCode) {
		switch (entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
			case ObjectEntities.EVALUATION_ENTITY_CODE:
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			case ObjectEntities.MS_ENTITY_CODE:
			case ObjectEntities.RESULT_ENTITY_CODE:
			case ObjectEntities.TEST_ENTITY_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new UnsupportedOperationException("entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition");
		}

	}

	public boolean isNeedMore(final java.util.Set collection) {
		return true;
	}
}
