/*-
 * $Id: RTUBeanFactory.java,v 1.1 2005/07/14 10:14:11 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class RTUBeanFactory extends AbstractBeanFactory {
	
	private static RTUBeanFactory instance;
	
	private RTUBeanFactory() {
		super("RTU", 
			"RTU", 
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
		return new AbstractBean(null, null, null) {
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				// TODO Auto-generated method stub
				
			}

		};
	}
	
}
