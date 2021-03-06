/*-
* $Id: ManagerHandler.java,v 1.12 2005/12/12 13:40:56 bob Exp $
*
* Copyright ? 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.extensions.AbstractExtensionHandler;
import com.syrus.AMFICOM.extensions.manager.BeanFactory;
import com.syrus.AMFICOM.extensions.manager.ManagerExtensions;
import com.syrus.AMFICOM.extensions.manager.ManagerResource;
import com.syrus.AMFICOM.extensions.manager.Perspective;
import com.syrus.AMFICOM.extensions.manager.PopupMenu;
import com.syrus.AMFICOM.extensions.manager.UiHandler;
import com.syrus.AMFICOM.extensions.manager.Validator;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.manager.UI.AbstractItemPopupMenu;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.perspective.PerspectiveData;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.12 $, $Date: 2005/12/12 13:40:56 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class ManagerHandler extends AbstractExtensionHandler<ManagerExtensions> {
	
	private ManagerExtensions	managerExtensions;
	private ManagerMainFrame	managerMainFrame;
	private Map<String, com.syrus.AMFICOM.manager.perspective.Perspective> perspectiveMap;
	
	private Map<String, AbstractBeanFactory> factories;
	private Map<String, AbstractItemPopupMenu> popupMenus;
	private Map<String, BeanUI> beanUIs;

	public void addHandlerData(final ManagerExtensions managerExtensions) {
		this.managerExtensions = managerExtensions;
		this.perspectiveMap = new HashMap<String, com.syrus.AMFICOM.manager.perspective.Perspective>();
		this.factories = new HashMap<String, AbstractBeanFactory>();
		this.popupMenus = new HashMap<String, AbstractItemPopupMenu>();
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

	private final AbstractItemPopupMenu loadPopupMenuHandler(final String handlerClass) {
		AbstractItemPopupMenu popupMenu = this.popupMenus.get(handlerClass);
		if (popupMenu == null) {
			popupMenu = (AbstractItemPopupMenu) super.loadHandler(handlerClass, 
			new Class[] {}, 
			new Object[] {});
			
			assert Log.debugMessage("popup menu " 
					+ handlerClass
					+ " loaded.", 
				Log.DEBUGLEVEL10);
			
			this.popupMenus.put(handlerClass, popupMenu);
		}
		return popupMenu;
	}
	
	public final void loadPerspectives() throws ApplicationException {
			for (final ManagerResource managerResource : this.managerExtensions.getManagerResourceArray()) {
				if (managerResource instanceof Perspective) {
					Perspective perspective = (Perspective) managerResource;
					final String id = perspective.getId().intern();
					
					final String handler = perspective.getHandler();
					final PerspectiveData acquirePerspective = this.acquirePerspective(perspective);

					if (handler != null) {
						com.syrus.AMFICOM.manager.perspective.Perspective perspective2 = 
							(com.syrus.AMFICOM.manager.perspective.Perspective) 
								super.loadHandler(handler, 
									new Class[] {}, 
									new Object[] {});
						perspective2.setManagerMainFrame(this.managerMainFrame);
						perspective2.setPerspectiveData(acquirePerspective);
						perspective2.createNecessaryItems();
						this.perspectiveMap.put(id, perspective2);
					}
					
					
					assert Log.debugMessage("id:" + id , Log.DEBUGLEVEL10);
				}
		}
	}
	
	public Collection<com.syrus.AMFICOM.manager.perspective.Perspective> getPerspectives(){
		return this.perspectiveMap.values();
	}
	
	public com.syrus.AMFICOM.manager.perspective.Perspective getPerspective(final String codename){
		
		assert Log.debugMessage(codename, Log.DEBUGLEVEL10);
		
		final com.syrus.AMFICOM.manager.perspective.Perspective perspective = 
			this.perspectiveMap.get(codename);
		if (perspective != null) {
			return perspective;			
		}
		
		for (final com.syrus.AMFICOM.manager.perspective.Perspective perspective3 : 
				this.perspectiveMap.values()) {
			final com.syrus.AMFICOM.manager.perspective.Perspective perspective2 = 
				perspective3.getPerspective(codename);
			if (perspective2 != null) {
				return perspective2;
			}
		}
		return null;
	}
	
	private PerspectiveData acquirePerspective(final Perspective perspective) {
		final Map<String, AbstractBeanFactory> perspectiveFactories = 
			new HashMap<String, AbstractBeanFactory>();
		final Map<String, BeanUI> beanUI = new HashMap<String, BeanUI>();

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
			perspectiveFactories.put(factoryId, 
				factoryInstance);
		}
		
		final UiHandler[] uiHandlerArray = perspective.getUiHandlerArray();
		for (final UiHandler handler : uiHandlerArray) {
			beanUI.put(handler.getId(), this.loadBeanUI(handler.getUiHandlerClass()));
		}
		
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
		
		final Map<String, AbstractItemPopupMenu> popupMenus = 
			new HashMap<String, AbstractItemPopupMenu>();
		
		PopupMenu[] popupMenuArray = perspective.getPopupMenuArray();
		for (final PopupMenu menu : popupMenuArray) {
			popupMenus.put(menu.getId(), this.loadPopupMenuHandler(menu.getPopupMenuHandler()));
		}
		
		final Map<String, PerspectiveData> subperspectiveMap = 
			new HashMap<String, PerspectiveData>();
		
		Perspective[] perspectiveArray = perspective.getPerspectiveArray();
		for (final Perspective perspective2 : perspectiveArray) {
			PerspectiveData data = this.acquirePerspective(perspective2);
			subperspectiveMap.put(perspective2.getId(), data);
		}
		
		final Set<String> undeletable = new HashSet<String>();
		undeletable.addAll(Arrays.asList(perspective.getUndeletableArray()));
		
		
		final Set<String> cutable = new HashSet<String>();
		cutable.addAll(Arrays.asList(perspective.getCutableArray()));
		
		PerspectiveData perspectiveData = 
			new PerspectiveData(perspectiveFactories, 
				beanUI,
				popupMenus,
				subperspectiveMap,
				new PerspectiveValidator(sourceTargetMap),
				undeletable,
				cutable);
		
		final String perspectiveCodename = perspective.getId().intern();
		
		assert Log.debugMessage("perspective '" 
				+ perspectiveCodename
				+ "' registered successfully.",
			Log.DEBUGLEVEL10);
		return perspectiveData;	
	}
	
	private class PerspectiveValidator implements  com.syrus.AMFICOM.manager.perspective.Validator {
		
		private final Map<String, Set<String>>	sourceTargetMap;

		public PerspectiveValidator(final Map<String, Set<String>> sourceTargetMap) {
			this.sourceTargetMap = sourceTargetMap;
		}

		private String getCodename(final String id) {
			final int index = id.indexOf(Identifier.SEPARATOR);
			return index >= 0 ? id.substring(0, index) : id;
		}
		
		public boolean isValid(final String source,
				final String target) {
			final String sourceCodename = this.getCodename(source);
			final String targetCodename = this.getCodename(target);
			final Set<String> targetKeys = this.sourceTargetMap.get(sourceCodename);
			
			assert Log.debugMessage(sourceCodename 
				+ " -> "
				+ targetCodename 
				+ "(expected:"
				+ targetKeys
				+ ')', 
			Log.DEBUGLEVEL10);			
			
			return targetKeys != null ? targetKeys.contains(targetCodename) : false;
		}
	}
}

