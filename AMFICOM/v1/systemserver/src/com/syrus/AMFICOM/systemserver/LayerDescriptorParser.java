/*-
 * $Id: LayerDescriptorParser.java,v 1.1 2006/06/23 14:02:34 cvsadmin Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.systemserver;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.syrus.AMFICOM.map.LayerDescriptor;
import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.util.Log;

/**
 * @author max
 * @author $Author: cvsadmin $
 * @version $Revision: 1.1 $, $Date: 2006/06/23 14:02:34 $
 * @module systemserver
 */

final class LayerDescriptorParser {

	List<LayerDescriptor> getLayerFiles(final MapDescriptor mapDescriptor) {
		final List<LayerDescriptor> layerFiles = new LinkedList<LayerDescriptor>();
		final SAXReader reader = new SAXReader();
		Document document;
		final String mapMDF = mapDescriptor.getFilePathName();
		try {
			document = reader.read(mapMDF);
		} catch (DocumentException e) {
			Log.errorMessage(e);
			return layerFiles;
		}

		@SuppressWarnings("unchecked")
		final List<Node> layerNodeList = document.selectNodes("//MapDefinitionLayer");
		for (Node node : layerNodeList) {
			final Node layerNameNode = node.selectSingleNode("ServerQuery/Table");
			final Node layerPathNode = node.selectSingleNode("Connection/Url");
			if (layerNameNode == null || layerPathNode == null) {
				continue;
			}
			final String layerName = layerNameNode.getText();
			String layerPath = layerPathNode.getText();
			if (layerName == null || layerName.equals("") || layerPath == null || layerPath.equals("")) {
				Log.errorMessage("Wrong xml content in file " + mapMDF);
				continue;
			}
			//TODO: somthing wrong, try to devise something better
			layerPath = layerPath.substring(4);
			String datFileName = layerName.substring(0 , layerName.length()-3) + "DAT";
			String idFileName = layerName.substring(0 , layerName.length()-3) + "ID";
			String mapFileName = layerName.substring(0 , layerName.length()-3) + "MAP";
			String indFileName = layerName.substring(0 , layerName.length()-3) + "IND";
			
			final File layerFile = new File(layerPath, layerName);
			final File datFile = new File(layerPath, datFileName);
			final File idFile = new File(layerPath, idFileName);
			final File mapFile = new File(layerPath, mapFileName);
			final File indFile = new File(layerPath, indFileName);
						
			layerFiles.add(new LayerDescriptor(layerName, layerFile.getPath(), layerFile.length(), layerFile.lastModified()));
			layerFiles.add(new LayerDescriptor(datFileName, datFile.getPath(), datFile.length(), datFile.lastModified()));
			layerFiles.add(new LayerDescriptor(idFileName, idFile.getPath(), idFile.length(), idFile.lastModified()));
			layerFiles.add(new LayerDescriptor(mapFileName, mapFile.getPath(), mapFile.length(), mapFile.lastModified()));
			if(indFile.exists() && indFile.isFile()) {
				layerFiles.add(new LayerDescriptor(indFileName, indFile.getPath(), indFile.length(), indFile.lastModified()));
			}
		}
		return layerFiles;
	}	
}
