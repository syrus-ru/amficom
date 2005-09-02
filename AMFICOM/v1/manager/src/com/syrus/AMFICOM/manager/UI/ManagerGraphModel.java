/*-
* $Id: ManagerGraphModel.java,v 1.10 2005/09/02 13:27:13 bob Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/09/02 13:27:13 $
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
			Log.debugMessage("ManagerGraphModel.acceptsSource | port " + port 
				+ "\n\t" + source + " -> " + target, Log.DEBUGLEVEL09);
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
		
		Log.debugMessage("ManagerGraphModel.acceptsSource() | > " 
			+ result, Log.DEBUGLEVEL09);
		return result;
	}	
	
	public boolean isConnectionPermitted(final MPort source,
	                                     final MPort target) {
		boolean result = source != null && target != null;
		if (result) {
			result = this.isTargetValid(source, target);
		}
		return result;
	}
	
	private boolean isLooped(final MPort startPort,
	                         final MPort destPort) {
		Log.debugMessage("ManagerGraphModel.isLooped | startPort:" 
				+ startPort 
				+ ", destPort:" 
				+ destPort, 
			Log.DEBUGLEVEL10);
		boolean result = destPort == startPort;				
		if (!result) {
			for(Object source : startPort.getSources()) {
				MPort source2 = (MPort) source;
				result = this.isLooped(source2, destPort);
				if (result) {
					break;
				}
			}
		}
		return result;
	}
	
	private boolean isTargetValid(final MPort source,
	                              final MPort target) {		
		if (source.getTargets().size() > 0) {
			Log.debugMessage("ManagerGraphModel.isTargetValid | source already refer to target", Log.DEBUGLEVEL09);
			return false;
		}		
		Object sourceObject = source.getUserObject();
		Object targetObject = target.getUserObject();
		Log.debugMessage("ManagerGraphModel.isTargetValid | " 
				+ source 
				+ " > " 
				+ target, 
			Log.DEBUGLEVEL09);
		if (sourceObject instanceof AbstractBean && targetObject instanceof AbstractBean) {
			AbstractBean sourceBean = (AbstractBean) sourceObject;
			AbstractBean targetBean = (AbstractBean) targetObject;
			boolean result = sourceBean.isTargetValid(targetBean) && !this.isLooped(source, target);
			Log.debugMessage("ManagerGraphModel.isTargetValid | " + result, 
				Log.DEBUGLEVEL10);
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

