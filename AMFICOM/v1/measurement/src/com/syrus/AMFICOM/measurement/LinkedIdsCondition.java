/*
 * $Id: LinkedIdsCondition.java,v 1.21 2004/12/24 10:02:23 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;

/**
 * @version $Revision: 1.21 $, $Date: 2004/12/24 10:02:23 $
 * @author $Author: bob $
 * @module measurement_v1
 */
class LinkedIdsCondition extends com.syrus.AMFICOM.general.LinkedIdsCondition {

	protected static final Short		ANALYSISTYPE_SHORT		= new Short(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
	protected static final Short		EVALUATIONTYPE_SHORT	= new Short(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);
	protected static final Short		MEASUREMENT_SHORT		= new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE);
	protected static final Short		RESULT_SHORT			= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	protected static final Short		MEASUREMENTTYPE_SHORT	= new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
	protected static final Short		MS_SHORT				= new Short(ObjectEntities.MS_ENTITY_CODE);
	
	private LinkedIdsCondition(List linkedIds, Short entityCode) {
		this.linkedIds = linkedIds;
		this.entityCode = entityCode;
	}

	private LinkedIdsCondition(Identifier identifier, Short entityCode) {
		this.identifier = identifier;
		this.entityCode = entityCode;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 * 
	 * <li>if {@link #entityCode}is {@link AnalysisType}for all analysesType
	 * for criteria ParameterTypes identifier in linkedIds;</li>
	 * 
	 * <li>if {@link #entityCode}is {@link EvaluationType}for all analysesType
	 * for threshold ParameterTypes identifier in linkedIds;</li>
	 * 
	 * <li>if {@link #entityCode}is {@link Measurement}for all measurements
	 * for Test identifier in linkedIds;</li>
	 * 
	 * <li>if {@link #entityCode}is {@link Result}for all results for
	 * Measurement identifier in linkedIds;</li>
	 * 
	 * <li>if {@link #entityCode}is {@link MeasurementType}for all
	 * measurementTypes MeasurementPortType identifier in linkedIds;</li>
	 * 
	 * <li>if {@link #entityCode}is {@link MeasurementSetup}for all
	 * measurement setups for MonitoredElement identifier in linkedIds;</li>
	 * 
	 * </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSISTYPE_ENTITY_CODE:
				if (object instanceof AnalysisType) {
					AnalysisType analysisType = (AnalysisType) object;
					List criteriaParameterTypes = analysisType.getCriteriaParameterTypes();
					if (this.linkedIds == null) {
						Identifier criteriaParameterTypeId = this.identifier;
						for (Iterator it = criteriaParameterTypes.iterator(); it.hasNext();) {
							Identifier id = ((ParameterType) it.next()).getId();
							if (criteriaParameterTypeId.equals(id)) {
								condition = true;
								break;
							}
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier criteriaParameterTypeId = (Identifier) it.next();
							for (Iterator it2 = criteriaParameterTypes.iterator(); it2.hasNext();) {
								Identifier id = ((ParameterType) it2.next()).getId();
								if (criteriaParameterTypeId.equals(id)) {
									condition = true;
									break;
								}
							}
						}
					}
				}
				break;
			case ObjectEntities.EVALUATIONTYPE_ENTITY_CODE:
				if (object instanceof EvaluationType) {
					EvaluationType evaluationType = (EvaluationType) object;
					List thresholdParameterTypes = evaluationType.getThresholdParameterTypes();
					if (this.linkedIds == null) {
						Identifier thresholdParameterTypeId = this.identifier;
						for (Iterator it = thresholdParameterTypes.iterator(); it.hasNext();) {
							Identifier id = ((ParameterType) it.next()).getId();
							if (thresholdParameterTypeId.equals(id)) {
								condition = true;
								break;
							}
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier thresholdParameterTypeId = (Identifier) it.next();
							for (Iterator it2 = thresholdParameterTypes.iterator(); it2.hasNext();) {
								Identifier id = ((ParameterType) it2.next()).getId();
								if (thresholdParameterTypeId.equals(id)) {
									condition = true;
									break;
								}
							}
						}
					}
				}
				break;
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
				if (object instanceof Measurement) {
					Identifier testId = ((Measurement) object).getTestId();
					if (this.linkedIds == null) {
						Identifier id = this.identifier;
						if (id.equals(testId)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier id = (Identifier) it.next();
							if (testId.equals(id)) {
								condition = true;
								break;
							}
						}
					}
				}
				break;
			case ObjectEntities.RESULT_ENTITY_CODE:
				if (object instanceof Result) {
					Identifier resultMeasurementId = ((Result)object).getMeasurement().getId();
					if (this.linkedIds == null) {
						Identifier id = this.identifier;
						if (id.equals(resultMeasurementId)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier measurementId = (Identifier) it.next();
							if (measurementId.equals(resultMeasurementId)) {
								condition = true;
								break;
							}
						}
					}
				}
				break;
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
				if (object instanceof MeasurementType) {
					MeasurementType measurementType = (MeasurementType) object;
					List measurementPortTypes = measurementType.getMeasurementPortTypes();
					if (measurementPortTypes != null){
						for (Iterator it = measurementPortTypes.iterator(); it.hasNext();) {
							MeasurementPortType measurementPortType = (MeasurementPortType) it.next();
							Identifier id2 = measurementPortType.getId();
							if (!condition) {
								if (this.linkedIds == null) {
									Identifier id = this.identifier;
									if (id.equals(id2)) {
										condition = true;
										break;
									}
								} else {
									for (Iterator iter = this.linkedIds.iterator(); iter.hasNext();) {
										Identifier id = (Identifier) iter.next();
										if (id2.equals(id)) {
											condition = true;
											break;
										}
	
									}
								}
							}
						}
					} else {
						condition = false;
					}
				}
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				if (object instanceof MeasurementSetup) {
					MeasurementSetup measurementSetup = (MeasurementSetup) object;
					for (Iterator it = measurementSetup.getMonitoredElementIds().iterator(); it.hasNext();) {
						Identifier id2 = (Identifier) it.next();
						if (!condition) {
							if (this.linkedIds == null) {
								if (this.identifier.equals(id2)) {
									condition = true;
									break;
								}
							} else {
								for (Iterator iter = this.linkedIds.iterator(); iter.hasNext();) {
									Identifier id = (Identifier) iter.next();
									if (id.equals(id2)) {
										condition = true;
										break;
									}
								}
							}
						}
					}
				}
				break;
			default:
				throw new UnsupportedOperationException("entityCode " + ObjectEntities.codeToString(this.entityCode.shortValue()) + " is unknown for this condition");
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
				super.setEntityCode(entityCode);
		}

	}	
}
