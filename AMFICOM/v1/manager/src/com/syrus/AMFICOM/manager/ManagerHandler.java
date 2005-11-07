/*-
* $Id: ManagerHandler.java,v 1.1 2005/11/07 15:21:45 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.UIManager;

import com.syrus.AMFICOM.extensions.AbstractExtensionHandler;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.viewers.BeanUI;
import com.syrus.amficom.extensions.ExtensionPoint;
import com.syrus.amficom.extensions.manager.BeanFactory;
import com.syrus.amficom.extensions.manager.ManagerExtensions;
import com.syrus.amficom.extensions.manager.ManagerResource;
import com.syrus.amficom.extensions.manager.UiHandler;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/11/07 15:21:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerHandler extends AbstractExtensionHandler {
	
	private final ManagerExtensions	managerExtensions;
	private ManagerMainFrame	managerMainFrame;
	private Map<String, AbstractBeanFactory> beanFactories;
	private Pattern	pattern;

	public ManagerHandler(final ExtensionPoint extensionPoint) {
		this((ManagerExtensions)extensionPoint);
	}
	
	public ManagerHandler(final ManagerExtensions managerExtensions) {
		this.managerExtensions = managerExtensions;
		this.beanFactories = new HashMap<String, AbstractBeanFactory>();
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
	
	public final AbstractBeanFactory getBeanFactory(final String name) {
		assert Log.debugMessage("name:" + name, Log.DEBUGLEVEL10);
		if (this.pattern == null) {
			this.pattern = Pattern.compile("^([a-zA-Z]+)");
		}
		final Matcher matcher = this.pattern.matcher(name);
		
		final String codename;
		if (matcher.find()) {
			// extract codename prefix from name 
			codename = name.substring(matcher.start(1), matcher.end(1)).intern();
		} else {
			codename = name.intern();
		}
		
		AbstractBeanFactory beanFactory = this.beanFactories.get(codename);
		
		if (beanFactory == null) { 
			for (final ManagerResource managerResource : this.managerExtensions.getManagerResourceArray()) {
				if (managerResource instanceof BeanFactory) {
					final BeanFactory handler = (BeanFactory) managerResource;
					final String id = handler.getId().intern();
					if (id == codename) {
						final String handlerClass = handler.getBeanFactoryClass();
						beanFactory = this.loadAbstractBeanFactory(handlerClass);
						assert Log.debugMessage("factory class " 
								+ handlerClass
								+ " for " 
								+ id 
								+ (beanFactory != null ? " registered successfully." : 
									" register failed."),
							Log.DEBUGLEVEL10);
						this.beanFactories.put(id, beanFactory);
						return beanFactory;
					}
				}
			}
			final String msg = "factory for " 
				+ codename 
				+ " not found.";
			Log.errorMessage(msg);
			
			throw new IllegalStateException(msg);
		}
		
		return beanFactory;
	}
	
	public final BeanUI getBeanUI(String beanUICodename) {
		beanUICodename = beanUICodename.intern();
		BeanUI beanUI = (BeanUI) UIManager.get(beanUICodename);
		
		if (beanUI == null) { 
			for (final ManagerResource managerResource : this.managerExtensions.getManagerResourceArray()) {
				if (managerResource instanceof UiHandler) {
					final UiHandler handler = (UiHandler) managerResource;
					final String id = handler.getId().intern();
					if (id == beanUICodename) {
						final String handlerClass = handler.getUiHandlerClass();
						beanUI = loadBeanUI(handlerClass);
						assert Log.debugMessage("handler class " 
								+ handlerClass
								+ " for " 
								+ id 
								+ (beanUI != null ? " registered successfully." : 
									" register failed."),
							Log.DEBUGLEVEL10);
						UIManager.put(id, beanUI);
						return beanUI;
					}
				}
			}
			final String msg = "handler for " 
				+ beanUICodename 
				+ " not found.";
			Log.errorMessage(msg);
			
			throw new IllegalStateException(msg);			
		}
		
		return beanUI;
	}
}

