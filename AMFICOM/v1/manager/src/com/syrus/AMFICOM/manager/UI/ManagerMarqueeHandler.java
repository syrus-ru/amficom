/*-
* $Id: ManagerMarqueeHandler.java,v 1.8 2005/11/11 10:58:02 bob Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.manager.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.PortView;

import com.syrus.AMFICOM.manager.AbstractBean;
import com.syrus.AMFICOM.manager.MPort;
import com.syrus.AMFICOM.manager.Perspective;


/**
 * @version $Revision: 1.8 $, $Date: 2005/11/11 10:58:02 $
 * @author $Author: bob $
 * @author Vladimir Dolzhenko
 * @module manager
 */
public class ManagerMarqueeHandler extends BasicMarqueeHandler {

	// Holds the Start and the Current Point
	protected Point2D start, current;

	// Holds the First and the Current Port
	protected PortView port, firstPort;

	private final ManagerMainFrame	graphText;
	
	
	public ManagerMarqueeHandler(final ManagerMainFrame graphText) {
		this.graphText = graphText;
	}

	// Override to Gain Control (for PopupMenu and ConnectMode)
	@Override
	public boolean isForceMarqueeEvent(MouseEvent e) {
//		System.out.println("MyMarqueeHandler.isForceMarqueeEvent()");
		if (e.isShiftDown())
			return false;
		// If Right Mouse Button we want to Display the PopupMenu
		if (SwingUtilities.isRightMouseButton(e))
			// Return Immediately
			return true;
		// Find and Remember Port
		this.port = getSourcePortAt(e.getPoint());
		// If Port Found and in ConnectMode (=Ports Visible)
		if (this.port != null && this.graphText.graph.isPortsVisible())
			return true;
		// Else Call Superclass
		return super.isForceMarqueeEvent(e);
	}

	// Display PopupMenu or Remember Start Location and First Port
	@SuppressWarnings("unchecked")
	@Override
	public void mousePressed(final MouseEvent e) {
		// If Right Mouse Button
		if (SwingUtilities.isRightMouseButton(e)) {
			// TODO
//			// Find Cell in Model Coordinates
			DefaultGraphCell cell = (DefaultGraphCell) this.graphText.graph.getFirstCellForLocation(e.getX(), e.getY());
			if (cell == null) {
				return;
			}
			if (cell.getAllowsChildren()) {
				MPort port1 = (MPort) cell.getChildAt(0);
				Object userObject = port1.getUserObject();
				if (userObject instanceof AbstractBean) {
					AbstractBean bean = (AbstractBean)userObject;
					final Perspective perspective = this.graphText.getPerspective();
					final String codename = bean.getCodename();
					final AbstractItemPopupMenu popupMenu = perspective.getPopupMenu(codename);					
					if (popupMenu != null) {
						JPopupMenu menu = popupMenu.getPopupMenu(cell, this.graphText);
						menu.show(this.graphText.graph, e.getX(), e.getY());
					} 
				}
			}
		} else if (this.port != null && this.graphText.graph.isPortsVisible()) {
			// Remember Start Location
			this.start = this.graphText.graph.toScreen(this.port.getLocation(null));
			// Remember First Port
			this.firstPort = this.port;
//			System.out.println("MyMarqueeHandler.mousePressed() | " + e.getClickCount());
//			if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
//				System.out.println("MyMarqueeHandler.mousePressed() | try enter");
//			}
		} else {
			// Call Superclass
			super.mousePressed(e);
		}
	}

	// Find Port under Mouse and Repaint Connector
	@Override
	public void mouseDragged(MouseEvent e) {
		// If remembered Start Point is Valid
		if (this.start != null) {
			// Fetch Graphics from Graph
			Graphics g = this.graphText.graph.getGraphics();
			// Reset Remembered Port
			PortView newPort = getTargetPortAt(e.getPoint());
			// Do not flicker (repaint only on real changes)
			if (newPort == null || newPort != this.port) {
				// Xor-Paint the old Connector (Hide old Connector)
				paintConnector(Color.BLACK, this.graphText.graph.getBackground(), g);
				// If Port was found then Point to Port Location
				this.port = newPort;
				if (this.port != null)
					this.current = this.graphText.graph.toScreen(this.port.getLocation(null));
				// Else If no Port was found then Point to Mouse Location
				else
					this.current = this.graphText.graph.snap(e.getPoint());
				// Xor-Paint the new Connector
				paintConnector(this.graphText.graph.getBackground(), Color.BLACK, g);
			}
		}
		// Call Superclass
		super.mouseDragged(e);
	}

