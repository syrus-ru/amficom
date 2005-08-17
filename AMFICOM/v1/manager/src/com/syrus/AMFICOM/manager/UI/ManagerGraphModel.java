/*-
* $Id: ManagerGraphModel.java,v 1.8 2005/08/17 15:59:40 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.util.List;

import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.Edge;
import org.jgraph.graph.Port;

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.MPort;


/**
 * @version $Revision: 1.8 $, $Date: 2005/08/17 15:59:40 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModel extends DefaultGraphModel {
	
	private boolean direct;
	
	public ManagerGraphModel(boolean direct) {
		this.direct = direct;
	}
	
	@Override
	public boolean acceptsSource(	Object edge,
									Object port) {
		
		
		// null port doesn't supply
		boolean result = port != null;
		if (result) {
			Edge edge2 = (Edge) edge;
			result = this.isTargetValid((MPort)edge2.getSource(), 
				(MPort) edge2.getTarget());
			if (result) {
				List<Port> targets = ((MPort)port).getTargets();
				result = targets.size() == 0;
			}
		}
		return result;
	}			
	
//	private boolean isLooped(final MPort port,
//	                         final MPort basePort, 
//	                         final boolean target) {
//		boolean result = basePort == port;				
//		if (!result) {
//			for(Object object : port.getEdges()) {
//				Edge edge  = (Edge)object;
//				MPort defaultPort = (MPort) (this.direct ? edge.getTarget() : edge.getSource());
//				result = this.isLooped(defaultPort, basePort, target);
//				if (result) {
//					break;
//				}
//			}
//		}
//		return result;
//	}
	
	private boolean isTargetValid(MPort source,
	                              MPort target) {
		Object sourceObject = source.getUserObject();
		Object targetObject = target.getUserObject();
		if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
			AbstractBean sourceBean = (AbstractBean) sourceObject;
			AbstractBean targetBean = (AbstractBean) targetObject;
			return sourceBean.isTargetValid(targetBean);
		}
		return false;
	}
	
	@Override
	public boolean acceptsTarget(	Object edge,
									Object port) {
		// null port doesn't supply
		boolean result = port != null;			
		if (result) {
			Edge edge2 = (Edge) edge;
			result = this.isTargetValid((MPort)edge2.getSource(), 
				(MPort) edge2.getTarget());
		}
		return result;
	}			
}

