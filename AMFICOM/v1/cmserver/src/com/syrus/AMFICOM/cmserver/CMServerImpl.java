/*
 * $Id: CMServerImpl.java,v 1.9 2004/09/20 07:45:39 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
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
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2004/09/20 07:45:39 $
 * @author $Author: max $
 * @module cmserver_v1
 */
public class CMServerImpl implements CMServerOperations {

	public void receiveTests(Test_Transferable[] test_Transferables, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("Received " + test_Transferables.length + " tests", Log.DEBUGLEVEL07);
		List testList = new ArrayList(test_Transferables.length);
		try {

			for (int i = 0; i < test_Transferables.length; i++) {
				Test test = new Test(test_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(test);
				testList.add(test);
			}

			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			testDatabase.insert(testList);

		} catch (CreateObjectException e) {
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
		}
	}

	public void receiveSets(Set_Transferable[] set_Transferables, AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		/**
		 * TODO check user for access
		 */
		Log.debugMessage("Received " + set_Transferables.length + " sets", Log.DEBUGLEVEL07);
		List setList = new ArrayList(set_Transferables.length);
		try {

			for (int i = 0; i < set_Transferables.length; i++) {
				Set set = new Set(set_Transferables[i]);
				MeasurementStorableObjectPool.putStorableObject(set);
				setList.add(set);
			}

			SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
			setDatabase.insert(setList);

		} catch (CreateObjectException e) {
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
		}
	}

	public AnalysisType_Transferable transmitAnalysisType(	Identifier_Transferable identifier_Transferable,
								AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public EvaluationType_Transferable transmitEvaluationType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public MeasurementSetup_Transferable transmitMeasurementSetup(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public MeasurementType_Transferable transmitMeasurementType(	Identifier_Transferable identifier_Transferable,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public Set_Transferable transmitSet(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public Test_Transferable transmitTest(	Identifier_Transferable identifier_Transferable,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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

	public AnalysisType_Transferable[] transmitAnalysisTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {	

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
			
			AnalysisType_Transferable[] transferables = null;
			
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);

				transferables = new AnalysisType_Transferable[list.size()];
				int i = 0;
				for (Iterator it = list.iterator(); it.hasNext(); i++) {
					AnalysisType analysisType = (AnalysisType) it.next();
					transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
				}
			} else {
				List list = MeasurementStorableObjectPool
						.getStorableObjectsByDomain(ObjectEntities.ANALYSISTYPE_ENTITY_CODE,
										domain);
				
				AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext.getAnalysisTypeDatabase();
				
				List listFromDatabase = database.retrieveButIds(list);
				
				list.addAll(listFromDatabase);

				transferables = new AnalysisType_Transferable[list.size()];
				int i = 0;
				for (Iterator it = list.iterator(); it.hasNext(); i++) {
					AnalysisType analysisType = (AnalysisType) it.next();
					transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
				}
			}
			return transferables;

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
		} catch (IllegalDataException ide){
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		}

	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {

		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			EvaluationType_Transferable[] transferables = null;
			if (identifier_Transferables.length > 0) {
				List idsList = new ArrayList(identifier_Transferables.length);
				for (int i = 0; i < identifier_Transferables.length; i++)
					idsList.add(new Identifier(identifier_Transferables[i]));

				List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);				

				transferables = new EvaluationType_Transferable[list.size()];
				int i = 0;
				for (Iterator it = list.iterator(); it.hasNext(); i++) {
					EvaluationType evaluationType = (EvaluationType) it.next();
					transferables[i] = (EvaluationType_Transferable) evaluationType
							.getTransferable();
				}
			} else {
				List list = MeasurementStorableObjectPool
						.getStorableObjectsByDomain(ObjectEntities.EVALUATIONTYPE_ENTITY_CODE,
										domain);
				
				EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext.getEvaluationTypeDatabase();
				
				List listFromDatabase = database.retrieveButIds(list);
				
				list.addAll(listFromDatabase);

				transferables = new EvaluationType_Transferable[list.size()];
				int i = 0;
				for (Iterator it = list.iterator(); it.hasNext(); i++) {
					EvaluationType evaluationType = (EvaluationType) it.next();
					transferables[i] = (EvaluationType_Transferable) evaluationType
							.getTransferable();
				}

			}

			return transferables;

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
		} catch (IllegalDataException ide){
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
					.getMessage());
		}

	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(	Identifier_Transferable[] identifier_Transferables,
										AccessIdentifier_Transferable accessIdentifier)
			                            throws AMFICOMRemoteException {
        try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);
            
            MeasurementSetup_Transferable[] transferables = null;
            if(identifier_Transferables.length > 0 ) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
                transferables = new MeasurementSetup_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                    transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
                }
                
            } else {
                List list = MeasurementStorableObjectPool
                        .getStorableObjectsByDomain(ObjectEntities.MS_ENTITY_CODE,
                                        domain);
                
                MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext.getMeasurementSetupDatabase();
                List listFromDatabase = database.retrieveButIds(list);
                list.addAll(listFromDatabase);
                transferables = new MeasurementSetup_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
                    transferables[i] = (MeasurementSetup_Transferable) measurementSetup
                            .getTransferable();
                }
    
            }
            return transferables;
            
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
		} catch (IllegalDataException ide){
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        }
	}

	public MeasurementType_Transferable[] transmitMeasurementTypes(	Identifier_Transferable[] identifier_Transferables,
									AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

            MeasurementType_Transferable[] transferables = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);                

                transferables = new MeasurementType_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    MeasurementType measurementType = (MeasurementType) it.next();
                    transferables[i] = (MeasurementType_Transferable) measurementType
                            .getTransferable();
                }
            } else {
                List list = MeasurementStorableObjectPool
                        .getStorableObjectsByDomain(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE,
                                        domain);
                
                MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext.getEvaluationTypeDatabase();
                
                List listFromDatabase = database.retrieveButIds(list);
                
                list.addAll(listFromDatabase);

                transferables = new MeasurementType_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    MeasurementType measurementType = (MeasurementType) it.next();
                    transferables[i] = (MeasurementType_Transferable) measurementType
                            .getTransferable();
                }

            }

            return transferables;

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
        } catch (IllegalDataException ide){
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        }
	}

	public Set_Transferable[] transmitSets(	Identifier_Transferable[] identifier_Transferables,
						AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

            Set_Transferable[] transferables = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);                

                transferables = new Set_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    Set set = (Set) it.next();
                    transferables[i] = (Set_Transferable) set.getTransferable();
                }
            } else {
                List list = MeasurementStorableObjectPool
                        .getStorableObjectsByDomain(ObjectEntities.SET_ENTITY_CODE,
                                        domain);
                SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
                List listFromDatabase = database.retrieveButIds(list);
                list.addAll(listFromDatabase);
                transferables = new Set_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    MeasurementType measurementType = (MeasurementType) it.next();
                    transferables[i] = (Set_Transferable) measurementType
                            .getTransferable();
                }

            }

            return transferables;

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
        } catch (IllegalDataException ide){
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        }
	}

	public Test_Transferable[] transmitTests(	Identifier_Transferable[] identifier_Transferables,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		try {
            Identifier domainId = new Identifier(accessIdentifier.domain_id);
            Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

            Test_Transferable[] transferables = null;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));

                List list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);                

                transferables = new Test_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    Test test = (Test) it.next();
                    transferables[i] = (Test_Transferable) test.getTransferable();
                }
            } else {
                List list = MeasurementStorableObjectPool
                        .getStorableObjectsByDomain(ObjectEntities.TEST_ENTITY_CODE,
                                        domain);
                TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getSetDatabase();
                List listFromDatabase = database.retrieveButIds(list);
                list.addAll(listFromDatabase);
                transferables = new Test_Transferable[list.size()];
                int i = 0;
                for (Iterator it = list.iterator(); it.hasNext(); i++) {
                    Test test = (Test) it.next();
                    transferables[i] = (Test_Transferable) test.getTransferable();
                }

            }

            return transferables;

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
        } catch (IllegalDataException ide){
            Log.errorException(ide);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ide
                    .getMessage());
        }
	}

	public Test_Transferable[] transmitTestsByTime(	long startTime,
							long endTime,
							AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		List list = null;

		Test_Transferable[] transferables = null;
		try {
			Identifier domainId = new Identifier(accessIdentifier.domain_id);
			Domain domain = (Domain) ConfigurationStorableObjectPool.getStorableObject(domainId, true);

			Date start = new Date(startTime);
			Date end = new Date(endTime);
			// List<Identifier> that get from cache
			List ids = MeasurementStorableObjectPool.getTestsByTimeRange(domain, start, end);

			try {
				TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
				list = database.retrieveButIdsByTimeRange(ids, domain, start, end);
			} catch (RetrieveObjectException roe) {
				Log.errorException(roe);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
									CompletionStatus.COMPLETED_NO, roe.getMessage());
			}

			transferables = new Test_Transferable[list.size()];
			int i = 0;
			for (Iterator it = list.iterator(); it.hasNext(); i++) {
				Test test = (Test) it.next();
				transferables[i] = (Test_Transferable) test.getTransferable();
			}
		} catch (DatabaseException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		} catch (CommunicationException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e
					.getMessage());
		}
		return transferables;
	}

	///////////////////////////////////////// Identifier Generator
	// ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
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
	
	public Domain_Transferable transmitDomain(Identifier_Transferable identifier_Transferable,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
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
    
	public Domain_Transferable[] transmitDomains(Identifier_Transferable[] ids,
			AccessIdentifier_Transferable accessIdentifier)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList();
        Domain_Transferable[] idsTransfefableArray;
        try {
            for (int i = 0; i < ids.length; i++)
                idsList.add(new Identifier(ids[i]));
            List domainList = ConfigurationStorableObjectPool.getStorableObjects(idsList, true);
            int i=0;
            for (Iterator it = domainList.iterator(); it.hasNext();i++) {
				idsTransfefableArray[i]= (Domain_Transferable) it.next();
				
			}
            return idsTransfefableArray;
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
    
}
