/*
 * $Id: MapInfoVirtualCorbaConnection.java,v 1.13.2.1 2006/06/27 17:07:13 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import static com.syrus.io.FileLoader.BUFF_SIZE;
import static com.syrus.io.FileLoader.NULL_STUB;
import static java.util.logging.Level.FINEST;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.general.ClientServantManager;
import com.syrus.AMFICOM.general.ClientSessionEnvironment;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.MapClientServantManager;
import com.syrus.AMFICOM.general.MapServerConnectionManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.map.LayerDescriptor;
import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.AMFICOM.map.MapFileDescriptor;
import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.AMFICOM.systemserver.corba.MapServer;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.13.2.1 $, $Date: 2006/06/27 17:07:13 $
 * @author $Author: arseniy $
 * @author ХЗ кто
 * @module mapinfo
 */
public class MapInfoVirtualCorbaConnection extends MapInfoConnection {
	private static final String CACHE_DIR = "CacheDir";
	private static final String MAPS_LOADED_DIR = "/mapsLoadedDir";
	private MapServerConnectionManager mapServerConnectionManager;

	@Override
	public MapImageLoader createImageLoader() throws MapConnectionException {
		return new MapInfoLocalStubImageLoader(this);
	}

	@Override
	public List<String> getAvailableViews() throws MapDataException {
		try {
			final List<String> mapDescriptors = new ArrayList<String>();
			final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
			final IdlMapDescriptor[] idlMapDescriptors = mapServer.getMapDescriptors(LoginManager.getSessionKey().getIdlTransferable());
			for (int i = 0; i < idlMapDescriptors.length; i++){
				mapDescriptors.add(idlMapDescriptors[i].name);
			}
			return mapDescriptors;
		} catch (CommunicationException ce) {
			throw new MapDataException("Failed getting map descriptors list", ce);
		} catch (AMFICOMRemoteException are) {
			throw new MapDataException("Failed getting map descriptors list -- " + are.message);
		}
	}