	public PortView getSourcePortAt(Point2D point) {
		// Disable jumping
		this.graphText.graph.setJumpToDefaultPort(false);
		PortView result;
		try {
			// Find a Port View in Model Coordinates and Remember
			result = this.graphText.graph.getPortViewAt(point.getX(), point.getY());
		} finally {
			this.graphText.graph.setJumpToDefaultPort(true);
		}
		return result;
	}

	// Find a Cell at point and Return its first Port as a PortView
	protected PortView getTargetPortAt(Point2D point) {
		// Find a Port View in Model Coordinates and Remember
		return this.graphText.graph.getPortViewAt(point.getX(), point.getY());
	}

	// Connect the First Port and the Current Port in the Graph or Repaint
	@Override
	public void mouseReleased(MouseEvent e) {
//		System.out.println("MyMarqueeHandler.mouseReleased()");
		// If Valid Event, Current and First Port
		if (e != null && this.port != null && this.firstPort != null
				&& this.firstPort != this.port) {
			// Then Establish Connection
			// connect((Port) firstPort.getCell(), (Port) port.getCell());
			MPort sourcePort = (MPort) this.firstPort.getCell();
			MPort targetPort = (MPort) this.port.getCell();
			DefaultEdge edge = this.graphText.getGraphRoutines().createEdge(
				(DefaultGraphCell)sourcePort.getParent(), 
				(DefaultGraphCell)targetPort.getParent());
			
			
			
			if (edge != null) {
//				Object userObject = sourcePort.getUserObject();
//				if (userObject instanceof AbstractBean) {
////					System.out.println("MyMarqueeHandler.mouseReleased()");
//					AbstractBean bean = (AbstractBean)userObject;
//					// XXX
////					bean.updateEdgeAttributes(edge, targetPort);
//				}
				GraphLayoutCache graphLayoutCache = this.graphText.graph.getGraphLayoutCache();
				graphLayoutCache.refresh(graphLayoutCache.getMapping(edge, true), true);
			}
			e.consume();
			// Else Repaint the Graph
		} 
//		else
			this.graphText.graph.repaint();
		// Reset Global Vars
		this.firstPort = this.port = null;
		this.start = this.current = null;
		// Call Superclass
		super.mouseReleased(e);
	}

	// Show Special Cursor if Over Port
	@Override
	public void mouseMoved(MouseEvent e) {
		// Check Mode and Find Port
		if (e != null && getSourcePortAt(e.getPoint()) != null
				&& this.graphText.graph.isPortsVisible()) {
			// Set Cusor on Graph (Automatically Reset)
			this.graphText.graph.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			// Consume Event
			// Note: This is to signal the BasicGraphUI's
			// MouseHandle to stop further event processing.
			e.consume();
		} else
			// Call Superclass
			super.mouseMoved(e);
	}

	// Use Xor-Mode on Graphics to Paint Connector
	protected void paintConnector(Color fg, Color bg, Graphics g) {
		// Set Foreground
		g.setColor(fg);
		// Set Xor-Mode Color
		g.setXORMode(bg);
		// Highlight the Current Port
		paintPort(this.graphText.graph.getGraphics());
		// If Valid First Port, Start and Current Point
		if (this.firstPort != null && this.start != null && this.current != null)
			// Then Draw A Line From Start to Current Point
			g.drawLine((int) this.start.getX(), (int) this.start.getY(),
					(int) this.current.getX(), (int) this.current.getY());
	}

	// Use the Preview Flag to Draw a Highlighted Port
	protected void paintPort(Graphics g) {
		// If Current Port is Valid
		if (this.port != null) {
			// If Not Floating Port...
			boolean o = (GraphConstants.getOffset(this.port.getAttributes()) != null);
			// ...Then use Parent's Bounds
			Rectangle2D r = (o) ? this.port.getBounds() : this.port.getParentView()
					.getBounds();
			// Scale from Model to Screen
			r = this.graphText.graph.toScreen((Rectangle2D) r.clone());
			// Add Space For the Highlight Border
			r.setFrame(r.getX() - 3, r.getY() - 3, r.getWidth() + 6, r
					.getHeight() + 6);
			// Paint Port in Preview (=Highlight) Mode
			this.graphText.graph.getUI().paintCell(g, this.port, r, true);
		}
	}

}
