/*
 * $Id: MServerImplementation.java,v 1.26 2005/01/19 20:57:34 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.mserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.administration.MCM;
import com.syrus.AMFICOM.administration.Server;
import com.syrus.AMFICOM.administration.User;
import com.syrus.AMFICOM.administration.corba.Domain_Transferable;
import com.syrus.AMFICOM.administration.corba.MCM_Transferable;
import com.syrus.AMFICOM.administration.corba.Server_Transferable;
import com.syrus.AMFICOM.administration.corba.User_Transferable;
import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.PortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.general.corba.CharacteristicType_Transferable;
import com.syrus.AMFICOM.general.corba.Characteristic_Transferable;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.LinkedIdsCondition_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;
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
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;
import com.syrus.AMFICOM.measurement.corba.TestStatus;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServerPOA;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.26 $, $Date: 2005/01/19 20:57:34 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */

public class MServerImplementation extends MServerPOA {
	static final long serialVersionUID = 3278262178679212253L;

/////////////////////////////////////////	Identifier Generator ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable)identifier.getTransferable();
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}	
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size) throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable)identifiers[i].getTransferable();
			return identifiersT;
		}
		catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
																			 CompletionStatus.COMPLETED_NO,
																			 "Illegal object entity: '" + ObjectEntities.codeToString(entityCode) + "'");
		}
		catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE,
																			 CompletionStatus.COMPLETED_NO,
																			 "Cannot create major/minor entries of identifier for entity: '" + ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}
	}


////////////////////////////////////////////	Receive ///////////////////////////////////////////////////
	public void receiveResults(Result_Transferable[] resultsT, Identifier_Transferable mcmIdT) throws AMFICOMRemoteException {
		Identifier mcmId = new Identifier(mcmIdT);
		Log.debugMessage("Received " + resultsT.length + " results from MCM '" + mcmId + "'", Log.DEBUGLEVEL03);
		synchronized (MServerMeasurementObjectLoader.lock) {
			MServerMeasurementObjectLoader.mcmId = mcmId;
			Result result;
			List results = new ArrayList(resultsT.length);
			for (int i = 0; i < resultsT.length; i++) {
				try {
					result = new Result(resultsT[i]);
					results.add(result);
				}
				catch (CreateObjectException coe) {
					Log.errorException(coe);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, coe.getMessage());
				}
				catch (Throwable t) {
					Log.errorException(t);
					throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
				}
			}
			ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext.getResultDatabase();
			try {
				resultDatabase.insert(results);
			}
			catch (CreateObjectException e) {
				Log.errorException(e);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
			}
			catch (IllegalDataException e) {
				Log.errorException(e);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_SAVE, CompletionStatus.COMPLETED_NO, e.getMessage());
			}
		}
	}


