/*-
 * $Id: Plugger.java,v 1.6 2005/10/30 15:20:23 bass Exp $
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
import java.util.logging.Level;

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
 * @version $Revision: 1.6 $, $Date: 2005/10/30 15:20:23 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module schedulerClone
 */
public class Plugger {

	private File		file	= new File("plugin.xml");

	private Document	doc;
	private Node		plugin;

	private UIDefaults	plugins	= new UIDefaults();

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
			assert Log.debugMessage("Class " + className + " not found -- " + e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (SecurityException e) {
			assert Log.debugMessage("Security exception-- " + e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			assert Log.debugMessage("No such constuctor -- " + e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (IllegalArgumentException e) {
			assert Log.debugMessage("Illegal argument -- " + e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (InstantiationException e) {
			assert Log.debugMessage(e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			assert Log.debugMessage(e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			assert Log.debugMessage(e.getMessage(), Level.WARNING);
			assert Log.errorMessage(e);
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
				assert Log.debugMessage(this.file.getAbsolutePath() + " not found.", Level.FINEST);
				return null;
			}
			return this.doc;
		} catch (SAXException e) {
			// A parsing error occurred; the xml input is not valid
			assert Log.errorMessage("Caught " + e.getMessage());
		} catch (ParserConfigurationException e) {
			assert Log.errorMessage("Caught " + e.getMessage());
		} catch (IOException e) {
			assert Log.errorMessage("Caught " + e.getMessage());
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
