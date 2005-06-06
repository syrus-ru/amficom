/*
 * $Id: MServerConfigurationTransmit.java,v 1.4 2005/06/06 14:43:06 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.configuration.corba.MeasurementPort_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.configuration.corba.Port_Transferable;
import com.syrus.AMFICOM.configuration.corba.TransmissionPath_Transferable;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/06 14:43:06 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
abstract class MServerConfigurationTransmit extends MServerAdministrationTransmit {

	private static final long serialVersionUID = -766205624533149592L;



	/* Transmit multiple objects*/

	public MeasurementPortType_Transferable[] transmitMeasurementPortTypes(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MeasurementPortType_Transferable[] ret = new MeasurementPortType_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public KIS_Transferable[] transmitKISs(Identifier_Transferable[] idsT, SessionKey_Transferable sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		KIS_Transferable[] ret = new KIS_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPorts(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MeasurementPort_Transferable[] ret = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public MonitoredElement_Transferable[] transmitMonitoredElements(Identifier_Transferable[] idsT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final MonitoredElement_Transferable[] ret = new MonitoredElement_Transferable[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}


	/* Transmit multiple objects but ids by condition*/

	public KIS_Transferable[] transmitKISsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final KIS_Transferable[] ret = new KIS_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public Port_Transferable[] transmitPortsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final Port_Transferable[] ret = new Port_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public MeasurementPort_Transferable[] transmitMeasurementPortsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final MeasurementPort_Transferable[] ret = new MeasurementPort_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public TransmissionPath_Transferable[] transmitTransmissionPathsButIdsByCondition(Identifier_Transferable[] idsT,
			StorableObjectCondition_Transferable conditionT,
			SessionKey_Transferable sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final TransmissionPath_Transferable[] ret = new TransmissionPath_Transferable[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
