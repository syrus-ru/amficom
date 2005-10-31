/*
 * $Id: StorableObjectXMLDriver.java,v 1.36 2005/10/31 12:30:18 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
 * @version $Revision: 1.36 $, $Date: 2005/10/31 12:30:18 $
 * @author $Author: bass $
 * @module general
 */
public class StorableObjectXMLDriver extends StorableObjectXMLData {

	private String fileName;
	private Document doc;
	private String packageName;
	private Node root;

	public StorableObjectXMLDriver(final File path, final String packageName) {
		if (path.exists()) {
			if (path.isDirectory()) {
				this.fileName = path.getAbsolutePath() + File.separatorChar + packageName + ".xml";
			} else {
				this.fileName = path.getParent();
			}
		} else {
			path.mkdirs();
			this.fileName = path.getAbsolutePath() + File.separatorChar + packageName + ".xml";
		}
		this.packageName = packageName;
		this.parseXmlFile(false);
	}

	public void putObjectMap(final Identifier identifier, final Map<String, Object> objects) {		
		final Element element = this.doc.createElement(identifier.getIdentifierString());
		for (final String key : objects.keySet()) {
			final Object obj = objects.get(key);
			if (obj != null)
				this.addObject(element, key, obj);

		}
		this.root.appendChild(element);
	}

	public Map<String, Object> getObjectMap(final Identifier identifier)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		Map<String, Object> map = null;
		try {
			final NodeList objList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/" + identifier.getIdentifierString());
			if (objList.getLength() > 1)
				throw new IllegalDataException("StorableObjectXMLDriver.getObjectMap | more that one entity with id "
						+ identifier.getIdentifierString());
			if (objList.getLength() == 0)
				throw new ObjectNotFoundException("StorableObjectXMLDriver.getObjectMap | object not found : "
						+ identifier.getIdentifierString());
			final Node item = objList.item(0);
			final NodeList childNodes = item.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				final Node node = childNodes.item(i);
				if (map == null)
					map = new HashMap<String, Object>();
				map.put(node.getNodeName(), this.parse(node));
			}

