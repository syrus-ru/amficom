package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.MUFF;
import static com.syrus.AMFICOM.configuration.EquipmentTypeCodename.OTHER;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

public final class PathElementsPanel extends AnalysisPanel {
	private static final long serialVersionUID = 5067614728945559108L;

	protected boolean paint_path_elements = false;
	private boolean setting_active_pe = false;

	private int dockedX;
	private int dockedEvent;
	private static final int DOCK_RANGE = 7; // in pixels
	private static final int GRAB_RANGE = 30; // in pixels

	private int textWidth;
	private int textHeight;
	static Color movingColor = Color.GREEN.brighter();
	static Color selectionColor = Color.GREEN;
	static Color anchoredColor = Color.BLUE;
	static Color defaultColor = Color.BLACK;
	
	PathResource pathResource;	
	
	public PathElementsPanel(final PathElementsLayeredPanel panel,
			final Dispatcher dispatcher,
			final double y[],
			final double deltaX) {
		super(panel, dispatcher, y, deltaX);
	}

	public void setPath(final SchemePath path) {
		this.pathResource = new PathResource(path);

		try {
			this.pathResource.validateAnchors();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}

	@Override
	protected void setGraphBounds(final int start, final int end) {
		super.setGraphBounds(start, end);
		if (this.pathResource != null) {
			this.pathResource.setStartPathElement(this.pathResource.getPathElementByOpticalDistance(start * this.deltaX));
			if (this.pathResource.hasPrevious(this.pathResource.getStartPathElement())) {
				this.pathResource.setStartPathElement(this.pathResource.getPrevious(this.pathResource.getStartPathElement()));
			}
			this.pathResource.setEndPathElement(this.pathResource.getPathElementByOpticalDistance(end * this.deltaX));
			if (this.pathResource.hasNext(this.pathResource.getEndPathElement())) {
				this.pathResource.setEndPathElement(this.pathResource.getNext(this.pathResource.getEndPathElement()));
			}
		}
	}

	@Override
	protected void this_mousePressed(final MouseEvent e) {
		this.startpos = e.getPoint();
		this.currpos = e.getPoint();

		if (this.paint_path_elements && this.pathResource != null) {
			if (this.currpos.y > 4 && this.currpos.y < 14) {
				this.setting_active_pe = true;
				updateDock();
				double distance = this.deltaX * coord2index(this.currpos.x);
				
				PathElementContainer container = this.pathResource.getPathElementByOpticalDistance(distance);
				
				// get nearest PE which is SE
				if (container.getPathElement().getKind() != IdlKind.SCHEME_ELEMENT) {
					final double[] d = this.pathResource.getOpticalDistanceFromStart(container);
					if (Math.abs(distance - d[0]) < Math.abs(distance - d[1])) {
						container = this.pathResource.getPrevious(container);
						Log.debugMessage("Set previous pathElement : " + container.getPathElement().getName(),Level.FINEST);
					} else {
						container = this.pathResource.getNext(container);
						Log.debugMessage("Set next pathElement : " + container.getPathElement().getName(), Level.FINEST);
					}
				}
				this.pathResource.setSelectedPathElement(container);
				
				if (!e.isShiftDown()) {
					this.pathResource.clearActivePathElements();
				}
				this.pathResource.addActivePathElement(container);
				
				// get all PE's in epsilon vicinity which is not CL
				PathElementContainer current = container;
				while (this.pathResource.hasPrevious(current)) {
					final double[] _d = this.pathResource.getOpticalDistanceFromStart(current);
					current = this.pathResource.getPrevious(current);
					
					// if is CL - not including it
					if (current.getPathElement().getKind() == IdlKind.SCHEME_CABLE_LINK) {
						break;
					}
					final double[] d = this.pathResource.getOpticalDistanceFromStart(current);
					if (((_d[0] - d[0]) / super.deltaX) * super.scaleX > GRAB_RANGE) {
						break;
					}
					this.pathResource.addActivePathElement(container);
				}
				current = container;
				while (this.pathResource.hasNext(current)) {
					final double[] _d = this.pathResource.getOpticalDistanceFromStart(current);
					current = this.pathResource.getNext(current);
					
					// if is CL - not including it
					if (current.getPathElement().getKind() == IdlKind.SCHEME_CABLE_LINK) {
						break;
					}
					final double[] d = this.pathResource.getOpticalDistanceFromStart(current);
					if (((d[1] - _d[1]) / super.deltaX) * super.scaleX > GRAB_RANGE) {
						break;
					}
					this.pathResource.addActivePathElement(container);
				}
				this.parent.repaint();
				return;
			}
		}
		super.this_mousePressed(e);
	}

	@Override
	protected void this_mouseDragged(final MouseEvent e) {
		if (this.setting_active_pe) {
			final List<PathElementContainer> activePathElements = this.pathResource.getActivePathElements();
			if (!activePathElements.isEmpty()) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
//				super.parent.repaint(this.startpos.x - 1, 0, 1, super.parent.getHeight());
//				super.parent.repaint(this.startpos.x + 1, 0, this.textWidth, this.textHeight);
				super.parent.repaint(this.currpos.x - 2, 0, 1, super.parent.getHeight());
				super.parent.repaint(this.currpos.x + 1, 0, this.textWidth, this.textHeight);
				
				Graphics g = getGraphics();
				g.setColor(movingColor);
				
				for (PathElementContainer container : activePathElements) {
					if (container.getPathElement().getKind() == IdlKind.SCHEME_ELEMENT) {
						SchemeElement se = getSchemeElement(container);
						paintPathElement(g, this.currpos.x, se);						
					}
				}
				this.upd_currpos(e);
				updateDock();
				
				this.currpos = new Point(index2coord(this.dockedX), this.currpos.y);
				
//				for (PathElement pe : this.activePathElements) {
//					if (pe.getKind() == IdlKind.SCHEME_ELEMENT) {
//						SchemeElement se = getSchemeElement(pe);
//						paintPathElement(g, this.currpos.x, se);						
//					}
//				}
			}
			return;
		}
		super.this_mouseDragged(e);
	}

