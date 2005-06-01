/*
* $Id: MCMGeneralObjectLoader.java,v 1.23 2005/06/01 20:54:59 arseniy Exp $
*
* Copyright © 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.mcm;

import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.DatabaseGeneralObjectLoader;
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
 * @version $Revision: 1.23 $, $Date: 2005/06/01 20:54:59 $
 * @author $Author: arseniy $
 * @module mcm_v1
 */
final class MCMGeneralObjectLoader extends MCMObjectLoader implements GeneralObjectLoader {

	public MCMGeneralObjectLoader(final MCMServantManager mcmServantManager) {
		super(mcmServantManager, new DatabaseGeneralObjectLoader());
	}

	/* Load multiple objects*/

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.PARAMETERTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitParameterTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTICTYPE_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCharacteristicTypes(idsT, sessionKey);
			}
		});
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		return super.loadStorableObjects(ids, ObjectEntities.CHARACTERISTIC_ENTITY_CODE, new TransmitProcedure() {
			public IDLEntity[] transmitStorableObjects(CommonServer server,
					Identifier_Transferable[] idsT,
					SessionKey_Transferable sessionKey) throws AMFICOMRemoteException {
				return ((MServer) server).transmitCharacteristics(idsT, sessionKey);
			}
		});
	}



	/* Load multiple objects nut ids by condition*/

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return super.loadStorableObjectsButIdsCondition(ids,
				condition,
				ObjectEntities.PARAMETERTYPE_ENTITY_CODE,
				new TransmitButIdsConditionProcedure() {
					public IDLEntity[] transmitStorableObjectsButIdsCondition(CommonServer server,
							Identifier_Transferable[] idsT,
							SessionKey_Transferable sessionKey,
							StorableObjectCondition_Transferable conditionT) throws AMFICOMRemoteException {
						return ((MServer) server).transmitParameterTypesByCondition(idsT, conditionT, sessionKey);
					}
				});
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) {
		throw new UnsupportedOperationException("Method not implemented, ids: " + ids + ", condition: " + condition);
	}



	/*
	 * MCM do not need in all below methods
	 * */

	public void saveParameterTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristicTypes(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}

	public void saveCharacteristics(Set objects, boolean force) {
		throw new UnsupportedOperationException("Method not implemented, collection: " + objects + ", force: " + force);
	}
}
