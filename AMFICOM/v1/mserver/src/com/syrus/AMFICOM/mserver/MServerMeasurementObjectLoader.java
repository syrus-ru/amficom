/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.16 2004/12/20 08:55:01 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.configuration.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.LinkedIdsCondition;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.corba.Analysis_Transferable;
import com.syrus.AMFICOM.measurement.corba.Evaluation_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.16 $, $Date: 2004/12/20 08:55:01 $
 * @author $Author: bob $
 * @module mserver_v1
 */

public final class MServerMeasurementObjectLoader extends DatabaseMeasurementObjectLoader {
	protected static Identifier mcmId;
	protected static Object lock;
	
	static {
		lock = new Object();
	}

	public Measurement loadMeasurement(Identifier id) throws RetrieveObjectException, CommunicationException {
		Measurement measurement = null;
		try {
			measurement = new Measurement(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
					measurement.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Measurement '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve measurement '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				MeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return measurement;
	}

	public Analysis loadAnalysis(Identifier id) throws RetrieveObjectException, CommunicationException {
		Analysis analysis = null;
		try {
			analysis = new Analysis(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					analysis = new Analysis(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
					analysis.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Analysis '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve analysis '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				MeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return analysis;
	}

	public Evaluation loadEvaluation(Identifier id) throws RetrieveObjectException, CommunicationException {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(id);
		}
		catch (ObjectNotFoundException onfe) {
			Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
			if (mcmRef != null) {
				try {
					evaluation = new Evaluation(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
					evaluation.insert();
				}
				catch (org.omg.CORBA.SystemException se) {
					Log.errorException(se);
					MeasurementServer.activateMCMReferenceWithId(mcmId);
					throw new CommunicationException("System exception -- " + se.getMessage(), se);
				}
				catch (AMFICOMRemoteException are) {
					if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
						Log.errorMessage("Evaluation '" + id + "' not found on server database");
					else
						Log.errorMessage("Cannot retrieve evaluation '" + id + "' from server database -- " + are.message);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
				}
			}
			else {
				Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
				MeasurementServer.activateMCMReferenceWithId(mcmId);
			}
		}
		return evaluation;
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list;
		List copyOfList;
		Analysis analysis;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						analysis = new Analysis(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));						
						list.add(analysis);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						MeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Analysis '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve analysis '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
					Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
					MeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			AnalysisDatabase analysisDatabase = (AnalysisDatabase) MeasurementDatabaseContext.getAnalysisDatabase();
			analysisDatabase.insert(list);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list;
		List copyOfList;
		Evaluation evaluation;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						evaluation = new Evaluation(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
						list.add(evaluation);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						MeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Evaluation '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve evaluation '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
					Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
					MeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			EvaluationDatabase evaluationDatabase = (EvaluationDatabase) MeasurementDatabaseContext.getEvaluationDatabase();
			evaluationDatabase.insert(list);
		}		
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}

  public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list;
		List copyOfList;
		Measurement measurement;
		try {
			list = database.retrieveByIds(ids, null);
			copyOfList = new LinkedList(list);
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				if(ids.contains(id))
					it.remove();
			}
			for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
				Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
				com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
				if (mcmRef != null) {
					try {
						measurement = new Measurement(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
						list.add(measurement);
					}
					catch (org.omg.CORBA.SystemException se) {
						Log.errorException(se);
						MeasurementServer.activateMCMReferenceWithId(mcmId);
						throw new CommunicationException("System exception -- " + se.getMessage(), se);
					}
					catch (AMFICOMRemoteException are) {
						if (are.error_code.equals(ErrorCode.ERROR_NOT_FOUND))
							Log.errorMessage("Measurement '" + id + "' not found on server database");
						else
							Log.errorMessage("Cannot retrieve measurement '" + id + "' from server database -- " + are.message);
					}
					catch (CreateObjectException coe) {
						Log.errorException(coe);
					}
				}
				else {
						Log.errorMessage("Remote reference for MCM '" + mcmId + "' is null; will try to reactivate it");
						MeasurementServer.activateMCMReferenceWithId(mcmId);
				}
			}
			MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
			measurementDatabase.insert(list);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
		}
		return list;
	}	

	public List loadMeasurementsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Measurement_Transferable[] measurementTransferables;
														
		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
				ids2.add( ((Analysis)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()]; 
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
				identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
			measurementTransferables = mcmRef.transmitMeasurementsButIds( (LinkedIdsCondition_Transferable)((LinkedIdsCondition) condition).getTransferable() , identifierTransferables);
			list.add(measurementTransferables);
			return list;
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			MeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + ae.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadMeasurementsButIds | Illegal Storable Object: " + ae.getMessage());
		}
	}

	public List loadAnalysesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Analysis_Transferable[] analysesTransferables;

		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
					ids2.add( ((Analysis)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()];
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
					identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
										 
			analysesTransferables = mcmRef.transmitAnalysesButIds((LinkedIdsCondition_Transferable)condition.getTransferable(), identifierTransferables);
			list.add(analysesTransferables);
			return list;
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			MeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + ae.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadAnalysesButIds | Illegal Storable Object: " + ae.getMessage());
		}
	}

	public List loadEvaluationsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException {
		EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
		List list;
		List ids2 = new ArrayList(ids);
		Evaluation_Transferable[] evaluationTransferables;
		
		try {
			list = database.retrieveByCondition(ids2, condition);
			for (Iterator it = list.iterator(); it.hasNext();) {
				ids2.add( ((Analysis)it.next()).getId() );
			}
			Identifier_Transferable[] identifierTransferables = new Identifier_Transferable[ids2.size()];
			int i = 0;
			for (Iterator it = ids2.iterator(); it.hasNext(); i++) {
				identifierTransferables[i] = (Identifier_Transferable)( (Identifier) it.next() ).getTransferable();
			}
			com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
													 
			evaluationTransferables = mcmRef.transmitEvaluationsButIds((LinkedIdsCondition_Transferable)((LinkedIdsCondition) condition).getTransferable(), identifierTransferables);
			list.add(evaluationTransferables);
			return list;               
		}
		catch (org.omg.CORBA.SystemException se) {
			Log.errorException(se);
			MeasurementServer.activateMCMReferenceWithId(mcmId);
			throw new CommunicationException("System exception -- " + se.getMessage(), se);
		}
		catch (IllegalDataException e) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + e.getMessage());
		}
		catch (AMFICOMRemoteException ae) {
			Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + ae.getMessage());
			throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluationsButIds | Illegal Storable Object: " + ae.getMessage());
		}
	}	

}
