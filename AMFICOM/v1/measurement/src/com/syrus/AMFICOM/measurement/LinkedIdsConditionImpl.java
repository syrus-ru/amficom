/*-
 * $Id: LinkedIdsConditionImpl.java,v 1.72 2006/03/24 07:33:05 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ObjectEntities.ANALYSIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.DOMAIN_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.KIS_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MCM_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTSETUP_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MODELING_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.PORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.RESULT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.TEST_CODE;

import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.DomainMember;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.72 $, $Date: 2006/03/24 07:33:05 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
final class LinkedIdsConditionImpl extends LinkedIdsCondition {
	private static final long serialVersionUID = 7761036306511084255L;

	@SuppressWarnings("unused")
	private LinkedIdsConditionImpl(final Set<? extends Identifiable> linkedIdentifiables, final Short linkedEntityCode, final Short entityCode) {
		this.linkedIdentifiables = linkedIdentifiables;
		this.linkedEntityCode = linkedEntityCode.shortValue();
		this.entityCode = entityCode;
	}

	private boolean checkDomain(final DomainMember domainMember) {
		boolean condition = false;
		try {
			final Domain dmDomain = (Domain) StorableObjectPool.getStorableObject(domainMember.getDomainId(), true);
			for (final Identifiable identifiable : this.linkedIdentifiables) {
				final Identifier id = identifiable.getId();
				if (id.getMajor() == ObjectEntities.DOMAIN_CODE) {
					final Domain domain = (Domain) StorableObjectPool.getStorableObject(id, true);
					if (dmDomain.equals(domain) || dmDomain.isChild(domain)) {
						condition = true;
					}
				}
			}
		} catch (final ApplicationException ae) {
			Log.errorMessage(ae);
		}
		return condition;
	}

	/**
	 * @return <code>true</code>
	 *         <ul>
	 *         <li>if {@link #entityCode} is {@link AnalysisType}for all
	 *         analysesType for criteria ParameterTypes identifier in linkedIds;
	 *         </li>
	 *         <li>if {@link #entityCode} is {@link Analysis} for all
	 *         analysis for parent action identifier in
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
			case ANALYSIS_CODE:
				final Action action = (Action) storableObject;
				switch (this.linkedEntityCode) {
					case MEASUREMENT_CODE:
						final Identifier parentActionId = action.getParentActionId();
						condition = super.conditionTest(parentActionId);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case MEASUREMENT_CODE:
				final Measurement measurement = (Measurement) storableObject;
				switch (this.linkedEntityCode) {
					case TEST_CODE:
						condition = super.conditionTest(measurement.getTestId());
						break;
					case MONITOREDELEMENT_CODE:
						condition = super.conditionTest(measurement.getMonitoredElementId());
						break;
					case MEASUREMENTSETUP_CODE:
						condition = super.conditionTest(measurement.getSetup());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				/* measurement haven't parent action*/
				break;
			case MODELING_CODE:
				final Modeling modeling = (Modeling) storableObject;
				switch (this.linkedEntityCode) {
					case MONITOREDELEMENT_CODE:
						condition = super.conditionTest(modeling.getMonitoredElementId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case MEASUREMENTSETUP_CODE:
				final MeasurementSetup measurementSetup = (MeasurementSetup) storableObject;
				switch (this.linkedEntityCode) {
					case MONITOREDELEMENT_CODE:
						final Set<Identifier> params = new HashSet<Identifier>();
						params.addAll(measurementSetup.getMonitoredElementIds());
						condition = super.conditionTest(params);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case RESULT_CODE:
				final Result result = (Result) storableObject;
				if (this.linkedEntityCode == MEASUREMENT_CODE
						|| this.linkedEntityCode == ANALYSIS_CODE
						|| this.linkedEntityCode == MODELING_CODE) {
					try {
						for (Action a = result.getAction(); a != null; a = a.getParentAction()) {
							if (super.conditionTest(a.getId())) {
								condition = true;
								break;
							}
						}
					} catch (ApplicationException ae) {
						Log.errorMessage(ae);
					}
				} else {
					throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
							+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
							IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case TEST_CODE:
				final Test test = (Test) storableObject;
				switch (this.linkedEntityCode) {
					case MONITOREDELEMENT_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getId());
						break;
					case MEASUREMENTPORT_CODE:
						condition = super.conditionTest(test.getMonitoredElement().getMeasurementPortId());
						break;
					case MCM_CODE:
						condition = super.conditionTest(test.getMCMId());
						break;
					case MEASUREMENTSETUP_CODE:
						condition = super.conditionTest(test.getMeasurementSetupIds());
						break;
					case TEST_CODE:
						condition = super.conditionTest(test.getGroupTestId());
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case MEASUREMENTPORT_CODE:
				final MeasurementPort measurementPort = (MeasurementPort) storableObject;
				switch (this.linkedEntityCode) {
					case PORT_CODE:
						condition = super.conditionTest(measurementPort.getPortId());
						break;
					case KIS_CODE:
						condition = super.conditionTest(measurementPort.getKISId());
						break;
					case MCM_CODE:
						try {
							final KIS kis1 = (KIS) StorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = super.conditionTest(kis1.getMCMId());
						} catch (ApplicationException ae) {
							Log.errorMessage(ae);
						}
						break;
					case DOMAIN_CODE:
						try {
							final KIS kis1 = (KIS) StorableObjectPool.getStorableObject(measurementPort.getKISId(), true);
							condition = this.checkDomain(kis1);
						} catch (ApplicationException ae) {
							Log.errorMessage(ae);
						}
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case KIS_CODE:
				final KIS kis = (KIS) storableObject;
				switch (this.linkedEntityCode) {
					case MCM_CODE:
						condition = super.conditionTest(kis.getMCMId());
						break;
					case DOMAIN_CODE:
						condition = this.checkDomain(kis);
						break;
					default:
						throw new IllegalObjectEntityException(LINKED_ENTITY_CODE_NOT_REGISTERED + this.linkedEntityCode
								+ ", " + ObjectEntities.codeToString(this.linkedEntityCode),
								IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
				}
				break;
			case MONITOREDELEMENT_CODE:
				final MonitoredElement monitoredElement = (MonitoredElement) storableObject;
				switch (this.linkedEntityCode) {
					case MEASUREMENTPORT_CODE:
						condition = super.conditionTest(monitoredElement.getMeasurementPortId());
						break;
					case DOMAIN_CODE:
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
			case ANALYSIS_CODE:
			case MEASUREMENT_CODE:
			case MEASUREMENTSETUP_CODE:
			case RESULT_CODE:
			case TEST_CODE:
			case KIS_CODE:
			case MONITOREDELEMENT_CODE:
			case MEASUREMENTPORT_CODE:
				this.entityCode = entityCode;
				break;
			default:
				throw new IllegalObjectEntityException("entityCode "
						+ ObjectEntities.codeToString(entityCode.shortValue()) + " is unknown for this condition", IllegalObjectEntityException.ENTITY_NOT_REGISTERED_CODE);
		}

	}

	@Override
	public boolean isNeedMore(final Set<? extends Identifiable> identifiables) {
		//Identifier id;
		switch (this.entityCode.shortValue()) {
			case MEASUREMENT_CODE:
				switch (this.linkedEntityCode) {
					case TEST_CODE:
						return needMoreMeasurementsForTests(this.linkedIdentifiables, identifiables);
					default:
						return true;
				}
			default:
				return true;
		}
	}

	private static boolean needMoreMeasurementsForTests(final Set<? extends Identifiable> testIdentifiables,
			final Set<? extends Identifiable> measurementIdentifiables) {
		for (final Identifiable testIdentifiable : testIdentifiables) {
			Test test = null;
			if (testIdentifiable instanceof Test) {
				test = (Test) testIdentifiable;
			} else if (testIdentifiable instanceof Identifier) {
				try {
					test = StorableObjectPool.getStorableObject((Identifier) testIdentifiable, false);
				} catch (ApplicationException ae) {
					Log.errorMessage(ae);
				}
				if (test == null) {
					return true;
				}
			}

			int numberOfTestMeasurements = 0;
			for (final Identifiable measurementIdentifiable : measurementIdentifiables) {
				Measurement measurement = null;
				if (measurementIdentifiable instanceof Measurement) {
					measurement = (Measurement) measurementIdentifiable;
				} else if (measurementIdentifiable instanceof Identifier) {
					try {
						measurement = StorableObjectPool.getStorableObject((Identifier) measurementIdentifiable, false);
					} catch (ApplicationException ae) {
						Log.errorMessage(ae);
					}
				} else {
					measurement = null;
				}
				if (measurement == null) {
					continue;
				}

				if (measurement.getTestId().equals(test.getId())) {
					numberOfTestMeasurements++;
				}
			}

			if (numberOfTestMeasurements < test.getNumberOfMeasurements()) {
				return true;
			}
		}
		return false;
	}

}
