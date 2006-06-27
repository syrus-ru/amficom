/*-
 * $Id: MapInfoCorbaImageLoader.java,v 1.19.2.1 2006/06/27 17:07:13 arseniy Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import static com.syrus.io.FileLoader.BUFF_SIZE;
import static com.syrus.io.FileLoader.NULL_STUB;
import static java.util.logging.Level.FINEST;

import java.awt.Image;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.LayerDescriptor;
import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.AMFICOM.map.MapFileDescriptor;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.map.corba.IdlRenderedImage;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.systemserver.corba.MapServer;
import com.syrus.io.ImageToByte;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.19.2.1 $, $Date: 2006/06/27 17:07:13 $
 * @author $Author: arseniy $
 * @module mapinfo
 */

public class MapInfoCorbaImageLoader implements MapImageLoader {
	private static final String CACHE_DIR = "CacheDir";
	private final MapInfoCorbaConnection connection;

	MapInfoCorbaImageLoader(final MapInfoConnection connection) {
		this.connection = (MapInfoCorbaConnection) connection;
	}

	public void stopRendering() {
		try {
			this.connection.getMapServerConnectionManager().getMapServerReference().stopRenderTopologicalImage(LoginManager.getSessionKey().getIdlTransferable());
		} catch (CommunicationException ce) {
			Log.errorMessage("loading has been cancelled");
			Log.errorMessage(ce);
		} catch (AMFICOMRemoteException are) {
			Log.errorMessage("loading has been cancelled -- " + are.message);
		}
	}

	public Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException, MapDataException {
		long t0 = 0, t1 = 0, t2 = 0, t3 = 0, t4 = 0;
		t0 = System.currentTimeMillis();
		final IdlTopologicalImageQuery transf = query.getIdlTransferable();
		final IdlSessionKey keyt = LoginManager.getSessionKey().getIdlTransferable();
		final IdlRenderedImage rit;

		try {
			t1 = System.currentTimeMillis();
			final MapServer mscharServer = this.connection.getMapServerConnectionManager().getMapServerReference();
			t2 = System.currentTimeMillis();
			rit = mscharServer.transmitTopologicalImage(transf, keyt);
			t3 = System.currentTimeMillis();
		} catch (CommunicationException ce) {
			Log.errorMessage(ce);
			return null;
		} catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
			return null;
		}
		final byte[] image = rit.image;
		if (image.length == 1) {
			Log.debugMessage("loading has been cancelled", FINEST);
			return null;
		}

		final BufferedImage bufferedImage = ImageToByte.byteToImqge(image);
		t4 = System.currentTimeMillis();
		Log.debugMessage((t1 - t0)
				+ " (creating transf and session key)" + (t2 - t1) + " (getting session ref)" + (t3 - t2) + "rendering"
				+ (t4 - t3) + "creating image", Log.DEBUGLEVEL09);

		return bufferedImage;
	}

	public MapConnection getMapConnection() throws MapConnectionException {
		return this.connection;
	}

