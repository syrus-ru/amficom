/*
* $Id: MCMGeneralObjectLoader.java,v 1.25 2005/06/06 14:39:58 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.GeneralObjectLoader;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.general.corba.StorableObjectCondition_Transferable;
import com.syrus.AMFICOM.mserver.corba.MServer;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;


/**
 * @version $Revision: 1.25 $, $Date: 2005/06/06 14:39:58 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader extends MCMObjectLoader implements GeneralObjectLoader {

	public MCMGeneralObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager);
	}

	/* Load multiple objects*/

	public Set loadParameterTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitParameterTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypes(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCharacteristicTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristics(final Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCharacteristics(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects but ids by condition*/

	public Set loadParameterTypesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitParameterTypesButIdsByCondition(idsT, conditionT, sessionKey);
					}
				});
	}



	/*
	 * MCM do not need in all below methods
	 * */

	public Set loadCharacteristicTypesButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}

	public Set loadCharacteristicsButIds(final StorableObjectCondition condition, final Set ids) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", ids: " + ids + ", condition: " + condition;
		return null;
	}



	public void saveParameterTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCharacteristicTypes(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}

	public void saveCharacteristics(final Set objects, boolean force) {
		assert false : ErrorMessages.METHOD_NOT_NEEDED + ", objects: " + objects + ", force: " + force;
	}
}
