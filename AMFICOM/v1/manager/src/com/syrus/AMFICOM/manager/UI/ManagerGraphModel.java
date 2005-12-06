/*-
* $Id: ManagerGraphModel.java,v 1.17 2005/12/06 15:14:39 bob Exp $
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

import com.syrus.AMFICOM.manager.beans.AbstractBean;
import com.syrus.AMFICOM.manager.graph.MPort;
import com.syrus.AMFICOM.manager.graph.ManagerGraphCell;
import com.syrus.AMFICOM.manager.perspective.Perspective;
import com.syrus.AMFICOM.manager.perspective.Validator;
import com.syrus.util.Log;


/**
 * @version $Revision: 1.17 $, $Date: 2005/12/06 15:14:39 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerGraphModel extends DefaultGraphModel {
	
	private final ManagerMainFrame managerMainFrame;
	
	ManagerGraphModel(final ManagerMainFrame managerMainFrame){
		this.managerMainFrame = managerMainFrame;
	}
	
	@Override
	public boolean acceptsSource(	final Object edge,
	                             	final Object port) {
		// null port doesn't supply
		boolean result = port != null;
		if (result) {
			final Edge edge2 = (Edge) edge;
			final MPort source = (MPort)edge2.getSource();
			final MPort target = (MPort) edge2.getTarget();
//			assert Log.debugMessage("port " + port 
//				+ "\n\t" + source + " -> " + target, Log.DEBUGLEVEL03);
			if (port == source) {
//				assert Log.debugMessage("port == source", Log.DEBUGLEVEL03);
//				assert Log.debugMessage(source + ", " + target, Log.DEBUGLEVEL03);
//				result = false;
				// Changing source 
			} else {
				//	Changing target
				result = this.isTargetValid(source, target);					
				if (result) {
					List<Port> targets = ((MPort)port).getTargets();
					result = targets.size() == 0;
				}
			}
		}
		
		assert Log.debugMessage("> " 
			+ result, Log.DEBUGLEVEL10);
		return result;
	}	
	
	public boolean isConnectionPermitted(final MPort source,
	                                     final MPort target) {
		boolean result = source != null && target != null;
		if (result) {
			if (source.getTargets().size() > 0) {
				Log.debugMessage("source already refer to target", Log.DEBUGLEVEL03);
				result = false;
			}
			if (result) {
				result = this.isTargetValid(source, target);
			}
		}
		return result;
	}
	
	private boolean isLooped(final MPort startPort,
	                         final MPort destPort) {
		Log.debugMessage("startPort:" 
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
		final AbstractBean sourceBean = source.getBean();
		final AbstractBean targetBean = target.getBean();
		if (sourceBean != null && targetBean != null) {
			final ManagerGraphCell sourceCell = (ManagerGraphCell) source.getParent();
			final ManagerGraphCell targetCell = (ManagerGraphCell) target.getParent();
			final Perspective perspective = sourceCell.getPerspective();
			
			assert perspective == targetCell.getPerspective();
			
			final Validator validator = perspective.getValidator();
			final boolean valid = validator.isValid(sourceBean.getId(), targetBean.getId());
			final boolean looped = this.isLooped(source, target);
			final boolean result = valid && !looped;
			assert Log.debugMessage(result 
					+", valid:" + valid + ", looped:" + looped, 
				Log.DEBUGLEVEL10);
			if (!valid) {
				assert Log.debugMessage(source 
					+ " > " 
					+ target, 
				Log.DEBUGLEVEL10);
				assert Log.debugMessage(result 
					+", valid:" + valid + ", looped:" + looped, 
				Log.DEBUGLEVEL10);				
			}
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
			final Edge edge2 = (Edge) edge;
			final MPort source = (MPort)edge2.getSource();
			final MPort target = (MPort) edge2.getTarget();

			assert Log.debugMessage("source:" + source + ", target:" + target + ", port: " + port, Log.DEBUGLEVEL10);
			
			result = this.isTargetValid((MPort)edge2.getSource(), 
				(MPort) port);
			
			// XXX is need check for link count ?
			// XXX check for looping
		}
		assert Log.debugMessage(port + "> " + result, Log.DEBUGLEVEL10);
		return result;
	}			
}

