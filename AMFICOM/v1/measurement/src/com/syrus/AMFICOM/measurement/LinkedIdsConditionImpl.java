/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.36 2005/05/06 13:09:10 bob Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.36 $, $Date: 2005/05/06 13:09:10 $
 * @author $Author: bob $
 * @module measurement_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short	ANALYSISTYPE_SHORT		= new Short(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
	protected static final Short	EVALUATIONTYPE_SHORT	= new Short(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);	
	protected static final Short	ANALYSIS_SHORT		= new Short(ObjectEntities.ANALYSIS_ENTITY_CODE);
	protected static final Short	EVALUATION_SHORT	= new Short(ObjectEntities.EVALUATION_ENTITY_CODE);
	protected static final Short	MEASUREMENT_SHORT		= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	protected static final Short	RESULT_SHORT			= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	protected static final Short	MEASUREMENTTYPE_SHORT	= new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
	protected static final Short	MS_SHORT				= new Short(ObjectEntities.MS_ENTITY_CODE);

	private LinkedIdsConditionImpl(final Set linkedIds, final Short linkedEntityCode, final Short entityCode) {
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
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				AnalysisType analysisType = (AnalysisType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						Set params = new HashSet();
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
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + ObjectEntities.codeToString(this.linkedEntityCode)
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				EvaluationType evaluationType = (EvaluationType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						Set params = new HashSet();
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
				ModelingType modelingType = (ModelingType)storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETERTYPE_ENTITY_CODE:
						Set params = new HashSet();
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
				Action action = (Action) storableObject;
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
				Measurement measurement = (Measurement) storableObject;
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
				MeasurementType measurementType = (MeasurementType) storableObject;
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
				MeasurementSetup measurementSetup = (MeasurementSetup) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.ME_ENTITY_CODE:
						Set params = new HashSet();
						params.addAll(measurementSetup.getMonitoredElementIds());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
						params = new HashSet();
						params.addAll(measurementSetup.getMeasurementTypeIds());
						condition = super.conditionTest(params);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				Result result = (Result) storableObject;
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
				Test test = (Test) storableObject;
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

	public void setEntityCode(final Short entityCode) throws IllegalObjectEntityException {
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
				throw new IllegalObjectEntityException("entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

	}

	public boolean isNeedMore(final Set storableObjects) {
		Identifier id;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				switch (this.linkedEntityCode) {
					case ObjectEntities.TEST_ENTITY_CODE:
						Set measurements = new HashSet(storableObjects);
						Test test;
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							id = (Identifier) it.next();
							try {
								test = (Test) MeasurementStorableObjectPool.getStorableObject(id, false);
							}
							catch (ApplicationException ae) {
								Log.errorException(ae);
								continue;
							}
							Measurement measurement;
							int testNumberOfMeasurements = 0;
							for (Iterator it1 = measurements.iterator(); it1.hasNext();) {
								measurement = (Measurement) it1.next();
								if (measurement.getTestId().equals(id)) {
									it1.remove();
									testNumberOfMeasurements++;
								}
							}
							if (test == null || testNumberOfMeasurements < test.getNumberOfMeasurements())
								return true;
						}
						return false;
				}
					
				break;
		}

		return true;
	}
}
