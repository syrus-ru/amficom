/*-
 * $Id: TestAddMeasurementSetup.java,v 1.1.2.1 2006/03/23 07:40:16 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import static com.syrus.AMFICOM.general.ErrorMessages.ONLY_ONE_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.MEASUREMENTPORT_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.MONITOREDELEMENT_CODE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_DESCRIPTION;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlCompoundConditionPackage.CompoundConditionSort.AND;
import static com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort.OPERATION_EQUALS;
import static com.syrus.AMFICOM.measurement.MeasurementPortTypeCodename.REFLECTOMETRY_PK7600;
import static com.syrus.AMFICOM.measurement.MeasurementTypeCodename.REFLECTOMETRY;
import static com.syrus.AMFICOM.reflectometry.ReflectometryParameterTypeCodename.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CORBACommonTest;
import com.syrus.AMFICOM.general.CompoundCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.measurement.ActionParameterTypeBinding.ParameterValueKind;
import com.syrus.util.ByteArray;

/**
 * @version $Revision: 1.1.2.1 $, $Date: 2006/03/23 07:40:16 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestAddMeasurementSetup extends TestCase {

	public TestAddMeasurementSetup(final String name) {
		super(name);
	}

	public static Test suite() {
		final CORBACommonTest commonTest = new CORBACommonTest();
		commonTest.addTestSuite(TestAddMeasurementSetup.class);
		return commonTest.createTestSetup();
	}

	public void testAdd() throws ApplicationException, IOException {
		final Identifier creatorId = LoginManager.getUserId();

		final MeasurementType measurementType = MeasurementType.valueOf(REFLECTOMETRY.stringValue());
		final MeasurementPortType measurementPortType = MeasurementPortType.valueOf(REFLECTOMETRY_PK7600.stringValue());

		final String measurementPortDescription = "MeasurementPort_1_KIS_1_Equipment_1";
		final StorableObjectCondition measurementPortCondition = new CompoundCondition(new LinkedIdsCondition(measurementPortType,
						MEASUREMENTPORT_CODE),
				AND,
				new TypicalCondition(measurementPortDescription,
						OPERATION_EQUALS,
						MEASUREMENTPORT_CODE,
						COLUMN_DESCRIPTION));
		final Set<MeasurementPort> measurementPorts = StorableObjectPool.getStorableObjectsByCondition(measurementPortCondition, true);
		if (measurementPorts.isEmpty()) {
			throw new ObjectNotFoundException("MeasurementPort " + measurementPortDescription + " of type " + measurementPortType.getCodename() + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + measurementPorts, measurementPorts.size() == 1);
		final MeasurementPort measurementPort = measurementPorts.iterator().next();

		final StorableObjectCondition monitoredElementCondition = new LinkedIdsCondition(measurementPort, MONITOREDELEMENT_CODE);
		final Set<MonitoredElement> monitoredElements = StorableObjectPool.getStorableObjectsByCondition(monitoredElementCondition, true);
		if (monitoredElements.isEmpty()) {
			throw new ObjectNotFoundException("MonitoredElement for MeasurementPort " + measurementPortDescription + " not found");
		}
		assertTrue(ONLY_ONE_EXPECTED + ": " + monitoredElements, monitoredElements.size() == 1);
		final MonitoredElement monitoredElement = monitoredElements.iterator().next();
		System.out.println("MonitoredElement: " + monitoredElement.getName());

		final Set<ActionParameterTypeBinding> actionParameterTypeBindings = ActionParameterTypeBinding.getValues(measurementType, measurementPortType);
		final Set<Identifier> actionParameterIds = new HashSet<Identifier>();
		for (final ActionParameterTypeBinding actionParameterTypeBinding : actionParameterTypeBindings) {
			final ParameterValueKind parameterValueKind = actionParameterTypeBinding.getParameterValueKind();
			final ParameterType parameterType = actionParameterTypeBinding.getParameterType();
			final String parameterTypeCodename = parameterType.getCodename();
			final StringBuffer stringBuffer = new StringBuffer("Value kind: " + parameterValueKind + ", type codename: " + parameterTypeCodename);
			final Set<ActionParameter> actionParameters = actionParameterTypeBinding.getActionParameters();
			switch (parameterValueKind) {
				case ENUMERATED:
					stringBuffer.append(", values: ");
					for (final ActionParameter actionParameter : actionParameters) {
						final String actionParameterStringValue = actionParameter.stringValue();
						stringBuffer.append(actionParameterStringValue + " ");

						if (parameterTypeCodename.equals(WAVE_LENGTH.stringValue()) && actionParameterStringValue.equals("1625")) {
							actionParameterIds.add(actionParameter.getId());
						} else if (parameterTypeCodename.equals(TRACE_LENGTH.stringValue()) && actionParameterStringValue.equals("131.072")) {
							actionParameterIds.add(actionParameter.getId());
						} else if (parameterTypeCodename.equals(RESOLUTION.stringValue()) && actionParameterStringValue.equals("8.0")) {
							actionParameterIds.add(actionParameter.getId());
						} else if (parameterTypeCodename.equals(PULSE_WIDTH_M.stringValue()) && actionParameterStringValue.equals("1000")) {
							actionParameterIds.add(actionParameter.getId());
						} else if (parameterTypeCodename.equals(INDEX_OF_REFRACTION.stringValue()) && actionParameterStringValue.equals("1.4682")) {
							actionParameterIds.add(actionParameter.getId());
						}
					}
					break;
				case CONTINUOUS:
					if (parameterTypeCodename.equals(AVERAGE_COUNT.stringValue())) {
						stringBuffer.append(", existing values: ");
						Identifier scansActionParameterId = null;
						for (final ActionParameter actionParameter : actionParameters) {
							final String actionParameterStringValue = actionParameter.stringValue();
							stringBuffer.append(actionParameterStringValue + ", id: " + actionParameter.getId().getIdentifierCode());
							if (actionParameterStringValue.equals("4000")) {
								scansActionParameterId = actionParameter.getId();
							}
						}
						if (scansActionParameterId == null) {
							scansActionParameterId = ActionParameter.createInstance(creatorId, ByteArray.toByteArray(4000), actionParameterTypeBinding.getId()).getId();
						}
						actionParameterIds.add(scansActionParameterId);
					}
					break;
				default:
					System.err.println("Illegal parameterValueKind: " + parameterValueKind);
					continue;
			}
			System.out.println(stringBuffer);
		}

		final ActionTemplate actionTemplate = ActionTemplate.createInstance(creatorId,
				"test",
				0,
				actionParameterIds,
				Collections.singleton(monitoredElement.getId()));

		final MeasurementSetup measurementSetup = MeasurementSetup.createInstance(creatorId,
				actionTemplate.getId(),
				VOID_IDENTIFIER,
				"test",
				actionTemplate.getMonitoredElementIds());

		StorableObjectPool.flush(measurementSetup, creatorId, false);
	}
}
