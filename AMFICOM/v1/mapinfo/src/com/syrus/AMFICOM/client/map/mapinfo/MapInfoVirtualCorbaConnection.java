/*
 * $Id: MapInfoVirtualCorbaConnection.java,v 1.8 2005/10/31 12:30:11 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.MscharClientServantManager;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CommonServer;
import com.syrus.AMFICOM.map.LayerDescriptor;
import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.AMFICOM.map.MapFileDescriptor;
import com.syrus.AMFICOM.map.corba.IdlLayerDescriptor;
import com.syrus.AMFICOM.map.corba.IdlMapDescriptor;
import com.syrus.AMFICOM.mscharserver.corba.MscharServer;
import com.syrus.AMFICOM.mscharserver.corba.MscharServerHelper;
import com.syrus.AMFICOM.security.corba.IdlSessionKey;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

import static com.syrus.io.FileLoader.BUFF_SIZE;
import static com.syrus.io.FileLoader.NULL_STUB;


public class MapInfoVirtualCorbaConnection extends MapInfoConnection{
	private static final String	CACHE_DIR	= "CacheDir";
	private static final String	MAPS_LOADED_DIR	= "/mapsLoadedDir";	
	private MscharServer mscharServer;

	@Override
	public MapImageLoader createImageLoader() throws MapConnectionException {
		return new MapInfoLocalStubImageLoader(this);
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	@Override
	public List<String> getAvailableViews() throws MapDataException {
		final List<String> listToReturn = new ArrayList<String>();
		try {
			IdlMapDescriptor[] mapDescriptors = this.mscharServer.getMapDescriptors(LoginManager.getSessionKeyTransferable());
			for (int i = 0; i < mapDescriptors.length; i++){
				listToReturn.add(mapDescriptors[i].name);
			}
		} catch (AMFICOMRemoteException e) {
			throw new MapDataException("Failed getting map descriptors list");
		}

		return listToReturn;
	}
	
	@Override
	public boolean connect() throws MapConnectionException {
		boolean flag = false;		
		try {
			final MscharClientServantManager mscharClientServantManager = MscharClientServantManager.create();
			final CommonServer commonServer = mscharClientServantManager.getServerReference();
			this.mscharServer = MscharServerHelper.narrow(commonServer);
			
			IdlMapDescriptor[] mapDescriptors = this.mscharServer.getMapDescriptors(LoginManager.getSessionKeyTransferable());
			for (int i = 0; i < mapDescriptors.length; i++)
				if (mapDescriptors[i].name.equals(this.getView())){
					MapDescriptor mapDescriptor = new MapDescriptor(mapDescriptors[i]);
					this.syncronizeMap(mapDescriptor);
					flag = true;					
					break;
				}
			if (!flag)
				throw new MapConnectionException("MapInfoCorbaConnection - No map with the name defined (probably bad properties)");				
		} catch (CommunicationException e) {
			throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);
		} catch (AMFICOMRemoteException e) {
			throw new MapConnectionException("MapInfoCorbaConnection - failed initializing MscharClientServantManager.", e);			}

		if (flag)
			flag = super.connect();
		
		return flag;
	}
	
	public List<MapDescriptor> getMapDescriptors() {
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKeyTransferable();
		final IdlMapDescriptor[] idlMapDescriptors;
		try {
			idlMapDescriptors = this.mscharServer.getMapDescriptors(idlSessionKey);
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage(e.getMessage());
			return Collections.emptyList();
		}
		if(idlMapDescriptors.length == 1) {
			MapDescriptor mapDescriptor = new MapDescriptor(idlMapDescriptors[0]);
			if(mapDescriptor.getFileName().equals("") 
					&& mapDescriptor.getFilePathName().equals("")) {
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
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKeyTransferable();
		final IdlLayerDescriptor[] idlLayerDescriptors;
		try {
			idlLayerDescriptors = this.mscharServer.getLayerDescriptors(mapDescriptor.getTransferable(),idlSessionKey);
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage(e.getMessage());
			return Collections.emptyList();
		}
		if(idlLayerDescriptors.length == 1) {
			LayerDescriptor layerDescriptor = new LayerDescriptor(idlLayerDescriptors[0]);
			if(layerDescriptor.getFileName().equals("") 
					&& layerDescriptor.getFilePathName().equals("")) {
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
	
	public void syncronizeMap(final MapDescriptor mapDescriptor) throws MapConnectionException{
		String cacheDir = ApplicationProperties.getString(CACHE_DIR, "cache") + MAPS_LOADED_DIR;
		File cacheDirFile = new File(cacheDir);
		if(!cacheDirFile.isDirectory()) {
			Log.debugMessage("Cache dir + " + "\"" + cacheDirFile.getAbsolutePath() + "\"" + "does not exist, tryin to create", Log.DEBUGLEVEL05);
			cacheDirFile.mkdirs();
		}
		File localMapDir = new File(cacheDirFile, mapDescriptor.getMapName());
		if(!localMapDir.exists()) {
			Log.debugMessage("Cache dir + " + "\"" + localMapDir.getAbsolutePath() + "\"" + "does not exist, tryin to create", Log.DEBUGLEVEL05);
			localMapDir.mkdir();
		}
		
		//Запись пропатченного MDF файла в локальный дисковый кэш
		File localMDF = new File(localMapDir, mapDescriptor.getFileName());
		compareAndLoadFile(localMDF, mapDescriptor);
		patchMapMDF(localMDF, localMDF.getParentFile().getAbsolutePath());
		this.setPath(localMDF.getAbsolutePath());		
		//Запись файлов слоёв в локальный дисковый кэш		
		final List<LayerDescriptor> layerDescriptors = getLayerDescriptors(mapDescriptor);
		for (LayerDescriptor descriptor : layerDescriptors) {
			File localLayer = new File(localMapDir, descriptor.getFileName());
			compareAndLoadFile(localLayer, descriptor);
		}

		if (MapPropertiesManager.isVirtualDiskUsed()){
			//Если используем виртуальынй диск
			String virtualDiskPath = MapPropertiesManager.getVirtualDiskPath() + MAPS_LOADED_DIR;
			File virtualDiskFile = new File(virtualDiskPath);
			if(!virtualDiskFile.isDirectory()) {
				Log.debugMessage("Virtual disk dir + " + "\"" + virtualDiskFile.getAbsolutePath() + "\"" + "does not exist, tryin to create", Log.DEBUGLEVEL05);
				virtualDiskFile.mkdirs();
			}
			//Запись пропатченного MDF файла на виртуальный диск			
			File virtualDiskMapDir = new File(virtualDiskFile, mapDescriptor.getMapName());
			if(!virtualDiskMapDir.exists()) {
				Log.debugMessage("Virtual disk dir + " + "\"" + virtualDiskMapDir.getAbsolutePath() + "\"" + "does not exist, tryin to create", Log.DEBUGLEVEL05);
				virtualDiskMapDir.mkdir();
			}
			
			File virtualDiskMDF = new File(virtualDiskMapDir, mapDescriptor.getFileName());
			compareAndLoadFile(virtualDiskMDF, mapDescriptor);
			patchMapMDF(virtualDiskMDF, virtualDiskMDF.getParentFile().getAbsolutePath());
			this.setPath(virtualDiskMDF.getAbsolutePath());
			
			//Копируем файлы слоёв на виртуальный диск			
			byte[] buffer = new byte[BUFF_SIZE];
			int bytesRead;
			try {
				for (LayerDescriptor descriptor : layerDescriptors) {
					File diskCacheLayer = new File(localMapDir, descriptor.getFileName());
					File virtualDiskLayer = new File(virtualDiskMapDir, descriptor.getFileName());
					
					boolean toCopy = false;
					
					if(virtualDiskLayer.exists()) {
						if(		!virtualDiskLayer.isFile() 
							|| virtualDiskLayer.length() != diskCacheLayer.length() 
							|| virtualDiskLayer.lastModified() != diskCacheLayer.lastModified())
							toCopy = true;
					} else
						toCopy = true;
					
					if (toCopy){
						FileInputStream inputStream = new FileInputStream(diskCacheLayer);
						FileOutputStream outputStream =  new FileOutputStream(virtualDiskLayer);
						
						while ((bytesRead = inputStream.read(buffer)) > 0){
							outputStream.write(buffer,0,bytesRead);
						}
						inputStream.close();				
						outputStream.close();
						virtualDiskLayer.setLastModified(diskCacheLayer.lastModified());
					}
				}
			} catch (FileNotFoundException e) {
				throw new MapConnectionException("MapInfoCorbaImageLoader.syncronizeMap | Failed while copying files to virtual drive!");
			} catch (IOException e) {
				throw new MapConnectionException("MapInfoCorbaImageLoader.syncronizeMap | Failed while copying files to virtual drive!");			}
		}
	}
	
	private File loadFile(final File localFile, final MapFileDescriptor mapFileDescriptor) {
		final IdlSessionKey idlSessionKey = LoginManager.getSessionKeyTransferable();
		boolean eof = false;
		long offset = 0;
		File tempFile = new File(localFile.getPath() + ".swp");
		if(tempFile.exists()) {
			Log.debugMessage("Warning: swp file\" " + tempFile.getAbsolutePath() +  " \" exsists. Removing it...", Log.DEBUGLEVEL05);
			tempFile.delete();
		}
		try {
			FileOutputStream fos = new FileOutputStream(tempFile);
			while(!eof) {
				byte[] partOfFile = this.mscharServer.loadFile(mapFileDescriptor.getFilePathName(), offset, idlSessionKey);
				if(partOfFile.equals(NULL_STUB)) {
					eof = true;
					break;
				}
				fos.write(partOfFile);
				if(partOfFile.length < BUFF_SIZE) {
					eof = true;
					break;
				}
				offset += BUFF_SIZE;
			}
			fos.flush();
			fos.close();
			if(localFile.exists()) {
				localFile.delete();
			}
			tempFile.renameTo(localFile);
			localFile.setLastModified(mapFileDescriptor.getLastModified());
			return localFile;
		} catch (AMFICOMRemoteException e) {
			Log.errorMessage("AMFICOMRemoteException " + e.getMessage());
			return null;
		} catch (IOException e) {
			Log.errorMessage("IOException" + e.getMessage());
			return null;
		}		
	}
	
	private void compareAndLoadFile(final File localFile, final MapFileDescriptor descriptor) {
		if(localFile.exists()) {
			if(!localFile.isFile() 
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
		Document document;
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
			String layerPath = layerPathNode.getText();
			if (layerPath == null || layerPath.equals("")) {
				Log.errorMessage("Wrong xml content in file " + localMdf.getAbsolutePath());
				continue;
			}
			layerPathNode.setText("tab:" + newDir);
		}
		
		try {
	        XMLWriter writer = new XMLWriter(new FileWriter(localMdf));
	        writer.write(document);
	        writer.close();
		} catch (IOException e) {
			Log.errorMessage("IOException " + e.getMessage());
		}
	}
}
