/*-
 * $Id: LayerDescriptorParser.java,v 1.2 2005/08/01 13:32:33 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import java.io.File;
import java.util.Iterator;
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
 * @author $Author: arseniy $
 * @version $Revision: 1.2 $, $Date: 2005/08/01 13:32:33 $
 * @module mshserver_v1
 */

public class LayerDescriptorParser {

	public List<LayerDescriptor> getLayerFiles(final MapDescriptor mapDescriptor) {
		final List<LayerDescriptor> layerFiles = new LinkedList<LayerDescriptor>();
		final SAXReader reader = new SAXReader();
		Document document;
		final String mapMDF = mapDescriptor.getFilePathName();
		try {
			document = reader.read(mapMDF);
		} catch (DocumentException e) {
			Log.errorException(e);
			return layerFiles;
		}
		final List layerNodeList = document.selectNodes("//MapDefinitionLayer");
		for (final Iterator it = layerNodeList.iterator(); it.hasNext();) {
			final Node node = (Node) it.next();
			final Node layerNameNode = node.selectSingleNode("ServerQuery/Table");
			final Node layerPathNode = node.selectSingleNode("Connection/Url");
			if (layerNameNode == null || layerPathNode == null) {
				continue;
			}
			final String layerName = layerNameNode.getText();
			String layerPath = layerPathNode.getText();
			if (layerName == null || layerName.equals("") || layerPath == null || layerPath.equals("")) {
				Log.errorMessage("LayerFilesParser.getLayerFiles() | Wrong xml content in file " + mapMDF);
				continue;
			}
			layerPath = layerPath.substring(4);
			final File layerFile = new File(layerPath, layerName);
			final long size = layerFile.length();
			final long modified = layerFile.lastModified();
			layerFiles.add(new LayerDescriptor(layerName, layerFile.getPath(), size, modified));
		}
		return layerFiles;
	}
}
