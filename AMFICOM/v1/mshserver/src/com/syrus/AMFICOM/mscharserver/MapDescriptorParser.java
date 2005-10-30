/*-
 * $Id: MapDescriptorParser.java,v 1.5 2005/10/30 14:48:59 bass Exp $
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

import com.syrus.AMFICOM.map.MapDescriptor;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @author max
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/10/30 14:48:59 $
 * @module mshserver_v1
 */

final class MapDescriptorParser {
	private static final String	MAP_DESCRIPTOR_FILE_KEY = "MapDescriptor";
	private static final String	DEFAULT_MAP_DESCRIPTOR_FILE	= "map_descriptor.xml";

	List<MapDescriptor> getMapDescriptors() {
		final List<MapDescriptor> mapDescriptors = new LinkedList<MapDescriptor>();
		final String mapDescriptorFile = ApplicationProperties.getString(MAP_DESCRIPTOR_FILE_KEY, DEFAULT_MAP_DESCRIPTOR_FILE);
		final File descFile = new File(mapDescriptorFile);
		final SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(descFile);
		} catch (DocumentException e) {
			Log.errorMessage(e);
			return mapDescriptors;
		}
		final List<Node> mapNodeList = document.selectNodes("//map");
		for (Node node : mapNodeList) {
			final Node nameNode = node.selectSingleNode("name");
			final Node mdfNode = node.selectSingleNode("mdf");
			if (nameNode == null || mdfNode == null) {
				Log.errorMessage("Wrong xml format in file " + mapDescriptorFile);
				continue;
			}
			final String name = nameNode.getText();
			final String mdf = mdfNode.getText();
			if (name == null || name.equals("") || mdf == null) {
				Log.errorMessage("Wrong xml content in file " + mapDescriptorFile);
				continue;
			}
			final File mdfFile = new File(mdf);
			if (!mdfFile.isFile()) {
				Log.errorMessage("Couldn't locate mdf: + " + mdfFile.getAbsolutePath());
				continue;
			}
			mapDescriptors.add(new MapDescriptor(name, mdfFile.getName(), mdfFile.getPath(), mdfFile.length(), mdfFile.lastModified()));
		}
		return mapDescriptors;
	}
}

