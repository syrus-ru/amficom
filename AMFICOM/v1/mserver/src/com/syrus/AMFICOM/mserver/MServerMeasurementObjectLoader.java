/*
 * $Id: MServerMeasurementObjectLoader.java,v 1.4 2004/09/27 12:24:36 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.measurement.AnalysisDatabase;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationDatabase;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.Modeling;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.ParameterTypeDatabase;
import com.syrus.AMFICOM.measurement.ResultDatabase;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2004/09/27 12:24:36 $
 * @author $Author: bob $
 * @module mserver_v1
 */

public class MServerMeasurementObjectLoader implements MeasurementObjectLoader {
	protected static Identifier mcmId;
	protected static Object lock;
	
	static {
		lock = new Object();
	}

	public MServerMeasurementObjectLoader() {
	}

	public ParameterType loadParameterType(Identifier id) throws DatabaseException {
		return new ParameterType(id);
	}

	public MeasurementType loadMeasurementType(Identifier id) throws DatabaseException {
		return new MeasurementType(id);
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException {
		return new AnalysisType(id);
	}

	public EvaluationType loadEvaluationType(Identifier id) throws DatabaseException {
		return new EvaluationType(id);
	}

	public Set loadSet(Identifier id) throws DatabaseException {
		return new Set(id);
	}

	public Modeling loadModeling(Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MServerMeasurementObjectLoader.loadModeling | mserver doesn't need in modeling");		
	}
	
	public MeasurementSetup loadMeasurementSetup(Identifier id) throws DatabaseException {
		return new MeasurementSetup(id);
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
					measurement = Measurement.getInstance(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
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
					analysis = Analysis.getInstance(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
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
					evaluation = Evaluation.getInstance(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
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

	public Test loadTest(Identifier id) throws DatabaseException {
		return new Test(id);
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		return new Result(id);
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws DatabaseException {
		return new TemporalPattern(id);
	}
    
	public List loadAnalyses(List ids) throws DatabaseException,
			CommunicationException {
		AnalysisDatabase database = (AnalysisDatabase)MeasurementDatabaseContext.getAnalysisDatabase();
        List list;
        List copyOfList;
        Analysis analysis;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
				Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Analysis '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
                com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
                if (mcmRef != null) {
                    try {
                        analysis = Analysis.getInstance(mcmRef.transmitAnalysis((Identifier_Transferable)id.getTransferable()));
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadAnalyses | Illegal Storable Object: " + e.getMessage());
        }
        return list;
	}
    
    public List loadAnalysisTypes(List ids) throws DatabaseException,
            CommunicationException {
    	AnalysisTypeDatabase database = (AnalysisTypeDatabase)MeasurementDatabaseContext.getAnalysisTypeDatabase();
    	List list = null;
        try {
            list = database.retrieveByIds(ids, null);
            } catch (IllegalDataException e) {
                Log.errorMessage("MServerMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
                throw new DatabaseException("MServerMeasumentObjectLoader.loadAnalysisTypes | Illegal Storable Object: " + e.getMessage());
            }
        return list;
    }
    
    public List loadEvaluations(List ids) throws DatabaseException,
            CommunicationException {
        EvaluationDatabase database = (EvaluationDatabase)MeasurementDatabaseContext.getEvaluationDatabase();
        List list;
        List copyOfList;
        Evaluation evaluation;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Evaluation '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
                com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
                if (mcmRef != null) {
                    try {
                        evaluation = Evaluation.getInstance(mcmRef.transmitEvaluation((Identifier_Transferable)id.getTransferable()));
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
    public List loadEvaluationTypes(List ids) throws DatabaseException,
            CommunicationException {
        EvaluationTypeDatabase database = (EvaluationTypeDatabase)MeasurementDatabaseContext.getEvaluationTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluationTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
    
	public List loadModelings(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException("MServerMeasurementObjectLoader.loadModelings | mserver doesn't need in modeling");
	}
    
    public List loadMeasurements(List ids) throws DatabaseException,
            CommunicationException {
        MeasurementDatabase database = (MeasurementDatabase)MeasurementDatabaseContext.getMeasurementDatabase();
        List list;
        List copyOfList;
        Measurement measurement;
        try {
            list = database.retrieveByIds(ids, null);
            copyOfList = new LinkedList(list);
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                if(ids.contains(id)) it.remove();
            }
            for (Iterator it = copyOfList.iterator(); it.hasNext();) {
                Identifier id = ((StorableObject) it.next()).getId();
                Log.debugMessage("Measurement '" + id + "' not found in database; trying to load from MCM '" + mcmId + "'", Log.DEBUGLEVEL08);
                com.syrus.AMFICOM.mcm.corba.MCM mcmRef = (com.syrus.AMFICOM.mcm.corba.MCM)MeasurementServer.mcmRefs.get(mcmId);
                if (mcmRef != null) {
                    try {
                        measurement = Measurement.getInstance(mcmRef.transmitMeasurement((Identifier_Transferable)id.getTransferable()));
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
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadEvaluations | Illegal Storable Object: " + e.getMessage());
        }
        return list;
        
    }
    public List loadMeasurementSetups(List ids) throws DatabaseException,
            CommunicationException {
        MeasurementSetupDatabase database = (MeasurementSetupDatabase)MeasurementDatabaseContext.getMeasurementSetupDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadMeasurementSetups | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    public List loadMeasurementTypes(List ids) throws DatabaseException,
            CommunicationException {
        MeasurementTypeDatabase database = (MeasurementTypeDatabase)MeasurementDatabaseContext.getMeasurementTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadMeasurementTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    public List loadParameterTypes(List ids) throws DatabaseException,
            CommunicationException {
        ParameterTypeDatabase database = (ParameterTypeDatabase)MeasurementDatabaseContext.getParameterTypeDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadParameterTypes | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    public List loadResults(List ids) throws DatabaseException,
            CommunicationException {
        ResultDatabase database = (ResultDatabase)MeasurementDatabaseContext.getResultDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadResults | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
    
    public List loadSets(List ids) throws DatabaseException,
            CommunicationException {
        SetDatabase database = (SetDatabase)MeasurementDatabaseContext.getSetDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadSets | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadTemporalPatterns(List ids) throws DatabaseException,
            CommunicationException {
        TemporalPatternDatabase database = (TemporalPatternDatabase)MeasurementDatabaseContext.getTemporalPatternDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadTemporalPatterns | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }

    public List loadTests(List ids) throws DatabaseException,
            CommunicationException {
        TestDatabase database = (TestDatabase)MeasurementDatabaseContext.getTestDatabase();
        List list = null;
        try {
            list = database.retrieveByIds(ids, null);
        } catch (IllegalDataException e) {
            Log.errorMessage("MServerMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
            throw new DatabaseException("MServerMeasumentObjectLoader.loadTests | Illegal Storable Object: " + e.getMessage());
        }
        return list;
    }
}
