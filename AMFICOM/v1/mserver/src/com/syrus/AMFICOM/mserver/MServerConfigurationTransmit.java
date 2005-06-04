/*
 * $Id: MServerConfigurationTransmit.java,v 1.3 2005/06/04 16:56:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import java.util.Iterator;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MeasurementPort;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MonitoredElement;
import com.syrus.AMFICOM.configuration.Port;
import com.syrus.AMFICOM.configuration.TransmissionPath;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectConditionBuilder;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/04 16:56:21 $
 * @author $Author: bass $
 * @module mserver_v1
 */
abstract class MServerConfigurationTransmit extends MServerAdministrationTransmit {

	private static final long serialVersionUID = -766205624533149592L;

	/* Transmit multiple objects*/

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjects(idsT);

		MeasurementPortType_Transferable[] transferables = new MeasurementPortType_Transferable[objects.size()];
		int i = 0;
		MeasurementPortType measurementPortType;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPortType = (MeasurementPortType) it.next();
			transferables[i] = (MeasurementPortType_Transferable) measurementPortType.getTransferable();
		}
		return transferables;
	}

	public KIS_Transferable[] transmitKISs(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjects(idsT);

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		KIS kis;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			kis = (KIS) it.next();
			transferables[i] = (KIS_Transferable) kis.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjects(idsT);

		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		MeasurementPort measurementPort;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			measurementPort = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		return transferables;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(Identifier_Transferable[] idsT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjects(idsT);

		MonitoredElement_Transferable[] transferables = new MonitoredElement_Transferable[objects.size()];
		int i = 0;
		MonitoredElement monitoredElement;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			monitoredElement = (MonitoredElement) it.next();
			transferables[i] = (MonitoredElement_Transferable) monitoredElement.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getConfigurationObjects(Identifier_Transferable[] idsT) throws AMFICOMRemoteException {
		try {
			java.util.Set ids = Identifier.fromTransferables(idsT);
			java.util.Set objects = StorableObjectPool.getStorableObjects(ids, true);
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



	/* Transmit multiple objects but ids by condition*/

	public KIS_Transferable[] transmitKISsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjectsButIdsCondition(idsT, conditionT);

		KIS_Transferable[] transferables = new KIS_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			KIS kis = (KIS) it.next();
			transferables[i] = (KIS_Transferable) kis.getTransferable();
		}
		return transferables;
	}

	public Port_Transferable[] transmitPortsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjectsButIdsCondition(idsT, conditionT);

		Port_Transferable[] transferables = new Port_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			Port port = (Port) it.next();
			transferables[i] = (Port_Transferable) port.getTransferable();
		}
		return transferables;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjectsButIdsCondition(idsT, conditionT);
		
		MeasurementPort_Transferable[] transferables = new MeasurementPort_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			MeasurementPort measurementPort = (MeasurementPort) it.next();
			transferables[i] = (MeasurementPort_Transferable) measurementPort.getTransferable();
		}
		return transferables;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		java.util.Set objects = this.getConfigurationObjectsButIdsCondition(idsT, conditionT);
		
		TransmissionPath_Transferable[] transferables = new TransmissionPath_Transferable[objects.size()];
		int i = 0;
		for (Iterator it = objects.iterator(); it.hasNext(); i++) {
			TransmissionPath transmissionPath = (TransmissionPath) it.next();
			transferables[i] = (TransmissionPath_Transferable) transmissionPath.getTransferable();
		}
		return transferables;
	}


	private java.util.Set getConfigurationObjectsButIdsCondition(Identifier_Transferable[] idsT, StorableObjectCondition_Transferable conditionT)
			throws AMFICOMRemoteException {
		try {

			StorableObjectCondition condition = null;
			try {
				condition = StorableObjectConditionBuilder.restoreCondition(conditionT);
			}
			catch (IllegalDataException ide) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA,
						CompletionStatus.COMPLETED_NO,
						"Cannot restore condition -- " + ide.getMessage());
			}

			try {
				java.util.Set ids = Identifier.fromTransferables(idsT);
				java.util.Set objects = StorableObjectPool.getStorableObjectsByConditionButIds(ids, condition, true);
				return objects;
			}
			catch (ApplicationException ae) {
				Log.errorException(ae);
				throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, ae.getMessage());
			}
		}
		catch (Throwable throwable) {
			Log.errorException(throwable);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, throwable.getMessage());
		}
	}

}
