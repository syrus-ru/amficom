/*-
 * $Id: Plugger.java,v 1.2 2005/05/17 07:30:44 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

import javax.swing.UIDefaults;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/05/17 07:30:44 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module schedulerClone
 */
public class Plugger {

	private File		file	= new File("plugin.xml");

	private Document	doc;
	private Node		plugin;

	private Hashtable	plugins	= new UIDefaults();

	public Plugger() {
		this.parseXmlFile(false);
		this.parseXML();
	}

	private void parseXML() {
		final NodeList childNodes = this.plugin.getChildNodes();

		NamedNodeMap attributes = this.plugin.getAttributes();
		if (attributes != null) {
			String id = null;
			String className = null;
			for (int j = 0; j < attributes.getLength(); j++) {
				Node node = attributes.item(j);
				String nodeName = node.getNodeName();
				String nodeValue = node.getNodeValue();
				if (nodeName.equals("id")) {
					id = nodeValue;
				} else if (nodeName.equals("class")) {
					className = nodeValue;
				}
			}
			if (id != null && className != null) {
				final String className2 = className;
				this.plugins.put(id, new UIDefaults.LazyValue() {

					public Object createValue(UIDefaults arg0) {
						return reflectClass(className2, new Class[] {NodeList.class}, new Object[] {childNodes});
					}
				});
			}
		}
	}

	public static Object reflectClass(String className, Class[] classes, Object[] arguments) {
		Object object = null;
		try {
			Class clazz = Class.forName(className);
			Constructor constructor = clazz.getConstructor(classes);
			object = constructor.newInstance(arguments);
		} catch (ClassNotFoundException e) {
			Log.debugMessage("Plugger.reflectClass | Class " + className + " not found -- " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (SecurityException e) {
			Log.debugMessage("Plugger.reflectClass | Security exception-- " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (NoSuchMethodException e) {
			Log.debugMessage("Plugger.reflectClass | No such constuctor -- " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (IllegalArgumentException e) {
			Log.debugMessage("Plugger.reflectClass | Illegal argument -- " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (InstantiationException e) {
			Log.debugMessage("Plugger.reflectClass | " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (IllegalAccessException e) {
			Log.debugMessage("Plugger.reflectClass | " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		} catch (InvocationTargetException e) {
			Log.debugMessage("Plugger.reflectClass | " + e.getMessage(), Log.WARNING);
			Log.errorException(e);
		}

		return object;
	}

	private Document parseXmlFile(final boolean validating) {
		try {
			// Create a builder factory
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(validating);

			// Create the builder and parse the file
			if (this.file.exists()) {
				this.doc = factory.newDocumentBuilder().parse(this.file);
				this.plugin = this.doc.getElementsByTagName("plugin").item(0);
			} else {
				Log.debugMessage("Plugger.parseXmlFile | " + this.file.getAbsolutePath() + " not found.", Log.FINEST);
				return null;
			}
			return this.doc;
		} catch (SAXException e) {
			// A parsing error occurred; the xml input is not valid
			Log.errorMessage("Plugger.parseXmlFile | Caught " + e.getMessage());
		} catch (ParserConfigurationException e) {
			Log.errorMessage("Plugger.parseXmlFile | Caught " + e.getMessage());
		} catch (IOException e) {
			Log.errorMessage("Plugger.parseXmlFile | Caught " + e.getMessage());
		}
		return null;
	}

//	public Plugin getPlugin(String id) {
//		return (Plugin) this.plugins.get(id);
//	}
	
//	public static void main(String[] args) {
//		Plugger plugger = new Plugger();
//		Plugin plugin = plugger.getPlugin("parametersPanel");
//		System.out.println("plugin " + plugin);
//	}
}
