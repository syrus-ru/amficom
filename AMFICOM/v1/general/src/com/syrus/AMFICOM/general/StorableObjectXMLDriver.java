/*
 * $Id: StorableObjectXMLDriver.java,v 1.3 2005/01/25 06:10:48 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/01/25 06:10:48 $
 * @author $Author: bob $
 * @module general_v1
 */
public class StorableObjectXMLDriver {

	private String		fileName;
	private Document	doc;
	private String		packageName;
	private Node		root;

	public StorableObjectXMLDriver(final File path, final String packageName) {
		String dir;
		if (path.exists()) {
			if (path.isDirectory())
				dir = path.getAbsolutePath();
			else
				dir = path.getParent();
		} else {
			path.mkdirs();
			dir = path.getAbsolutePath();
		}
		this.fileName = dir + File.separatorChar + packageName + ".xml";
		this.packageName = packageName;
		this.parseXmlFile(false);
	}

	public void putObjectMap(final Identifier identifier, final Map objects) throws IllegalDataException {
		this.deleteObject(identifier);
		Element element = this.doc.createElement(identifier.getIdentifierString());
		for (Iterator it = objects.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object obj = objects.get(key);
			String value = null;
			if (obj instanceof StorableObject) {
				StorableObject storableObject = (StorableObject) obj;
				value = storableObject.getId().getIdentifierString();
			} else if (obj instanceof Identifier) {
				Identifier id = (Identifier) obj;
				value = id.getIdentifierString();
			} else if (obj instanceof String) {
				value = (String) obj;
			} else if (obj instanceof Collection) {
				Collection collection = (Collection) obj;
				Element subElement = this.doc.createElement(key);
				int i = 0;
				for (Iterator iter = collection.iterator(); iter.hasNext();) {
					Object obj2 = iter.next();
					String s = null;
					if (obj2 instanceof StorableObject) {
						StorableObject storableObject = (StorableObject) obj2;
						s = storableObject.getId().getIdentifierString();
					} else if (obj2 instanceof Identifier) {
						Identifier id = (Identifier) obj2;
						s = id.getIdentifierString();
					} else if (obj2 instanceof String) {
						s = (String) obj2;
					} else
						Log.errorMessage("StorableObjectXMLDriver.putObjectMap | key : " + key
								+ " unsupported class value : " + obj2.getClass().getName());

					if (s != null)
						subElement.setAttribute(key + (i++), s);
				}
				element.appendChild(subElement);
			} else
				Log.errorMessage("StorableObjectXMLDriver.putObjectMap | key : " + key + " unsupported class value : "
						+ obj.getClass().getName());

			if (value != null)
				element.setAttribute(key, value);

		}
		this.root.appendChild(element);
	}

	public Map getObjectMap(final Identifier identifier) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Map map = null;
		try {
			NodeList objList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/"
					+ identifier.getIdentifierString());
			if (objList.getLength() > 1)
				throw new IllegalDataException("StorableObjectXMLDriver.getObjectMap | more that one entity with id "
						+ identifier.getIdentifierString());
			if (objList.getLength() == 0)
				throw new ObjectNotFoundException("StorableObjectXMLDriver.getObjectMap | object not found : "
						+ identifier.getIdentifierString());
			for (int i = 0; i < objList.getLength(); i++) {
				Node children = objList.item(i);
				if (children.hasAttributes()) {
					NamedNodeMap namedNodeMap = children.getAttributes();
					for (int j = 0; j < namedNodeMap.getLength(); j++) {
						Node node = namedNodeMap.item(j);
						if (map == null)
							map = new HashMap();
						map.put(node.getNodeName(), node.getNodeValue());
					}
				}

				if (children.hasChildNodes()) {
					NodeList childNodes = children.getChildNodes();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node node = childNodes.item(j);
						List nodeItems = new LinkedList();
						if (node.hasAttributes()) {
							NamedNodeMap namedNodeMap = node.getAttributes();
							for (int k = 0; k < namedNodeMap.getLength(); k++) {
								Node nodeAttribute = namedNodeMap.item(k);
								nodeItems.add(nodeAttribute.getNodeValue());
							}
						}
						if (map == null)
							map = new HashMap();
						map.put(node.getNodeName(), nodeItems);
					}
				}
			}

			if (map == null)
				map = Collections.EMPTY_MAP;
		} catch (TransformerException te) {
			Log.errorMessage("StorableObjectXMLDriver.getObjectMap | Caught " + te.getMessage());
			throw new RetrieveObjectException("StorableObjectXMLDriver.getObjectMap | Caught " + te.getMessage(), te);
		}

		return map;
	}

	public void deleteObject(final Identifier identifier) throws IllegalDataException {
		try {
			NodeList sizeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/"
					+ identifier.getIdentifierString());
			if (sizeList.getLength() > 1)
				throw new IllegalDataException("StorableObjectXMLDriver.deleteObject | more that one entity with id "
						+ identifier.getIdentifierString());

			for (int i = 0; i < sizeList.getLength(); i++) {
				Node children = sizeList.item(i);
				this.root.removeChild(children);
			}
		} catch (TransformerException te) {
			Log.errorMessage("StorableObjectXMLDriver.deleteObject | Caught " + te.getMessage());
			throw new IllegalDataException("StorableObjectXMLDriver.deleteObject | Caught " + te.getMessage(), te);
		}
	}

	// Parses an XML file and returns a DOM document.
	// If validating is true, the contents is validated against the DTD
	// specified in the file.
	private Document parseXmlFile(final boolean validating) {
		try {
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validating);

			// Create the builder and parse the file
			File file = new File(this.fileName);
			if (file.exists()) {
				this.doc = factory.newDocumentBuilder().parse(file);
				this.root = this.doc.getFirstChild();
			} else {
				/* if file not exists creat default document */
				DocumentBuilder builder = factory.newDocumentBuilder();
				this.doc = builder.newDocument();
				/* with root item of package name */
				this.root = this.doc.createElement(this.packageName);
				this.doc.appendChild(this.root);
			}
			return this.doc;
		} catch (SAXException e) {
			// A parsing error occurred; the xml input is not valid
			Log.errorMessage("StorableObjectXMLDriver.parseXmlFile | Caught " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.errorMessage("StorableObjectXMLDriver.parseXmlFile | Caught " + e.getMessage());
		} catch (IOException e) {
			Log.errorMessage("StorableObjectXMLDriver.parseXmlFile | Caught " + e.getMessage());
		}
		return null;
	}

	public void writeXmlFile() {
		try {
			// Prepare the DOM document for writing
			Source source = new DOMSource(this.doc);

			// Prepare the output file
			File file = new File(this.fileName);
			Result result = new StreamResult(file);

			// Write the DOM document to the file
			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			Log.errorMessage("StorableObjectXMLDriver.writeXmlFile | Caught " + e.getMessage());
		} catch (TransformerException e) {
			Log.errorMessage("StorableObjectXMLDriver.writeXmlFile | Caught " + e.getMessage());
		}
	}
}
