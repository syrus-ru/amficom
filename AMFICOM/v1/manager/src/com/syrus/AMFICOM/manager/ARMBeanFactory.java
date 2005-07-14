/*-
 * $Id: ARMBeanFactory.java,v 1.1 2005/07/14 10:14:11 bob Exp $
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
public class ARMBeanFactory extends AbstractBeanFactory {
	
	private static ARMBeanFactory instance;
	
	private ARMBeanFactory() {
		super("AWP", 
			"AWP", 
			"com/syrus/AMFICOM/manager/resources/icons/arm.gif", 
			"com/syrus/AMFICOM/manager/resources/arm.gif");
	}
	
	public static final ARMBeanFactory getInstance() {
		if(instance == null) {
			synchronized (ARMBeanFactory.class) {
				if(instance == null) {
					instance = new ARMBeanFactory();
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
