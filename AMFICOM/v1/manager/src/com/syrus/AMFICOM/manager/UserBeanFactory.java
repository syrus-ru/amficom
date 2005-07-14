/*-
* $Id: UserBeanFactory.java,v 1.1 2005/07/14 10:14:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager;

import java.awt.Color;

import org.jgraph.graph.AttributeMap;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;



/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends AbstractBeanFactory {

	private static UserBeanFactory instance;
	
	static int count = 0;
	
	private UserBeanFactory() {
		super("User", 
			"User", 
			"com/syrus/AMFICOM/manager/resources/icons/user.gif", 
			"com/syrus/AMFICOM/manager/resources/user.gif");
	}
	
	public static final UserBeanFactory getInstance() {
		if (instance == null) {
			synchronized (UserBeanFactory.class) {
				if (instance == null) {
					instance = new UserBeanFactory();
				}				
			}
		}
		return instance;
	}

	
	@Override
	public AbstractBean createBean() {
		final String name1 = "User" + (++count);
		// TODO Auto-generated method stub
		return new AbstractBean(null, null, null) {
			@Override
			public String toString() {
				return name1;
			}
			
			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
//				// TODO Auto-generated method stub
				System.out.println(".updateEdgeAttributes()");
				AttributeMap attributes = edge.getAttributes();
				GraphConstants.setLineWidth(attributes, 10.0f);
				GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
				GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
				GraphConstants.setForeground(attributes, Color.BLACK);
			}
		};		
	}
	
}

