/*
 * $Id: StorableObjectXMLDriver.java,v 1.22 2005/07/11 08:18:56 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
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
 * @version $Revision: 1.22 $, $Date: 2005/07/11 08:18:56 $
 * @author $Author: bass $
 * @module general_v1
 */
public class StorableObjectXMLDriver {

	private String		fileName;
	private Document	doc;
	private String		packageName;
	private Node		root;

	public StorableObjectXMLDriver(final File path, final String packageName) {
		if (path.exists()) {
			if (path.isDirectory()) {
				this.fileName = path.getAbsolutePath() + File.separatorChar + packageName + ".xml";
			} else {
				this.fileName = path.getParent();
			}
		} else {
			if (path.isDirectory()) {
				path.mkdirs();
			} else {
				path.getParentFile().mkdirs();
			}
			this.fileName = path.getAbsolutePath();
		}
		this.packageName = packageName;
		this.parseXmlFile(false);
	}

	public void putObjectMap(final Identifier identifier, final Map objects) {		
		Element element = this.doc.createElement(identifier.getIdentifierString());
		for (Iterator it = objects.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object obj = objects.get(key);
			if (obj != null)
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
			Log.debugMessage("StorableObjectXMLDriver.reflectStorableObject | className " + className, Level.INFO);
			Class clazz = Class.forName(className);
			Log.debugMessage("StorableObjectXMLDriver.reflectStorableObject | className " + clazz.getName(), Level.INFO);
			Method method = clazz.getMethod("getStorableObject", new Class[] { Identifier.class, boolean.class});
			storableObject = (StorableObject) method.invoke(null, new Object[] { id, Boolean.TRUE});
			Log.debugMessage("StorableObjectXMLDriver.reflectStorableObject | id "
					+ storableObject.getId().getIdentifierString(), Level.INFO);

		} catch (ClassNotFoundException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Class " + className
					+ " not found on the classpath - " + e.getMessage());
		} catch (SecurityException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Caught SecurityException "
					+ e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalDataException("StorableObjectXMLDriver.reflectStorableObject | Class " + className
					+ " haven't getStorableObject static method - " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalDataException(
											"StorableObjectXMLDriver.reflectStorableObject | Caught IllegalArgumentException "
													+ e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalDataException(
											"StorableObjectXMLDriver.reflectStorableObject | Caught IllegalAccessException "
													+ e.getMessage());
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else		{
				Throwable targetException = e.getTargetException();
				throw new IllegalDataException(
												"StorableObjectXMLDriver.reflectStorableObject | Caught InvocationTargetException "
														+ targetException.getMessage(), targetException);
			}
		}
		return storableObject;
	}

	private Object parse(Node node) throws IllegalDataException {
//		Log.debugMessage("StorableObjectXMLDriver.parse | node name:" +
//		node.getNodeName(), Log.INFO);
		Object object = null;
		NamedNodeMap attributes = node.getAttributes();
		if (attributes == null)
			return object;
		if (attributes.getLength() > 0) {
			Node namedItem = attributes.getNamedItem("className");
			String className = namedItem.getNodeValue();
			if (className.equals(Collection.class.getName())) {
				NodeList childNodes = node.getChildNodes();
				Set set = new HashSet(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Object object2 = this.parse(childNodes.item(i));
					if (object2 != null)
						set.add(object2);
				}
				object = set;
			} else if (className.equals(Map.class.getName())) {
				NodeList childNodes = node.getChildNodes();
				Map map = new HashMap(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node node2 = childNodes.item(i);
					Object object2 = this.parse(node2);
					if (object2 != null)
						map.put(node2.getNodeName(), object2);
				}
				object = map;
			} else {
				object = this.getObject(node.getChildNodes(), className);
			}
		} else
			Log.errorMessage("StorableObjectXMLDriver.parse | there is no attributes for : " + node.getNodeName());
		return object;
	}

	private Object getObject(NodeList childNodes, String className) throws IllegalDataException {
		String value = null;
		/* just simple objects */
		if (childNodes.getLength() > 1)
			throw new IllegalDataException("StorableObjectXMLDriver.getObject | more that one child for : "
					+ childNodes.item(0).getParentNode().getNodeName());
		if (childNodes.getLength() == 1)
			value = childNodes.item(0).getNodeValue();
		Object object = null;
		if (className.equals(StorableObject.class.getName())) {
			Identifier identifier = new Identifier(value);
			object = this.reflectStorableObject(identifier);
		} else if (className.equals(Boolean.class.getName())) {
			object = new Boolean(value);
		} else if (className.equals(Identifier.class.getName())) {
			object = new Identifier(value);
		} else if (className.equals(Date.class.getName())) {
			object = new Date(Long.parseLong(value));
		} else if (className.equals(Short.class.getName())) {
			Short short1 = Short.valueOf(value);
			object = short1;
		} else if (className.equals(Integer.class.getName())) {
			Integer integer = Integer.valueOf(value);
			object = integer;
		} else if (className.equals(Long.class.getName())) {
			Long long1 = Long.valueOf(value);
			object = long1;
		} else if (className.equals(Float.class.getName())) {
			Float float1 = Float.valueOf(value);
			object = float1;
		} else if (className.equals(Double.class.getName())) {
			Double double1 = Double.valueOf(value);
			object = double1;
		} else if (className.equals(String.class.getName())) {
			if (value != null)
				object = value;
			else
				object = "";
		} else if (className.equals(byte[].class.getName())) {
			/* if value is null, array is empty */
			if (value != null) {
				byte[] bs = new byte[value.length() / 2];
				for (int j = 0; j < bs.length; j++)
					bs[j] = (byte) ((char) Integer.parseInt(value.substring(2 * j, 2 * (j + 1)), 16));
				object = bs;
			} else
				object = new byte[0];
		} else
			object = value;
		return object;
	}

	private void addObject(Node node, String key, Object object) {
		Element element = this.doc.createElement(key);
		if (object == null) {
			Log.errorMessage("StorableObjectXMLDriver.addObject | key : " + key + " , value is 'null'");
			return;
		}
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
		} else if (object instanceof Boolean) {
			Boolean boolean1 = (Boolean) object;
			Text text = this.doc.createTextNode(boolean1.toString());
			element.appendChild(text);
		} else if (object instanceof Integer) {
			Integer integer = (Integer) object;
			Text text = this.doc.createTextNode(integer.toString());
			element.appendChild(text);
		} else if (object instanceof Short) {
			Short short1 = (Short) object;
			Text text = this.doc.createTextNode(short1.toString());
			element.appendChild(text);
		} else if (object instanceof Long) {
			Long long1 = (Long) object;
			Text text = this.doc.createTextNode(long1.toString());
			element.appendChild(text);
		} else if (object instanceof Float) {
			Float float1 = (Float) object;
			Text text = this.doc.createTextNode(float1.toString());
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
			/* TODO replace for java.util.Set*/
			Collection collection = (Collection) object;
			className = Collection.class.getName();
			Set set = new HashSet(collection);
			for (Iterator it = set.iterator(); it.hasNext();) {
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
			Log.errorMessage("StorableObjectXMLDriver.addObject | unsupported class value : "
					+ object.getClass().getName());
			String string = object.toString();
			Text text = this.doc.createTextNode(string);
			element.appendChild(text);
		}
		element.setAttribute("className", className);
		node.appendChild(element);
	}

	public Set getIdentifiers(short entityCode) throws IllegalDataException {

		try {
			NodeList idNodeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/"
					+ "*[starts-with(name(),'" + ObjectEntities.codeToString(entityCode) + "')]");
			int size = idNodeList.getLength();
			if (size == 0)
				return Collections.EMPTY_SET;

			Set idSet = new HashSet(size);
			for (int i = 0; i < idNodeList.getLength(); i++) {
				Node node = idNodeList.item(i);
				idSet.add(new Identifier(node.getNodeName()));
			}
			return idSet;
		} catch (TransformerException e) {
			String msg = "StorableObjectXMLDriver.getIdentifiers | Caught " + e.getMessage()
					+ " during retrieve identifiers for '" + ObjectEntities.codeToString(entityCode) + '\'';
			Log.errorMessage(msg);
			throw new IllegalDataException(msg, e);
		}
	}

	public void deleteObject(final Identifier identifier) {
		try {
			NodeList sizeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/" + identifier.getIdentifierString());
			if (sizeList.getLength() > 1) {
				Log.errorMessage("StorableObjectXMLDriver.deleteObject | more that one entity with id "
						+ identifier.getIdentifierString());
				return;
			}

			for (int i = 0; i < sizeList.getLength(); i++) {
				Node children = sizeList.item(i);
				this.root.removeChild(children);
			}
		} catch (TransformerException te) {
			Log.errorException(te);
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
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			Log.errorMessage("StorableObjectXMLDriver.writeXmlFile | Caught " + e.getMessage());
		} catch (TransformerException e) {
			Log.errorMessage("StorableObjectXMLDriver.writeXmlFile | Caught " + e.getMessage());
		}
	}
}
