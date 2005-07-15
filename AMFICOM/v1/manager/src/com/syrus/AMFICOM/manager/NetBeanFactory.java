/*-
 * $Id: NetBeanFactory.java,v 1.3 2005/07/15 08:26:11 bob Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.manager;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;


/**
 * @version $Revision: 1.3 $, $Date: 2005/07/15 08:26:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager_v1
 */
public class NetBeanFactory extends AbstractBeanFactory {
	
	private static NetBeanFactory instance;
	
	private int count = 0;
	
	private Validator validator;
	
	private NetBeanFactory() {
		super("Entity.Net", 
			"Entity.Net", 
			"com/syrus/AMFICOM/manager/resources/icons/cloud.gif", 
			"com/syrus/AMFICOM/manager/resources/cloud.png");
	}
	
	public static final NetBeanFactory getInstance() {
		if(instance == null) {
			synchronized (NetBeanFactory.class) {
				if(instance == null) {
					instance = new NetBeanFactory();
				}
			}
		}		
		return instance;
	}

	@Override
	public AbstractBean createBean() {
		final String name1 = "Net" + (++this.count);
		
		return new AbstractBean(name1, this.getValidator(), null) {
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public String toString() {
				return this.storableObject;
			}

		};
	}

	private final Validator getValidator() {
		if (this.validator == null) {
			 this.validator = new Validator() {
					
					public boolean isValid(	AbstractBean sourceBean,
											AbstractBean targetBean) {
						
						return false;
					}
				};
		}
		return this.validator;
	}
}
