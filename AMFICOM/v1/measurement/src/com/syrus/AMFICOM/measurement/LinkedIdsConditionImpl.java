/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.55 2005/08/20 19:25:23 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.55 $, $Date: 2005/08/20 19:25:23 $
 * @author $Author: arseniy $
 * @module measurement
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<Identifier> linkedIds, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIds = linkedIds;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Iterator it = this.linkedIds.iterator(); it.hasNext() && !condition;) {
				final Identifier id = (Identifier) it.next();
				if (id.getMajor() == ObjectEntities.DOMAIN_CODE) {
					final Domain domain = (Domain) StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.isChild(domain)) {
						condition = true;
					}
				}
			}
		} catch (final ApplicationException ae) {
			Log.errorException(ae);
		}
		return condition;
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
	@Override
	public boolean isConditionTrue(final StorableObject storableObject) throws IllegalObjectEntityException {
		boolean condition = false;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.ANALYSIS_TYPE_CODE:
				AnalysisType analysisType = (AnalysisType) storableObject;
				switch (this.linkedEntityCode) {
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
					case ObjectEntities.MEASUREMENT_TYPE_CODE:
						condition = super.conditionTest(evaluationType.getMeasurementTypeIds());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.ANALYSIS_CODE:
			case ObjectEntities.EVALUATION_CODE:
				final Action action = (Action) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MEASUREMENT_CODE:
						final Identifier parentActionId = action.getParentAction().getId();
						condition = super.conditionTest(parentActionId);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MEASUREMENT_CODE:
				final Measurement measurement = (Measurement) storableObject;
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
			case ObjectEntities.MEASUREMENT_TYPE_CODE:
				final MeasurementType measurementType = (MeasurementType) storableObject;
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
			case ObjectEntities.MEASUREMENTSETUP_CODE:
				final MeasurementSetup measurementSetup = (MeasurementSetup) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MONITOREDELEMENT_CODE:
						Set<Identifier> params = new HashSet<Identifier>();
						params.addAll(measurementSetup.getMonitoredElementIds());
						condition = super.conditionTest(params);
						break;
					case ObjectEntities.MEASUREMENT_TYPE_CODE:
						params = new HashSet<Identifier>();
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
				final Result result = (Result) storableObject;
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
				} else {
					throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
							+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.TEST_CODE:
				final Test test = (Test) storableObject;
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
						break;
					case ObjectEntities.TEST_CODE:
						condition = super.conditionTest(test.getGroupTestId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MEASUREMENTPORT_CODE:
				MeasurementPort measurementPort = (MeasurementPort) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.KIS_CODE:
						condition = super.conditionTest(measurementPort.getKISId());
						break;
					case ObjectEntities.MCM_CODE:
						try {
							final KIS kis1 = (KIS) StorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = super.conditionTest(kis1.getMCMId());
						} catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						break;
					case ObjectEntities.DOMAIN_CODE:
						try {
							KIS kis1 = (KIS) StorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = this.checkDomain(kis1);
						} catch (ApplicationException ae) {
							Log.errorException(ae);
						}
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.KIS_CODE:
				KIS kis = (KIS) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.MCM_CODE:
						condition = super.conditionTest(kis.getMCMId());
						break;
					case ObjectEntities.DOMAIN_CODE:
						condition = this.checkDomain(kis);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case ObjectEntities.MONITOREDELEMENT_CODE:
				MonitoredElement monitoredElement = (MonitoredElement) storableObject;
				switch (this.linkedEntityCode) {
					case ObjectEntities.DOMAIN_CODE:
						condition = this.checkDomain(monitoredElement);
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

	@Override
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
			case ObjectEntities.KIS_CODE:
			case ObjectEntities.MONITOREDELEMENT_CODE:
			case ObjectEntities.MEASUREMENTPORT_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException("entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

	}

	@Override
	public boolean isNeedMore(final Set<? extends StorableObject> storableObjects) {
		//Identifier id;
		switch (this.entityCode.shortValue()) {
			case ObjectEntities.MEASUREMENT_CODE:
				switch (this.linkedEntityCode) {
					case ObjectEntities.TEST_CODE:
						final Set<? extends StorableObject> measurements = new HashSet<StorableObject>(storableObjects);
						for (final Identifier id : this.linkedIds) {
							Test test;
							try {
								test = (Test) StorableObjectPool.getStorableObject(id, false);
							} catch (ApplicationException ae) {
								Log.errorException(ae);
								continue;
							}
							int testNumberOfMeasurements = 0;
							for (final Iterator<? extends StorableObject> it = measurements.iterator(); it.hasNext();) {
								final Measurement measurement = (Measurement) it.next();
								if (measurement.getTestId().equals(id)) {
									it.remove();
									testNumberOfMeasurements++;
								}
							}
							if (test == null || testNumberOfMeasurements < test.getNumberOfMeasurements())
								return true;
						}
						return false;
					default:
						return true;
				}
			default:
				return true;
		}
	}
}
