/*-
* $Id: ManagerGraphModel.java,v 1.1 2005/07/14 10:14:11 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;


/**
 * @version $Revision: 1.1 $, $Date: 2005/07/14 10:14:11 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModel extends DefaultGraphModel {
	
	private final DefaultGraphCell rootItem;
	
	public ManagerGraphModel(final DefaultGraphCell rootItem) {
		this.rootItem = rootItem;
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
//			DefaultPort sourcePort = (DefaultPort) edge2.getSource();
//			DefaultPort targetPort = (DefaultPort) edge2.getTarget();
//			System.out.println(".acceptsSource() | 1' " + port + ", " 
//				+ targetPort);
//			if (port != sourcePort) {
//				result = !this.isLooped(targetPort,
//					(DefaultPort) port,
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
	
	private boolean isLooped(final DefaultPort port,
	                         final DefaultPort basePort, 
	                         final boolean target) {
		boolean result = basePort == port;				
		if (!result) {
			for(Object object : port.getEdges()) {
				Edge edge  = (Edge)object;
				DefaultPort defaultPort = (DefaultPort) (edge.getTarget());
				result = this.isLooped(defaultPort, basePort, target);
				if (result) {
					break;
				}
			}
		}
		return result;
	}
	
	@Override
	public boolean acceptsTarget(	Object edge,
									Object port) {
		// null port doesn't supply
		boolean result = port != null;			
		if (result) {
			DefaultPort port2 = (DefaultPort) port;
			
			DefaultPort rootPort = (DefaultPort) this.rootItem.getChildAt(0);
			
			// check for simplify
			int yetConnected = 0;
			for(Object oEdge : port2.getEdges()) {
				Edge edge3 = (Edge)oEdge;
				DefaultPort sourcePort = (DefaultPort) edge3.getSource();
				DefaultPort targetPort = (DefaultPort) edge3.getTarget();
				if (targetPort == port && sourcePort != rootPort) {
					yetConnected++;
				}
			}

			result = yetConnected == 0;
			
			if (result) {
				// check for looping
				for(Object oEdge : port2.getEdges()) {
					Edge edge3 = (Edge)oEdge;
					DefaultPort sourcePort = (DefaultPort) edge3.getSource();
					DefaultPort targetPort = (DefaultPort) edge3.getTarget();
					if (port != targetPort) {
						result = !this.isLooped((DefaultPort) port, 
							sourcePort,
							true);
					}
				}
			}
		}
		return result;
	}			
}

