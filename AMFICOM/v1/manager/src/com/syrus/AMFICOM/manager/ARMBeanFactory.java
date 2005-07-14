/*-
 * $Id: ARMBeanFactory.java,v 1.2 2005/07/14 13:16:36 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.2 $, $Date: 2005/07/14 13:16:36 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class ARMBeanFactory extends AbstractBeanFactory {
	
	private static ARMBeanFactory instance;
	
	private static int count = 0;
	
	private ARMBeanFactory() {
		super("Entity.AutomatedWorkplace", 
			"Entity.AutomatedWorkplace.acronym", 
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
		final String name1 = "ARM" + (++count);
		// TODO Auto-generated method stub
		
		Validator validator = new Validator() {
			
			public boolean isValid(	AbstractBean sourceBean,
									AbstractBean targetBean) {
				System.out.println("ARMBeanFactory.Validator$1.isValid() | " 
					+ sourceBean.getStorableObject() 
					+ " -> " 
					+ targetBean.getStorableObject());
				return sourceBean != null && 
					targetBean != null && 
					sourceBean.getStorableObject().startsWith("ARM") &&
					targetBean.getStorableObject().startsWith("Net");
			}
		};
		return new AbstractBean(name1, validator, null) {

			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				// TODO Auto-generated method stub
				
			}
		};
	}	
}
