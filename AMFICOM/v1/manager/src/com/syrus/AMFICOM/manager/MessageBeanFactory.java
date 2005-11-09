/*-
 * $Id: MessageBeanFactory.java,v 1.1 2005/11/09 15:08:45 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2005/11/09 15:08:45 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class MessageBeanFactory extends AbstractBeanFactory<NonStorableBean> {
	
	public static final String MESSAGE_CODENAME = "Message";
	
	private Validator validator;
	
	public MessageBeanFactory(final ManagerMainFrame graphText) {
		super("Manager.Entity.Message", 
			"Manager.Entity.Message");
		super.graphText = graphText;
	}
	
	@Override
	public NonStorableBean createBean(Perspective perspective) throws ApplicationException {
		return this.createBean(MESSAGE_CODENAME + this.count);
	}
	
	@Override
	public NonStorableBean createBean(final String codename) 
	throws ApplicationException {
		++super.count;
		final MessageBean bean = new MessageBean();
		bean.setName(this.getName());
		bean.setGraphText(super.graphText);
		bean.setValidator(this.getValidator());
		bean.setId(codename);
		bean.setIdentifier(Identifier.VOID_IDENTIFIER);		
		return bean;
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					assert Log.debugMessage(sourceBean.getName() 
							+ " -> " 
							+ targetBean.getName(), 
						Log.DEBUGLEVEL10);
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getId().startsWith(ObjectEntities.ROLE) &&
						targetBean.getId().startsWith(MESSAGE_CODENAME);
				}
			};
		}
		return this.validator;
	}	
	
	private class MessageBean extends NonStorableBean {
		
		@Override
		public String getCodename() {
			return MESSAGE_CODENAME;
		}
		
	}
	
	@Override
	public String getCodename() {
		return MESSAGE_CODENAME;
	}
}
