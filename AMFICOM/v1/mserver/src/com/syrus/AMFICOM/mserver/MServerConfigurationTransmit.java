/*
 * $Id: MServerConfigurationTransmit.java,v 1.11 2005/06/30 16:11:25 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */
package com.syrus.AMFICOM.mserver;

import org.omg.CORBA.ORB;
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.configuration.corba.IdlEquipment;
import com.syrus.AMFICOM.configuration.corba.IdlEquipmentType;
import com.syrus.AMFICOM.configuration.corba.IdlKIS;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPortType;
import com.syrus.AMFICOM.configuration.corba.IdlMeasurementPort;
import com.syrus.AMFICOM.configuration.corba.IdlMonitoredElement;
import com.syrus.AMFICOM.configuration.corba.IdlPort;
import com.syrus.AMFICOM.configuration.corba.IdlPortType;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPath;
import com.syrus.AMFICOM.configuration.corba.IdlTransmissionPathType;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectCondition;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/30 16:11:25 $
 * @author $Author: arseniy $
 * @module mserver_v1
 */
abstract class MServerConfigurationTransmit extends MServerAdministrationTransmit {

	private static final long serialVersionUID = -766205624533149592L;

	MServerConfigurationTransmit(final ORB orb) {
		super(orb);
	}

	/* Transmit multiple objects*/

	public IdlEquipmentType[] transmitEquipmentTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlEquipmentType[] ret = new IdlEquipmentType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlPortType[] transmitPortTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlPortType[] ret = new IdlPortType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurementPortType[] transmitMeasurementPortTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMeasurementPortType[] ret = new IdlMeasurementPortType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlTransmissionPathType[] transmitTransmissionPathTypes(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlTransmissionPathType[] ret = new IdlTransmissionPathType[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}



	public IdlEquipment[] transmitEquipments(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlEquipment[] ret = new IdlEquipment[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlPort[] transmitPorts(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlPort[] ret = new IdlPort[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurementPort[] transmitMeasurementPorts(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMeasurementPort[] ret = new IdlMeasurementPort[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlTransmissionPath[] transmitTransmissionPaths(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlTransmissionPath[] ret = new IdlTransmissionPath[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlKIS[] transmitKISs(final IdlIdentifier[] idsT, final IdlSessionKey sessionKeyT)
			throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		IdlKIS[] ret = new IdlKIS[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}

	public IdlMonitoredElement[] transmitMonitoredElements(final IdlIdentifier[] idsT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity[] storableObjectsT = super.transmitStorableObjects(idsT, sessionKeyT);
		final int length = storableObjectsT.length;
		final IdlMonitoredElement[] ret = new IdlMonitoredElement[length];
		System.arraycopy(storableObjectsT, 0, ret, 0, length);
		return ret;
	}


	/* Transmit multiple objects but ids by condition*/

	public IdlKIS[] transmitKISsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlKIS[] ret = new IdlKIS[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlPort[] transmitPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlPort[] ret = new IdlPort[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlMeasurementPort[] transmitMeasurementPortsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlMeasurementPort[] ret = new IdlMeasurementPort[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

	public IdlTransmissionPath[] transmitTransmissionPathsButIdsByCondition(final IdlIdentifier[] idsT,
			final IdlStorableObjectCondition conditionT,
			final IdlSessionKey sessionKeyT) throws AMFICOMRemoteException {
		final IDLEntity storableObjects[] = super.transmitStorableObjectsButIdsByCondition(idsT, sessionKeyT, conditionT);
		final int length = storableObjects.length;
		final IdlTransmissionPath[] ret = new IdlTransmissionPath[length];
		System.arraycopy(storableObjects, 0, ret, 0, length);
		return ret;
	}

}
