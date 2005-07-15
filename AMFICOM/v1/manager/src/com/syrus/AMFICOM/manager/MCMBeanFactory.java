/*-
 * $Id: MCMBeanFactory.java,v 1.2 2005/07/15 11:59:00 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;



/**
 * @version $Revision: 1.2 $, $Date: 2005/07/15 11:59:00 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class MCMBeanFactory extends AbstractBeanFactory {
	
	private static MCMBeanFactory instance;
	
	private MCMBeanFactory() {
		super("Entity.MeasurementContolModule", 
			"Entity.MeasurementContolModule.acronym", 
			"com/syrus/AMFICOM/manager/resources/icons/mcm.gif", 
			"com/syrus/AMFICOM/manager/resources/mcm.png");
	}
	
	public static final MCMBeanFactory getInstance() {
		if(instance == null) {
			synchronized (MCMBeanFactory.class) {
				if(instance == null) {
					instance = new MCMBeanFactory();
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
