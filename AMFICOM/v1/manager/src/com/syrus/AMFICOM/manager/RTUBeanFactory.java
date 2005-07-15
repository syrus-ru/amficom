/*-
 * $Id: RTUBeanFactory.java,v 1.3 2005/07/15 11:59:00 bob Exp $
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
public class RTUBeanFactory extends AbstractBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private RTUBeanFactory() {
		super("Entity.RemoteTestUnit", 
			"Entity.RemoteTestUnit.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/rtu.gif", 
			"com/syrus/AMFICOM/manager/resources/rtu.png");
	}
	
	public static final RTUBeanFactory getInstance() {
		if(instance == null) {
			synchronized (RTUBeanFactory.class) {
				if(instance == null) {
					instance = new RTUBeanFactory();
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
