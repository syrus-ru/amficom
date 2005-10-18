/*-
* $Id: BeanUI.java,v 1.1 2005/10/18 15:10:39 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.UIManager;

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.1 $, $Date: 2005/10/18 15:10:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public interface BeanUI<T extends AbstractBean> {

	JPanel getPropertyPanel(final T bean);
	
	JPopupMenu getPopupMenu(final T bean, 
	                        final Object cell);
	
	Icon getImage(final T bean);
	
	Icon  getIcon(final AbstractBeanFactory<T> factory);
	
	void disposePropertyPanel();
	
	static class BeanUIFactory {
		public static final BeanUI getBeanUI(final String beanUIClass,
		                                     final ManagerMainFrame managerMainFrame) {
			BeanUI beanUI = (BeanUI) UIManager.get(beanUIClass);
			
			if (beanUI == null) { 
				final String className = "com.syrus.AMFICOM.manager.viewers." + beanUIClass;
				try {
					final Constructor ctor = Class.forName(className).getDeclaredConstructor(new Class[] {ManagerMainFrame.class});
					beanUI = (BeanUI) ctor.newInstance(new Object[] {managerMainFrame});
					assert Log.debugMessage("BeanUIFactory.getBeanUI | ui class " 
						+ beanUIClass 
						+ " has bean registered.",
						Log.DEBUGLEVEL09);
					UIManager.put(beanUIClass, beanUI);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					assert Log.debugMessage("BeanUIFactory.getBeanUI | ctor(ManagerMainFrame) undefined for " 
						+ beanUIClass,
						Log.DEBUGLEVEL09);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					final Throwable cause = e.getCause();
					if (cause != null) {
						cause.printStackTrace();
					} else {
						e.printStackTrace();
					}
				}
			}
			
			return beanUI;
		}
	}
}

