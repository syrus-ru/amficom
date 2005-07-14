/*-
 * $Id: MCMBeanFactory.java,v 1.1 2005/07/14 14:03:29 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 14:03:29 $
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
		return new AbstractBean(null, null, null) {
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				// TODO Auto-generated method stub
				
			}

		};
	}
	
}
