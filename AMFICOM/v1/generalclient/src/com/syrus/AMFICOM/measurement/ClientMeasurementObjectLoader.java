/*
 * $Id: ClientMeasurementObjectLoader.java,v 1.35 2005/05/04 10:42:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.HashSet;
import java.util.Iterator;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.general.AbstractClientObjectLoader;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
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
 * @version $Revision: 1.35 $, $Date: 2005/05/04 10:42:40 $
 * @author $Author: bass $
 * @module generalclient_v1
 */

public final class ClientMeasurementObjectLoader extends AbstractClientObjectLoader implements MeasurementObjectLoader {

	private CMServer								server;

	public ClientMeasurementObjectLoader(CMServer server) {
		this.server = server;
	}

	public void delete(java.util.Set ids) {
		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[ids.size()];
		int i = 0;
		for (Iterator it = ids.iterator(); it.hasNext(); i++) {
			Identifier id = (Identifier) it.next();
			identifier_Transferables[i] = (Identifier_Transferable) id.getTransferable();
		}
		try {
			this.server.deleteList(identifier_Transferables, SessionContext.getAccessIdentityTransferable());
		} catch (AMFICOMRemoteException e) {
			Log.errorException(e);
		} catch (SystemException e) {
			Log.errorException(e);
		}
	}
	
	private StorableObject fromTransferable(Identifier id, IDLEntity transferable) throws ApplicationException {
		StorableObject so = null;
		try {
			so = MeasurementStorableObjectPool.getStorableObject(id, false);
		} catch (ApplicationException e) {
			// nothing do
		}
		if (so != null)
			super.fromTransferable(so, transferable);
		return so;
	}

