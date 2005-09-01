/*-
* $Id: ManagerGraphModel.java,v 1.9 2005/09/01 14:33:48 bob Exp $
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
import com.syrus.util.Log;


/**
 * @version $Revision: 1.9 $, $Date: 2005/09/01 14:33:48 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModel extends DefaultGraphModel {
	
	@Override
	public boolean acceptsSource(	final Object edge,
	                             	final Object port) {
		// null port doesn't supply
		boolean result = port != null;
		if (result) {
			Edge edge2 = (Edge) edge;
			final MPort source = (MPort)edge2.getSource();
			final MPort target = (MPort) edge2.getTarget();
			result = this.isTargetValid(source, target);
//			Log.debugMessage("ManagerGraphModel.acceptsSource | port " + port 
//				+ "\n\t" + source + " -> " + target, Level.FINEST);
			if (result) {
				if (port == source) {			
					// Changing source 
				} else {
					//	Changing target
					List<Port> targets = ((MPort)port).getTargets();
					result = targets.size() == 0;
				}
				
				// XXX check for looping
			}
		}
		
		Log.debugMessage("ManagerGraphModel.acceptsSource() | > " + result, Log.DEBUGLEVEL10);
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
	
	private boolean isTargetValid(final MPort source,
	                              final MPort target) {
		Object sourceObject = source.getUserObject();
		Object targetObject = target.getUserObject();
		if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
			AbstractBean sourceBean = (AbstractBean) sourceObject;
			AbstractBean targetBean = (AbstractBean) targetObject;
			boolean result = sourceBean.isTargetValid(targetBean);
			System.out.println("ManagerGraphModel.isTargetValid() | " + result);
			return result;
		}
		return false;
	}
	
	@Override
	public boolean acceptsTarget(	final Object edge,
	                             	final Object port) {
		// null port doesn't supply
		boolean result = port != null;			
		if (result) {
			Edge edge2 = (Edge) edge;
			result = this.isTargetValid((MPort)edge2.getSource(), 
				(MPort) edge2.getTarget());
			
			// XXX is need check for link count ?
			// XXX check for looping
		}
		return result;
	}			
}

