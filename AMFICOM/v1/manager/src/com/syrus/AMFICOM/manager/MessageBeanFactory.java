/*-
 * $Id: MessageBeanFactory.java,v 1.3 2005/11/11 13:46:25 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import java.util.HashMap;
import java.util.Map;

import com.syrus.AMFICOM.event.DeliveryAttributes;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.ManagerMainFrame;
import com.syrus.AMFICOM.reflectometry.ReflectogramMismatch.Severity;

/**
 * @version $Revision: 1.3 $, $Date: 2005/11/11 13:46:25 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public final class MessageBeanFactory extends AbstractBeanFactory<MessageBean> {
	
	private final Severity	severity;

	private final static Map<Severity, MessageBeanFactory> INSTANCES = 
		new HashMap<Severity, MessageBeanFactory>();
	
	public MessageBeanFactory(final ManagerMainFrame graphText) {
		this(graphText, null);
	}
	
	private MessageBeanFactory(final ManagerMainFrame graphText,
			final Severity severity) {
		super(severity != null ? severity.getLocalizedName() : null, 
			severity != null ? severity.getLocalizedName() : null);
		super.graphText = graphText;
		this.severity = severity;
	}
	
	public static synchronized MessageBeanFactory getInstance(final ManagerMainFrame graphText, 
			final Severity severity) {
		MessageBeanFactory factory = INSTANCES.get(severity);
		if (factory == null) {
			factory = new MessageBeanFactory(graphText, severity);
			INSTANCES.put(severity, factory);
		}
		return factory;
	}
	
	@Override
	public String getName() {
		return this.nameKey;
	}
	
	@Override
	public String getShortName() {
		return this.shortNameKey;
	}	
	
	@Override
	public MessageBean createBean(final Perspective perspective) 
	throws ApplicationException {
		final DeliveryAttributes deliveryAttributes = 
			DeliveryAttributes.createInstance(LoginManager.getUserId(), this.severity);
		return this.createBean(deliveryAttributes.getId());
	}
	
	@Override
	public MessageBean createBean(final String codename) 
	throws ApplicationException {
		return this.createBean(new Identifier(codename));
	}
	
	private MessageBean createBean(final Identifier identifier) 
	throws ApplicationException {
		++super.count;
		final MessageBean bean = new MessageBean();
		bean.setGraphText(super.graphText);
		bean.setIdentifier(identifier);		
		bean.setId(identifier.getIdentifierString());
		return bean;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.DELIVERYATTRIBUTES;
	}
	
	public final Severity getSeverity() {
		return this.severity;
	}
}