	@Override
	public boolean connect() throws MapConnectionException {
		boolean flag = false;

		final ClientServantManager clientServantManager = ClientSessionEnvironment.getInstance().getConnectionManager();
		if (!(clientServantManager instanceof MapClientServantManager)) {
			throw new IllegalStateException("Must establish Map session");
		}
		this.mapServerConnectionManager = (MapServerConnectionManager) clientServantManager;

		try {
			final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
			final IdlMapDescriptor[] mapDescriptors = mapServer.getMapDescriptors(LoginManager.getSessionKey().getIdlTransferable());
			for (int i = 0; i < mapDescriptors.length; i++)
				if (mapDescriptors[i].name.equals(this.getView())) {
					final MapDescriptor mapDescriptor = new MapDescriptor(mapDescriptors[i]);
					this.syncronizeMap(mapDescriptor);
					flag = true;
					break;
				}
			if (!flag) {
				throw new MapConnectionException("MapInfoCorbaConnection - No map with the name defined (probably bad properties)");
			}
		} catch (CommunicationException e) {
			throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);
		} catch (AMFICOMRemoteException e) {
			throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);
		}

		if (flag) {
			flag = super.connect();
		}

		return flag;
	}

	public List<MapDescriptor> getMapDescriptors() {
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
		final IdlMapDescriptor[] idlMapDescriptors;
		try {
			final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
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
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
		final IdlLayerDescriptor[] idlLayerDescriptors;
		try {
			final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
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

	public void syncronizeMap(final MapDescriptor mapDescriptor) throws MapConnectionException {
		final String cacheDir = ApplicationProperties.getString(CACHE_DIR, "cache") + MAPS_LOADED_DIR;
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
			localMapDir.mkdir();
		}

		// Запись пропатченного MDF файла в локальный дисковый кэш
		final File localMDF = new File(localMapDir, mapDescriptor.getFileName());
		compareAndLoadFile(localMDF, mapDescriptor);
		patchMapMDF(localMDF, localMDF.getParentFile().getAbsolutePath());
		this.setPath(localMDF.getAbsolutePath());
		// Запись файлов слоёв в локальный дисковый кэш
		final List<LayerDescriptor> layerDescriptors = getLayerDescriptors(mapDescriptor);
		for (final LayerDescriptor descriptor : layerDescriptors) {
			final File localLayer = new File(localMapDir, descriptor.getFileName());
			compareAndLoadFile(localLayer, descriptor);
		}

		if (MapPropertiesManager.isVirtualDiskUsed()) {
			// Если используем виртуальынй диск
			final String virtualDiskPath = MapPropertiesManager.getVirtualDiskPath() + MAPS_LOADED_DIR;
			final File virtualDiskFile = new File(virtualDiskPath);
			if (!virtualDiskFile.isDirectory()) {
				Log.debugMessage("Virtual disk dir + "
						+ "\"" + virtualDiskFile.getAbsolutePath() + "\"" + "does not exist, tryin to create", FINEST);
				virtualDiskFile.mkdirs();
			}
			// Запись пропатченного MDF файла на виртуальный диск
			final File virtualDiskMapDir = new File(virtualDiskFile, mapDescriptor.getMapName());
			if (!virtualDiskMapDir.exists()) {
				Log.debugMessage("Virtual disk dir + "
						+ "\"" + virtualDiskMapDir.getAbsolutePath() + "\"" + "does not exist, tryin to create", FINEST);
				virtualDiskMapDir.mkdir();
			}

			final File virtualDiskMDF = new File(virtualDiskMapDir, mapDescriptor.getFileName());
			compareAndLoadFile(virtualDiskMDF, mapDescriptor);
			patchMapMDF(virtualDiskMDF, virtualDiskMDF.getParentFile().getAbsolutePath());
			this.setPath(virtualDiskMDF.getAbsolutePath());

			// Копируем файлы слоёв на виртуальный диск
			final byte[] buffer = new byte[BUFF_SIZE];
			int bytesRead;
			try {
				for (final LayerDescriptor descriptor : layerDescriptors) {
					final File diskCacheLayer = new File(localMapDir, descriptor.getFileName());
					final File virtualDiskLayer = new File(virtualDiskMapDir, descriptor.getFileName());

					boolean toCopy = false;

					if (virtualDiskLayer.exists()) {
						if (!virtualDiskLayer.isFile()
								|| virtualDiskLayer.length() != diskCacheLayer.length()
								|| virtualDiskLayer.lastModified() != diskCacheLayer.lastModified()) {
							toCopy = true;
						}
					} else {
						toCopy = true;
					}

					if (toCopy) {
						final FileInputStream inputStream = new FileInputStream(diskCacheLayer);
						final FileOutputStream outputStream = new FileOutputStream(virtualDiskLayer);

						while ((bytesRead = inputStream.read(buffer)) > 0) {
							outputStream.write(buffer, 0, bytesRead);
						}
						inputStream.close();
						outputStream.close();
						virtualDiskLayer.setLastModified(diskCacheLayer.lastModified());
					}
				}
			} catch (FileNotFoundException e) {
				throw new MapConnectionException("MapInfoCorbaImageLoader.syncronizeMap | Failed while copying files to virtual drive!");
			} catch (IOException e) {
				throw new MapConnectionException("MapInfoCorbaImageLoader.syncronizeMap | Failed while copying files to virtual drive!");
			}
		}
	}

	private File loadFile(final File localFile, final MapFileDescriptor mapFileDescriptor) {
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKey().getIdlTransferable();
		boolean eof = false;
		long offset = 0;
		final File tempFile = new File(localFile.getPath() + ".swp");
		if (tempFile.exists()) {
			Log.debugMessage("Warning: swp file\" " + tempFile.getAbsolutePath() + " \" exsists. Removing it...", FINEST);
			tempFile.delete();
		}
		try {
			final FileOutputStream fos = new FileOutputStream(tempFile);
			while (!eof) {
				final MapServer mapServer = this.mapServerConnectionManager.getMapServerReference();
				final byte[] partOfFile = mapServer.loadFile(mapFileDescriptor.getFilePathName(), offset, idlSessionKey);
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
		} catch (AMFICOMRemoteException are) {
			Log.errorMessage(are.message);
			return null;
		} catch (IOException ioe) {
			Log.errorMessage(ioe);
			return null;
		}
	}

	private void compareAndLoadFile(final File localFile, final MapFileDescriptor descriptor) {
		if (localFile.exists()) {
			if (!localFile.isFile()
					|| localFile.length() != descriptor.getLength()
					|| localFile.lastModified() != descriptor.getLastModified()) {
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
			final XMLWriter writer = new XMLWriter(new FileOutputStream(localMdf), new OutputFormat());
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			Log.errorMessage(e);
		}
	}
}
