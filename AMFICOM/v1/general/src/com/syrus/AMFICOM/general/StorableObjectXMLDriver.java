/*
 * $Id: StorableObjectXMLDriver.java,v 1.6 2005/01/31 13:54:14 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
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
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.syrus.util.Log;

/**
 * XML Driver for storable object package, one per package.
 * 
 * @version $Revision: 1.6 $, $Date: 2005/01/31 13:54:14 $
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
			this.addObject(element, key, obj);

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
			Node item = objList.item(0);
			NodeList childNodes = item.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node node = childNodes.item(i);
				if (map == null)
					map = new HashMap();
				map.put(node.getNodeName(), this.parse(node));
			}

			if (map == null)
				map = Collections.EMPTY_MAP;
		} catch (TransformerException te) {
			String msg = "StorableObjectXMLDriver.getObjectMap | Caught " + te.getMessage();
			Log.errorMessage(msg);
			throw new RetrieveObjectException(msg, te);
		}

		return map;
	}

	private StorableObject reflectStorableObject(Identifier id) throws IllegalDataException {
		StorableObject storableObject = null;
		String className = ObjectGroupEntities.getPackageName(id.getMajor()) + "."
				+ ObjectGroupEntities.getGroupName(id.getMajor()).replaceAll("Group$", "") + "StorableObjectPool";
		try {
			Class clazz = Class.forName(className);
			Method method = clazz.getMethod("getStorableObject", new Class[] { Identifier.class, boolean.class});
			storableObject = (StorableObject) method.invoke(null, new Object[] { id, Boolean.TRUE});

		} catch (ClassNotFoundException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Class " + className //$NON-NLS-1$
					+ " not found on the classpath - " + e.getMessage());
		} catch (SecurityException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Caught " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Class " + className //$NON-NLS-1$
					+ " haven't getStorableObject static method - " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Caught " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Caught " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Caught " + e.getMessage());
		}
		System.out.println("storableObject " + storableObject.getId() + "\t" + storableObject.getClass().getName());
		return storableObject;
	}

	private Object parse(Node node) throws IllegalDataException {
		Object object = null;
		NamedNodeMap attributes = node.getAttributes();
		if (attributes.getLength() > 0) {
			Node namedItem = attributes.getNamedItem("className");
			String className = namedItem.getNodeValue();
			if (className.equals(Collection.class.getName())) {
				NodeList childNodes = node.getChildNodes();
				List list = new ArrayList(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					list.add(this.parse(childNodes.item(i)));
				}
				object = list;
			} else if (className.equals(Map.class.getName())) {
				NodeList childNodes = node.getChildNodes();
				Map map = new HashMap(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node node2 = childNodes.item(i);
					map.put(node2.getNodeName(), this.parse(node2));
				}
				object = map;
			} else {
				/* just simple objects */
				NodeList childNodes = node.getChildNodes();
				if (childNodes.getLength() != 1)
					Log.errorMessage("StorableObjectXMLDriver.parse | more that one child for : " + node.getNodeName());
				System.out.println("name:" + node.getNodeName());
				object = this.getObject(childNodes.item(0).getNodeValue(), className);
			}
		} else
			Log.errorMessage("StorableObjectXMLDriver.parse | there is no attributes for : " + node.getNodeName());
		return object;
	}

	private Object getObject(String value, String className) throws IllegalDataException {
		System.out.println("\tValue:" + value + "\tclassName:" + className);
		Object object = null;
		if (className.equals(StorableObject.class.getName())) {
			Identifier identifier = new Identifier(value);
			object = this.reflectStorableObject(identifier);
		} else if (className.equals(Identifier.class.getName())) {
			object = new Identifier(value);
		} else if (className.equals(Date.class.getName())) {
			object = new Date(Long.parseLong(value));
		} else if (className.equals(Integer.class.getName())) {
			Integer integer = Integer.valueOf(value);
			object = integer;
		} else if (className.equals(Long.class.getName())) {
			Long long1 = Long.valueOf(value);
			object = long1;
		} else if (className.equals(Double.class.getName())) {
			Double double1 = Double.valueOf(value);
			object = double1;
		} else if (className.equals(String.class.getName())) {
			object = value;
		} else if (className.equals(byte[].class.getName())) {
			byte[] bs = new byte[value.length() / 2];
			for (int j = 0; j < bs.length; j++)
				bs[j] = (byte) ((char) Integer.parseInt(value.substring(2 * j, 2 * (j + 1)), 16));
			object = bs;
		} else
			object = value;
		return object;
	}

	private void addObject(Node node, String key, Object object) {
		Element element = this.doc.createElement(key);
		String className = object.getClass().getName();
		if (object instanceof StorableObject) {
			StorableObject storableObject = (StorableObject) object;
			String string = storableObject.getId().getIdentifierString();
			Text text = this.doc.createTextNode(string);
			className = StorableObject.class.getName();
			element.appendChild(text);
		} else if (object instanceof Identifier) {
			Identifier id = (Identifier) object;
			String string = id.getIdentifierString();
			Text text = this.doc.createTextNode(string);
			element.appendChild(text);
		} else if (object instanceof Date) {
			Date date = (Date) object;
			Text text = this.doc.createTextNode(Long.toString(date.getTime()));
			element.appendChild(text);
		} else if (object instanceof Integer) {
			Integer integer = (Integer) object;
			Text text = this.doc.createTextNode(integer.toString());
			element.appendChild(text);
		} else if (object instanceof Long) {
			Long long1 = (Long) object;
			Text text = this.doc.createTextNode(long1.toString());
			element.appendChild(text);
		} else if (object instanceof Double) {
			Double double1 = (Double) object;
			Text text = this.doc.createTextNode(double1.toString());
			element.appendChild(text);
		} else if (object instanceof String) {
			String string = (String) object;
			Text text = this.doc.createTextNode(string);
			element.appendChild(text);
		} else if (object instanceof byte[]) {
			byte[] bs = (byte[]) object;
			StringBuffer buffer = new StringBuffer();
			for (int j = 0; j < bs.length; j++) {
				String s = Integer.toString(bs[j] & 0xFF, 16);
				buffer.append((s.length() == 1 ? "0" : "") + s);
			}
			Text text = this.doc.createTextNode(buffer.toString());
			element.appendChild(text);
		} else if (object instanceof Collection) {
			Collection collection = (Collection) object;
			className = Collection.class.getName();
			for (Iterator it = collection.iterator(); it.hasNext();) {
				this.addObject(element, key + "item", it.next());
			}
		} else if (object instanceof Map) {
			Map map = (Map) object;
			className = Map.class.getName();
			for (Iterator it = map.keySet().iterator(); it.hasNext();) {
				String key2 = (String) it.next();
				this.addObject(element, key2, map.get(key2));
			}
		} else {
			Log.errorMessage("StorableObjectXMLDriver.convertToString | unsupported class value : "
					+ object.getClass().getName());
			String string = object.toString();
			Text text = this.doc.createTextNode(string);
			element.appendChild(text);
		}
		element.setAttribute("className", className);
		node.appendChild(element);
	}

	public List getIdentifiers(short entityCode) throws IllegalDataException {

		try {
			NodeList idNodeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/"
					+ "*[starts-with(name(),'" + ObjectEntities.codeToString(entityCode) + "')]");
			int size = idNodeList.getLength();
			if (size == 0)
				return Collections.EMPTY_LIST;

			List idList = new ArrayList(size);
			for (int i = 0; i < idNodeList.getLength(); i++) {
				Node node = idNodeList.item(i);
				idList.add(new Identifier(node.getNodeName()));
			}
			return idList;
		} catch (TransformerException e) {
			String msg = "StorableObjectXMLDriver.getIdentifiers | Caught " + e.getMessage()
					+ " during retrieve identifiers for '" + ObjectEntities.codeToString(entityCode) + '\'';
			Log.errorMessage(msg);
			throw new IllegalDataException(msg, e);
		}
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
			String msg = "StorableObjectXMLDriver.deleteObject | Caught " + te.getMessage();
			Log.errorMessage(msg);
			throw new IllegalDataException(msg, te);
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
			this.doc.normalize();
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
