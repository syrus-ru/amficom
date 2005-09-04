/*-
 * $Id: DomainBeanFactory.java,v 1.13 2005/09/04 09:27:50 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import com.syrus.AMFICOM.administration.Domain;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.manager.UI.JGraphText;



/**
 * @version $Revision: 1.13 $, $Date: 2005/09/04 09:27:50 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class DomainBeanFactory extends TabledBeanFactory {
	
	private static DomainBeanFactory instance;
	
	private DomainBeanFactory(final JGraphText graphText) {
		super("Entity.Domain", 
			"Entity.Domain", 
			"com/syrus/AMFICOM/manager/resources/icons/domain.gif", 
			"com/syrus/AMFICOM/manager/resources/domain.png");
		super.graphText = graphText;
	}
	
	public static final DomainBeanFactory getInstance(final JGraphText graphText) {
		if(instance == null) {
			synchronized (DomainBeanFactory.class) {
				if(instance == null) {
					instance = new DomainBeanFactory(graphText);
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean(Perspective perspective) throws IllegalObjectEntityException, 
	CreateObjectException {		
		Domain domain = Domain.createInstance(LoginManager.getUserId(), 
				Identifier.VOID_IDENTIFIER, "", "");
		return this.createBean(domain.getId());
	}
	
	@Override
	protected AbstractBean createBean(final Identifier id) {
		DomainBean bean = new DomainBean();
		bean.setGraphText(super.graphText);
		bean.setCodeName(id.getIdentifierString());
		bean.setValidator(this.getValidator());
		bean.setId(id);		
		bean.table = super.getTable(bean, 
			DomainBeanWrapper.getInstance(),
			new String[] {
				DomainBeanWrapper.KEY_NAME,
				DomainBeanWrapper.KEY_DESCRIPTION}
			);
		bean.addPropertyChangeListener(this.listener);
		
		bean.setPropertyPanel(this.panel);
		
		
		return bean;
	}

	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					return sourceBean != null && 
						targetBean != null && 
						(sourceBean.getCodeName().startsWith(ObjectEntities.DOMAIN) ||
						 sourceBean.getCodeName().startsWith(NetBeanFactory.NET_CODENAME) &&
						targetBean.getCodeName().startsWith(ObjectEntities.DOMAIN));
				}
			};
		}
		return this.validator;
	}
	
	@Override
	public String getCodename() {
		return ObjectEntities.DOMAIN;
	}
}
