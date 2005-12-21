/*-
* $Id: ExtensionLauncher.java,v 1.5 2005/12/21 10:39:54 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/
package com.syrus.AMFICOM.extensions;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.xmlbeans.XmlException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.5 $, $Date: 2005/12/21 10:39:54 $
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
	private Map<URL, Map<String, Set<ExtensionPoint>>> extensionManifests;
	
	private ExtensionLauncher() {
		this.extensionHandlers = 
			Collections.synchronizedMap(new HashMap<String, ExtensionHandler>());
		
		this.extensionManifests = 
			Collections.synchronizedMap(new HashMap<URL, Map<String, Set<ExtensionPoint>>>());
	}

    public static synchronized ExtensionLauncher getInstance() {
    	if (instance == null) {
    		instance = new ExtensionLauncher();
    	}
    	return instance;
    }
    
    public final void addExtensions(final URL url) {
    	assert Log.debugMessage(url, Log.DEBUGLEVEL08);
    	Map<String, Set<ExtensionPoint>> extensionManifest = 
    		this.extensionManifests.get(url);
    	if (extensionManifest == null) {
    		extensionManifest = new HashMap<String, Set<ExtensionPoint>>();
			if (url != null) {
				this.extensionManifests.put(url, extensionManifest);
				final RootDocument document;
				try {
					document = this.parseXml(url);
				} catch (final XmlException e) {
					Log.errorMessage(e);
					return;
				}
				final ExtensionPoint[] extensionArray = document.getRoot().getExtensionArray();
				for (final ExtensionPoint point : extensionArray) {
					final String id = point.getId();
					Set<ExtensionPoint> set = extensionManifest.get(id);
					if (set == null) {
						set = new HashSet<ExtensionPoint>();
						extensionManifest.put(id, set);
					}
					synchronized (set) {
//						assert Log.debugMessage("id:" + id + ", " + url, Log.DEBUGLEVEL03);
						set.add(point);
					}					
				}
			} else {
				Log.errorMessage("Extension file '" + url + "' doesn't exist");
			}
    	}
    }
    
    public final <T extends ExtensionHandler> T getExtensionHandler(final String id) {
    	// get from initialized extension handles
    	T handler = (T) this.extensionHandlers.get(id);
    	for (final URL xmlFileName : this.extensionManifests.keySet()) {
//    		assert Log.debugMessage("id:" + id + ", " + xmlFileName, Log.DEBUGLEVEL03);
			final Map<String, Set<ExtensionPoint>> extensionManifest = 
				this.extensionManifests.get(xmlFileName);
			final Set<ExtensionPoint> pointSet = extensionManifest.get(id);
			if (pointSet != null) {
				ExtensionHandler handler2 = this.extensionHandlers.get(id);
				if (handler2 == null) {
					// acqure extension handler from extension point
					final ExtensionHandler extensionHandler;
					final ExtensionHandler extensionHandler2 = this.extensionHandlers.get(id);
					if (extensionHandler2 == null) {
						extensionHandler = this.getExtension(id);
						this.extensionHandlers.put(id, extensionHandler);
					} else {
						extensionHandler = extensionHandler2;
					}
					handler = (T) extensionHandler;						
				} else {
					handler = (T) handler2;
				}
				synchronized (pointSet) {
					for(final Iterator<ExtensionPoint> iterator = pointSet.iterator(); iterator.hasNext();) {
						final ExtensionPoint next = iterator.next();
//						assert Log.debugMessage(handler + " > " + next, Log.DEBUGLEVEL03);
						handler.addHandlerData(next);
						iterator.remove();
					}
				}
			} else {
					assert Log.debugMessage("pointSet for " + id + " is null.", Log.DEBUGLEVEL03);
			}
		}
    	
    	if (handler == null) {
    		Log.errorMessage("Extension handler for '" + id + "' isn't exist");
    	}
    	return handler;
    }
    
    private final ExtensionHandler getExtension(final String id) {
    	try {
			final Constructor ctor = 
				Class.forName(id).getDeclaredConstructor(new Class[] {});
			final ExtensionHandler extensionHandler = 
				(ExtensionHandler) ctor.newInstance(new Object[] {});			
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

    private final RootDocument parseXml(final URL xmlFile) throws XmlException {		
		final RootDocument rootDoc;
		try {			
			rootDoc = RootDocument.Factory.parse(xmlFile);
			if (rootDoc.validate()) {
				return rootDoc;
			}
			throw new XmlException("Document '" + xmlFile + "' isn't valid.");
		} catch (final IOException e) {
			Log.errorException(e);
			throw new XmlException("Cannot read document '" + xmlFile + "'.");
		}
	}    
}
