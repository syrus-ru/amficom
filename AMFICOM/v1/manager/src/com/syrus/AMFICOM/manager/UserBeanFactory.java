/*-
* $Id: UserBeanFactory.java,v 1.3 2005/07/15 08:26:11 bob Exp $
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
 * @version $Revision: 1.3 $, $Date: 2005/07/15 08:26:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class UserBeanFactory extends AbstractBeanFactory {

	private static UserBeanFactory instance;
	
	private int count = 0;
	
	private Validator validator;
	
	private UserBeanFactory() {
		super("Entity.User", 
			"Entity.User", 
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
		final String name1 = "User" + (++this.count);

		
		
		return new AbstractBean(name1, this.getValidator(), null) {

			@Override
			public void updateEdgeAttributes(	DefaultEdge edge,
												DefaultPort port) {
				AttributeMap attributes = edge.getAttributes();
				GraphConstants.setLineWidth(attributes, 10.0f);
				GraphConstants.setLineEnd(attributes, GraphConstants.ARROW_TECHNICAL);
				GraphConstants.setLineColor(attributes, Color.LIGHT_GRAY);
				GraphConstants.setForeground(attributes, Color.BLACK);
			}
		};		
	}
	
	private Validator getValidator() {
		if (this.validator == null) {
			this.validator = new Validator() {
				
				public boolean isValid(	AbstractBean sourceBean,
										AbstractBean targetBean) {
					System.out.println("UserBeanFactory.Validator$1.isValid() | " 
						+ sourceBean.getStorableObject() 
						+ " -> " 
						+ targetBean.getStorableObject());
					return sourceBean != null && 
						targetBean != null && 
						sourceBean.getStorableObject().startsWith("User") &&
						targetBean.getStorableObject().startsWith("ARM");
				}
			};
		}
		return this.validator;
	}
	
}

