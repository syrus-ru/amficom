/*-
 * $Id: MscharServerImpl.java,v 1.11 2005/06/29 14:22:43 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import java.util.List;

import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.CompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.ErrorCode;
import com.syrus.AMFICOM.map.MapFeature;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlMapFeature;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.11 $, $Date: 2005/06/29 14:22:43 $
 * @author $Author: arseniy $
 * @module mscharserver_v1
 */
public final class MscharServerImpl extends MscharServerSchemeTransmit {
	private static final long serialVersionUID = 3762810480783274295L;

	MscharServerImpl() {
		super(MscharServerSessionEnvironment.getInstance().getConnectionManager().getCORBAServer().getOrb());
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
			final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			//validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("MscharServerImpl.transmitTopologicalImage() | Trying to transmit "
					+ '\'', Log.INFO);
			TopologicalImageQuery topologicalImageQuery = new TopologicalImageQuery(topologicalImageQuery_Transferable);
			byte[] image;
			try {
				image = MapInfoPool.getImage(topologicalImageQuery, new SessionKey(idlSessionKey));
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
			final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			//validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("MscharServerImpl.stopRenderTopologicalImage() | Trying to stop rendering image"
					+ '\'', Log.INFO);
			MapInfoPool.cancelRendering(new SessionKey(idlSessionKey));
		} catch (IllegalDataException e) {
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_DATA, CompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
	
	public IdlMapFeature[] findFeature(String featureName, IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			//validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("MscharServerImpl.findFeature() | Trying to find feature " + featureName, Log.INFO);
			List<MapFeature> mapFeatures = MapInfoPool.findFeature(featureName, new SessionKey(idlSessionKey));
			IdlMapFeature[] idlMapFeatures = new IdlMapFeature[mapFeatures.size()];
			int i = 0;
			for (MapFeature mapFeature: mapFeatures) {
				i++;
				idlMapFeatures[i] = mapFeature.getTransferable();				
			}
			return idlMapFeatures;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}	
	}
}
