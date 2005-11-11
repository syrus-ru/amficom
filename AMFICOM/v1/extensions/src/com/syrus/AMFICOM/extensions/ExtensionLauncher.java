/*-
* $Id: ExtensionLauncher.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.extensions;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlException;

import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.RootDocument;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module resources
 */
public final class ExtensionLauncher {	
	
	private static final String INVOKE_EXTENSION = 
		"ExtensionLauncher.invokeExtension() | ";
	private static final String INVALID_UNDERLYING_IMPLEMENTATION = 
		"Invalid underlying implementation: ";
	
	private static ExtensionLauncher instance;
	
	private Map<String, ExtensionHandler> extensionHandlers; 
	private Map<String, Map<String, ExtensionPoint>> extensionManifests;
	
	private ExtensionLauncher() {
		this.extensionHandlers = 
			Collections.synchronizedMap(new HashMap<String, ExtensionHandler>());
		
		this.extensionManifests = 
			Collections.synchronizedMap(new HashMap<String, Map<String, ExtensionPoint>>());
	}

    public static synchronized ExtensionLauncher getInstance() {
    	if (instance == null) {
    		instance = new ExtensionLauncher();
    	}
    	return instance;
    }
    
    public final void addExtensions(final String xmlFilePath) {
    	Map<String, ExtensionPoint> extensionManifest = 
    		this.extensionManifests.get(xmlFilePath);
    	if (extensionManifest == null) {
    		extensionManifest = new HashMap<String, ExtensionPoint>();
	    	final File xmlFile = new File(xmlFilePath);    	
	    	if (xmlFile.exists()) {
	    		this.extensionManifests.put(xmlFilePath, extensionManifest);
		    	final RootDocument document;
				try {
					document = this.parseXml(xmlFile);
				} catch (final XmlException e) {
					Log.errorException(e);
					return;
				}
		    	final ExtensionPoint[] extensionArray = document.getRoot().getExtensionArray();
		    	for (final ExtensionPoint point : extensionArray) {
		    		extensionManifest.put(point.getId(), point);
				}
	    	} else {
	    		Log.errorMessage("Extension file '" + xmlFilePath + "' isn't exist");
	    	}
    	}
    }
    
    public final <T extends ExtensionHandler> T getExtensionHandler(final String id) {
    	// get from initialized extension handles
    	T handler = (T) this.extensionHandlers.get(id);
    	// if extension handler has not exist - init it 
    	if (handler == null) {
    		// search for all xml file name contexts
    		for (final String xmlFileName : this.extensionManifests.keySet()) {
    			final Map<String, ExtensionPoint> extensionManifest = 
    				this.extensionManifests.get(xmlFileName);
				final ExtensionPoint point = extensionManifest.get(id);
				if (point != null) {
					// acqure extension handler from extension point
					final ExtensionHandler extensionHandler = this.getExtension(point);
					this.extensionHandlers.put(id, extensionHandler);
					handler = (T) extensionHandler;
					break;
				}
			}
    	}
    	
    	if (handler == null) {
    		Log.errorMessage("Extension handler for '" + id + "' isn't exist");
    	}
    	return handler;
    }
    
    private final ExtensionHandler getExtension(final ExtensionPoint point) {
    	final String id = point.getId();
    	try {
			final Constructor ctor = 
				Class.forName(id).getDeclaredConstructor(new Class[] {ExtensionPoint.class});
			final ExtensionHandler extensionHandler = 
				(ExtensionHandler) ctor.newInstance(new Object[] {point});			
			return extensionHandler;
		} catch (final ClassNotFoundException cnfe) {
			Log.errorMessage(INVOKE_EXTENSION + "Class " + id
					+ " not found on the classpath");
		} catch (final ClassCastException cce) {
			Log.errorMessage(INVOKE_EXTENSION + "Caught an ClassCastException");
		} catch (final NoSuchMethodException nsme) {
			Log.errorMessage(INVOKE_EXTENSION + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ id + " doesn't have the constructor expected");
		} catch (final InstantiationException ie) {
			Log.errorMessage(INVOKE_EXTENSION + INVALID_UNDERLYING_IMPLEMENTATION + "class "
					+ id + " is abstract");
		} catch (final InvocationTargetException ite) {
			final Throwable cause = ite.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false : message;
			} else
				Log.errorMessage(INVOKE_EXTENSION + INVALID_UNDERLYING_IMPLEMENTATION
						+ "constructor throws an exception in class "
						+ id);
		} catch (final IllegalAccessException iae) {
			/*
			 * Never.
			 */
			Log.errorException(iae);
			Log.errorMessage(INVOKE_EXTENSION + "Caught an IllegalAccessException");
		} catch (final IllegalArgumentException iae) {
			/*
			 * Never.
			 */
			Log.errorException(iae);
			Log.errorMessage(INVOKE_EXTENSION + "Caught an IllegalArgumentException");
		} catch (final SecurityException se) {
			/*
			 * Never.
			 */
			Log.errorException(se);
			Log.errorMessage(INVOKE_EXTENSION + "Caught a SecurityException");
		}
		
		return null;
    }

    private final RootDocument parseXml(final File xmlFile) throws XmlException {		
		final RootDocument rootDoc;
		try {			
			rootDoc = RootDocument.Factory.parse(xmlFile);
			if (rootDoc.validate()) {
				return rootDoc;
			}
			throw new XmlException("Document '" + xmlFile.getAbsolutePath() + "' isn't valid.");
		} catch (final IOException e) {
			Log.errorException(e);
			throw new XmlException("Cannot read document '" + xmlFile.getAbsolutePath() + "'.");
		}
	}    
}
