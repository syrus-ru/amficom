/*
 * $Id: CMMeasurementReceive.java,v 1.14 2005/05/03 14:27:01 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.HashSet;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.SessionKey_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.CronTemporalPattern;
import com.syrus.AMFICOM.measurement.CronTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ModelingDatabase;
import com.syrus.AMFICOM.measurement.ModelingType;
import com.syrus.AMFICOM.measurement.ModelingTypeDatabase;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPattern;
import com.syrus.AMFICOM.measurement.PeriodicalTemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
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
 * @version $Revision: 1.14 $, $Date: 2005/05/03 14:27:01 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMMeasurementReceive extends CMConfigurationReceive {

	private static final long serialVersionUID = 2044666930827736818L;

//////////////////////////////////Measurement Receive/////////////////////////////////////////////

	public StorableObject_Transferable[] receiveMeasurementTypes(MeasurementType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MeasurementType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MeasurementType) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MeasurementType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MeasurementTypeDatabase database = MeasurementDatabaseContext.getMeasurementTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveAnalysisTypes(AnalysisType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			AnalysisType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (AnalysisType) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new AnalysisType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		AnalysisTypeDatabase database = MeasurementDatabaseContext.getAnalysisTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEvaluationTypes(EvaluationType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			EvaluationType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (EvaluationType) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new EvaluationType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		EvaluationTypeDatabase database = MeasurementDatabaseContext.getEvaluationTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveModelingTypes(ModelingType_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			ModelingType object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (ModelingType) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new ModelingType(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ModelingTypeDatabase database = MeasurementDatabaseContext.getModelingTypeDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}



	public StorableObject_Transferable[] receiveMeasurements(Measurement_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Measurement object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Measurement) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Measurement(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MeasurementDatabase database = MeasurementDatabaseContext.getMeasurementDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveAnalyses(Analysis_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Analysis object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Analysis) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Analysis(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		AnalysisDatabase database = MeasurementDatabaseContext.getAnalysisDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEvaluations(Evaluation_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Evaluation object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Evaluation) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Evaluation(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		EvaluationDatabase database = MeasurementDatabaseContext.getEvaluationDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveModelings(Modeling_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Modeling object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Modeling) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Modeling(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ModelingDatabase database = MeasurementDatabaseContext.getModelingDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementSetups(MeasurementSetup_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			MeasurementSetup object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (MeasurementSetup) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new MeasurementSetup(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		MeasurementSetupDatabase database = MeasurementDatabaseContext.getMeasurementSetupDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveResults(Result_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Result object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Result) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Result(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		ResultDatabase database = MeasurementDatabaseContext.getResultDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveSets(Set_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Set object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Set) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Set(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		SetDatabase database = MeasurementDatabaseContext.getSetDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTests(Test_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			Test object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (Test) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new Test(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		TestDatabase database = MeasurementDatabaseContext.getTestDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveCronTemporalPatterns(CronTemporalPattern_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			CronTemporalPattern object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (CronTemporalPattern) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new CronTemporalPattern(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		CronTemporalPatternDatabase database = MeasurementDatabaseContext.getCronTemporalPatternDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveIntervalsTemporalPatterns(IntervalsTemporalPattern_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		//TODO Implement IntervalsTemporalPatternDatabase
		return new StorableObject_Transferable[0];
//		Identifier modifierId = super.validateAccess(sessionKeyT);
//
//		java.util.Set objects = new HashSet(transferables.length);
//		for (int i = 0; i < transferables.length; i++) {
//			IntervalsTemporalPattern object = null;
//			try {
//				final Identifier id = new Identifier(transferables[i].header.id);
//				object = (IntervalsTemporalPattern) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
//				if (object == null)
//					object = new IntervalsTemporalPattern(transferables[i]);
//			}
//			catch (ApplicationException ae) {
//				Log.errorException(ae);
//			}
//
//			if (object != null)
//				objects.add(object);
//		}
//
//		IntervalsTemporalPatternDatabase database = MeasurementDatabaseContext.getIntervalsTemporalPatternDatabase();
//		try {
//			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
//			return StorableObject.createHeadersTransferable(objects);
//		}
//		catch (UpdateObjectException uoe) {
//			Log.errorException(uoe);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
//		}
//		catch (VersionCollisionException vce) {
//			Log.errorException(vce);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
//		}
//		catch (Throwable throwable) {
//			Log.errorException(throwable);
//			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
//		}
	}

	public StorableObject_Transferable[] receivePeriodicalTemporalPatterns(PeriodicalTemporalPattern_Transferable[] transferables,
			boolean force,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		Identifier modifierId = super.validateAccess(sessionKeyT);

		java.util.Set objects = new HashSet(transferables.length);
		for (int i = 0; i < transferables.length; i++) {
			PeriodicalTemporalPattern object = null;
			try {
				final Identifier id = new Identifier(transferables[i].header.id);
				object = (PeriodicalTemporalPattern) MeasurementStorableObjectPool.fromTransferable(id, transferables[i]);
				if (object == null)
					object = new PeriodicalTemporalPattern(transferables[i]);
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
			}

			if (object != null)
				objects.add(object);
		}

		PeriodicalTemporalPatternDatabase database = MeasurementDatabaseContext.getPeriodicalTemporalPatternDatabase();
		try {
			database.update(objects, modifierId, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return StorableObject.createHeadersTransferable(objects);
		}
		catch (UpdateObjectException uoe) {
			Log.errorException(uoe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, uoe.getMessage());
		}
		catch (VersionCollisionException vce) {
			Log.errorException(vce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, vce.getMessage());
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_UPDATE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