	public MeasurementType loadMeasurementType(Identifier id) throws ApplicationException {
		try {
			MeasurementType_Transferable mtt = this.server.transmitMeasurementType((Identifier_Transferable) id
				.getTransferable(), SessionContext.getAccessIdentityTransferable());
			MeasurementType measurementType = (MeasurementType) this.fromTransferable(id, mtt);
			if (measurementType == null)
				measurementType = new MeasurementType(mtt);
			return measurementType;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementType | server.transmitMeasurementType("
					+ id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public AnalysisType loadAnalysisType(Identifier id) throws ApplicationException {
		try {
			AnalysisType_Transferable att = this.server.transmitAnalysisType((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			AnalysisType analysisType = (AnalysisType) this.fromTransferable(id, att);
			if (analysisType == null)
				analysisType = new AnalysisType(att);
			return analysisType;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysisType | server.transmitAnalysisType("
					+ id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public EvaluationType loadEvaluationType(Identifier id) throws ApplicationException {
		try {
			EvaluationType_Transferable ett = this.server.transmitEvaluationType(
				(Identifier_Transferable) id.getTransferable(), SessionContext.getAccessIdentityTransferable());
			EvaluationType evaluationType = (EvaluationType) this.fromTransferable(id, ett);
			if (evaluationType == null)
				evaluationType = new EvaluationType(ett);
			return evaluationType;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluationType | server.transmitEvaluationType("
					+ id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Set loadSet(Identifier id) throws ApplicationException {
		try {
			Set_Transferable st = this.server.transmitSet((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Set set = (Set) this.fromTransferable(id, st);
			if (set == null)
				set = new Set(st);
			return set;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadSet | server.transmitSet(" + id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws ApplicationException {
		try {
			MeasurementSetup_Transferable mst = this.server.transmitMeasurementSetup((Identifier_Transferable) id
				.getTransferable(), SessionContext.getAccessIdentityTransferable());
			MeasurementSetup measurementSetup = (MeasurementSetup) this.fromTransferable(id, mst);
			if (measurementSetup == null)
				measurementSetup = new MeasurementSetup(mst);
			return measurementSetup;	
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurementSetup | server.transmitMeasurementSetup("
					+ id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Modeling loadModeling(Identifier id) throws ApplicationException {
		try {
			Modeling_Transferable mt = this.server.transmitModeling((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Modeling modeling = (Modeling) this.fromTransferable(id, mt);
			if (modeling == null)
				modeling = new Modeling(mt);
			return modeling;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadModeling | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public ModelingType loadModelingType(Identifier id) throws ApplicationException {
		try {
			ModelingType_Transferable mtt = this.server.transmitModelingType((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			ModelingType modelingType = (ModelingType) this.fromTransferable(id, mtt);
			if (modelingType == null)
				modelingType = new ModelingType(mtt);
			return modelingType;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadModelingType | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Measurement loadMeasurement(Identifier id) throws ApplicationException {
		try {
			Measurement_Transferable mt = this.server.transmitMeasurement((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Measurement measurement = (Measurement) this.fromTransferable(id, mt);
			if (measurement == null)
				measurement = new Measurement(mt);
			return measurement;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadMeasurement | server.transmitMeasurement(" + id.toString()
					+ ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Analysis loadAnalysis(Identifier id) throws ApplicationException {
		try {
			Analysis_Transferable at = this.server.transmitAnalysis((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Analysis analysis = (Analysis) this.fromTransferable(id, at);
			if (analysis == null)
				analysis = new Analysis(at);
			return analysis;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadAnalysis | server.transmitAnalysis(" + id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Evaluation loadEvaluation(Identifier id) throws ApplicationException {
		try {
			Evaluation_Transferable et = this.server.transmitEvaluation((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Evaluation evaluation = (Evaluation) this.fromTransferable(id, et);
			if (evaluation == null)
				evaluation = new Evaluation(et);
			return evaluation;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadEvaluation | server.transmitEvaluation(" + id.toString()
					+ ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Test loadTest(Identifier id) throws ApplicationException {
		try {
			Test_Transferable tt = this.server.transmitTest((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Test test = (Test) this.fromTransferable(id, tt);
			if (test == null)
				test = new Test(tt);
			return test;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTestObjectLoader.loadTest | server.transmitTest(" + id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public Result loadResult(Identifier id) throws ApplicationException {
		try {
			Result_Transferable rt = this.server.transmitResult((Identifier_Transferable) id.getTransferable(),
				SessionContext.getAccessIdentityTransferable());
			Result result = (Result) this.fromTransferable(id, rt);
			if (result == null)
				result = new Result(rt);
			return result;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadResult | server.transmitResult(" + id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public CronTemporalPattern loadCronTemporalPattern(Identifier id) throws ApplicationException {
		try {
			CronTemporalPattern_Transferable ctpt = this.server.transmitCronTemporalPattern((Identifier_Transferable) id.getTransferable(),
					SessionContext.getAccessIdentityTransferable());
			CronTemporalPattern cronTemporalPattern = (CronTemporalPattern) this.fromTransferable(id, ctpt);
			if (cronTemporalPattern == null)
				cronTemporalPattern = new CronTemporalPattern(ctpt);
			return cronTemporalPattern;
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadCronTemporalPattern | server.transmitCronTemporalPattern("
					+ id.toString() + ")";
			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadAnalyses(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Analysis_Transferable[] transferables = this.server.transmitAnalyses(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Analysis analysis = (Analysis) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (analysis == null)
					analysis = new Analysis(transferables[j]);
				set.add(analysis);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadAnalysesButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Analysis_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitAnalysesButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Analysis(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadAnalysisTypes(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			AnalysisType_Transferable[] transferables = this.server.transmitAnalysisTypes(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				AnalysisType analysisType = (AnalysisType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (analysisType == null)
					analysisType = new AnalysisType(transferables[j]);
				set.add(analysisType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadAnalysisTypesButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			AnalysisType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitAnalysisTypesButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new AnalysisType(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadEvaluations(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Evaluation_Transferable[] transferables = this.server.transmitEvaluations(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Evaluation evaluation = (Evaluation) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (evaluation == null)
					evaluation = new Evaluation(transferables[j]);
				set.add(evaluation);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadEvaluationsButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Evaluation_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitEvaluationsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Evaluation(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadEvaluationTypes(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			EvaluationType_Transferable[] transferables = this.server.transmitEvaluationTypes(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				EvaluationType evaluationType = (EvaluationType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (evaluationType == null)
					evaluationType = new EvaluationType(transferables[j]);
				set.add(evaluationType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadEvaluationTypesButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			EvaluationType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitEvaluationTypesButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new EvaluationType(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurements(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			Measurement_Transferable[] transferables = this.server.transmitMeasurements(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Measurement measurement = (Measurement) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (measurement == null)
					measurement = new Measurement(transferables[j]);
				set.add(measurement);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurementsButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Measurement_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}

			transferables = this.server.transmitMeasurementsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Measurement(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadModelings(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Modeling_Transferable[] transferables = this.server.transmitModelings(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Modeling modeling = (Modeling) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (modeling == null)
					modeling = new Modeling(transferables[j]);
				set.add(modeling);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadModelingTypes(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			ModelingType_Transferable[] transferables = this.server.transmitModelingTypes(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				ModelingType modelingType = (ModelingType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (modelingType == null)
					modelingType = new ModelingType(transferables[j]);
				set.add(modelingType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadModelingsButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Modeling_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitModelingsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));

			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Modeling(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadModelingTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			ModelingType_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitModelingTypesButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));

			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new ModelingType(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurementSetups(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementSetup_Transferable[] transferables = this.server.transmitMeasurementSetups(
				identifierTransferables, SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (measurementSetup == null)
					measurementSetup = new MeasurementSetup(transferables[j]);
				set.add(measurementSetup);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurementSetupsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			MeasurementSetup_Transferable[] transferables = null;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitMeasurementSetupsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));

			java.util.Set set = null;
			if (transferables != null) {
				set = new HashSet(transferables.length);
				for (int j = 0; j < transferables.length; j++) {
					set.add(new MeasurementSetup(transferables[j]));
				}

			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurementTypes(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables = this.server.transmitMeasurementTypes(
				identifierTransferables, SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				MeasurementType measurementType = (MeasurementType) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (measurementType == null)
					measurementType = new MeasurementType(transferables[j]);
				set.add(measurementType);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadMeasurementTypesButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			MeasurementType_Transferable[] transferables;
			transferables = this.server.transmitMeasurementTypesButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new MeasurementType(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadResults(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Result_Transferable[] transferables = this.server.transmitResults(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Result result = (Result) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (result == null)
					result = new Result(transferables[j]);
				set.add(result);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadResultsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Result_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitResultsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));

			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Result(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadSets(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Set_Transferable[] transferables = this.server.transmitSets(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Set set2 = (Set) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (set2 == null)
					set2 = new Set(transferables[j]);
				set.add(set2);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadSetsButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Set_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitSetsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(storableObjectCondition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Set(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadCronTemporalPatterns(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CronTemporalPattern_Transferable[] transferables = this.server.transmitCronTemporalPatterns(identifierTransferables,
					SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				CronTemporalPattern cronTemporalPattern = (CronTemporalPattern) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (cronTemporalPattern == null)
					cronTemporalPattern = new CronTemporalPattern(transferables[j]);
				set.add(cronTemporalPattern);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}
	
	public java.util.Set loadIntervalsTemporalPatterns(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			IntervalsTemporalPattern_Transferable[] transferables = this.server.transmitIntervalsTemporalPatterns(identifierTransferables,
					SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				IntervalsTemporalPattern intervalsTemporalPattern = (IntervalsTemporalPattern) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (intervalsTemporalPattern == null)
					intervalsTemporalPattern = new IntervalsTemporalPattern(transferables[j]);
				set.add(intervalsTemporalPattern);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadPeriodicalTemporalPatterns(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PeriodicalTemporalPattern_Transferable[] transferables = this.server.transmitPeriodicalTemporalPatterns(identifierTransferables,
					SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				PeriodicalTemporalPattern periodicalTemporalPattern = (PeriodicalTemporalPattern) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (periodicalTemporalPattern == null)
					periodicalTemporalPattern = new PeriodicalTemporalPattern(transferables[j]);
				set.add(periodicalTemporalPattern);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadCronTemporalPatternsButIds(StorableObjectCondition storableObjectCondition, java.util.Set ids)
			throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			CronTemporalPattern_Transferable[] transferables = this.server.transmitCronTemporalPatternsButIds(
				identifierTransferables, SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new CronTemporalPattern(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}
	
	public java.util.Set loadIntervalsTemporalPatternsButIds(StorableObjectCondition storableObjectCondition,
														java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			IntervalsTemporalPattern_Transferable[] transferables = this.server.transmitIntervalsTemporalPatternsButIds(
				identifierTransferables, SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new IntervalsTemporalPattern(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadPeriodicalTemporalPatternsButIds(StorableObjectCondition storableObjectCondition,
														java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			PeriodicalTemporalPattern_Transferable[] transferables = this.server.transmitPeriodicalTemporalPatternsButIds(
				identifierTransferables, SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new PeriodicalTemporalPattern(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadTests(java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			Test_Transferable[] transferables = this.server.transmitTests(identifierTransferables,
				SessionContext.getAccessIdentityTransferable());
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				Test test = (Test) this.fromTransferable(new Identifier(transferables[j].header.id), transferables[j]);
				if (test == null)
					test = new Test(transferables[j]);
				set.add(test);
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set loadTestsButIds(StorableObjectCondition condition, java.util.Set ids) throws ApplicationException {
		try {
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids.size()];
			Test_Transferable[] transferables;
			int i = 0;
			for (Iterator it = ids.iterator(); it.hasNext(); i++) {
				Identifier id = (Identifier) it.next();
				identifierTransferables[i] = (Identifier_Transferable) id.getTransferable();
			}
			transferables = this.server.transmitTestsButIdsCondition(identifierTransferables,
				SessionContext.getAccessIdentityTransferable(), StorableObjectConditionBuilder.getConditionTransferable(condition));
			java.util.Set set = new HashSet(transferables.length);
			for (int j = 0; j < transferables.length; j++) {
				set.add(new Test(transferables[j]));
			}
			return set;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}
	
	private void updateStorableObjectHeader(java.util.Set storableObjects, StorableObject_Transferable[] transferables) {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			Identifier_Transferable id = (Identifier_Transferable) storableObject.getId().getTransferable();
			for (int i = 0; i < transferables.length; i++) {
				if (transferables[i].id.equals(id)) {
					storableObject.updateFromHeaderTransferable(transferables[i]);
				}
			}
		}
	}

	public void saveMeasurementType(MeasurementType measurementType, boolean force) throws ApplicationException {
		MeasurementType_Transferable transferables = (MeasurementType_Transferable) measurementType.getTransferable();
		try {
			measurementType.updateFromHeaderTransferable(this.server.receiveMeasurementType(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType| receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveAnalysisType(AnalysisType analysisType, boolean force) throws VersionCollisionException,
			ApplicationException {

		AnalysisType_Transferable transferables = (AnalysisType_Transferable) analysisType.getTransferable();

		try {
			analysisType.updateFromHeaderTransferable(this.server.receiveAnalysisType(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisType | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveEvaluationType(EvaluationType evaluationType, boolean force) throws VersionCollisionException,
			ApplicationException {
		EvaluationType_Transferable transferables = (EvaluationType_Transferable) evaluationType.getTransferable();
		try {
			evaluationType.updateFromHeaderTransferable(this.server.receiveEvaluationType(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveSet(Set set, boolean force) throws VersionCollisionException, ApplicationException {
		Set_Transferable transferables = (Set_Transferable) set.getTransferable();
		try {
			set.updateFromHeaderTransferable(this.server.receiveSet(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSet | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveMeasurementSetup(MeasurementSetup measurementSetup, boolean force)
			throws VersionCollisionException, ApplicationException {
		MeasurementSetup_Transferable transferables = (MeasurementSetup_Transferable) measurementSetup
				.getTransferable();
		try {
			measurementSetup.updateFromHeaderTransferable(this.server.receiveMeasurementSetup(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementSetup | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveModeling(Modeling modeling, boolean force) throws VersionCollisionException, ApplicationException {
		Modeling_Transferable transferables = (Modeling_Transferable) modeling.getTransferable();
		try {
			modeling.updateFromHeaderTransferable(this.server.receiveModeling(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModeling | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveModelingType(ModelingType modelingType, boolean force) throws VersionCollisionException,
			ApplicationException {
		ModelingType_Transferable transferables = (ModelingType_Transferable) modelingType.getTransferable();
		try {
			modelingType.updateFromHeaderTransferable(this.server.receiveModelingType(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveModelingType | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveMeasurement(Measurement measurement, boolean force) throws VersionCollisionException,
			ApplicationException {
		Measurement_Transferable transferables = (Measurement_Transferable) measurement.getTransferable();
		try {
			measurement.updateFromHeaderTransferable(this.server.receiveMeasurement(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurement | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveAnalysis(Analysis analysis, boolean force) throws VersionCollisionException, ApplicationException {
		Analysis_Transferable transferables = (Analysis_Transferable) analysis.getTransferable();
		try {
			analysis.updateFromHeaderTransferable(this.server.receiveAnalysis(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysis | receiveAnalysiss";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveEvaluation(Evaluation evaluation, boolean force) throws VersionCollisionException,
			ApplicationException {
		Evaluation_Transferable transferables = (Evaluation_Transferable) evaluation.getTransferable();
		try {
			evaluation.updateFromHeaderTransferable(this.server.receiveEvaluation(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluation | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveTest(Test test, boolean force) throws VersionCollisionException, ApplicationException {
		Test_Transferable transferables = (Test_Transferable) test.getTransferable();
		try {
			test.updateFromHeaderTransferable(this.server.receiveTest(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTest | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveResult(Result result, boolean force) throws VersionCollisionException, ApplicationException {
		Result_Transferable transferables = (Result_Transferable) result.getTransferable();
		try {
			result.updateFromHeaderTransferable(this.server.receiveResult(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveResult | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	} 

	public void saveCronTemporalPattern(CronTemporalPattern cronTemporalPattern, boolean force) throws VersionCollisionException,
			ApplicationException {
		CronTemporalPattern_Transferable transferables = (CronTemporalPattern_Transferable) cronTemporalPattern.getTransferable();
		try {
			cronTemporalPattern.updateFromHeaderTransferable(this.server.receiveCronTemporalPattern(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveCronTemporalPattern | receiveCronTemporalPatterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveMeasurementTypes(java.util.Set measurementTypes, boolean force) throws VersionCollisionException,
			ApplicationException {
		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[measurementTypes.size()];
		int i = 0;
		for (Iterator it = measurementTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementType_Transferable) ((MeasurementType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurementTypes, this.server.receiveMeasurementTypes(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurementType | receiveMeasurementTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);

		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveAnalysisTypes(java.util.Set analysisTypes, boolean force) throws VersionCollisionException,
			ApplicationException {
		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[analysisTypes.size()];
		int i = 0;
		for (Iterator it = analysisTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (AnalysisType_Transferable) ((AnalysisType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(analysisTypes, this.server.receiveAnalysisTypes(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveAnalysisTypes | receiveAnalysisTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveEvaluationTypes(java.util.Set evaluationTypes, boolean force) throws VersionCollisionException,
			ApplicationException {
		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[evaluationTypes.size()];
		int i = 0;
		for (Iterator it = evaluationTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (EvaluationType_Transferable) ((EvaluationType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(evaluationTypes, this.server.receiveEvaluationTypes(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluationType | receiveEvaluationTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveSets(java.util.Set sets, boolean force) throws ApplicationException,
			VersionCollisionException {
		Set_Transferable[] transferables = new Set_Transferable[sets.size()];
		int i = 0;
		for (Iterator it = sets.iterator(); it.hasNext(); i++) {
			transferables[i] = (Set_Transferable) ((Set) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(sets, this.server.receiveSets(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveSets | receiveSets";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveModelings(java.util.Set modelings, boolean force) throws VersionCollisionException, ApplicationException {
		Modeling_Transferable[] transferables = new Modeling_Transferable[modelings.size()];
		int i = 0;
		for (Iterator it = modelings.iterator(); it.hasNext(); i++) {
			transferables[i] = (Modeling_Transferable) ((Modeling) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(modelings, this.server.receiveModelings(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingObjectLoader.saveModelings | receiveModelings";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveModelingTypes(java.util.Set modelingTypes, boolean force) throws VersionCollisionException, ApplicationException {
		ModelingType_Transferable[] transferables = new ModelingType_Transferable[modelingTypes.size()];
		int i = 0;
		for (Iterator it = modelingTypes.iterator(); it.hasNext(); i++) {
			transferables[i] = (ModelingType_Transferable) ((ModelingType) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(modelingTypes, this.server.receiveModelingTypes(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientModelingTypeObjectLoader.saveModelingTypes | receiveModelingTypes";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveMeasurementSetups(java.util.Set measurementSetups, boolean force) throws VersionCollisionException,
			ApplicationException {
		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[measurementSetups.size()];
		int i = 0;
		for (Iterator it = measurementSetups.iterator(); it.hasNext(); i++) {
			transferables[i] = (MeasurementSetup_Transferable) ((MeasurementSetup) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurementSetups, this.server.receiveMeasurementSetups(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementSetupObjectLoader.saveMeasurementSetups | receiveMeasurementSetups";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveMeasurements(java.util.Set measurements, boolean force) throws VersionCollisionException, ApplicationException {
		Measurement_Transferable[] transferables = new Measurement_Transferable[measurements.size()];
		int i = 0;
		for (Iterator it = measurements.iterator(); it.hasNext(); i++) {
			transferables[i] = (Measurement_Transferable) ((Measurement) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(measurements, this.server.receiveMeasurements(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveMeasurements | receiveMeasurements";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveAnalyses(java.util.Set analyses, boolean force) throws VersionCollisionException, ApplicationException {
		Analysis_Transferable[] transferables = new Analysis_Transferable[analyses.size()];
		int i = 0;
		for (Iterator it = analyses.iterator(); it.hasNext(); i++) {
			transferables[i] = (Analysis_Transferable) ((Analysis) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(analyses, this.server.receiveAnalyses(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.receiveAnalyses | receiveAnalyses";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveEvaluations(java.util.Set evaluations, boolean force) throws VersionCollisionException, ApplicationException {
		Evaluation_Transferable[] transferables = new Evaluation_Transferable[evaluations.size()];
		int i = 0;
		for (Iterator it = evaluations.iterator(); it.hasNext(); i++) {
			transferables[i] = (Evaluation_Transferable) ((Evaluation) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(evaluations, this.server.receiveEvaluations(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveEvaluations | receiveEvaluations";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveTests(java.util.Set tests, boolean force) throws ApplicationException,
			VersionCollisionException {
		Test_Transferable[] transferables = new Test_Transferable[tests.size()];
		int i = 0;
		for (Iterator it = tests.iterator(); it.hasNext(); i++) {
			transferables[i] = (Test_Transferable) ((Test) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(tests, this.server.receiveTests(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.saveTests | receiveTests";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveResults(java.util.Set results, boolean force) throws VersionCollisionException, ApplicationException {
		Result_Transferable[] transferables = new Result_Transferable[results.size()];
		int i = 0;
		for (Iterator it = results.iterator(); it.hasNext(); i++) {
			transferables[i] = (Result_Transferable) ((Result) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(results, this.server.receiveResults(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientResultObjectLoader.saveResults | receiveResults";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void saveCronTemporalPatterns(java.util.Set cronTemporalPatterns, boolean force) throws VersionCollisionException,
			ApplicationException {
		CronTemporalPattern_Transferable[] transferables = new CronTemporalPattern_Transferable[cronTemporalPatterns.size()];
		int i = 0;
		for (Iterator it = cronTemporalPatterns.iterator(); it.hasNext(); i++) {
			transferables[i] = (CronTemporalPattern_Transferable) ((CronTemporalPattern) it.next()).getTransferable();
		}
		try {
			this.updateStorableObjectHeader(cronTemporalPatterns, this.server.receiveCronTemporalPatterns(transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTemporalPaternObjectLoader.saveCronTemporalPaterns | receiveCronTemporalPaterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}
	
	public void saveIntervalsTemporalPatterns(	java.util.Set cronTemporalPatterns,
												boolean force) throws VersionCollisionException, ApplicationException {
		IntervalsTemporalPattern_Transferable[] transferables = new IntervalsTemporalPattern_Transferable[cronTemporalPatterns
				.size()];
		int i = 0;
		for (Iterator it = cronTemporalPatterns.iterator(); it.hasNext(); i++) {
			transferables[i] = (IntervalsTemporalPattern_Transferable) ((IntervalsTemporalPattern) it.next())
					.getTransferable();
		}
		try {
			this.updateStorableObjectHeader(cronTemporalPatterns, this.server.receiveIntervalsTemporalPatterns(
				transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTemporalPaternObjectLoader.saveIntervalsTemporalPaterns | receiveIntervalsTemporalPaterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public void savePeriodicalTemporalPatterns(	java.util.Set cronTemporalPatterns,
												boolean force) throws VersionCollisionException, ApplicationException {
		PeriodicalTemporalPattern_Transferable[] transferables = new PeriodicalTemporalPattern_Transferable[cronTemporalPatterns
				.size()];
		int i = 0;
		for (Iterator it = cronTemporalPatterns.iterator(); it.hasNext(); i++) {
			transferables[i] = (PeriodicalTemporalPattern_Transferable) ((PeriodicalTemporalPattern) it.next())
					.getTransferable();
		}
		try {
			this.updateStorableObjectHeader(cronTemporalPatterns, this.server.receivePeriodicalTemporalPatterns(
				transferables, force, SessionContext.getAccessIdentityTransferable()));
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientTemporalPaternObjectLoader.savePeriodicalTemporalPaterns | receivePeriodicalTemporalPaterns";

			if (e.error_code.equals(ErrorCode.ERROR_VERSION_COLLISION))
				throw new VersionCollisionException(msg, 0l, 0l);

			throw new ApplicationException(msg, e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}

	public java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException {
		try {
			java.util.Set refreshedIds = new HashSet();
			Identifier_Transferable[] identifier_Transferables;
			StorableObject_Transferable[] storableObject_Transferables = new StorableObject_Transferable[storableObjects
					.size()];
			int i = 0;
			for (Iterator it = storableObjects.iterator(); it.hasNext(); i++) {
				StorableObject storableObject = (StorableObject) it.next();
				storableObject_Transferables[i] = storableObject.getHeaderTransferable();
			}
			identifier_Transferables = this.server.transmitRefreshedMeasurementObjects(storableObject_Transferables,
				SessionContext.getAccessIdentityTransferable());

			for (int j = 0; j < identifier_Transferables.length; j++) {
				refreshedIds.add(new Identifier(identifier_Transferables[j]));
			}

			return refreshedIds;
		} catch (AMFICOMRemoteException e) {
			throw new ApplicationException(e);
		} catch (SystemException e) {
			throw new ApplicationException("System corba exception:" + e.getMessage(), e);
		}
	}
}