/////////////////////////////////////////	Configuration ////////////////////////////////////////////////
	public CharacteristicType_Transferable transmitCharacteristicType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			CharacteristicType characteristicType = new CharacteristicType(id);
			return (CharacteristicType_Transferable)characteristicType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public EquipmentType_Transferable transmitEquipmentType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			EquipmentType equipmentType = new EquipmentType(id);
			return (EquipmentType_Transferable)equipmentType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
	
	public PortType_Transferable transmitPortType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			PortType portType = new PortType(id);
			return (PortType_Transferable)portType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MeasurementPortType_Transferable transmitMeasurementPortType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementPortType measurementPortType = new MeasurementPortType(id);
			return (MeasurementPortType_Transferable)measurementPortType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Exception e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Characteristic_Transferable transmitCharacteristic(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Characteristic characteristic = new Characteristic(id);
			return (Characteristic_Transferable)characteristic.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public User_Transferable transmitUser(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			User user = new User(id);
			return (User_Transferable)user.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Domain_Transferable transmitDomain(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Domain domain = new Domain(id);
			return (Domain_Transferable)domain.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Server_Transferable transmitServer(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Server server = new Server(id);
			return (Server_Transferable)server.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MCM_Transferable transmitMCM(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MCM mcm = new MCM(id);
			return (MCM_Transferable)mcm.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Exception e)  {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Equipment_Transferable transmitEquipment(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Equipment equipment = new Equipment(id);
			return (Equipment_Transferable)equipment.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Port_Transferable transmitPort(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Port port = new Port(id);
			return (Port_Transferable)port.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public TransmissionPath_Transferable transmitTransmissionPath(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			TransmissionPath transmissionPath = new TransmissionPath(id);
			return (TransmissionPath_Transferable)transmissionPath.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public KIS_Transferable transmitKIS(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			KIS kis = new KIS(id);
			return (KIS_Transferable)kis.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Exception e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
	
    public Measurement_Transferable transmitMeasurement(Identifier_Transferable id_Transferable) throws AMFICOMRemoteException {
        Identifier id = new Identifier(id_Transferable);
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
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
	public MeasurementPort_Transferable transmitMeasurementPort(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementPort measurementPort = new MeasurementPort(id);
			return (MeasurementPort_Transferable)measurementPort.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MonitoredElement_Transferable transmitMonitoredElement(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MonitoredElement monitoredElement = new MonitoredElement(id);
			return (MonitoredElement_Transferable)monitoredElement.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}


	public MonitoredElement_Transferable[] transmitKISMonitoredElements(Identifier_Transferable kisId) throws AMFICOMRemoteException {
		Identifier id = new Identifier(kisId);
		try {
			KIS kis = new KIS(id);
			List monitoredElements = kis.retrieveMonitoredElements();
			MonitoredElement_Transferable[] mesT = new MonitoredElement_Transferable[monitoredElements.size()];
			int i = 0;
			for (Iterator it = monitoredElements.iterator(); it.hasNext();)
				mesT[i++] = (MonitoredElement_Transferable)((MonitoredElement)it.next()).getTransferable();
			return mesT;
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}



/////////////////////////////////////////	Measurement //////////////////////////////////////////////////
	public ParameterType_Transferable transmitParameterType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			ParameterType parameterType = new ParameterType(id);
			return (ParameterType_Transferable)parameterType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		}
		catch (Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementType measurementType = new MeasurementType(id);
			return (MeasurementType_Transferable)measurementType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public Measurement_Transferable[] transmitMeasurements(Identifier_Transferable[] id_Transferables) throws AMFICOMRemoteException  {
        
        try {
            List list;
            List idsList = new ArrayList(id_Transferables.length);
            for (int i = 0; i < id_Transferables.length; i++) {
                idsList.add(new Identifier(id_Transferables[i]));
            }
            
            list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
            
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
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
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
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public Analysis_Transferable transmitAnalysis(Identifier_Transferable id_Transferable) throws AMFICOMRemoteException  {
        
        try {
            Identifier id  = new Identifier(id_Transferable);
            Analysis analysis = (Analysis) MeasurementStorableObjectPool.getStorableObject(id, true);
            Analysis_Transferable transferables = (Analysis_Transferable) analysis.getTransferable();
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public Analysis_Transferable[] transmitAnalyses(Identifier_Transferable[] id_Transferables) throws AMFICOMRemoteException  {
        
        try {
            List list;
            List idsList = new ArrayList(id_Transferables.length);
            for (int i = 0; i < id_Transferables.length; i++) {
                idsList.add(new Identifier(id_Transferables[i]));
            }
            
            list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
            
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
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public Analysis_Transferable[] transmitAnalysesButIds(
            LinkedIdsCondition_Transferable linkedIdsCondition_Transferable,
            Identifier_Transferable[] identifier_Transferables)
            throws AMFICOMRemoteException {
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
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public AnalysisType_Transferable transmitAnalysisType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			AnalysisType analysisType = new AnalysisType(id);
			return (AnalysisType_Transferable)analysisType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}
    
    public Evaluation_Transferable transmitEvaluation(Identifier_Transferable id_Transferable) throws AMFICOMRemoteException  {
        
        try {
            Identifier id  = new Identifier(id_Transferable);
            Evaluation evaluation = (Evaluation) MeasurementStorableObjectPool.getStorableObject(id, true);
            Evaluation_Transferable transferables = (Evaluation_Transferable) evaluation.getTransferable();
            return transferables;

        } catch (RetrieveObjectException roe) {
            Log.errorException(roe);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
                    .getMessage());
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }
    
    public Evaluation_Transferable[] transmitEvaluations(Identifier_Transferable[] id_Transferables) throws AMFICOMRemoteException  {
        
        try {
            List list;
            List idsList = new ArrayList(id_Transferables.length);
            for (int i = 0; i < id_Transferables.length; i++) {
                idsList.add(new Identifier(id_Transferables[i]));
            }
            
            list = MeasurementStorableObjectPool.getStorableObjects(idsList, true);
            
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
        } catch (ApplicationException e) {
            Log.errorException(e);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, e.getMessage());
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
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
        } catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
    }

	public EvaluationType_Transferable transmitEvaluationType(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			EvaluationType evaluationType = new EvaluationType(id);
			return (EvaluationType_Transferable)evaluationType.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Set_Transferable transmitSet(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Set set = new Set(id);
			return (Set_Transferable)set.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(id);
			return (MeasurementSetup_Transferable)measurementSetup.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public Test_Transferable transmitTest(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			Test test = new Test(id);
			return (Test_Transferable)test.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public TemporalPattern_Transferable transmitTemporalPattern(Identifier_Transferable idT) throws AMFICOMRemoteException {
		Identifier id = new Identifier(idT);
		try {
			TemporalPattern temporalPattern = new TemporalPattern(id);
			return (TemporalPattern_Transferable)temporalPattern.getTransferable();
		}
		catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES, onfe.getMessage());
		}
		catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe.getMessage());
		} catch (Throwable t) {
            Log.errorException(t);
            throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, t.getMessage());
        }
	}

	public void updateTestStatus(Identifier_Transferable idT, TestStatus status) {
		Identifier id = new Identifier(idT);
		Log.debugMessage("Updating status of test '" + id + "' to " + status.value(), Log.DEBUGLEVEL07);
		try {
			Test test = (Test)MeasurementStorableObjectPool.getStorableObject(id, true);
			test.updateStatus(status, MeasurementServer.iAm.getUserId());
		}
		catch (ApplicationException ae) {
			Log.errorMessage("updateTestStatus | Cannot update status of test '" + id + "' -- " + ae.getMessage());
		}
	}



	public void ping(int i) throws AMFICOMRemoteException {
		System.out.println("i == " + i);
	}
}
