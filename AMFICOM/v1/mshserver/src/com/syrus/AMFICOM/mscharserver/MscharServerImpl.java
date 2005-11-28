/*-
 * $Id: MscharServerImpl.java,v 1.24 2005/11/28 12:47:46 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.mscharserver;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.general.IdentifierGeneratorServerCore;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.IdlIdentifierHolder;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlCompletionStatus;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteExceptionPackage.IdlErrorCode;
import com.syrus.AMFICOM.map.LayerDescriptor;
import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.AMFICOM.map.MapFeature;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.map.corba.IdlMapFeature;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerOperations;
import com.syrus.AMFICOM.security.SessionKey;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.io.FileLoader;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.24 $, $Date: 2005/11/28 12:47:46 $
 * @author $Author: arseniy $
 * @module mscharserver
 */
final class MscharServerImpl extends IdentifierGeneratorServerCore implements MscharServerOperations {
	private static final long serialVersionUID = 3762810480783274295L;

	MscharServerImpl(final MscharServerServantManager mscharServerServantManager) {
		super(mscharServerServantManager, mscharServerServantManager.getCORBAServer().getOrb());
	}

	public IdlRenderedImage transmitTopologicalImage(final IdlTopologicalImageQuery topologicalImageQueryT,
			final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("Trying to transmit " + '\'', Level.INFO);
			final TopologicalImageQuery topologicalImageQuery = new TopologicalImageQuery(topologicalImageQueryT);
			byte[] image;
			try {
				image = MapInfoPool.getImage(topologicalImageQuery, new SessionKey(idlSessionKey));
			} catch (IllegalDataException e) {
				throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_DATA, IdlCompletionStatus.COMPLETED_NO, e.getMessage());
			}
			final IdlRenderedImage renderedImageT = new IdlRenderedImage(image);
			return renderedImageT;
		} catch (final AMFICOMRemoteException are) {
			throw are;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}

	public void stopRenderTopologicalImage(final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("Trying to stop rendering image" + '\'', Level.INFO);
			MapInfoPool.cancelRendering(new SessionKey(idlSessionKey));
		} catch (IllegalDataException e) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_DATA, IdlCompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
	
	public IdlMapFeature[] findFeature(final String featureName, final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			Log.debugMessage("Trying to find feature " + featureName, Level.INFO);
			final List<MapFeature> mapFeatures = MapInfoPool.findFeature(featureName, new SessionKey(idlSessionKey));
			final IdlMapFeature[] idlMapFeatures = new IdlMapFeature[mapFeatures.size()];
			int i = 0;
			for (final MapFeature mapFeature : mapFeatures) {
				idlMapFeatures[i++] = mapFeature.getTransferable();
			}
			return idlMapFeatures;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}

	public IdlMapDescriptor[] getMapDescriptors(final IdlSessionKey idlSessionKey) throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			final MapDescriptorParser parser = new MapDescriptorParser();
			List<MapDescriptor> mapDescriptors = parser.getMapDescriptors();
			if (mapDescriptors.isEmpty()) {
				final MapDescriptor nullMapDescriptor = new MapDescriptor("", "", "", 0, 0);
				final IdlMapDescriptor nullTransferable = nullMapDescriptor.getTransferable();
				final IdlMapDescriptor[] IdlMapDescriptors = new IdlMapDescriptor[] { nullTransferable };
				return IdlMapDescriptors;
			}
			final IdlMapDescriptor[] idlMapDescriptors = new IdlMapDescriptor[mapDescriptors.size()];
			int i = 0;
			for (final MapDescriptor descriptor : mapDescriptors) {
				idlMapDescriptors[i++] = descriptor.getTransferable();
			}
			return idlMapDescriptors;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}

	public IdlLayerDescriptor[] getLayerDescriptors(final IdlMapDescriptor idlMapDescriptor, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			final MapDescriptor mapDescriptor = new MapDescriptor(idlMapDescriptor);
			final LayerDescriptorParser parser = new LayerDescriptorParser();
			final List<LayerDescriptor> layerDescriptors = parser.getLayerFiles(mapDescriptor);
			final IdlLayerDescriptor[] idlLayerDescriptors = new IdlLayerDescriptor[layerDescriptors.size()];
			if (layerDescriptors.isEmpty()) {
				final LayerDescriptor nullLayerDescriptor = new LayerDescriptor("", "", 0, 0);
				final IdlLayerDescriptor nullTransferable = nullLayerDescriptor.getTransferable();
				final IdlLayerDescriptor[] IdlLayerDescriptors = new IdlLayerDescriptor[] { nullTransferable };
				return IdlLayerDescriptors;
			}
			int i = 0;
			for (final LayerDescriptor layerFile : layerDescriptors) {
				idlLayerDescriptors[i++] = layerFile.getTransferable();
			}
			return idlLayerDescriptors;
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}

	public byte[] loadFile(final String fileName, final long offset, final IdlSessionKey idlSessionKey)
			throws AMFICOMRemoteException {
		try {
			final IdlIdentifierHolder userId = new IdlIdentifierHolder();
			final IdlIdentifierHolder domainId = new IdlIdentifierHolder();
			super.validateAccess(idlSessionKey, userId, domainId);
			final byte[] partOfFile = FileLoader.fileToByte(fileName, offset);
			return partOfFile;
		} catch (final IOException e) {
			throw new AMFICOMRemoteException(IdlErrorCode.ERROR_ILLEGAL_DATA, IdlCompletionStatus.COMPLETED_NO, e.getMessage());
		} catch (final Throwable t) {
			throw super.processDefaultThrowable(t);
		}
	}
}
