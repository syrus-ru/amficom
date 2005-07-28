/*-
 * $Id: LayerDescriptorParser.java,v 1.1 2005/07/28 15:33:16 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

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
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/07/28 15:33:16 $
 * @module mshserver_v1
 */

public class LayerDescriptorParser {
	
	public List<LayerDescriptor> getLayerFiles(MapDescriptor mapDescriptor) {
		
		List<LayerDescriptor> layerFiles = new LinkedList<LayerDescriptor>();
		SAXReader reader = new SAXReader();
		Document document;
		String mapMDF = mapDescriptor.getFilePathName();
		try {
			document = reader.read(mapMDF);
		} catch (DocumentException e) {
			Log.errorException(e);
			return layerFiles;
		}
		@SuppressWarnings("unchecked") List<Node> layerNodeList = document.selectNodes("//MapDefinitionLayer");
		for (Node node: layerNodeList) {
			Node layerNameNode = node.selectSingleNode("ServerQuery/Table");
			Node layerPathNode = node.selectSingleNode("Connection/Url");
			if (layerNameNode == null || layerPathNode == null) {
				continue;
			}
			String layerName = layerNameNode.getText();
			String layerPath = layerPathNode.getText();
			if (layerName == null || layerName.equals("") || layerPath == null || layerPath.equals("")) {
				Log.errorMessage("LayerFilesParser.getLayerFiles() | Wrong xml content in file "
						+ mapMDF);
				continue;
			}
			layerPath = layerPath.substring(4);
			File layerFile = new File(layerPath, layerName);
			long size = layerFile.length();
			long modified = layerFile.lastModified();
			layerFiles.add(new LayerDescriptor(layerName, layerFile.getPath(), size, modified));				
		}
		return layerFiles;
	}
}