	@Override
	protected void this_mouseReleased(final MouseEvent e) {
		if (this.setting_active_pe) {
			final int change = (this.dockedX - coord2index(this.startpos.x));
			final List<PathElementContainer> activePathElements = this.pathResource.getActivePathElements();
			if (!activePathElements.isEmpty() && Math.abs(change) > 0) {
				PathElementContainer firstMovingPE = this.pathResource.getFirstActivePathElement();
				PathElementContainer lastMovingPE = this.pathResource.getLastActivePathElement();
				
				double[] initial = this.pathResource.getOpticalDistanceFromStart(this.pathResource.getSelectedPathElement());
				double d = this.dockedX * this.deltaX - initial[0]; 
				
				PathElementContainer lastNode = this.pathResource.getPreviousAnchored(firstMovingPE);
				PathElementContainer nextNode = this.pathResource.getNextAnchored(lastMovingPE);
				
				if (lastNode != null && !firstMovingPE.equals(lastNode)) {
					this.pathResource.changeOpticalLength(lastNode, firstMovingPE, d);
				}
				if (nextNode != null && !lastMovingPE.equals(nextNode)) {
					this.pathResource.changeOpticalLength(lastMovingPE, nextNode, -d);
				}

				if (Heap.hasEtalon()) {
					ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
					this.pathResource.updateAnchor(firstMovingPE, mtae, this.dockedEvent);
				}
			}

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.setting_active_pe = false;
			this.parent.jLayeredPane.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}
	
	/*
	PathElement getPreviousAnchoredPE(PathElement pe) throws ApplicationException {
		if (Heap.hasEtalon()) {
			ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
			EventAnchorer ea = Heap.obtainAnchorer();
			int nEvent = mtae.getEventByCoord(coord2index(this.startpos.x));
			
			if (nEvent > 0 ) {
				for (int i = nEvent; i >= 0; i--) {
					if (!ea.getEventAnchor(i).isVoid()) {
						Identifier peId = Identifier.valueOf(ea.getEventAnchor(i).getValue());
						PathElement previousPE = StorableObjectPool.getStorableObject(peId, false);
						if (previousPE.compareTo(pe) < 0) {
							return previousPE;	
						}
					}
				}
			}
		}
		return PathResource.getPathMembers().first();
	}
	
	PathElement getNextAnchoredPE(PathElement pe) throws ApplicationException {
		if (Heap.hasEtalon()) {
			ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
			EventAnchorer ea = Heap.obtainAnchorer();
			int nEvent = mtae.getEventByCoord(coord2index(this.startpos.x));
			
			SimpleReflectogramEvent[] events = mtae.getSimpleEvents();
			if (nEvent < events.length - 2) {
				for (int i = nEvent + 1; i < events.length; i++) {
					if (!ea.getEventAnchor(i).isVoid()) {
						Identifier peId = Identifier.valueOf(ea.getEventAnchor(i).getValue());
						PathElement nextPE = StorableObjectPool.getStorableObject(peId, false);
						if (nextPE.compareTo(pe) > 0) {
							return nextPE;	
						}
					}
				}
			}		
		}
		return PathResource.getPathMembers().last();
	}
	*/
	
	void setPaintPathElements(boolean b) {
		this.paint_path_elements = b;
		if (b) {
			try {
				this.pathResource.validateAnchors();
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);

		if (this.paint_path_elements && this.pathResource != null) {
			if (this.pathResource.getStartPathElement() == null || this.pathResource.getEndPathElement() == null)
				this.setGraphBounds(this.start, this.end);

			ListIterator<PathElementContainer> it = 
					this.pathResource.getPathMembers().listIterator(
							this.pathResource.getPathMembers().indexOf(this.pathResource.getStartPathElement()));
			
			while (it.hasNext()) {
				PathElementContainer container = it.next();
				if (isNeedPaint(container)) {

					final double d[] = this.pathResource.getOpticalDistanceFromStart(container);
					final int start1 = index2coord((int) Math.round(d[0] / this.deltaX));
					final int end1 = index2coord((int) Math.round(d[1] / this.deltaX));
					
					final PathElement pathElement = container.getPathElement();
					if (pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
						if (this.pathResource.getActivePathElements().contains(container)) {
							g.setColor(selectionColor);
						}	else if (container.isAnchored()) {
							g.setColor(anchoredColor);
						} else {
							g.setColor(defaultColor);
						}
						
						SchemeElement se = getSchemeElement(container);
						paintPathElement(g, start1, se);
					} else {
						if (pathElement.getKind() == IdlKind.SCHEME_CABLE_LINK) {
							g.setColor(new Color(0.5f, 0.5f, 1f, 0.5f));
						} else if (pathElement.getKind() == IdlKind.SCHEME_LINK) {
							g.setColor(new Color(0.5f, 1.0f, 0.5f, 0.5f));
						}
//							g.drawLine(start1, 3, start1, 5);
						g.fillRect(start1, 5, end1 - start1, 9);
//							g.drawLine(start1, 5, end1, 5);
//							g.drawLine(end1, 5, end1, 3);
						g.setColor(Color.BLACK);
						g.drawString(String.valueOf((int)pathElement.getOpticalLength()), start1 + this.textWidth, 25);
					}
				}
				if (container.equals(this.pathResource.getEndPathElement())) {
					break;
				}
			}
			
			if (this.setting_active_pe) {
				g.setColor(movingColor);
				for (PathElementContainer pe : this.pathResource.getActivePathElements()) {
					if (pe.getPathElement().getKind() == IdlKind.SCHEME_ELEMENT) {
						SchemeElement se = getSchemeElement(pe);
						paintPathElement(g, this.currpos.x, se);						
					}
				}
			}
		}
	}
	
	void paintPathElement(Graphics g, int coord, SchemeElement se) {
		EquipmentType type;
		try {
			type = se.getProtoEquipment().getType();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			try {
				type = EquipmentType.valueOf(OTHER.stringValue());
			} catch (ApplicationException e1) {
				Log.errorMessage(e1);
				return;
			}
		}
		// if muff - paint small box and dashed line
		if (type.getCodename().equals(MUFF.stringValue())) {
			g.fill3DRect(coord - 1, 6, 2, 8, true);
			((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
			g.drawLine(coord, 6, coord, getHeight());
			((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
		} else { // paint large box, dashed line and name of SE
			g.fill3DRect(coord - 4, 6, 8, 8, true);
			((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
			g.drawLine(coord, 6, coord, getHeight());
			((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
		}
		String text = se.getName();
		Scheme parentScheme = se.getParentScheme();
		if (parentScheme != null) {
			text = text + "(" + parentScheme.getName() + ")";
		}
		
		final FontMetrics fm = this.parent.getFontMetrics(this.parent.getFont());
		this.textHeight = fm.stringWidth(text);
		this.textWidth = fm.getHeight();
		final int y1 = 6;
		final Graphics2D g2 = (Graphics2D) g;
		final AffineTransform t = g2.getTransform();
		g2.translate(0, this.textHeight + 10);
		g2.rotate(Math.toRadians(270), coord + this.textWidth - 5, y1);
		g2.drawString(text, coord + this.textWidth - 5, y1);
		g2.setTransform(t);
	}
	
	/**
	 * need paint if: 
	 * 1. First element
	 * 2. Non zero length element
	 * 3. Previous element is non zero  
	 * @param container
	 * @return true if pe need to be shown at field
	 */
	private boolean isNeedPaint(PathElementContainer container) {
		if (this.pathResource.hasPrevious(container)) {
			if (container.getPathElement().getOpticalLength() == 0) {
				PathElementContainer previous = this.pathResource.getPrevious(container);
				if (previous != null && previous.getPathElement().getOpticalLength() == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	void updateDock() {
		ModelTraceAndEvents mtae;
		if (Heap.hasEtalon()) {
			mtae = Heap.getMTMEtalon().getMTAE();
		} else {
			mtae = Heap.getMTAEPrimary();
		}

		int nEvent = mtae.getEventByCoord(coord2index(this.currpos.x));
		
		if (nEvent != -1 ) {
			SimpleReflectogramEvent event = mtae.getSimpleEvent(nEvent);
//			int currCoord = coord2index(this.currpos.x);			
			// get current event
			if (event.getEventType() == SimpleReflectogramEvent.LINEAR || 
					event.getEventType() == SimpleReflectogramEvent.NOTIDENTIFIED) { // may not be anchored
				SimpleReflectogramEvent prevEvent = mtae.getSimpleEvent(nEvent - 1);
				if (this.currpos.x - index2coord(prevEvent.getBegin()) < DOCK_RANGE) {
					this.dockedEvent = nEvent - 1;
					this.dockedX = event.getBegin();
					return;
				} 
				if (index2coord(event.getEnd()) - this.currpos.x < DOCK_RANGE) {
					this.dockedEvent = nEvent + 1;
					this.dockedX = event.getEnd();
					return;
				} 
			} else if (this.currpos.x - index2coord(event.getBegin()) < DOCK_RANGE) { // may be anchored
				this.dockedEvent = nEvent;
				this.dockedX = event.getBegin();
				return;
			}
		}
		this.dockedEvent = -1;
		this.dockedX = coord2index(this.currpos.x);
	}
	
	private SchemeElement getSchemeElement(PathElementContainer container) {
		final PathElement pathElement = container.getPathElement();
		if (container != null && pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
			SchemeElement se = pathElement.getSchemeElement();
			SchemeElement parentSE = se.getParentSchemeElement(); 
			while (parentSE != null) {
				se = parentSE;
				parentSE = se.getParentSchemeElement();
			}
			return se;
		}
		return null;
	}
}
