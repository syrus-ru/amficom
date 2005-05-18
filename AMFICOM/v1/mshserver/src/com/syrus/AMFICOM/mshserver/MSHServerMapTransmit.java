/*-
 * $Id: MSHServerMapTransmit.java,v 1.4 2005/05/18 13:34:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mshserver;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/05/18 13:34:16 $
 * @module mshserver_v1
 */
abstract class MSHServerMapTransmit extends MSHServerSchemeReceive {
	abstract IDLEntity transmitStorableObject(
			final Identifier_Transferable id,
			final SessionKey_Transferable sessionKey)
			throws AMFICOMRemoteException;

	/*-********************************************************************
	 * Map -- transmit multiple objects.                                  *
	 **********************************************************************/
}
