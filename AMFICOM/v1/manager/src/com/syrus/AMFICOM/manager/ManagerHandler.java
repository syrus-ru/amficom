/*-
* $Id: ManagerHandler.java,v 1.2 2005/11/09 15:09:48 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.extensions.AbstractExtensionHandler;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.amficom.extensions.ExtensionPoint;
import com.syrus.amficom.extensions.manager.BeanFactory;
import com.syrus.amficom.extensions.manager.ManagerExtensions;
import com.syrus.amficom.extensions.manager.ManagerResource;
import com.syrus.amficom.extensions.manager.Perspective;
import com.syrus.amficom.extensions.manager.UiHandler;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.2 $, $Date: 2005/11/09 15:09:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerHandler extends AbstractExtensionHandler {
	
	private final ManagerExtensions	managerExtensions;
	private ManagerMainFrame	managerMainFrame;
	private final Map<String, PerspectiveData> perspectives;

	public ManagerHandler(final ExtensionPoint extensionPoint) {
		this((ManagerExtensions)extensionPoint);
	}
	
	public ManagerHandler(final ManagerExtensions managerExtensions) {
		this.managerExtensions = managerExtensions;
		this.perspectives = new HashMap<String, PerspectiveData>();
	}
	
	public final void setManagerMainFrame(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;		
	}
	
	private final BeanUI loadBeanUI(final String handlerClass) {
		return (BeanUI) super.loadHandler(handlerClass, 
			new Class[] {ManagerMainFrame.class}, 
			new Object[] {this.managerMainFrame});
	}
	
	private final AbstractBeanFactory loadAbstractBeanFactory(final String handlerClass) {
		return (AbstractBeanFactory) super.loadHandler(handlerClass, 
			new Class[] {ManagerMainFrame.class}, 
			new Object[] {this.managerMainFrame});
	}

	public final PerspectiveData getPerspectiveData(String perspectiveCodename) {
		perspectiveCodename = perspectiveCodename.intern();
		PerspectiveData perspectiveData = this.perspectives.get(perspectiveCodename);
		
		if (perspectiveData == null) { 
			for (final ManagerResource managerResource : this.managerExtensions.getManagerResourceArray()) {
				if (managerResource instanceof Perspective) {
					Perspective perspective = (Perspective) managerResource;
					final String id = perspective.getId().intern();
					
					assert Log.debugMessage(id, Log.DEBUGLEVEL10);
					
					if (id == perspectiveCodename) {
						
						final Map<String, AbstractBeanFactory> factories = 
							new HashMap<String, AbstractBeanFactory>();
						final Map<String, BeanUI> beanUI = new HashMap<String, BeanUI>();
						final Set<String> undeletable = new HashSet<String>();
						
						final BeanFactory[] beanFactoryArray = perspective.getBeanFactoryArray();
						for (final BeanFactory factory : beanFactoryArray) {
							final String beanFactoryClass = factory.getBeanFactoryClass();
							final String factoryId = factory.getId();
							final AbstractBeanFactory factoryInstance = this.loadAbstractBeanFactory(beanFactoryClass);
							assert Log.debugMessage("factory for " 
									+ factoryId
									+ (factoryInstance != null ? " registered successfull" : 
										" failed."), 
								Log.DEBUGLEVEL10);
							factories.put(factoryId, 
								factoryInstance);
						}
						
						final UiHandler[] uiHandlerArray = perspective.getUiHandlerArray();
						for (final UiHandler handler : uiHandlerArray) {
							beanUI.put(handler.getId(), this.loadBeanUI(handler.getUiHandlerClass()));
						}
						
						undeletable.addAll(Arrays.asList(perspective.getUndeletableArray()));
						
						perspectiveData = new PerspectiveData(factories, 
							beanUI,
							undeletable);
						
						assert Log.debugMessage("perspective '" 
								+ perspectiveCodename
								+ "' registered successfully.",
							Log.DEBUGLEVEL10);
						this.perspectives.put(perspectiveCodename, perspectiveData);
						return perspectiveData;
					}
				}
			}
			final String msg = "perspective " 
				+ perspectiveCodename 
				+ " not found.";
			Log.errorMessage(msg);
			
			throw new IllegalStateException(msg);			
		}
		
		return perspectiveData;
	}
}

