/*-
 * $Id: ServerBeanFactory.java,v 1.3 2005/07/15 11:59:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;



/**
 * @version $Revision: 1.3 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class ServerBeanFactory extends AbstractBeanFactory {
	
	private static ServerBeanFactory instance;
	
	private ServerBeanFactory() {
		super("Entity.Server", 
			"Entity.Server", 
			"com/syrus/AMFICOM/manager/resources/icons/server.gif", 
			"com/syrus/AMFICOM/manager/resources/server.png");
	}
	
	public static final ServerBeanFactory getInstance() {
		if(instance == null) {
			synchronized (ServerBeanFactory.class) {
				if(instance == null) {
					instance = new ServerBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		return new AbstractBean() {};
	}
	
}
