/*
 * $Id: ConfigurationObjectLoader.java,v 1.1 2004/08/11 16:45:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.CommunicationException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/11 16:45:02 $
 * @author $Author: arseniy $
 * @module configuration_v1
 */

public interface ConfigurationObjectLoader {

	CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException;

	EquipmentType loadEquipmentType(Identifier id) throws DatabaseException, CommunicationException;

	PortType loadPortType(Identifier id) throws DatabaseException, CommunicationException;

//	MeasurementPortType loadMeasurementPortType(Identifier id) throws DatabaseException, CommunicationException;

	Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException;

//	PermissionAttributes loadPermissionAttributes(Identifier id) throws DatabaseException, CommunicationException;

	User loadUser(Identifier id) throws DatabaseException, CommunicationException;

	Domain loadDomain(Identifier id) throws DatabaseException, CommunicationException;

	Server loadServer(Identifier id) throws DatabaseException, CommunicationException;

	MCM loadMCM(Identifier id) throws DatabaseException, CommunicationException;

	Equipment loadEquipment(Identifier id) throws DatabaseException, CommunicationException;

	Port loadPort(Identifier id) throws DatabaseException, CommunicationException;

	TransmissionPath loadTransmissionPath(Identifier id) throws DatabaseException, CommunicationException;

	KIS loadKIS(Identifier id) throws DatabaseException, CommunicationException;

//	MeasurementPort loadMeasurementPort(Identifier id) throws DatabaseException, CommunicationException;

	MonitoredElement loadMonitoredElement(Identifier id) throws DatabaseException, CommunicationException;
}
