/*
 * $Id: CMConfigurationMeasurementReceive.java,v 1.3 2004/12/17 16:31:59 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
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
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
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
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2004/12/17 16:31:59 $
 * @author $Author: bob $
 * @module module
 */
public abstract class CMConfigurationMeasurementReceive extends CMConfigurationReceive {
	static final long serialVersionUID = 2044666930827736818L;
	/**
	 * TODO set modifier_id
	 */
	//////////////////////////////////Measurement Receive/////////////////////////////////////////////
    public void receiveAnalysis( Analysis_Transferable analysis_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveAnalysis | Received " + " analysis", Log.DEBUGLEVEL07);
        try {
        	analysis_Transferable.header.modifier_id = accessIdentifier.user_id;
            Analysis analysis = new Analysis(analysis_Transferable);
            MeasurementStorableObjectPool.putStorableObject(analysis);
            AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext
                    .getAnalysisDatabase();
            analysisDatabase.update(analysis, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    public void receiveAnalyses(   Analysis_Transferable[] analysis_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveAnalyses | Received " + analysis_Transferables.length
            + " analysisTypes", Log.DEBUGLEVEL07);
        List analysisList = new ArrayList(analysis_Transferables.length);
        try {

        for (int i = 0; i < analysis_Transferables.length; i++) {
        	analysis_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            Analysis analysisType = new Analysis(analysis_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(analysisType);
            analysisList.add(analysisType);
        }

        AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext
                .getAnalysisDatabase();
        analysisDatabase.update(analysisList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveAnalysisType(   AnalysisType_Transferable analysisType_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveAnalysisType | Received " + " analysisType", Log.DEBUGLEVEL07);
        try {
        	analysisType_Transferable.header.modifier_id = accessIdentifier.user_id;
            AnalysisType analysisType = new AnalysisType(analysisType_Transferable);
            MeasurementStorableObjectPool.putStorableObject(analysisType);
            AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext
                    .getAnalysisTypeDatabase();
            analysisTypeDatabase.update(analysisType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    public void receiveAnalysisTypes(	AnalysisType_Transferable[] analysisType_Transferables, boolean force,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveAnalysisTypes | Received " + analysisType_Transferables.length
				+ " analysisTypes", Log.DEBUGLEVEL07);
		List analysisTypeList = new ArrayList(analysisType_Transferables.length);
		try {

			for (int i = 0; i < analysisType_Transferables.length; i++) {
				analysisType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				AnalysisType analysisType = new AnalysisType(analysisType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(analysisType);
				analysisTypeList.add(analysisType);
			}

			AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext
					.getAnalysisTypeDatabase();
			analysisTypeDatabase.update(analysisTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}


    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.cmserver.corba.CMServerOperations#receiveEvaluation(com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable, boolean, com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable)
     */
    public void receiveEvaluation(Evaluation_Transferable evaluation_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveEvaluation | Received " + " evaluation", Log.DEBUGLEVEL07);
         try {
         	evaluation_Transferable.header.modifier_id = accessIdentifier.user_id;
         	Evaluation evaluation = new Evaluation(evaluation_Transferable);
	        MeasurementStorableObjectPool.putStorableObject(evaluation);
	        EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext
	                .getEvaluationDatabase();
	        evaluationDatabase.update(evaluation, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveEvaluations( Evaluation_Transferable[] evaluation_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveEvaluations | Received "
            + evaluation_Transferables.length + " Evaluations", Log.DEBUGLEVEL07);
        List evaluationList = new ArrayList(evaluation_Transferables.length);
        try {

        for (int i = 0; i < evaluation_Transferables.length; i++) {
        	evaluation_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            Evaluation evaluation = new Evaluation(evaluation_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(evaluation);
            evaluationList.add(evaluation);
        }

        EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext
                .getEvaluationDatabase();
        evaluationDatabase.update(evaluationList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        }  catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveEvaluationType(EvaluationType_Transferable evaluationType_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveEvaluationType | Received " + " evaluationType", Log.DEBUGLEVEL07);
         try {
         	evaluationType_Transferable.header.modifier_id = accessIdentifier.user_id;
            EvaluationType evaluationType = new EvaluationType(evaluationType_Transferable);
            MeasurementStorableObjectPool.putStorableObject(evaluationType);
            EvaluationTypeDatabase evaluationTypeDatabase = (EvaluationTypeDatabase) MeasurementDatabaseContext
                    .getEvaluationTypeDatabase();
            evaluationTypeDatabase.update(evaluationType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveEvaluationTypes(	EvaluationType_Transferable[] evaluationType_Transferables, boolean force,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveEvaluationTypes | Received "
				+ evaluationType_Transferables.length + " EvaluationTypes", Log.DEBUGLEVEL07);
		List evaluationTypeList = new ArrayList(evaluationType_Transferables.length);
		try {

			for (int i = 0; i < evaluationType_Transferables.length; i++) {
				evaluationType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				EvaluationType evaluationType = new EvaluationType(evaluationType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(evaluationType);
				evaluationTypeList.add(evaluationType);
			}

			EvaluationTypeDatabase evaluationTypeDatabase = (EvaluationTypeDatabase) MeasurementDatabaseContext
					.getEvaluationTypeDatabase();
			evaluationTypeDatabase.update(evaluationTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		}  catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveMeasurement(
            Measurement_Transferable measurement_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveMeasurement | Received " + " measurement", Log.DEBUGLEVEL07);
         try {
         	measurement_Transferable.header.modifier_id = accessIdentifier.user_id;
            Measurement measurement = new Measurement(measurement_Transferable);
            MeasurementStorableObjectPool.putStorableObject(measurement);
            MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext
                    .getMeasurementDatabase();
            measurementDatabase.update(measurement, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMeasurements( Measurement_Transferable[] measurement_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveMeasurements | Received "
            + measurement_Transferables.length + " Measurements", Log.DEBUGLEVEL07);
        List measurementList = new ArrayList(measurement_Transferables.length);
        try {

        for (int i = 0; i < measurement_Transferables.length; i++) {
        	measurement_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            Measurement measurement = new Measurement(measurement_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(measurement);
            measurementList.add(measurement);
        }

        MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext
                .getMeasurementDatabase();
        measurementDatabase.update(measurementList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        }  catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMeasurementSetup(
            MeasurementSetup_Transferable measurementSetup_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveMeasurementSetup | Received " + " measurementSetup", Log.DEBUGLEVEL07);
         try {         		
         	measurementSetup_Transferable.header.modifier_id = accessIdentifier.user_id; 
         	MeasurementSetup measurementSetup = new MeasurementSetup(measurementSetup_Transferable);
         	MeasurementStorableObjectPool.putStorableObject(measurementSetup);
         	MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();
         	measurementSetupDatabase.update(measurementSetup, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }

    }

    public void receiveMeasurementSetups(    MeasurementSetup_Transferable[] measurementSetup_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveMeasurementSetups | Received "
            + measurementSetup_Transferables.length + " MeasurementSetups", Log.DEBUGLEVEL07);
        List measurementSetupList = new ArrayList(measurementSetup_Transferables.length);
        try {

        for (int i = 0; i < measurementSetup_Transferables.length; i++) {
        	measurementSetup_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            MeasurementSetup measurementSetup = new MeasurementSetup(measurementSetup_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(measurementSetup);
            measurementSetupList.add(measurementSetup);
        }

        MeasurementSetupDatabase measurementSetupDatabase = (MeasurementSetupDatabase) MeasurementDatabaseContext
                .getMeasurementSetupDatabase();
        measurementSetupDatabase.update(measurementSetupList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        }  catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveMeasurementType(
            MeasurementType_Transferable measurementType_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveMeasurementType | Received " + " measurementType", Log.DEBUGLEVEL07);
         try {
         	measurementType_Transferable.header.modifier_id = accessIdentifier.user_id;
         	MeasurementType measurementType = new MeasurementType(measurementType_Transferable);
         	MeasurementStorableObjectPool.putStorableObject(measurementType);
         	MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();
         	measurementTypeDatabase.update(measurementType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public void receiveMeasurementTypes(	MeasurementType_Transferable[] measurementType_Transferables, boolean force,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveMeasurementTypes | Received "
				+ measurementType_Transferables.length + " MeasurementTypes", Log.DEBUGLEVEL07);
		List measurementTypeList = new ArrayList(measurementType_Transferables.length);
		try {

			for (int i = 0; i < measurementType_Transferables.length; i++) {
				measurementType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				MeasurementType measurementType = new MeasurementType(measurementType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(measurementType);
				measurementTypeList.add(measurementType);
			}

			MeasurementTypeDatabase measurementTypeDatabase = (MeasurementTypeDatabase) MeasurementDatabaseContext
					.getMeasurementTypeDatabase();
			measurementTypeDatabase.update(measurementTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		}  catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveModeling(Modeling_Transferable modeling_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveModeling | Received " + " modeling", Log.DEBUGLEVEL07);
         try {
         	modeling_Transferable.header.modifier_id = accessIdentifier.user_id;
            Modeling modeling = new Modeling(modeling_Transferable);
            MeasurementStorableObjectPool.putStorableObject(modeling);
            ModelingDatabase modelingDatabase = (ModelingDatabase) MeasurementDatabaseContext
                   .getModelingDatabase();
            modelingDatabase.update(modeling, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveModelings(    Modeling_Transferable[] modeling_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveModelings | Received "
            + modeling_Transferables.length + " Modelings", Log.DEBUGLEVEL07);
        List modelingList = new ArrayList(modeling_Transferables.length);
        try {

        for (int i = 0; i < modeling_Transferables.length; i++) {
        	modeling_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            Modeling modeling = new Modeling(modeling_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(modeling);
            modelingList.add(modeling);
        }

        ModelingDatabase modelingDatabase = (ModelingDatabase) MeasurementDatabaseContext
                .getModelingDatabase();
        modelingDatabase.update(modelingList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        }  catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveResult(Result_Transferable result_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveResult | Received " + " result", Log.DEBUGLEVEL07);
         try {
         	result_Transferable.header.modifier_id = accessIdentifier.user_id;
            Result result = new Result(result_Transferable);
            MeasurementStorableObjectPool.putStorableObject(result);
            ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext
                    .getResultDatabase();
            resultDatabase.update(result, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveResults(  Result_Transferable[] result_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveResults | Received " + result_Transferables.length
            + " Results", Log.DEBUGLEVEL07);
        List resultList = new ArrayList(result_Transferables.length);
        try {

        for (int i = 0; i < result_Transferables.length; i++) {
        	result_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            Result result = new Result(result_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(result);
            resultList.add(result);
        }

        ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext
                .getResultDatabase();
        resultDatabase.update(resultList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        } catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveParameterType(
            ParameterType_Transferable parameterType_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveParameterType | Received " + " parameterType", Log.DEBUGLEVEL07);
         try {
         	parameterType_Transferable.header.modifier_id = accessIdentifier.user_id;
            ParameterType parameterType = new ParameterType(parameterType_Transferable);
            MeasurementStorableObjectPool.putStorableObject(parameterType);
            ParameterTypeDatabase parameterTypeDatabase = (ParameterTypeDatabase) MeasurementDatabaseContext
                    .getParameterTypeDatabase();
            parameterTypeDatabase.update(parameterType, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveParameterTypes(	ParameterType_Transferable[] parameterType_Transferables, boolean force,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveParameterTypes | Received " + parameterType_Transferables.length
				+ " ParameterTypes", Log.DEBUGLEVEL07);
		List parameterTypeList = new ArrayList(parameterType_Transferables.length);
		try {

			for (int i = 0; i < parameterType_Transferables.length; i++) {
				parameterType_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				ParameterType parameterType = new ParameterType(parameterType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(parameterType);
				parameterTypeList.add(parameterType);
			}

			ParameterTypeDatabase parameterTypeDatabase = (ParameterTypeDatabase) MeasurementDatabaseContext
					.getParameterTypeDatabase();
			parameterTypeDatabase.update(parameterTypeList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveTest(Test_Transferable test_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveTest | Received " + " test", Log.DEBUGLEVEL07);
         try {
         	test_Transferable.header.modifier_id = accessIdentifier.user_id;
            Test test = new Test(test_Transferable);
            MeasurementStorableObjectPool.putStorableObject(test);
            TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext
                    .getTestDatabase();
            testDatabase.update(test, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveTests(Test_Transferable[] test_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveTests | Received " + test_Transferables.length + " tests",
					Log.DEBUGLEVEL07);
		List testList = new ArrayList(test_Transferables.length);
		try {

			for (int i = 0; i < test_Transferables.length; i++) {
				test_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Test test = new Test(test_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(test);
				testList.add(test);
			}

			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			testDatabase.update(testList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

    public void receiveTemporalPattern(
            TemporalPattern_Transferable temporalPattern_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveTemporalPattern | Received " + " temporalPattern", Log.DEBUGLEVEL07);
         try {
         	temporalPattern_Transferable.header.modifier_id = accessIdentifier.user_id;
            TemporalPattern temporalPattern = new TemporalPattern(temporalPattern_Transferable);
            MeasurementStorableObjectPool.putStorableObject(temporalPattern);
            TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
                    .getTemporalPatternDatabase();
            temporalPatternDatabase.update(temporalPattern, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveTemporalPatterns(    TemporalPattern_Transferable[] temporalPattern_Transferables, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveTemporalPatterns | Received "
            + temporalPattern_Transferables.length + " TemporalPatterns", Log.DEBUGLEVEL07);
        List temporalPatternList = new ArrayList(temporalPattern_Transferables.length);
        try {

        for (int i = 0; i < temporalPattern_Transferables.length; i++) {
        	temporalPattern_Transferables[i].header.modifier_id = accessIdentifier.user_id;
            TemporalPattern temporalPattern = new TemporalPattern(temporalPattern_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(temporalPattern);
            temporalPatternList.add(temporalPattern);
        }

        TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
                .getTemporalPatternDatabase();
        temporalPatternDatabase.update(temporalPatternList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

        }  catch (UpdateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (IllegalDataException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (IllegalObjectEntityException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (VersionCollisionException e){
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                            CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (CreateObjectException e) {
        Log.errorException(e);
        throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                .getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveSet(Set_Transferable set_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveSet | Received " + " set", Log.DEBUGLEVEL07);
         try {
         	set_Transferable.header.modifier_id = accessIdentifier.user_id;
         	Set set = new Set(set_Transferable);
         	MeasurementStorableObjectPool.putStorableObject(set);
         	SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext
				.getSetDatabase();
         	setDatabase.update(set, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

         } catch (UpdateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (IllegalDataException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (IllegalObjectEntityException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (VersionCollisionException e){
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
                                             CompletionStatus.COMPLETED_NO, e.getMessage());
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
         } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

    public void receiveSets(Set_Transferable[] set_Transferables, boolean force, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("CMServerImpl.receiveSets | Received " + set_Transferables.length + " sets",
					Log.DEBUGLEVEL07);
		List setList = new ArrayList(set_Transferables.length);
		try {

			for (int i = 0; i < set_Transferables.length; i++) {
				set_Transferables[i].header.modifier_id = accessIdentifier.user_id;
				Set set = new Set(set_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(set);
				setList.add(set);
			}

			SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
			setDatabase.update(setList, force ? StorableObjectDatabase.UPDATE_FORCE : StorableObjectDatabase.UPDATE_CHECK, null);

		} catch (UpdateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (IllegalObjectEntityException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (VersionCollisionException e){
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_VERSION_COLLISION,
												CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

}
