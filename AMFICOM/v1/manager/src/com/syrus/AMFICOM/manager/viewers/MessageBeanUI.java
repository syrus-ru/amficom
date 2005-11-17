/*-
* $Id: MessageBeanUI.java,v 1.3 2005/11/17 09:00:35 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.beans.MessageBean;
import com.syrus.AMFICOM.manager.beans.MessageBeanFactory;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/11/17 09:00:35 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessageBeanUI extends AbstractBeanUI<MessageBean> {

	private Map<Severity, Icon> icons;
	private Map<Severity, Icon> images;
	@SuppressWarnings("unused")
	public MessageBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, 
			"com/syrus/AMFICOM/manager/resources/icons/envelope.gif", 
			"com/syrus/AMFICOM/manager/resources/envelope.gif");
	
		this.icons = new HashMap<Severity, Icon>();
		{
			URL resource = AbstractBeanFactory.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/icons/enwarning.gif");
			if (resource != null) {
				this.icons.put(Severity.SEVERITY_SOFT, new ImageIcon(resource));
			} else {
				assert Log.debugMessage("com/syrus/AMFICOM/manager/resources/icons/enwarning.gif not found ",
					Log.DEBUGLEVEL03);
			}
			
		}
		
		{
			URL resource = AbstractBeanFactory.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/icons/enerror.gif");
			if (resource != null) {
				this.icons.put(Severity.SEVERITY_HARD, new ImageIcon(resource));
			} else {
				assert Log.debugMessage("com/syrus/AMFICOM/manager/resources/icons/enerror.gif not found ",
					Log.DEBUGLEVEL03);
			}
			
			
		}
		
		this.images = new HashMap<Severity, Icon>();
		{
			URL resource = AbstractBeanFactory.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/enwarning.gif");
			if (resource != null) {
				this.images.put(Severity.SEVERITY_SOFT, new ImageIcon(resource));
			} else {
				assert Log.debugMessage("com/syrus/AMFICOM/manager/resources/icons/enwarning.gif not found ",
					Log.DEBUGLEVEL03);
			}
			
		}
		
		{
			URL resource = AbstractBeanFactory.class.getClassLoader().getResource("com/syrus/AMFICOM/manager/resources/enerror.gif");
			if (resource != null) {
				this.images.put(Severity.SEVERITY_HARD, new ImageIcon(resource));
			} else {
				assert Log.debugMessage("com/syrus/AMFICOM/manager/resources/enerror.gif not found ",
					Log.DEBUGLEVEL03);
			}
			
			
		}
		
	}
	
	@Override
	public Icon getIcon(AbstractBeanFactory<MessageBean> factory) {
		final MessageBeanFactory beanFactory = (MessageBeanFactory) factory;		
		final Severity severity = beanFactory.getSeverity();
		return this.icons.get(severity);
	}
	
	@Override
	public Icon getImage(MessageBean bean) {
		return this.images.get(bean.getSeverity());
	}
}

