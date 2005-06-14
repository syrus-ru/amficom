/*-
 * $Id: MscharServerImpl.java,v 1.4 2005/06/14 11:13:09 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.RenderedImage_Transferable;
import com.syrus.AMFICOM.map.corba.TopologicalImageQuery_Transferable;
import com.syrus.AMFICOM.security.corba.SessionKey_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.4 $, $Date: 2005/06/14 11:13:09 $
 * @author $Author: bass $
 * @module mscharserver_v1
 */
public final class MscharServerImpl extends MscharServerSchemeTransmit {
	private static final long serialVersionUID = 3762810480783274295L;

	/**
	 * @param sessionKey
	 * @param userId
	 * @param domainId
	 * @throws AMFICOMRemoteException
	 * @see com.syrus.AMFICOM.general.ServerCore#validateAccess(com.syrus.AMFICOM.security.corba.SessionKey_Transferable, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder, com.syrus.AMFICOM.general.corba.Identifier_TransferableHolder)
	 */
	protected void validateAccess(final SessionKey_Transferable sessionKey,
			final Identifier_TransferableHolder userId,
			final Identifier_TransferableHolder domainId)
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

	public RenderedImage_Transferable transmitTopologicalImage(
			final TopologicalImageQuery_Transferable topologicalImageQuery_Transferable,
			final SessionKey_Transferable sessionKey)
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
			RenderedImage_Transferable renderedImageT = new RenderedImage_Transferable(image);
			return renderedImageT;
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
	
	public void stopRenderTopologicalImage(
			final long userId,
			final SessionKey_Transferable sessionKey)
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
