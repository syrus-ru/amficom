/*
 * $Id: CMMeasurementTransmit.java,v 1.20 2005/04/13 17:03:08 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.syrus.AMFICOM.general.AccessIdentity;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ModelingType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.20 $, $Date: 2005/04/13 17:03:08 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMMeasurementTransmit extends CMConfigurationTransmit {

	private static final long serialVersionUID = 3410028455480782250L;

	public AnalysisType_Transferable transmitAnalysisType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (AnalysisType_Transferable) analysisType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public EvaluationType_Transferable transmitEvaluationType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementType | require '" + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (MeasurementType_Transferable) measurementType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public ModelingType_Transferable transmitModelingType(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMMeasurementTransmit.transmitModelingType | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			ModelingType modeling = (ModelingType) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (ModelingType_Transferable) modeling.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}





	public Analysis_Transferable transmitAnalysis(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysis | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Analysis analysis = (Analysis) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Analysis_Transferable) analysis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Evaluation_Transferable transmitEvaluation(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluation | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Evaluation evaluation = (Evaluation) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Evaluation_Transferable) evaluation.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Modeling_Transferable transmitModeling(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModeling | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Modeling modeling = (Modeling) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Modeling_Transferable) modeling.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Measurement_Transferable transmitMeasurement(Identifier_Transferable identifier_Transferable,
														AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurement | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Measurement measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Measurement_Transferable) measurement.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe
					.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetup | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Result_Transferable transmitResult(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitResult | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Result result = (Result) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Result_Transferable) result.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Set_Transferable transmitSet(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitSet | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Set set = (Set) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Set_Transferable) set.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public TemporalPattern_Transferable transmitTemporalPattern(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTest | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			TemporalPattern temporalPattern = (TemporalPattern) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (TemporalPattern_Transferable) temporalPattern.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public Test_Transferable transmitTest(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTest | require " + id.toString()
				+ "' for user '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Test test = (Test) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Test_Transferable) test.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}







	public AnalysisType_Transferable[] transmitAnalysisTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType analysisType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysisType = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType evaluationType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluationType = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		return transferables;
	}

	public MeasurementType_Transferable[] transmitMeasurementTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType measurementType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementType = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
		}
		return transferables;
	}

	public ModelingType_Transferable[] transmitModelingTypes(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModelingTypes | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		ModelingType modelingType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			modelingType = (ModelingType) it.next();
			transferables[i] = (ModelingType_Transferable) modelingType.getTransferable();
		}
		return transferables;
	}






	public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalyses | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluations(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluations | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}

	public Measurement_Transferable[] transmitMeasurements(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurements | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetups | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup measurementSetup;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementSetup = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelings(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModelings | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling modeling;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			modeling = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) modeling.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResults(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitResults | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result result;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			result = (Result) it.next();
			transferables[i] = (Result_Transferable) result.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSets(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitSets | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set set;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			set = (Set) it.next();
			transferables[i] = (Set_Transferable) set.getTransferable();
		}
		return transferables;
	}

	public TemporalPattern_Transferable[] transmitTemporalPatterns(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTemporalPatterns | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[objects.size()];
		int i = 0;
		TemporalPattern temporalPattern;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			temporalPattern = (TemporalPattern) it.next();
			transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTests(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTests | requiered " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjects(identifier_Transferables);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test test;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			test = (Test) it.next();
			transferables[i] = (Test_Transferable) test.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getObjects(Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(identifier_Transferables);
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





	public AnalysisType_Transferable[] transmitAnalysisTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.ANALYSISTYPE_ENTITY_CODE);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType analysisType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysisType = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType evaluationType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluationType = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		return transferables;
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType measurementType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementType = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
		}
		return transferables;
	}






	public Analysis_Transferable[] transmitAnalysesButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysesButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.ANALYSIS_ENTITY_CODE);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.EVALUATION_ENTITY_CODE);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}

	public Measurement_Transferable[] transmitMeasurementsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MEASUREMENT_ENTITY_CODE);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelingsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModelingsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MODELING_ENTITY_CODE);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling modeling;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			modeling = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) modeling.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetupsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.MS_ENTITY_CODE);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup measurementSetup;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementSetup = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResultsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.RESULT_ENTITY_CODE);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result result;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			result = (Result) it.next();
			transferables[i] = (Result_Transferable) result.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSetsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitSetsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.SET_ENTITY_CODE);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set set;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			set = (Set) it.next();
			transferables[i] = (Set_Transferable) set.getTransferable();
		}
		return transferables;
	}

	public TemporalPattern_Transferable[] transmitTemporalPatternsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTemporalPatternsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE);

		TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[objects.size()];
		int i = 0;
		TemporalPattern temporalPattern;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			temporalPattern = (TemporalPattern) it.next();
			transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTestsButIds(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTestsButIds | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIds(identifier_Transferables, ObjectEntities.TEST_ENTITY_CODE);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test test;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			test = (Test) it.next();
			transferables[i] = (Test_Transferable) test.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getObjectsButIds(Identifier_Transferable[] identifier_Transferables, short entityCode) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(identifier_Transferables);
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





	public AnalysisType_Transferable[] transmitAnalysisTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysisTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[objects.size()];
		int i = 0;
		AnalysisType analysisType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysisType = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[objects.size()];
		int i = 0;
		EvaluationType evaluationType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluationType = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		return transferables;
	}

	public MeasurementType_Transferable[] transmitMeasurementTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[objects.size()];
		int i = 0;
		MeasurementType measurementType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementType = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
		}
		return transferables;
	}

	public ModelingType_Transferable[] transmitModelingTypesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModelingTypesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		ModelingType_Transferable[] transferables = new ModelingType_Transferable[objects.size()];
		int i = 0;
		ModelingType modelingType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			modelingType = (ModelingType) it.next();
			transferables[i] = (ModelingType_Transferable) modelingType.getTransferable();
		}
		return transferables;
	}





	public Analysis_Transferable[] transmitAnalysesButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitAnalysesButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Analysis_Transferable[] transferables = new Analysis_Transferable[objects.size()];
		int i = 0;
		Analysis analysis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			analysis = (Analysis) it.next();
			transferables[i] = (Analysis_Transferable) analysis.getTransferable();
		}
		return transferables;
	}

	public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitEvaluationsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Evaluation_Transferable[] transferables = new Evaluation_Transferable[objects.size()];
		int i = 0;
		Evaluation evaluation;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			evaluation = (Evaluation) it.next();
			transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
		}
		return transferables;
	}

	public Modeling_Transferable[] transmitModelingsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitModelingsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Modeling_Transferable[] transferables = new Modeling_Transferable[objects.size()];
		int i = 0;
		Modeling modeling;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			modeling = (Modeling) it.next();
			transferables[i] = (Modeling_Transferable) modeling.getTransferable();
		}
		return transferables;
	}

	public Measurement_Transferable[] transmitMeasurementsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Measurement_Transferable[] transferables = new Measurement_Transferable[objects.size()];
		int i = 0;
		Measurement measurement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurement = (Measurement) it.next();
			transferables[i] = (Measurement_Transferable) measurement.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitMeasurementSetupsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[objects.size()];
		int i = 0;
		MeasurementSetup measurementSetup;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementSetup = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		return transferables;
	}

	public Result_Transferable[] transmitResultsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitResultsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Result_Transferable[] transferables = new Result_Transferable[objects.size()];
		int i = 0;
		Result result;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			result = (Result) it.next();
			transferables[i] = (Result_Transferable) result.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSetsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitSetsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Set_Transferable[] transferables = new Set_Transferable[objects.size()];
		int i = 0;
		Set set;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			set = (Set) it.next();
			transferables[i] = (Set_Transferable) set.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTestsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			AccessIdentifier_Transferable accessIdentifier,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitTestsButIdsCondition | All, but " + identifier_Transferables.length
				+ " item(s) for '" + accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);

		java.util.Set objects = this.getObjectsButIdsCondition(identifier_Transferables, condition_Transferable);

		Test_Transferable[] transferables = new Test_Transferable[objects.size()];
		int i = 0;
		Test test;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			test = (Test) it.next();
			transferables[i] = (Test_Transferable) test.getTransferable();
		}
		return transferables;
	}

	private java.util.Set getObjectsButIdsCondition(Identifier_Transferable[] identifier_Transferables,
			StorableObjectCondition_Transferable condition_Transferable) throws AMFICOMRemoteException {

		StorableObjectCondition condition = null;
		try {
			condition = StorableObjectConditionBuilder.restoreCondition(condition_Transferable);
		}
		catch (IllegalDataException ide) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
					CompletionStatus.COMPLETED_NO,
					"Cannot restore condition -- " + ide.getMessage());
		}

		try {
			java.util.Set ids = Identifier.fromTransferables(identifier_Transferables);
			java.util.Set objects = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
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





	// Refresh objects from a pool
	public Identifier_Transferable[] transmitRefreshedMeasurementObjects(StorableObject_Transferable[] storableObjects_Transferables,
			AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		AccessIdentity accessIdentity = new AccessIdentity(accessIdentifier);
		Log.debugMessage("CMMeasurementTransmit.transmitRefreshedMeasurementObjects | Refreshing for user '"
				+ accessIdentity.getUserId() + "'", Log.DEBUGLEVEL07);
		try {
			Map storableObjectsTMap = new HashMap();
			for (int i = 0; i < storableObjects_Transferables.length; i++)
				storableObjectsTMap.put(new Identifier(storableObjects_Transferables[i].id), storableObjects_Transferables[i]);

			MeasurementStorableObjectPool.refresh();
			java.util.Set storableObjects = MeasurementStorableObjectPool.getStorableObjects(storableObjectsTMap.keySet(), true);
			for (Iterator it = storableObjects.iterator(); it.hasNext();) {
				StorableObject so = (StorableObject) it.next();
				StorableObject_Transferable sot = (StorableObject_Transferable) storableObjectsTMap.get(so.getId());
				//Remove objects with older versions as well as objects with the same versions -- not only with older ones!
				if (!so.hasNewerVersion(sot.version))
					it.remove();
			}

			return Identifier.createTransferables(storableObjects);
		}
		catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce.getMessage());
		}
		catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de.getMessage());
		}
		catch (Throwable e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
}
