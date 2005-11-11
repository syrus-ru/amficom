/*-
* $Id: ResourceHandler.java,v 1.1 2005/11/11 11:14:30 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resources;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.AMFICOM.extensions.AbstractExtensionHandler;
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.resources.Handler;
import com.syrus.AMFICOM.extensions.resources.Resource;
import com.syrus.AMFICOM.extensions.resources.Resources;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/11 11:14:30 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module resources
 */
public final class ResourceHandler extends AbstractExtensionHandler {

	private static final Map<String, ConcreateResourceHandler> HANDLES = 
		new HashMap<String, ConcreateResourceHandler>();

	private final static Level LOGLEVEL = Log.DEBUGLEVEL08;
	
	private final Resources	resources;	

	public ResourceHandler(final ExtensionPoint extensionPoint) {
		this((Resources)extensionPoint);
	}
	
	public ResourceHandler(final Resources resources) {
		this.resources = resources;
		this.loadHandlers();
		this.loadResources();
	}

	private ConcreateResourceHandler getHandler(final String clazz) {
		return (ConcreateResourceHandler) super.loadHandler(clazz, 
			new Class[] {}, 
			new Object[] {});
    }
	
	private void loadHandlers() {
		for (final Resource resource : this.resources.getResourceArray()) {
			// processing only handlers
			if (resource instanceof Handler) {
				final Handler handler = (Handler) resource;
				final String id = handler.getId();
				if (HANDLES.get(id) == null) {
					final String handlerClass = handler.getHandlerClass();
					final ConcreateResourceHandler handlerClazz = this.getHandler(handlerClass);
					assert Log.debugMessage("ResourceHandler.loadHandlers | " 
							+ (handlerClazz != null ? "Added '" +
									handlerClass + '\'': "Cannot resolve ")							
							+ " handler for '" 
							+ id 
							+ '\'',
						LOGLEVEL);				
					HANDLES.put(id, handlerClazz);
				}
			}
		}
	}

	private void loadResources() {
		for (final Resource resource : this.resources.getResourceArray()) {
			// skip handler
			if (resource instanceof Handler) {
				continue;
			}
			
			final String type = resource.schemaType().getName().getLocalPart();
			final ConcreateResourceHandler<Resource> handler = HANDLES.get(type);
			if (handler != null) {
				handler.load(resource);
			} else {
				Log.errorMessage("There is no handler for " + type);
			}
		}
	}
}

