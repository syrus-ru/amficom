/*
 * $Id: LinkedIdsConditionImpl.java,v 1.7 2005/02/08 14:25:25 bob Exp $
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ParameterType;

/**
 * @version $Revision: 1.7 $, $Date: 2005/02/08 14:25:25 $
 * @author $Author: bob $
 * @module measurement_v1
 */
class LinkedIdsConditionImpl extends LinkedIdsCondition {

	protected static final Short	ANALYSISTYPE_SHORT		= new Short(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
	protected static final Short	EVALUATIONTYPE_SHORT	= new Short(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
	protected static final Short	MEASUREMENT_SHORT		= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	protected static final Short	RESULT_SHORT			= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	protected static final Short	MEASUREMENTTYPE_SHORT	= new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
	protected static final Short	MS_SHORT				= new Short(ObjectEntities.MS_ENTITY_CODE);

	private LinkedIdsConditionImpl(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsConditionImpl(Identifier identifier, Short entityCode) {
		this.linkedIds = Collections.singletonList(identifier);
		this.entityCode = entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode}is {@link AnalysisType}for all
	 *         analysesType for criteria ParameterTypes identifier in linkedIds;
	 *         </li>
	 *         <li>if {@link #entityCode}is {@link EvaluationType}for all
	 *         analysesType for threshold ParameterTypes identifier in
	 *         linkedIds;</li>
	 *         <li>if {@link #entityCode}is {@link Measurement}for all
	 *         measurements for Test identifier in linkedIds;</li>
	 *         <li>if {@link #entityCode}is {@link Result}for all results for
	 *         Measurement identifier in linkedIds;</li>
	 *         <li>if {@link #entityCode}is {@link MeasurementType}for all
	 *         measurementTypes MeasurementPortType identifier in linkedIds;
	 *         </li>
	 *         <li>if {@link #entityCode}is {@link MeasurementSetup}for all
	 *         measurement setups for MonitoredElement identifier in linkedIds;
	 *         </li>
	 *         </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		List params = new LinkedList();
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
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				Identifier testId = ((Measurement) object).getTestId();
				condition = super.conditionTest(testId);
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE: {
				MeasurementType measurementType = (MeasurementType) object;
				params.addAll(measurementType.getMeasurementPortTypes());
				params.addAll(measurementType.getInParameterTypes());
				params.addAll(measurementType.getOutParameterTypes());
				return super.conditionTest(params);
			}
			case ObjectEntities.MS_ENTITY_CODE:
				MeasurementSetup measurementSetup = (MeasurementSetup) object;
				Map codeIdsList = super.sort(this.linkedIds);
				for (Iterator iterator = codeIdsList.keySet().iterator(); iterator.hasNext();) {
					Short code = (Short) iterator.next();
					List ids = (List) codeIdsList.get(code);
					condition = super.conditionTest(ids);
					if (condition)
						return condition;

					for (Iterator iter = ids.iterator(); iter.hasNext();) {
						Identifier id = (Identifier) iter.next();
						MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool
								.getStorableObject(id, true);
						SetParameter[] setParameters = measurementSetup.getParameterSet().getParameters();
						for (int i = 0; (i < setParameters.length) && (!condition); i++) {
							ParameterType parameterType = (ParameterType) setParameters[i].getType();
							List inParameterTypes = measurementType.getInParameterTypes();
							for (Iterator it = inParameterTypes.iterator(); it.hasNext();) {
								Object element = it.next();
								if (element instanceof ParameterType) {
									ParameterType parameterType2 = (ParameterType) element;
									if (parameterType.getId().equals(parameterType2.getId())) {
										condition = true;
										break;
									}
								}
							}

							if (!condition) {
								List outParameterTypes = measurementType.getOutParameterTypes();
								for (Iterator it = outParameterTypes.iterator(); it.hasNext();) {
									Object element = it.next();
									if (element instanceof ParameterType) {
										ParameterType parameterType2 = (ParameterType) element;
										if (parameterType.getId().equals(parameterType2.getId())) {
											condition = true;
											break;
										}
									}

								}
							}

						}
					}
				}

				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				Identifier actionId = ((Result) object).getAction().getId();
				condition = super.conditionTest(actionId);
				break;
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

	public boolean isNeedMore(List list) {
		return true;
	}
}
