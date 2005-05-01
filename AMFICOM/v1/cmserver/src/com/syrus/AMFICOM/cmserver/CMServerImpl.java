/*
 * $Id: CMServerImpl.java,v 1.101 2005/05/01 17:27:12 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;


import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.syrus.AMFICOM.administration.AdministrationStorableObjectPool;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.general.GeneralStorableObjectPool;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectGroupEntities;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.SecurityKey;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.101 $, $Date: 2005/05/01 17:27:12 $
 * @author $Author: arseniy $
 * @module cmserver_v1
 */

public class CMServerImpl extends CMMeasurementTransmit {

	private static final long serialVersionUID = 3760563104903672628L;


	/* Delete*/

	public void delete(Identifier_Transferable[] idsT, SecurityKey securityKey) throws AMFICOMRemoteException {
		super.validateAccess(securityKey);

		Set ids = Identifier.fromTransferables(idsT);
		Set generalObjects = new HashSet(idsT.length);
		Set administrationObjects = new HashSet(idsT.length);
		Set configurationObjects = new HashSet(idsT.length);
		Set measurementObjects = new HashSet(idsT.length);
		for (Iterator it = ids.iterator(); it.hasNext();) {
			final Identifier id = (Identifier) it.next();
			final short entityCode = id.getMajor();
			if (ObjectGroupEntities.isInGeneralGroup(entityCode))
				generalObjects.add(id);
			else
				if (ObjectGroupEntities.isInAdministrationGroup(entityCode))
					administrationObjects.add(id);
				else
					if (ObjectGroupEntities.isInConfigurationGroup(entityCode))
						configurationObjects.add(id);
					else
						if (ObjectGroupEntities.isInMeasurementGroup(entityCode))
							measurementObjects.add(id);
						else
							Log.errorMessage("CMServerImpl.delete | Unknow group of code " + entityCode + " of object '" + id + "'");
		}
		GeneralStorableObjectPool.delete(generalObjects);
		AdministrationStorableObjectPool.delete(administrationObjects);
		ConfigurationStorableObjectPool.delete(configurationObjects);
		MeasurementStorableObjectPool.delete(measurementObjects);
	}

///////////////////////////////////////// Identifier Generator 	//////////////////////////////////////////////////

	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Log.debugMessage("CMServerImpl.getGeneratedIdentifier | generate new Identifer for "
					+ ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
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
					"Cannot create major/minor entries of identifier for entity: '"
							+ ObjectEntities.codeToString(entityCode)
							+ "' -- "
							+ ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		Log.debugMessage("CMServerImpl.getGeneratedIdentifierRange | generate new Identifer range "
				+ size + " for " + ObjectEntities.codeToString(entityCode), Log.DEBUGLEVEL07);
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
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
					"Cannot create major/minor entries of identifier for entity: '"
							+ ObjectEntities.codeToString(entityCode) + "' -- " + ige.getMessage());
		}
	}

	
///////////////////////////////////////// Verifiable 	//////////////////////////////////////////////////
	public void verify(byte i) {
		Log.debugMessage("Verify value: " + i, Log.DEBUGLEVEL10);
	}

}