			if (map == null)
				map = Collections.emptyMap();
		} catch (TransformerException te) {
			final String msg = "StorableObjectXMLDriver.getObjectMap | Caught " + te.getMessage();
			Log.errorMessage(msg);
			throw new RetrieveObjectException(msg, te);
		}

		return map;
	}


	private Object parse(final Node node) throws IllegalDataException {
//		Log.debugMessage("node name:" + node.getNodeName(), Log.INFO);
		Object object = null;
		final NamedNodeMap attributes = node.getAttributes();
		if (attributes == null)
			return object;
		if (attributes.getLength() > 0) {
			final Node namedItem = attributes.getNamedItem("className");
			final String className = namedItem.getNodeValue();
			if (className.equals(Collection.class.getName())) {
				final NodeList childNodes = node.getChildNodes();
				final Set<Object> set = new HashSet<Object>(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					final Object object2 = this.parse(childNodes.item(i));
					if (object2 != null)
						set.add(object2);
				}
				object = set;
			} else if (className.equals(Map.class.getName())) {
				final NodeList childNodes = node.getChildNodes();
				final Map<String, Object> map = new HashMap<String, Object>(childNodes.getLength());
				for (int i = 0; i < childNodes.getLength(); i++) {
					final Node node2 = childNodes.item(i);
					final Object object2 = this.parse(node2);
					if (object2 != null)
						map.put(node2.getNodeName(), object2);
				}
				object = map;
			} else {
				object = this.getObject(node.getChildNodes(), className);
			}
		} else
			Log.errorMessage("there is no attributes for : " + node.getNodeName());
		return object;
	}

	private Object getObject(final NodeList childNodes, final String className) throws IllegalDataException {
		String value = null;
		/* just simple objects */
		if (childNodes.getLength() > 1) {
			throw new IllegalDataException("StorableObjectXMLDriver.getObject | more that one child for : "
					+ childNodes.item(0).getParentNode().getNodeName());
		}
		if (childNodes.getLength() == 1) {
			value = childNodes.item(0).getNodeValue();
		}
		return super.getObject(className, value);
	}

	private void addObject(final Node node, final String key, final Object object) {
		final Element element = this.doc.createElement(key);
		if (object == null) {
			Log.errorMessage("key : " + key + " , value is 'null'");
			return;
		}
		
		final String className = super.getClassName(object);		
		if (object instanceof Collection) {
			/* TODO replace for java.util.Set*/
			final Collection<?> collection = (Collection) object;
			for (final Object element2 : new HashSet<Object>(collection)) {
				this.addObject(element, key + "item", element2);
			}
		} else if (object instanceof Map) {
			final Map map = (Map) object;
			for (final Iterator it = map.keySet().iterator(); it.hasNext();) {
				final String key2 = (String) it.next();
				this.addObject(element, key2, map.get(key2));
			}
		} else {
			String value = super.getValue(object);
			if (value != null) {
				final Text text = this.doc.createTextNode(value);
				element.appendChild(text);
			}
		}
		element.setAttribute("className", className);
		node.appendChild(element);
	}

	public Set<Identifier> getIdentifiers(final short entityCode) throws IllegalDataException {

		try {
			// XXX just only for current representation of id
			final NodeList idNodeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/"
					+ "*[starts-with(name(),'" + ObjectEntities.codeToString(entityCode) + Identifier.SEPARATOR + "')]");
			final int size = idNodeList.getLength();
			if (size == 0)
				return Collections.emptySet();

			final Set<Identifier> idSet = new HashSet<Identifier>(size);
			for (int i = 0; i < idNodeList.getLength(); i++) {
				final Node node = idNodeList.item(i);
				idSet.add(new Identifier(node.getNodeName()));
			}
			
			return idSet;
		} catch (TransformerException e) {
			final String msg = "StorableObjectXMLDriver.getIdentifiers | Caught " + e.getMessage()
					+ " during retrieve identifiers for '" + ObjectEntities.codeToString(entityCode) + '\'';
			Log.errorMessage(msg);
			throw new IllegalDataException(msg, e);
		}
	}

	public void deleteObject(final Identifier identifier) {
		try {
			final NodeList sizeList = XPathAPI.selectNodeList(this.doc, "//" + this.packageName + "/" + identifier.getIdentifierString());
			if (sizeList.getLength() > 1) {
				Log.errorMessage("more that one entity with id "
						+ identifier.getIdentifierString());
				return;
			}

			for (int i = 0; i < sizeList.getLength(); i++) {
				final Node children = sizeList.item(i);
				this.root.removeChild(children);
			}
		} catch (TransformerException te) {
			Log.errorMessage(te);
		}
	}

	// Parses an XML file and returns a DOM document.
	// If validating is true, the contents is validated against the DTD
	// specified in the file.
	private Document parseXmlFile(final boolean validating) {
		try {
			// Create a builder factory
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validating);

			// Create the builder and parse the file
			final File file = new File(this.fileName);
			if (file.exists()) {
				this.doc = factory.newDocumentBuilder().parse(file);
				this.root = this.doc.getFirstChild();
			} else {
				/* if file not exists creat default document */
				if (file.createNewFile()) {
					final DocumentBuilder builder = factory.newDocumentBuilder();
					this.doc = builder.newDocument();
					/* with root item of package name */
					this.root = this.doc.createElement(this.packageName);
					this.doc.appendChild(this.root);
				}
			}
			return this.doc;
		} catch (SAXException e) {
			// A parsing error occurred; the xml input is not valid
			Log.errorMessage("Caught " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.errorMessage("Caught " + e.getMessage());
		} catch (IOException e) {
			Log.errorMessage("Caught " + e.getMessage());
		}
		return null;
	}

	public void writeXmlFile() {
		try {
			this.doc.normalize();
			// Prepare the DOM document for writing
			final Source source = new DOMSource(this.doc);

			// Prepare the output file
			final File file = new File(this.fileName);
			FileOutputStream stream = new FileOutputStream(file);
			final Result result = new StreamResult(stream);

			// Write the DOM document to the file
			final Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.transform(source, result);
		} catch (TransformerConfigurationException e) {
			Log.errorMessage("Caught " + e.getMessage());
		} catch (TransformerException e) {
			Log.errorMessage("Caught " + e.getMessage());
		} catch (FileNotFoundException e) {
			Log.errorMessage("Caught " + e.getMessage());
		}
	}

	
	public final String getPackageName() {
		return this.packageName;
	}

	
	public final Document getDoc() {
		return this.doc;
	}
}
