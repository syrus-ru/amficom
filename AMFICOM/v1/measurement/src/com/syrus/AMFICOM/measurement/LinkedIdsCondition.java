/*
 * $Id: LinkedIdsCondition.java,v 1.8 2004/10/13 12:54:53 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;

/**
 * @version $Revision: 1.8 $, $Date: 2004/10/13 12:54:53 $
 * @author $Author: bob $
 * @module measurement_v1
 */
public class LinkedIdsCondition extends com.syrus.AMFICOM.configuration.LinkedIdsCondition {

	private static LinkedIdsCondition	instance = null;
	private static boolean			initialized	= false;
	private static Object			lock		= new Object();

	private Domain				domain;

	private static final Short		ANALYSIS_SHORT		= new Short(ObjectEntities.ANALYSIS_ENTITY_CODE);
	private static final Short		EVALUATION_SHORT	= new Short(
											ObjectEntities.EVALUATION_ENTITY_CODE);
	private static final Short		MEASUREMENT_SHORT	= new Short(
											ObjectEntities.MEASUREMENT_ENTITY_CODE);
	private static final Short		RESULT_SHORT		= new Short(ObjectEntities.RESULT_ENTITY_CODE);
	private static final Short		MEASUREMENTTYPE_SHORT	= new Short(
											ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
	private static final Short		MS_SHORT		= new Short(ObjectEntities.MS_ENTITY_CODE);

	private Short				entityCode;

	private List				linkedIds;
	private Identifier			identifier;

	private LinkedIdsCondition() {
		super((Identifier)null, (Short)null);
	}

	public static LinkedIdsCondition getInstance() {
		if (!initialized) {
			synchronized (lock) {
				if (!initialized && instance == null)
					instance = new LinkedIdsCondition();
			}
			synchronized (lock) {
				initialized = true;
			}
		}
		return instance;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link entityCode}is {@link Analysis}for all
	 *         analyses for CriteriaSet identifier in linkedIds;</li>
	 *         <li>if {@link entityCode}is {@link Evaluation}for all
	 *         analyses for ThresholdSet identifier in linkedIds;</li>
	 *         <li>if {@link entityCode}is {@link Measurement}for all
	 *         measurements for Test identifier in linkedIds;</li>
	 *         <li>if {@link entityCode}is {@link Result}for all results
	 *         for Measurement identifier in linkedIds;</li>
	 *         <li>if {@link entityCode}is {@link MeasurementType}for all
	 *         results for Measurement identifier in linkedIds;</li>
	 *         <li>if {@link entityCode}is {@link MeasurementSetup}for
	 *         all measurement setups for MonitoredElement identifier in
	 *         linkedIds;</li>
	 *         </ul>
	 */
	public boolean isConditionTrue(Object object) throws ApplicationException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
				if (object instanceof Analysis) {
					Analysis analysis = (Analysis) object;
					Identifier id = analysis.getCriteriaSet().getId();
					if (this.linkedIds == null) {
						Identifier criteriaSetId = this.identifier;
						if (criteriaSetId.equals(id)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier criteriaSetId = (Identifier) it.next();
							if (criteriaSetId.equals(id)) {
								condition = true;
								break;
							}
						}
					}
				}
				break;
			case ObjectEntities.EVALUATION_ENTITY_CODE:
				if (object instanceof Evaluation) {
					Evaluation evaluation = (Evaluation) object;
					Identifier id = evaluation.getThresholdSet().getId();
					if (this.linkedIds == null) {
						Identifier thresholdSetId = this.identifier;
						if (thresholdSetId.equals(id)) {
							condition = true;
							break;
						}
					} else {
						for (Iterator it = this.linkedIds.iterator(); it.hasNext();) {
							Identifier thresholdSetId = (Identifier) it.next();
							if (thresholdSetId.equals(id)) {
								condition = true;
								break;
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
					Result resultMeasurementId = (Result) object;
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
					for (Iterator it = measurementPortTypes.iterator(); it.hasNext();) {
						MeasurementPortType measurementPortType = (MeasurementPortType) it
								.next();
						Identifier id2 = measurementPortType.getId();
						if (!condition) {
							if (this.linkedIds == null) {
								Identifier id = this.identifier;
								if (id.equals(id2)) {
									condition = true;
									break;
								}
							} else {
								for (Iterator iter = this.linkedIds.iterator(); iter
										.hasNext();) {
									Identifier id = (Identifier) iter.next();
									if (id2.equals(id)) {
										condition = true;
										break;
									}

								}
							}
						}
					}
				}
				break;
			case ObjectEntities.MS_ENTITY_CODE:
				if (object instanceof MeasurementSetup) {
					MeasurementSetup measurementSetup = (MeasurementSetup) object;
					for (Iterator it = measurementSetup.getMonitoredElementIds().iterator(); it
							.hasNext();) {
						Identifier id2 = (Identifier) it.next();
						if (!condition) {
							if (this.linkedIds == null) {
								if (this.identifier.equals(id2)) {
									condition = true;
									break;
								}
							} else {
								for (Iterator iter = this.linkedIds.iterator(); iter
										.hasNext();) {
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
				condition = super.isConditionTrue(object);
		}

		return condition;
	}

	public void setEntityCode(short entityCode) {
		switch (entityCode) {
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
				throw new UnsupportedOperationException("entityCode is unknown for this condition");
		}

	}

	public Object getTransferable() {

		if (this.entityCode == null) { throw new UnsupportedOperationException("entityCode doesn't set"); }

		short s = this.entityCode.shortValue();
		switch (s) {
			case ObjectEntities.ANALYSIS_ENTITY_CODE:
			case ObjectEntities.EVALUATION_ENTITY_CODE:
			case ObjectEntities.MEASUREMENT_ENTITY_CODE:
			case ObjectEntities.RESULT_ENTITY_CODE:
			case ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE:
			case ObjectEntities.MS_ENTITY_CODE:
				break;
			default:
				throw new UnsupportedOperationException("entityCode is unknown");
		}

		LinkedIdsCondition_Transferable transferable = new LinkedIdsCondition_Transferable();
		Identifier_Transferable[] linkedId_Transferable;
		if (this.linkedIds != null) {
			linkedId_Transferable = new Identifier_Transferable[this.linkedIds.size()];
			int i = 0;

			for (Iterator it = this.linkedIds.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				linkedId_Transferable[i] = (Identifier_Transferable) id.getTransferable();
			}
		} else {
			linkedId_Transferable = new Identifier_Transferable[1];
			linkedId_Transferable[0] = (Identifier_Transferable) this.identifier.getTransferable();
		}

		transferable.domain_id = (Identifier_Transferable) this.domain.getId().getTransferable();
		transferable.linked_ids = linkedId_Transferable;
		transferable.entity_code = s;

		return transferable;
	}

	public List getCriteriaSetIds() {
		if (this.entityCode.equals(ANALYSIS_SHORT))
			return this.linkedIds;
		return null;
	}

	public List getMeasurementIds() {
		if (this.entityCode.equals(RESULT_SHORT))
			return this.linkedIds;
		return null;
	}

	public List getMeasurementPortTypeIds() {
		if (this.entityCode.equals(MEASUREMENTTYPE_SHORT))
			return this.linkedIds;
		return null;
	}

	public List getTestIds() {
		if (this.entityCode.equals(MEASUREMENT_SHORT))
			return this.linkedIds;
		return null;
	}

	public List getThresholdSetIds() {
		if (this.entityCode.equals(EVALUATION_SHORT))
			return this.linkedIds;
		return null;
	}
}
