/*-
* $Id: ManagerHandler.java,v 1.4 2005/11/11 08:04:06 bob Exp $
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
import com.syrus.AMFICOM.extensions.ExtensionPoint;
import com.syrus.AMFICOM.extensions.manager.BeanFactory;
import com.syrus.AMFICOM.extensions.manager.ManagerExtensions;
import com.syrus.AMFICOM.extensions.manager.ManagerResource;
import com.syrus.AMFICOM.extensions.manager.Perspective;
import com.syrus.AMFICOM.extensions.manager.UiHandler;
import com.syrus.AMFICOM.extensions.manager.Validator;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.4 $, $Date: 2005/11/11 08:04:06 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerHandler extends AbstractExtensionHandler {
	
	private final ManagerExtensions	managerExtensions;
	private ManagerMainFrame	managerMainFrame;
	private final Map<String, PerspectiveData> perspectives;
	
	private final Map<String, AbstractBeanFactory> factories;
	private final Map<String, BeanUI> beanUIs;

	public ManagerHandler(final ExtensionPoint extensionPoint) {
		this((ManagerExtensions)extensionPoint);
	}
	
	public ManagerHandler(final ManagerExtensions managerExtensions) {
		this.managerExtensions = managerExtensions;
		this.perspectives = new HashMap<String, PerspectiveData>();
		this.factories = new HashMap<String, AbstractBeanFactory>();
		this.beanUIs = new HashMap<String, BeanUI>();
	}
	
	public final void setManagerMainFrame(final ManagerMainFrame managerMainFrame) {
		this.managerMainFrame = managerMainFrame;		
	}
	
	private final BeanUI loadBeanUI(final String handlerClass) {
		BeanUI beanUI = this.beanUIs.get(handlerClass);
		if (beanUI == null) {
			beanUI = (BeanUI) super.loadHandler(handlerClass, 
			new Class[] {ManagerMainFrame.class}, 
			new Object[] {this.managerMainFrame});

			assert Log.debugMessage("beanUI " 
				+ handlerClass
				+ " loaded.", 
			Log.DEBUGLEVEL10);
			
			this.beanUIs.put(handlerClass, beanUI);
		}
		return beanUI;
	}
	
	private final AbstractBeanFactory loadAbstractBeanFactory(final String handlerClass) {
		AbstractBeanFactory factory = this.factories.get(handlerClass);
		if (factory == null) {
			factory = (AbstractBeanFactory) super.loadHandler(handlerClass, 
			new Class[] {ManagerMainFrame.class}, 
			new Object[] {this.managerMainFrame});
			
			assert Log.debugMessage("factory " 
					+ handlerClass
					+ " loaded.", 
				Log.DEBUGLEVEL10);
			
			this.factories.put(handlerClass, factory);
		}
		return factory;
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
						
						final Map<String, AbstractBeanFactory> perpectiveFactories = 
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
							perpectiveFactories.put(factoryId, 
								factoryInstance);
						}
						
						final UiHandler[] uiHandlerArray = perspective.getUiHandlerArray();
						for (final UiHandler handler : uiHandlerArray) {
							beanUI.put(handler.getId(), this.loadBeanUI(handler.getUiHandlerClass()));
						}
						
						undeletable.addAll(Arrays.asList(perspective.getUndeletableArray()));
						
						final Map<String, Set<String>> sourceTargetMap = new HashMap<String, Set<String>>();
						for (final Validator validator : perspective.getValidatorArray()) {
							final String source = validator.getSource();
							Set<String> targets = sourceTargetMap.get(source);
							if (targets == null) {
								targets = new HashSet<String>();
								sourceTargetMap.put(source,
									targets);
							}
							targets.add(validator.getTarget());
						}
						perspectiveData = new PerspectiveData(perpectiveFactories, 
							beanUI,
							undeletable,
							new PerspectiveValidator(sourceTargetMap));
						
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
	
	private class PerspectiveValidator implements  com.syrus.AMFICOM.manager.Validator {
		
		private final Map<String, Set<String>>	sourceTargetMap;

		public PerspectiveValidator(final Map<String, Set<String>> sourceTargetMap) {
			this.sourceTargetMap = sourceTargetMap;
		}
		
		public boolean isValid(final String source,
				final String target) {
			final Set<String> targetKeys = this.sourceTargetMap.get(source);
			
			assert Log.debugMessage(source 
				+ " -> "
				+ target 
				+ "(expected:"
				+ targetKeys
				+ ')', 
			Log.DEBUGLEVEL10);
			
			return targetKeys != null ? targetKeys.contains(target) : false;
		}
	}
}

