/*
 * $Id: MCMImplementation.java,v 1.17 2004/10/15 08:18:55 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mcm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.configuration.LinkedIdsCondition;
import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.mcm.corba.MCMPOA;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.17 $, $Date: 2004/10/15 08:18:55 $
 * @author $Author: max $
 * @module mcm_v1
 */

public class MCMImplementation extends MCMPOA {

	public MCMImplementation() {
	}

	public void receiveTests(Test_Transferable[] testsT) throws AMFICOMRemoteException {
		try{
			Log.debugMessage("Received " + testsT.length + " tests", Log.DEBUGLEVEL07);
			List testList = new ArrayList(testsT.length);
			for (int i = 0; i < testsT.length; i++) {
				try {
					Test test = new Test(testsT[i]);
					testList.add(test);
				}	
				
				catch (CreateObjectException coe) {
					Log.errorException(coe);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
				}
				catch (Exception e){
					Log.errorException(e);
					throw new AMFICOMRemoteException();
				}
			}
			
			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			try {
				testDatabase.insert(testList);
			} catch (CreateObjectException e) {
				Log.errorException(e);
				throw new AMFICOMRemoteException();
			} catch (IllegalDataException e) {
				Log.errorException(e);
				throw new AMFICOMRemoteException();
			}
			
			for (Iterator it = testList.iterator(); it.hasNext();) {
				Test test = (Test) it.next();
				MeasurementControlModule.addTest(test);			
			}
		}catch(Throwable throwable){
			Log.errorException(throwable);
			throw new AMFICOMRemoteException();			
		}
	}

	public void abortTest(Identifier_Transferable testIdT) throws AMFICOMRemoteException {
		Identifier testId = new Identifier(testIdT);
		Log.debugMessage("Received signal to abort test '" + testId + "'", Log.DEBUGLEVEL07);
		try {
			Test test = (Test)MeasurementStorableObjectPool.getStorableObject(testId, true);
			MeasurementControlModule.abortTest(test);
		}
		catch (ApplicationException ae) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, "Test '" + testIdT.identifier_string + "' not found -- " + ae.getMessage());
		}
	}

	public Measurement_Transferable transmitMeasurement(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Measurement measurement = new Measurement(id);
			return (Measurement_Transferable)measurement.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}

	public Measurement_Transferable[] transmitMeasurementsButIds(
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable,
            Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
        try {            
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);
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
    
    public Analysis_Transferable transmitAnalysis(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Analysis analysis = new Analysis(id);
			return (Analysis_Transferable)analysis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}
	    
    public Analysis_Transferable[] transmitAnalysesButIds(
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable,
            Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
               
        try {            
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

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
    
	public Evaluation_Transferable transmitEvaluation(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Evaluation evaluation = new Evaluation(id);
			return (Evaluation_Transferable)evaluation.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
	}
    
    public Evaluation_Transferable[] transmitEvaluationsButIds(
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable,
            Identifier_Transferable[] identifier_Transferables) throws AMFICOMRemoteException {
        try {            
            List list;
            if (identifier_Transferables.length > 0) {
                List idsList = new ArrayList(identifier_Transferables.length);
                for (int i = 0; i < identifier_Transferables.length; i++)
                    idsList.add(new Identifier(identifier_Transferables[i]));
                list = MeasurementStorableObjectPool.getStorableObjectsByConditionButIds(idsList, new LinkedIdsCondition(linkedIdsCondition_Transferable), true);
            } else 
                list = MeasurementStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(linkedIdsCondition_Transferable), true);

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

	public void ping(int i) {
		System.out.println("i == " + i);
	}
}