//	public List<SpatialObject> findSpatialObjects(final String searchText) throws MapConnectionException, MapDataException {
//		final List<SpatialObject> resultList = new ArrayList<SpatialObject>();
//
//		final IdlSessionKey keyt = LoginManager.getSessionKeyTransferable();
//		IdlMapFeature[] objectsFound;
//
//		long t1 = 0, t2 = 0;
//
//		try {
//			final MscharServer mscharServer = this.connection.getMscharServer();
//			t1 = System.currentTimeMillis();
//			objectsFound = mscharServer.findFeature(searchText, keyt);
//			t2 = System.currentTimeMillis();
//		} catch (AMFICOMRemoteException e) {
//			Log.errorMessage(e.getMessage());
//			return null;
//		}
//		Log.debugMessage("searched for " + (t2 - t1) + " ms.", Level.INFO);
//
//		if ((objectsFound.length == 1) && (objectsFound[0].name.equals(""))) {
//			return resultList;
//		}
//
//		for (final IdlMapFeature mapFeature : objectsFound) {
//			final MapInfoSpatialObject spatialObject = new MapInfoSpatialObject(new DoublePoint(mapFeature.centerX, mapFeature.centerY),
//					mapFeature.name);
//			resultList.add(spatialObject);
//		}
//
//		return resultList;
//	}

	public List<MapDescriptor> getMapDescriptors() {
		final IdlMapDescriptor[] idlMapDescriptors;
		try {
			final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
			final MapServer mapServer = this.connection.getMapServerConnectionManager().getMapServerReference();
			idlMapDescriptors = mapServer.getMapDescriptors(idlSessionKey);
		} catch (CommunicationException ce) {
			Log.errorMessage(ce);
			return Collections.emptyList();
		} catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
			return Collections.emptyList();
		}
		if (idlMapDescriptors.length == 1) {
			MapDescriptor mapDescriptor = new MapDescriptor(idlMapDescriptors[0]);
			if (mapDescriptor.getFileName().equals("") && mapDescriptor.getFilePathName().equals("")) {
				return Collections.emptyList();
			}
		}
		final List<MapDescriptor> mapDescriptors = new ArrayList<MapDescriptor>(idlMapDescriptors.length);
		for (int i = 0; i < idlMapDescriptors.length; i++) {
			IdlMapDescriptor descriptor = idlMapDescriptors[i];
			mapDescriptors.add(new MapDescriptor(descriptor));
		}
		return mapDescriptors;
	}

	private List<LayerDescriptor> getLayerDescriptors(MapDescriptor mapDescriptor) {
		final IdlLayerDescriptor[] idlLayerDescriptors;
		try {
			final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
			final MapServer mapServer = this.connection.getMapServerConnectionManager().getMapServerReference();
			idlLayerDescriptors = mapServer.getLayerDescriptors(mapDescriptor.getIdlTransferable(), idlSessionKey);
		} catch (CommunicationException ce) {
			Log.errorMessage(ce);
			return Collections.emptyList();
		} catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
			return Collections.emptyList();
		}
		if (idlLayerDescriptors.length == 1) {
			LayerDescriptor layerDescriptor = new LayerDescriptor(idlLayerDescriptors[0]);
			if (layerDescriptor.getFileName().equals("") && layerDescriptor.getFilePathName().equals("")) {
				return Collections.emptyList();
			}
		}
		final List<LayerDescriptor> layerDescriptors = new ArrayList<LayerDescriptor>(idlLayerDescriptors.length);
		for (int i = 0; i < idlLayerDescriptors.length; i++) {
			IdlLayerDescriptor descriptor = idlLayerDescriptors[i];
			layerDescriptors.add(new LayerDescriptor(descriptor));
		}
		return layerDescriptors;
	}

	public void syncronizeMap(final MapDescriptor mapDescriptor) {
		final String cacheDir = ApplicationProperties.getString(CACHE_DIR, "cache");
		final File cacheDirFile = new File(cacheDir);
		if (!cacheDirFile.isDirectory()) {
			Log.debugMessage("Cache dir + " + "\"" + cacheDirFile.getAbsolutePath() + "\"" + "does not exist, tryin to create",
					FINEST);
			cacheDirFile.mkdirs();
		}
		final File localMapDir = new File(cacheDirFile, mapDescriptor.getMapName());
		if (!localMapDir.exists()) {
			Log.debugMessage("Cache dir + " + "\"" + localMapDir.getAbsolutePath() + "\"" + "does not exist, tryin to create",
					FINEST);
			cacheDirFile.mkdir();
		}
		final File localMDF = new File(localMapDir, mapDescriptor.getFileName());
		compareAndLoadFile(localMDF, mapDescriptor);
		patchMapMDF(localMDF, localMDF.getParentFile().getAbsolutePath());
		final List<LayerDescriptor> layerDescriptors = getLayerDescriptors(mapDescriptor);
		for (LayerDescriptor descriptor : layerDescriptors) {
			final File localLayer = new File(localMapDir, descriptor.getFileName());
			compareAndLoadFile(localLayer, descriptor);
		}
	}

	private File loadFile(final File localFile, final MapFileDescriptor mapFileDescriptor) {
		boolean eof = false;
		long offset = 0;
		File tempFile = new File(localFile.getPath() + ".swp");
		if (tempFile.exists()) {
			Log.debugMessage("Warning: swp file\" " + tempFile.getAbsolutePath() + " \" exsists. Removing it...", FINEST);
			tempFile.delete();
		}
		try {
			final FileOutputStream fos = new FileOutputStream(tempFile);
			while (!eof) {
				final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
				final MapServer mapServer = this.connection.getMapServerConnectionManager().getMapServerReference();
				byte[] partOfFile = mapServer.loadFile(mapFileDescriptor.getFilePathName(), offset, idlSessionKey);
				if (partOfFile.equals(NULL_STUB)) {
					eof = true;
					break;
				}
				fos.write(partOfFile);
				if (partOfFile.length < BUFF_SIZE) {
					eof = true;
					break;
				}
				offset += BUFF_SIZE;
			}
			fos.flush();
			fos.close();
			if (localFile.exists()) {
				localFile.delete();
			}
			tempFile.renameTo(localFile);
			localFile.setLastModified(mapFileDescriptor.getLastModified());
			return localFile;
		} catch (CommunicationException ce) {
			Log.errorMessage(ce);
			return null;
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage(e.message);
			return null;
		} catch (IOException e) {
			Log.errorMessage(e);
			return null;
		}
	}

	private void compareAndLoadFile(final File localFile, final MapFileDescriptor descriptor) {
		if (localFile.exists()) {
			if (!localFile.isFile()
					|| localFile.length() != descriptor.getLength() || localFile.lastModified() != descriptor.getLastModified()) {
				loadFile(localFile, descriptor);
			}
		} else {
			loadFile(localFile, descriptor);
		}
	}

	private void patchMapMDF(final File localMdf, final String newDir) {
		final SAXReader reader = new SAXReader();
		final Document document;
		try {
			document = reader.read(localMdf);
		} catch (DocumentException e) {
			Log.errorMessage(e);
			return;
		}

		final List layerNodeList = document.selectNodes("//MapDefinitionLayer");
		for (final Iterator it = layerNodeList.iterator(); it.hasNext();) {
			final Node node = (Node) it.next();
			final Node layerPathNode = node.selectSingleNode("Connection/Url");
			if (layerPathNode == null) {
				continue;
			}
			final String layerPath = layerPathNode.getText();
			if (layerPath == null || layerPath.equals("")) {
				Log.errorMessage("Wrong xml content in file " + localMdf.getAbsolutePath());
				continue;
			}
			layerPathNode.setText("tab:" + newDir);
		}

		try {
			final XMLWriter writer = new XMLWriter(new FileWriter(localMdf));
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log.errorMessage("IOException " + e.getMessage());
		}
	}

	public List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final String searchText)
			throws MapConnectionException,
				MapDataException {
		// TODO Требуется реализация на сервере
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();
		return searchResultsList;
	}

	public List<SpatialObject> findSpatialObjects(final SpatialLayer layer, final Double bounds)
			throws MapConnectionException,
				MapDataException {
		// TODO Требуется реализация на сервере
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();
		return searchResultsList;
	}
}
