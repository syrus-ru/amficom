/*
 * $Id: CMServerImpl.java,v 1.33 2004/10/07 11:12:17 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.DomainCondition_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.UpdateObjectException;
import com.syrus.AMFICOM.general.VersionCollisionException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.DomainCondition;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupCondition;
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
import com.syrus.AMFICOM.measurement.TestCondition;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetupCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Modeling_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.StringFieldCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestCondition_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.33 $, $Date: 2004/10/07 11:12:17 $
 * @author $Author: max $
 * @module cmserver_v1
 */

public class CMServerImpl implements CMServerOperations {
	
	private DomainCondition domainCondition;
	
	////////////////////////////////////////////Measurement Receive
	// //////////////////////////////////////////////
    public void receiveAnalysis( Analysis_Transferable analysis_Transferable, boolean force,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
        * TODO check user for access
        */
        Log.debugMessage("CMServerImpl.receiveAnalysis | Received " + " analysis", Log.DEBUGLEVEL07);
        try {
            
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
		}
	}
    
    public AnalysisType_Transferable[] transmitAnalysisTypesButIds(
            Identifier_Transferable[] ids,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        // TODO Auto-generated method stub
        return null;
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
            Evaluation Evaluation = new Evaluation(evaluation_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(Evaluation);
            evaluationList.add(Evaluation);
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
				EvaluationType EvaluationType = new EvaluationType(evaluationType_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(EvaluationType);
				evaluationTypeList.add(EvaluationType);
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
		}
	}
        
    public EvaluationType_Transferable[] transmitEvaluationTypesButIds(
            Identifier_Transferable[] ids,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        // TODO Auto-generated method stub
        return null;
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
            Measurement Measurement = new Measurement(measurement_Transferables[i]);
            MeasurementStorableObjectPool.putStorableObject(Measurement);
            measurementList.add(Measurement);
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
         } catch (CreateObjectException e) {
         Log.errorException(e);
         throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
                 .getMessage());
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
		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
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
		}
	}
    
    /* (non-Javadoc)
     * @see com.syrus.AMFICOM.cmserver.corba.CMServerOperations#receiveTemporalPattern(com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable, boolean, com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable)
     */
    public void receiveTemporalPattern(
            TemporalPattern_Transferable temporalPattern_Transferable,
            boolean force, AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        /**
         * TODO check user for access
         */
         Log.debugMessage("CMServerImpl.receiveTemporalPattern | Received " + " temporalPattern", Log.DEBUGLEVEL07);
         try {
             
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
		}
	}

	///////////// Configuration Transmit /////////////
	public Domain_Transferable transmitDomain(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitDomain | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(id, true);
			return (Domain_Transferable) domain.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public MonitoredElement_Transferable transmitMonitoredElement(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMonitoredElement | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MonitoredElement monitoredElement = (MonitoredElement) ConfigurationStorableObjectPool
					.getStorableObject(id, true);
			return (MonitoredElement_Transferable) monitoredElement.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public TransmissionPath_Transferable transmitTransmissionPath(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTransmissionPath | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			TransmissionPath transmissionPath = (TransmissionPath) ConfigurationStorableObjectPool
					.getStorableObject(id, true);
			return (TransmissionPath_Transferable) transmissionPath.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	///////////// Measurement Transmit /////////////

	public AnalysisType_Transferable transmitAnalysisType(	Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitAnalysisType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			AnalysisType analysisType = (AnalysisType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (AnalysisType_Transferable) analysisType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}
	
    public Evaluation_Transferable transmitEvaluation(
            Identifier_Transferable idTrans,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        Identifier id = new Identifier(idTrans);
        Log.debugMessage("CMServerImpl.transmitEvaluation | require " + id.toString(), Log.DEBUGLEVEL07);
        try {
            Evaluation evaluation = (Evaluation) MeasurementStorableObjectPool.getStorableObject(id, true);
            return (Evaluation_Transferable) evaluation.getTransferable();
        } catch (ObjectNotFoundException onfe) {
            Log.errorException(onfe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
                                onfe.getMessage());
        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (CommunicationException ce) {
            Log.errorException(ce);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
                    .getMessage());
        } catch (DatabaseException de) {
            Log.errorException(de);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
                    .getMessage());
        }        
    }    
    
    public Evaluation_Transferable[] transmitEvaluations(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementTypes | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Evaluation_Transferable[] transmitEvaluationsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementTypes | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.EVALUATION_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Evaluation_Transferable[] transmitEvaluationsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Evaluation_Transferable[] transferables = new Evaluation_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Evaluation evaluation = (Evaluation) it.next();
                transferables[i] = (Evaluation_Transferable) evaluation.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public EvaluationType_Transferable transmitEvaluationType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitEvaluationType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			EvaluationType evaluationType = (EvaluationType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (EvaluationType_Transferable) evaluationType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurementType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementType measurementType = (MeasurementType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (MeasurementType_Transferable) measurementType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public ParameterType_Transferable transmitParameterType(Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitParameterType | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			ParameterType parameterType = (ParameterType) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (ParameterType_Transferable) parameterType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Analysis_Transferable transmitAnalysis(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitAnalysis | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Analysis analysis = (Analysis) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Analysis_Transferable) analysis.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Modeling_Transferable transmitModeling(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitModeling | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Modeling modeling = (Modeling) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Modeling_Transferable) modeling.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Measurement_Transferable transmitMeasurement(	Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurement | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Measurement measurement = (Measurement) MeasurementStorableObjectPool.getStorableObject(id,
														true);
			return (Measurement_Transferable) measurement.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitMeasurementSetup | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			MeasurementSetup measurementSetup = (MeasurementSetup) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Result_Transferable transmitResult(	Identifier_Transferable identifier_Transferable,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitResult | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Result result = (Result) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Result_Transferable) result.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Set_Transferable transmitSet(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitSet | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Set set = (Set) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Set_Transferable) set.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public TemporalPattern_Transferable transmitTemporalPattern(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			TemporalPattern temporalPattern = (TemporalPattern) MeasurementStorableObjectPool
					.getStorableObject(id, true);
			return (TemporalPattern_Transferable) temporalPattern.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	public Test_Transferable transmitTest(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		Log.debugMessage("CMServerImpl.transmitTest | require " + id.toString(), Log.DEBUGLEVEL07);
		try {
			Test test = (Test) MeasurementStorableObjectPool.getStorableObject(id, true);
			return (Test_Transferable) test.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		}
	}

	///////////// Configuration Transmit /////////////

	public Domain_Transferable[] transmitDomains(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList();

		Log.debugMessage("CMServerImpl.transmitDomains | requiere "
				+ (identifier_Transferables.length == 0 ? "all" : Integer
						.toString(identifier_Transferables.length)) + " item(s)",
					Log.DEBUGLEVEL07);
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			List domainList = null;
			if (identifier_Transferables.length > 0) {
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				domainList = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			}

			if (domainList == null) 
				domainList = ConfigurationStorableObjectPool
				.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.DOMAIN_ENTITY_CODE), true);

			int i = 0;
			Domain_Transferable[] transferables = new Domain_Transferable[domainList.size()];
			for (Iterator it = domainList.iterator(); it.hasNext(); i++) {
				Domain domain2 = (Domain) it.next();
				transferables[i] = (Domain_Transferable) domain2.getTransferable();

			}

			return transferables;
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			Log.debugMessage("CMServerImpl.transmitMonitoredElements | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length)) + " item(s)",
						Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else 				
				list = ConfigurationStorableObjectPool
						.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ME_ENTITY_CODE), true);
			

			MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MonitoredElement monitoredElement = (MonitoredElement) it.next();
				transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
			}

			return transferables;

		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}

	public TransmissionPath_Transferable[] transmitTransmissionPaths(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			Log.debugMessage("CMServerImpl.transmitTransmissionPaths | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length)) + " item(s)",
						Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
			} else 
				list = list = ConfigurationStorableObjectPool
					.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TRANSPATH_ENTITY_CODE), true);				

			TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TransmissionPath transmissionPath = (TransmissionPath) it.next();
				transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
			}

			return transferables;

		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (CommunicationException ce) {
			Log.errorException(ce);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ce
					.getMessage());
		} catch (DatabaseException de) {
			Log.errorException(de);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, de
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitAnalysisTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);

			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSISTYPE_ENTITY_CODE), true);
			

			AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];

			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				AnalysisType analysisType = (AnalysisType) it.next();
				transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}

	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitEvaluationTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.EVALUATIONTYPE_ENTITY_CODE), true);
			
			EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				EvaluationType evaluationType = (EvaluationType) it.next();
				transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}

	}
	
	public MeasurementType_Transferable[] transmitMeasurementTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitMeasurementTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

			MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementType measurementType = (MeasurementType) it.next();
				transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public MeasurementType_Transferable[] transmitMeasurementTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementTypes | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE),idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE), true);

            MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementType measurementType = (MeasurementType) it.next();
                transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }

	public ParameterType_Transferable[] transmitParameterTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitParameterTypes | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

			ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				ParameterType parameterType = (ParameterType) it.next();
				transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public ParameterType_Transferable[] transmitParameterTypesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitParameterTypes | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

            ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                ParameterType parameterType = (ParameterType) it.next();
                transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public ParameterType_Transferable[] transmitParameterTypesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            StringFieldCondition_Transferable stringFieldCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitParameterTypes | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.PARAMETERTYPE_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.PARAMETERTYPE_ENTITY_CODE), true);

            ParameterType_Transferable[] transferables = new ParameterType_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                ParameterType parameterType = (ParameterType) it.next();
                transferables[i] = (ParameterType_Transferable) parameterType.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }

	public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitAnalyses | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);

			Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Analysis analysis = (Analysis) it.next();
				transferables[i] = (Analysis_Transferable) analysis.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public Analysis_Transferable[] transmitAnalysesButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitAnalyses | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.ANALYSIS_ENTITY_CODE), idsList, true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.ANALYSIS_ENTITY_CODE), true);

            Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Analysis analysis = (Analysis) it.next();
                transferables[i] = (Analysis_Transferable) analysis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Analysis_Transferable[] transmitAnalysesButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Analysis_Transferable[] transferables = new Analysis_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Analysis analysis = (Analysis) it.next();
                transferables[i] = (Analysis_Transferable) analysis.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
        
	public Modeling_Transferable[] transmitModelings(	Identifier_Transferable[] identifier_Transferables,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitModelings | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

			Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Modeling modeling = (Modeling) it.next();
				transferables[i] = (Modeling_Transferable) modeling.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}

	}
    
    public Modeling_Transferable[] transmitModelingsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitModelings | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.MODELING_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

            Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Modeling modeling = (Modeling) it.next();
                transferables[i] = (Modeling_Transferable) modeling.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Modeling_Transferable[] transmitModelingsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitModelings | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new DomainCondition(domainCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MODELING_ENTITY_CODE), true);

            Modeling_Transferable[] transferables = new Modeling_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Modeling modeling = (Modeling) it.next();
                transferables[i] = (Modeling_Transferable) modeling.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
	public Measurement_Transferable[] transmitMeasurements(	Identifier_Transferable[] identifier_Transferables,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitMeasurements | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

			Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Measurement measurement = (Measurement) it.next();
				transferables[i] = (Measurement_Transferable) measurement.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}

	}
    
    public Measurement_Transferable[] transmitMeasurementsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurements | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.MEASUREMENT_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MEASUREMENT_ENTITY_CODE), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Measurement_Transferable[] transmitMeasurementsButIdsLinkedCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Measurement_Transferable[] transmitMeasurementsButIdsDomainCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Measurement_Transferable[] transmitMeasurementsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Measurement_Transferable[] transferables = new Measurement_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Measurement measurement = (Measurement) it.next();
                transferables[i] = (Measurement_Transferable) measurement.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
	public MeasurementSetup_Transferable[] transmitMeasurementSetups(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitMeasurementSetups | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));
				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MS_ENTITY_CODE), true);

			MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
				transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
			}
			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitMeasurementSetups | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.MS_ENTITY_CODE),idsList, true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.MS_ENTITY_CODE), true);

            MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }

    public MeasurementSetup_Transferable[] transmitMeasurementSetupsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            MeasurementSetupCondition_Transferable measurementSetupCondition_Transferable)
            throws AMFICOMRemoteException {
        
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new MeasurementSetupCondition(measurementSetupCondition_Transferable) ,  true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new MeasurementSetupCondition(measurementSetupCondition_Transferable), true);

            MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
            }
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }    
    
    public Result_Transferable[] transmitResults(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitResults | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.RESULT_ENTITY_CODE), true);

			Result_Transferable[] transferables = new Result_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Result result = (Result) it.next();
				transferables[i] = (Result_Transferable) result.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}    
    
    public Result_Transferable[] transmitResultsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitResults | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.RESULT_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.RESULT_ENTITY_CODE), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Result_Transferable[] transmitResultsButIdsDomainCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new DomainCondition(domainCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new DomainCondition(domainCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Result_Transferable[] transmitResultsButIdsLinkedCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList,new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition( new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

            Result_Transferable[] transferables = new Result_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Result result = (Result) it.next();
                transferables[i] = (Result_Transferable) result.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Set_Transferable[] transmitSets(	Identifier_Transferable[] identifier_Transferables,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitSets | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SET_ENTITY_CODE), true);

			Set_Transferable[] transferables = new Set_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Set set = (Set) it.next();
				transferables[i] = (Set_Transferable) set.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public Set_Transferable[] transmitSetsButIds(Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitSets | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.SET_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.SET_ENTITY_CODE), true);

            Set_Transferable[] transferables = new Set_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Set set = (Set) it.next();
                transferables[i] = (Set_Transferable) set.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Set_Transferable[] transmitSetsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            DomainCondition_Transferable domainCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList,new DomainCondition(domainCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new DomainCondition(domainCondition_Transferable), true);

            Set_Transferable[] transferables = new Set_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Set set = (Set) it.next();
                transferables[i] = (Set_Transferable) set.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }

	public TemporalPattern_Transferable[] transmitTemporalPatterns(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitTemporalPatterns | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

			TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				TemporalPattern temporalPattern = (TemporalPattern) it.next();
				transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
        
    public TemporalPattern_Transferable[] transmitTemporalPatternsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTemporalPatterns | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEMPORALPATTERN_ENTITY_CODE), true);

            TemporalPattern_Transferable[] transferables = new TemporalPattern_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                TemporalPattern temporalPattern = (TemporalPattern) it.next();
                transferables[i] = (TemporalPattern_Transferable) temporalPattern.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }    

	public Test_Transferable[] transmitTests(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			Log.debugMessage("CMServerImpl.transmitTests | requiere "
					+ (identifier_Transferables.length == 0 ? "all" : Integer
							.toString(identifier_Transferables.length))
					+ " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
			List list;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

			} else 
				list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEST_ENTITY_CODE), true);

			Test_Transferable[] transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}

			return transferables;

		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
					.getMessage());
		} catch (ApplicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
		}
	}
    
    public Test_Transferable[] transmitTestsButIds(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier)
            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            Log.debugMessage("CMServerImpl.transmitTests | requiere "
                    + (identifier_Transferables.length == 0 ? "all" : Integer
                            .toString(identifier_Transferables.length))
                    + " item(s) in domain: " + domainId.toString(), Log.DEBUGLEVEL07);
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsButIds(new Short(ObjectEntities.TEST_ENTITY_CODE), idsList, true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(getDomainCondition(domain, ObjectEntities.TEST_ENTITY_CODE), true);

            Test_Transferable[] transferables = new Test_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Test test = (Test) it.next();
                transferables[i] = (Test_Transferable) test.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }
    
    public Test_Transferable[] transmitTestsButIdsCondition(
            Identifier_Transferable[] identifier_Transferables,
            AccessIdentifier_Transferable accessIdentifier,
            TestCondition_Transferable testCondition_Transferable)
            throws AMFICOMRemoteException {
        try {
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds( idsList, new TestCondition(testCondition_Transferable), true);

            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new TestCondition(testCondition_Transferable), true);

            Test_Transferable[] transferables = new Test_Transferable[list.size()];
            int i = 0;
            for (Iterator it = list.iterator(); it.hasNext(); i++) {
                Test test = (Test) it.next();
                transferables[i] = (Test_Transferable) test.getTransferable();
            }

            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (IllegalDataException ide) {
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        } catch (IllegalObjectEntityException ioee) {
            Log.errorException(ioee);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ioee
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        }
    }

	

	///////////////////////////////////////// Identifier Generator
	// ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.getGeneratedIdentifier | generate new Identifer for "
					+ entityCode, Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}

	
	private com.syrus.AMFICOM.measurement.DomainCondition getDomainCondition(Domain domain, short entityCode){
		if (this.domainCondition == null){
			this.domainCondition = new com.syrus.AMFICOM.measurement.DomainCondition(domain, new Short(entityCode));
		} else{
			this.domainCondition.setDomain(domain);
			this.domainCondition.setEntityCode(new Short(entityCode));
		}
		
		return this.domainCondition;
	}

}
