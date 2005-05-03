/*
 * $Id: CMMeasurementTransmit.java,v 1.27 2005/05/03 14:27:02 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.CronTemporalPattern;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.IntervalsTemporalPattern;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.CronTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.IntervalsTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.PeriodicalTemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.27 $, $Date: 2005/05/03 14:27:02 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {

	private static final long serialVersionUID = 3410028455480782250L;



	/* Transmit multiple objects*/

	public MeasurementType_Transferable[] transmitMeasurementTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public ModelingType_Transferable[] transmitModelingTypes(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		ModelingType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ModelingType) it.next();
			transferables[i] = (ModelingType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Measurement_Transferable[] transmitMeasurements(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluations(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelings(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResults(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Result) it.next();
			transferables[i] = (Result_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSets(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Set) it.next();
			transferables[i] = (Set_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTests(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Test) it.next();
			transferables[i] = (Test_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatterns(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		CronTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CronTemporalPattern) it.next();
			transferables[i] = (CronTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatterns(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		IntervalsTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (IntervalsTemporalPattern) it.next();
			transferables[i] = (IntervalsTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatterns(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjects(idsT);

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		PeriodicalTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PeriodicalTemporalPattern) it.next();
			transferables[i] = (PeriodicalTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = MeasurementStorableObjectPool.getStorableObjects(ids, true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/* Transmit multiple objects but ids*/

	public MeasurementType_Transferable[] transmitMeasurementTypesButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.ANALYSISTYPE_ENTITY_CODE);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public ModelingType_Transferable[] transmitModelingTypesButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.MODELINGTYPE_ENTITY_CODE);

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		ModelingType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ModelingType) it.next();
			transferables[i] = (ModelingType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Measurement_Transferable[] transmitMeasurementsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.MEASUREMENT_ENTITY_CODE);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalysesButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.ANALYSIS_ENTITY_CODE);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.EVALUATION_ENTITY_CODE);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelingsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.MODELING_ENTITY_CODE);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResultsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.RESULT_ENTITY_CODE);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Result) it.next();
			transferables[i] = (Result_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSetsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.SET_ENTITY_CODE);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Set) it.next();
			transferables[i] = (Set_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTestsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.TEST_ENTITY_CODE);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Test) it.next();
			transferables[i] = (Test_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatternsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.CRONTEMPORALPATTERN_ENTITY_CODE);

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		CronTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CronTemporalPattern) it.next();
			transferables[i] = (CronTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatternsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.INTERVALS_TEMPORALPATTERN_ENTITY_CODE);

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		IntervalsTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (IntervalsTemporalPattern) it.next();
			transferables[i] = (IntervalsTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatternsButIds(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIds(idsT, ObjectEntities.PERIODICAL_TEMPORALPATTERN_ENTITY_CODE);

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		PeriodicalTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PeriodicalTemporalPattern) it.next();
			transferables[i] = (PeriodicalTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getObjectsButIds(Identifier_Transferable[] idsT, short entityCode) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(ids,
					new EquivalentCondition(entityCode),
					true);
			return objects;
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/* Transmit multiple objects but ids by condition*/

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public ModelingType_Transferable[] transmitModelingTypesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		ModelingType object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (ModelingType) it.next();
			transferables[i] = (ModelingType_Transferable) object.getTransferable();
		}
		return transferables;
	}



	public Measurement_Transferable[] transmitMeasurementsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Analysis_Transferable[] transmitAnalysesButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelingsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResultsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Result) it.next();
			transferables[i] = (Result_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSetsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Set) it.next();
			transferables[i] = (Set_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTestsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (Test) it.next();
			transferables[i] = (Test_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public CronTemporalPattern_Transferable[] transmitCronTemporalPatternsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[objects.size()];
		int i = 0;
		CronTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (CronTemporalPattern) it.next();
			transferables[i] = (CronTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public IntervalsTemporalPattern_Transferable[] transmitIntervalsTemporalPatternsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[objects.size()];
		int i = 0;
		IntervalsTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (IntervalsTemporalPattern) it.next();
			transferables[i] = (IntervalsTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}

	public PeriodicalTemporalPattern_Transferable[] transmitPeriodicalTemporalPatternsButIdsCondition(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT,
			StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		java.util.Set objects = this.getObjectsButIdsCondition(idsT, conditionT);

		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[objects.size()];
		int i = 0;
		PeriodicalTemporalPattern object;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			object = (PeriodicalTemporalPattern) it.next();
			transferables[i] = (PeriodicalTemporalPattern_Transferable) object.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				java.util.Set ids = Identifier.fromTransferables(idsT);
				java.util.Set objects = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	/*	Refresh*/

	public Identifier_Transferable[] transmitRefreshedMeasurementObjects(StorableObject_Transferable[] storableObjectsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		super.validateAccess(sessionKeyT);

		Map storableObjectsTMap = new HashMap();
		for (int i = 0; i < storableObjectsT.length; i++)
			storableObjectsTMap.put(new Identifier(storableObjectsT[i].id), storableObjectsT[i]);

		try {
			MeasurementStorableObjectPool.refresh();

			java.util.Set storableObjects = MeasurementStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				final StorableObject so = (StorableObject) it.next();
				final StorableObject_Transferable soT = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				// Remove objects with older versions as well as objects with the same versions.
				// Not only with older ones!
				if (!so.hasNewerVersion(soT.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (ApplicationException ae) {
			Log.errorException(ae);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, ae.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_PARTIALLY, throwable.getMessage());
		}
	}

}
