/*-
 * $Id: MapDescriptorParser.java,v 1.3 2005/08/22 08:30:25 max Exp $
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
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/08/22 08:30:25 $
 * @module mshserver_v1
 */

public class MapDescriptorParser {
	private static final String	MAP_DESCRIPTOR_FILE_KEY = "MapDescriptor";
	private static final String	DEFAULT_MAP_DESCRIPTOR_FILE	= "map_descriptor.xml";

	public List<MapDescriptor> getMapDescriptors() {
		final List<MapDescriptor> mapDescriptors = new LinkedList<MapDescriptor>();
		final String mapDescriptorFile = ApplicationProperties.getString(MAP_DESCRIPTOR_FILE_KEY, DEFAULT_MAP_DESCRIPTOR_FILE);
		final File descFile = new File(mapDescriptorFile);
		final SAXReader reader = new SAXReader();
		Document document;
		try {
			document = reader.read(descFile);
		} catch (DocumentException e) {
			Log.errorException(e);
			return mapDescriptors;
		}
		final List<Node> mapNodeList = document.selectNodes("//map");
		for (Node node : mapNodeList) {
			final Node nameNode = node.selectSingleNode("name");
			final Node mdfNode = node.selectSingleNode("mdf");
			if (nameNode == null || mdfNode == null) {
				Log.errorMessage("MapDescriptorParser.getMapDescriptors() | Wrong xml format in file " + mapDescriptorFile);
				continue;
			}
			final String name = nameNode.getText();
			final String mdf = mdfNode.getText();
			if (name == null || name.equals("") || mdf == null) {
				Log.errorMessage("MapDescriptorParser.getMapDescriptors() | Wrong xml content in file " + mapDescriptorFile);
				continue;
			}
			final File mdfFile = new File(mdf);
			if (!mdfFile.isFile()) {
				Log.errorMessage("MapDescriptorParser.getMapDescriptors() | Couldn't locate mdf: + " + mdfFile.getAbsolutePath());
				continue;
			}
			mapDescriptors.add(new MapDescriptor(name, mdfFile.getName(), mdfFile.getPath(), mdfFile.length(), mdfFile.lastModified()));
		}
		return mapDescriptors;
	}
}

