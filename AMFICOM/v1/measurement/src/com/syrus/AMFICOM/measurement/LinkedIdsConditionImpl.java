/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.46 2005/06/17 12:38:56 bass Exp $
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
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.46 $, $Date: 2005/06/17 12:38:56 $
 * @author $Author: bass $
 * @module measurement_v1
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

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
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				AnalysisType analysisType = (AnalysisType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETER_TYPE_CODE:
						Set params = new HashSet();
						params.addAll(analysisType.getCriteriaParameterTypeIds());
						params.addAll(analysisType.getInParameterTypeIds());
						params.addAll(analysisType.getOutParameterTypeIds());
						params.addAll(analysisType.getEtalonParameterTypeIds());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENT_TYPE_CODE:
						condition = super.conditionTest(analysisType.getMeasurementTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + ObjectEntities.codeToString(this.linkedEntityCode)
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.EVALUATION_TYPE_CODE:
				EvaluationType evaluationType = (EvaluationType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETER_TYPE_CODE:
						Set params = new HashSet();
						params.addAll(evaluationType.getThresholdParameterTypeIds());
						params.addAll(evaluationType.getInParameterTypeIds());
						params.addAll(evaluationType.getOutParameterTypeIds());
						params.addAll(evaluationType.getEtalonParameterTypeIds());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENT_TYPE_CODE:
						condition = super.conditionTest(evaluationType.getMeasurementTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MODELING_TYPE_CODE:
				ModelingType modelingType = (ModelingType)storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.PARAMETER_TYPE_CODE:
						Set params = new HashSet();
						params.addAll(modelingType.getInParameterTypeIds());
						params.addAll(modelingType.getOutParameterTypeIds());
						condition = super.conditionTest(params);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.ANALYSIS_CODE:
			case ObjectEntities.EVALUATION_CODE:
				Action action = (Action) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENT_CODE:
						Identifier parentActionId = action.getParentAction().getId();
						condition = super.conditionTest(parentActionId);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MEASUREMENT_CODE:
				Measurement measurement = (Measurement) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.TEST_CODE:
						condition = super.conditionTest(measurement.getTestId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				/* measurement haven't parent action*/
				break;
			case ObjectEntities.MEASUREMENT_TYPE_CODE: {
				MeasurementType measurementType = (MeasurementType) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENTPORT_TYPE_CODE:
						condition = super.conditionTest(measurementType.getMeasurementPortTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			}
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				MeasurementSetup measurementSetup = (MeasurementSetup) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MONITOREDELEMENT_CODE:
						Set params = new HashSet();
						params.addAll(measurementSetup.getMonitoredElementIds());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENT_TYPE_CODE:
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
			case ObjectEntities.RESULT_CODE:
				Result result = (Result) storableObject;
				if (this.linkedEntityCode == ObjectEntities.MEASUREMENT_CODE
						|| this.linkedEntityCode == ObjectEntities.ANALYSIS_CODE
						|| this.linkedEntityCode == ObjectEntities.EVALUATION_CODE
						|| this.linkedEntityCode == ObjectEntities.MODELING_CODE) {
					for (Action a = result.getAction(); a != null; a = a.getParentAction()) {
						if (super.conditionTest(a.getId())) {
							condition = true;
							break;
						}
					}
				} else
					throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
							+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				break;
			case ObjectEntities.TEST_CODE:
				Test test = (Test) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MONITOREDELEMENT_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getId());
						break;
					case ObjectEntities.MEASUREMENTPORT_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getMeasurementPortId());
						break;
					case ObjectEntities.MCM_CODE:
						condition = super.conditionTest(test.getMCMId());
						break;
					case ObjectEntities.MEASUREMENTSETUP_CODE:
						condition = super.conditionTest(test.getMeasurementSetupIds());
					case ObjectEntities.TEST_CODE:
						condition = super.conditionTest(test.getGroupTestId());
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
			case ObjectEntities.ANALYSIS_TYPE_CODE:
			case ObjectEntities.EVALUATION_TYPE_CODE:
			case ObjectEntities.ANALYSIS_CODE:
			case ObjectEntities.EVALUATION_CODE:
			case ObjectEntities.MEASUREMENT_CODE:
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
			case ObjectEntities.MEASUREMENTSETUP_CODE:
			case ObjectEntities.RESULT_CODE:
			case ObjectEntities.TEST_CODE:
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
			case ObjectEntities.MEASUREMENT_CODE:
				switch (this.linkedEntityCode) {
					case ObjectEntities.TEST_CODE:
						Set measurements = new HashSet(storableObjects);
						Test test;
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							id = (Identifier) it.next();
							try {
								test = (Test) StorableObjectPool.getStorableObject(id, false);
							} catch (ApplicationException ae) {
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
