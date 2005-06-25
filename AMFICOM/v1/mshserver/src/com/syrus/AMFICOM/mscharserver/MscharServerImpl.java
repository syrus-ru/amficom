/*-
 * $Id: MscharServerImpl.java,v 1.9 2005/06/25 18:05:56 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.9 $, $Date: 2005/06/25 18:05:56 $
 * @author $Author: bass $
 * @module mscharserver_v1
 */
public final class MscharServerImpl extends MscharServerSchemeTransmit {
	private static final long serialVersionUID = 3762810480783274295L;

	MscharServerImpl(final ORB orb) {
		super(orb);
	}

	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(IdlSessionKey, IdlIdentifierHolder, IdlIdentifierHolder)
	 */
	protected void validateAccess(final IdlSessionKey sessionKey,
			final IdlIdentifierHolder userId,
			final IdlIdentifierHolder domainId)
			throws AMFICOMRemoteException {
		try {
			MscharServerSessionEnvironment.getInstance()
					.getMscharServerServantManager()
					.getLoginServerReference()
					.validateAccess(sessionKey, userId, domainId);
		} catch (final CommunicationException ce) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_NO, ce.getMessage());
		} catch (final AMFICOMRemoteException are) {
			//-Pass AMFICOMRemoteException upward -- do not catch it by 'throw Throwable' below
			throw are;
		} catch (final Throwable t) {
			Log.errorException(t);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ACCESS_VALIDATION, CompletionStatus.COMPLETED_PARTIALLY, t.getMessage());
		}
	}

	public IdlRenderedImage transmitTopologicalImage(
			final IdlTopologicalImageQuery topologicalImageQuery_Transferable,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("MscharServerImpl.transmitTopologicalImage() | Trying to transmit "
					+ '\'', Log.INFO);
			TopologicalImageQuery topologicalImageQuery = new TopologicalImageQuery(topologicalImageQuery_Transferable);
			byte[] image;
			try {
				image = MapInfoPool.getImage(topologicalImageQuery);
			} catch (IllegalDataException e) {
				throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_NO, e.getMessage());
			}
			IdlRenderedImage renderedImageT = new IdlRenderedImage(image);
			return renderedImageT;
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
	
	public void stopRenderTopologicalImage(
			final long userId,
			final IdlSessionKey sessionKey)
			throws AMFICOMRemoteException {
		try {
			Log.debugMessage("MscharServerImpl.stopRenderTopologicalImage() | Trying to stop rendering image"
					+ '\'', Log.INFO);
			MapInfoPool.cancelRendering(userId);
		} catch (IllegalDataException e) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
}
