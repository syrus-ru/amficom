/*
 * $Id: CMMeasurementReceive.java,v 1.5 2005/02/14 13:50:04 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.general.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.StorableObject_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
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
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
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
 * @version $Revision: 1.5 $, $Date: 2005/02/14 13:50:04 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */
public abstract class CMMeasurementReceive extends CMConfigurationReceive {

	private static final long serialVersionUID = 2044666930827736818L;

//////////////////////////////////Measurement Receive/////////////////////////////////////////////
	public StorableObject_Transferable receiveAnalysis( Analysis_Transferable analysis_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveAnalysis | Received " + " analysis", Log.DEBUGLEVEL07);
		try {
			analysis_Transferable.header.modifier_id = accessIdentifier.user_id;
			Analysis analysis = new Analysis(analysis_Transferable);
			MeasurementStorableObjectPool.putStorableObject(analysis);
			AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
			analysisDatabase.update(analysis, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return analysis.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

  public StorableObject_Transferable[] receiveAnalyses(Analysis_Transferable[] analysis_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveAnalyses | Received " + analysis_Transferables.length + " analysisTypes", Log.DEBUGLEVEL07);
		List analysisList = new ArrayList(analysis_Transferables.length);
		try {
			for (int i = 0; i < analysis_Transferables.length; i++) {
				analysis_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Analysis analysisType = new Analysis(analysis_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(analysisType);
				analysisList.add(analysisType);
			}
			AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
			analysisDatabase.update(analysisList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(analysisList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveAnalysisType(AnalysisType_Transferable analysisType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveAnalysisType | Received " + " analysisType", Log.DEBUGLEVEL07);
		try {
			analysisType_Transferable.header.modifier_id = accessIdentifier.user_id;
			AnalysisType analysisType = new AnalysisType(analysisType_Transferable);
			MeasurementStorableObjectPool.putStorableObject(analysisType);
			AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext.getAnalysisTypeDatabase();
			analysisTypeDatabase.update(analysisType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return analysisType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveAnalysisTypes(AnalysisType_Transferable[] analysisType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
	/**
	 * TODO check user for access
	 */
		Log.debugMessage("CMMeasurementReceive.receiveAnalysisTypes | Received " + analysisType_Transferables.length
			+ " analysisTypes", Log.DEBUGLEVEL07);
		List analysisTypeList = new ArrayList(analysisType_Transferables.length);
		try {
			for (int i = 0; i < analysisType_Transferables.length; i++) {
				analysisType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				AnalysisType analysisType = new AnalysisType(analysisType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(analysisType);
				analysisTypeList.add(analysisType);
			}
		AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext.getAnalysisTypeDatabase();
		analysisTypeDatabase.update(analysisTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		return super.getListHeaders(analysisTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.cmserver.corba.CMServerOperations#receiveEvaluation(com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable, boolean, com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable)
     */
	public StorableObject_Transferable receiveEvaluation(Evaluation_Transferable evaluation_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveEvaluation | Received " + " evaluation", Log.DEBUGLEVEL07);
		try {
			evaluation_Transferable.header.modifier_id = accessIdentifier.user_id;
			Evaluation evaluation = new Evaluation(evaluation_Transferable);
			MeasurementStorableObjectPool.putStorableObject(evaluation);
			EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
			evaluationDatabase.update(evaluation, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return evaluation.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEvaluations(Evaluation_Transferable[] evaluation_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveEvaluations | Received " + evaluation_Transferables.length + " Evaluations", Log.DEBUGLEVEL07);
		List evaluationList = new ArrayList(evaluation_Transferables.length);
		try {
			for (int i = 0; i < evaluation_Transferables.length; i++) {
				evaluation_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Evaluation evaluation = new Evaluation(evaluation_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(evaluation);
				evaluationList.add(evaluation);
			}
			EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
			evaluationDatabase.update(evaluationList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(evaluationList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveEvaluationType(EvaluationType_Transferable evaluationType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveEvaluationType | Received " + " evaluationType", Log.DEBUGLEVEL07);
		try {
			evaluationType_Transferable.header.modifier_id = accessIdentifier.user_id;
			EvaluationType evaluationType = new EvaluationType(evaluationType_Transferable);
			MeasurementStorableObjectPool.putStorableObject(evaluationType);
			EvaluationTypeDatabase evaluationTypeDatabase = (EvaluationTypeDatabase) MeasurementDatabaseContext.getEvaluationTypeDatabase();
			evaluationTypeDatabase.update(evaluationType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return evaluationType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveEvaluationTypes(EvaluationType_Transferable[] evaluationType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveEvaluationTypes | Received " + evaluationType_Transferables.length + " EvaluationTypes", Log.DEBUGLEVEL07);
		List evaluationTypeList = new ArrayList(evaluationType_Transferables.length);
		try {
			for (int i = 0; i < evaluationType_Transferables.length; i++) {
				evaluationType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				EvaluationType evaluationType = new EvaluationType(evaluationType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(evaluationType);
				evaluationTypeList.add(evaluationType);
		}
		EvaluationTypeDatabase evaluationTypeDatabase = (EvaluationTypeDatabase) MeasurementDatabaseContext.getEvaluationTypeDatabase();
		evaluationTypeDatabase.update(evaluationTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		return super.getListHeaders(evaluationTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMeasurement(Measurement_Transferable measurement_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveMeasurement | Received " + " measurement", Log.DEBUGLEVEL07);
		try {
			measurement_Transferable.header.modifier_id = accessIdentifier.user_id;
			Measurement measurement = new Measurement(measurement_Transferable);
			MeasurementStorableObjectPool.putStorableObject(measurement);
			MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
			measurementDatabase.update(measurement, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return measurement.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurements(Measurement_Transferable[] measurement_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveMeasurements | Received " + measurement_Transferables.length + " Measurements", Log.DEBUGLEVEL07);
		List measurementList = new ArrayList(measurement_Transferables.length);
		try {
			for (int i = 0; i < measurement_Transferables.length; i++) {
				measurement_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Measurement measurement = new Measurement(measurement_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(measurement);
				measurementList.add(measurement);
			}
			MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
			measurementDatabase.update(measurementList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(measurementList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMeasurementSetup(MeasurementSetup_Transferable measurementSetup_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveMeasurementSetup | Received " + " measurementSetup", Log.DEBUGLEVEL07);
		try {         		
			measurementSetup_Transferable.header.modifier_id = accessIdentifier.user_id; 
			MeasurementSetup measurementSetup = new MeasurementSetup(measurementSetup_Transferable);
			MeasurementStorableObjectPool.putStorableObject(measurementSetup);
			MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext.getMeasurementSetupDatabase();
			measurementSetupDatabase.update(measurementSetup, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return measurementSetup.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
		Log.errorException(e);
		throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementSetups(MeasurementSetup_Transferable[] measurementSetup_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveMeasurementSetups | Received "
				+ measurementSetup_Transferables.length + " MeasurementSetups", Log.DEBUGLEVEL07);
		List measurementSetupList = new ArrayList(measurementSetup_Transferables.length);
		try {
			for (int i = 0; i < measurementSetup_Transferables.length; i++) {
				measurementSetup_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MeasurementSetup measurementSetup = new MeasurementSetup(measurementSetup_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(measurementSetup);
				measurementSetupList.add(measurementSetup);
			}
		MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext.getMeasurementSetupDatabase();
		measurementSetupDatabase.update(measurementSetupList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
		return super.getListHeaders(measurementSetupList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveMeasurementType(MeasurementType_Transferable measurementType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveMeasurementType | Received " + " measurementType", Log.DEBUGLEVEL07);
		try {
			measurementType_Transferable.header.modifier_id = accessIdentifier.user_id;
			MeasurementType measurementType = new MeasurementType(measurementType_Transferable);
			MeasurementStorableObjectPool.putStorableObject(measurementType);
			MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext.getMeasurementTypeDatabase();
			measurementTypeDatabase.update(measurementType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return measurementType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
		Log.errorException(e);
		throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveMeasurementTypes(MeasurementType_Transferable[] measurementType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveMeasurementTypes | Received " + measurementType_Transferables.length + " MeasurementTypes", Log.DEBUGLEVEL07);
		List measurementTypeList = new ArrayList(measurementType_Transferables.length);
		try {
			for (int i = 0; i < measurementType_Transferables.length; i++) {
				measurementType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MeasurementType measurementType = new MeasurementType(measurementType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(measurementType);
				measurementTypeList.add(measurementType);
			}
			MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext .getMeasurementTypeDatabase();
			measurementTypeDatabase.update(measurementTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(measurementTypeList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveModeling(Modeling_Transferable modeling_Transferable,
									boolean force, AccessIdentifier_Transferable accessIdentifier)
									throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveModeling | Received " + " modeling", Log.DEBUGLEVEL07);
		try {
			modeling_Transferable.header.modifier_id = accessIdentifier.user_id;
			Modeling modeling = new Modeling(modeling_Transferable);
			MeasurementStorableObjectPool.putStorableObject(modeling);
			ModelingDatabase modelingDatabase = (ModelingDatabase) MeasurementDatabaseContext.getModelingDatabase();
			modelingDatabase.update(modeling, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return modeling.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	
	public StorableObject_Transferable receiveModelingType(ModelingType_Transferable modelingType_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveModelingType | Received " + " modeling", Log.DEBUGLEVEL07);
		try {
			modelingType_Transferable.header.modifier_id = accessIdentifier.user_id;
			ModelingType modelingType = new ModelingType(modelingType_Transferable);
			MeasurementStorableObjectPool.putStorableObject(modelingType);
			ModelingTypeDatabase modelingTypeDatabase = (ModelingTypeDatabase) MeasurementDatabaseContext.getModelingTypeDatabase();
			modelingTypeDatabase.update(modelingType, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return modelingType.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}	

	public StorableObject_Transferable[] receiveModelingTypes(ModelingType_Transferable[] modelingType_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveModelingTypes | Received " + modelingType_Transferables.length
				+ " modelingTypes", Log.DEBUGLEVEL07);
		List modelingTypeList = new ArrayList(modelingType_Transferables.length);
		try {
			for (int i = 0; i < modelingType_Transferables.length; i++) {
				modelingType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				ModelingType modelingType = new ModelingType(modelingType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(modelingType);
				modelingTypeList.add(modelingType);
			}
			ModelingTypeDatabase modelingTypeDatabase = (ModelingTypeDatabase) MeasurementDatabaseContext
					.getModelingTypeDatabase();
			modelingTypeDatabase.update(modelingTypeList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(modelingTypeList);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveModelings(	Modeling_Transferable[] modeling_Transferables,
																boolean force,
																AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveModelings | Received " + modeling_Transferables.length
				+ " modelings", Log.DEBUGLEVEL07);
		List modelingList = new ArrayList(modeling_Transferables.length);
		try {
			for (int i = 0; i < modeling_Transferables.length; i++) {
				modeling_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Modeling modeling = new Modeling(modeling_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(modeling);
				modelingList.add(modeling);
			}
			ModelingDatabase modelingDatabase = (ModelingDatabase) MeasurementDatabaseContext.getModelingDatabase();
			modelingDatabase.update(modelingList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(modelingList);
		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}
	public StorableObject_Transferable receiveResult(Result_Transferable result_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveResult | Received " + " result", Log.DEBUGLEVEL07);
		try {
			result_Transferable.header.modifier_id = accessIdentifier.user_id;
			Result result = new Result(result_Transferable);
			MeasurementStorableObjectPool.putStorableObject(result);
			ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext.getResultDatabase();
			resultDatabase.update(result, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return result.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveResults(Result_Transferable[] result_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveResults | Received " + result_Transferables.length + " Results", Log.DEBUGLEVEL07);
		List resultList = new ArrayList(result_Transferables.length);
		try {
			for (int i = 0; i < result_Transferables.length; i++) {
				result_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Result result = new Result(result_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(result);
				resultList.add(result);
			}
			ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext.getResultDatabase();
			resultDatabase.update(resultList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(resultList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveTest(Test_Transferable test_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveTest | Received " + " test", Log.DEBUGLEVEL07);
		try {
			test_Transferable.header.modifier_id = accessIdentifier.user_id;
			Test test = new Test(test_Transferable);
			MeasurementStorableObjectPool.putStorableObject(test);
			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			testDatabase.update(test, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return test.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTests(Test_Transferable[] test_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveTests | Received " + test_Transferables.length + " tests", Log.DEBUGLEVEL07);
		List testList = new ArrayList(test_Transferables.length);
		try {
			for (int i = 0; i < test_Transferables.length; i++) {
				test_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Test test = new Test(test_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(test);
				testList.add(test);
			}
			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			testDatabase.update(testList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(testList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveTemporalPattern(TemporalPattern_Transferable temporalPattern_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveTemporalPattern | Received " + " temporalPattern", Log.DEBUGLEVEL07);
		try {
			temporalPattern_Transferable.header.modifier_id = accessIdentifier.user_id;
			TemporalPattern temporalPattern = new TemporalPattern(temporalPattern_Transferable);
			MeasurementStorableObjectPool.putStorableObject(temporalPattern);
			TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext.getTemporalPatternDatabase();
			temporalPatternDatabase.update(temporalPattern, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return temporalPattern.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveTemporalPatterns(TemporalPattern_Transferable[] temporalPattern_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		* TODO check user for access
		*/
		Log.debugMessage("CMMeasurementReceive.receiveTemporalPatterns | Received " + temporalPattern_Transferables.length + " TemporalPatterns", Log.DEBUGLEVEL07);
		List temporalPatternList = new ArrayList(temporalPattern_Transferables.length);
		try {
			for (int i = 0; i < temporalPattern_Transferables.length; i++) {
				temporalPattern_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				TemporalPattern temporalPattern = new TemporalPattern(temporalPattern_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(temporalPattern);
				temporalPatternList.add(temporalPattern);
			}
			TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext.getTemporalPatternDatabase();
			temporalPatternDatabase.update(temporalPatternList, new Identifier(accessIdentifier.user_id), force
					? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(temporalPatternList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable receiveSet(Set_Transferable set_Transferable,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMMeasurementReceive.receiveSet | Received " + " set", Log.DEBUGLEVEL07);
		try {
			set_Transferable.header.modifier_id = accessIdentifier.user_id;
			Set set = new Set(set_Transferable);
			MeasurementStorableObjectPool.putStorableObject(set);
			SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
			setDatabase.update(set, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return set.getHeaderTransferable();
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public StorableObject_Transferable[] receiveSets(Set_Transferable[] set_Transferables,
									boolean force,
									AccessIdentifier_Transferable accessIdentifier) throws AMFICOMRemoteException {
		/**
			* TODO check user for access
			*/
		Log.debugMessage("CMMeasurementReceive.receiveSets | Received " + set_Transferables.length + " sets", Log.DEBUGLEVEL07);
		List setList = new ArrayList(set_Transferables.length);
		try {
			for (int i = 0; i < set_Transferables.length; i++) {
				set_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Set set = new Set(set_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(set);
				setList.add(set);
			}
			SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
			setDatabase.update(setList, new Identifier(accessIdentifier.user_id), force ? StorableObjectDatabase.UPDATE_FORCE
					: StorableObjectDatabase.UPDATE_CHECK);
			return super.getListHeaders(setList);
		}
		catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (VersionCollisionException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

}
