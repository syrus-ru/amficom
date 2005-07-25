/*-
* $Id: ManagerGraphModel.java,v 1.6 2005/07/25 05:58:53 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;

import com.syrus.AMFICOM.manager.MPort;


/**
 * @version $Revision: 1.6 $, $Date: 2005/07/25 05:58:53 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModel extends DefaultGraphModel {
	
//	private final DefaultGraphCell rootItem;
	
	private boolean direct;
	
	public ManagerGraphModel(boolean direct) {
//		this.rootItem = rootItem;
		this.direct = direct;
	}
	
	@Override
	public boolean acceptsSource(	Object edge,
									Object port) {
		// null port doesn't supply
		boolean result = port != null;
		Edge edge2 = (Edge)edge;
//		if (result) {
//			System.out.println(".acceptsSource() | port:" + port +"\n\t\t"
//				+ edge2.getSource() + " -> " + edge2.getTarget() );
//		}
//		if (result) {
//			System.out.println(".acceptsSource() | 1");
//			MPort sourcePort = (MPort) edge2.getSource();
//			MPort targetPort = (MPort) edge2.getTarget();
//			System.out.println(".acceptsSource() | 1' " + port + ", " 
//				+ targetPort);
//			if (port != sourcePort) {
//				result = !this.isLooped(targetPort,
//					(MPort) port,
//					false);
//				System.out.println(".acceptsSource() | 1'' " + result);
////					result = true;
//			}
//		}
//		if (result) {
//			System.out.println(".acceptsSource() | 2 " + result);
//		} else {
//			System.err.println(".acceptsSource() | 2 " + result);
//		}
		return result;
	}			
	
	private boolean isLooped(final MPort port,
	                         final MPort basePort, 
	                         final boolean target) {
		boolean result = basePort == port;				
		if (!result) {
			for(Object object : port.getEdges()) {
				Edge edge  = (Edge)object;
				MPort defaultPort = (MPort) (this.direct ? edge.getTarget() : edge.getSource());
				result = this.isLooped(defaultPort, basePort, target);
				if (result) {
					break;
				}
			}
		}
		return result;
	}
	
//	@Override
//	public boolean acceptsTarget(	Object edge,
//									Object port) {
//		// null port doesn't supply
//		boolean result = port != null;			
//		if (result) {
//			MPort port2 = (MPort) port;
//			
//			MPort rootPort = (MPort) this.rootItem.getChildAt(0);
//			
//			// check for simplify
//			int yetConnected = 0;
//			for(Object oEdge : port2.getEdges()) {
//				Edge edge3 = (Edge)oEdge;
//				MPort sourcePort = (MPort) (this.direct ? edge3.getSource() : edge3.getTarget());
//				MPort targetPort = (MPort) (this.direct ? edge3.getTarget() : edge3.getSource());
//				if (targetPort == port && sourcePort != rootPort) {
//					yetConnected++;
//				}
//			}
//
//			result = yetConnected == 0;
//			
//			if (result) {
//				// check for looping
//				for(Object oEdge : port2.getEdges()) {
//					Edge edge3 = (Edge)oEdge;
//					MPort sourcePort = (MPort) (this.direct ? edge3.getSource() : edge3.getTarget());
//					MPort targetPort = (MPort) (this.direct ? edge3.getTarget() : edge3.getSource());
//					if (port != targetPort) {
//						result = !this.isLooped((MPort) port, 
//							sourcePort,
//							true);
//					}
//				}
//			}
//			
//			if (result) {
//				// check for looping
//				for(Object oEdge : port2.getEdges()) {
//					Edge edge3 = (Edge)oEdge;
//					MPort sourcePort = (MPort) (this.direct ? edge3.getSource() : edge3.getTarget());
//					MPort targetPort = (MPort) (this.direct ? edge3.getTarget() : edge3.getSource());
//					if (port != targetPort) {
//						Object sourceObject = sourcePort.getUserObject();
//						Object targetObject = targetPort.getUserObject();
//						if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
//							AbstractBean sourceBean = (AbstractBean) sourceObject;
//							AbstractBean targetBean = (AbstractBean) targetObject;
//							result = sourceBean.isTargetValid(targetBean);
//						}
//					}
//				}
//			}
//		}
//		System.out.println("ManagerGraphModel.acceptsTarget() | result is " + result);
//		return result;
//	}			
}

