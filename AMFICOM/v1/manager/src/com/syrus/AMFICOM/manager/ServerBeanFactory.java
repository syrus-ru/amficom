/*-
 * $Id: ServerBeanFactory.java,v 1.2 2005/07/14 13:16:36 bob Exp $
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
		return new AbstractBean(null, null, null) {
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				// TODO Auto-generated method stub
				
			}

		};
	}
	
}
