/*-
* $Id: MessageBeanUI.java,v 1.4 2005/12/07 14:08:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.viewers;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.UIManager;

import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.manager.beans.AbstractBeanFactory;
import com.syrus.AMFICOM.manager.beans.MessageBean;
import com.syrus.AMFICOM.manager.beans.MessageBeanFactory;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;


/**
 * @version $Revision: 1.4 $, $Date: 2005/12/07 14:08:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class MessageBeanUI extends AbstractBeanUI<MessageBean> {

	private Map<Severity, String> prefix;
	
	@SuppressWarnings("unused")
	public MessageBeanUI(final ManagerMainFrame managerMainFrame) {
		super(managerMainFrame, null);
	
		this.prefix = new HashMap<Severity, String>();
		this.prefix.put(Severity.SEVERITY_SOFT, "warning");
		this.prefix.put(Severity.SEVERITY_HARD, "error");
	}
	
	@Override
	public Icon getIcon(AbstractBeanFactory<MessageBean> factory) {
		final MessageBeanFactory beanFactory = (MessageBeanFactory) factory;		
		final Severity severity = beanFactory.getSeverity();
		return UIManager.getIcon("com.syrus.AMFICOM.manager.resources.icons." + this.prefix.get(severity));
	}
	
	@Override
	public Icon getImage(MessageBean bean) {
		return UIManager.getIcon("com.syrus.AMFICOM.manager.resources." + this.prefix.get(bean.getSeverity()));
	}
}

