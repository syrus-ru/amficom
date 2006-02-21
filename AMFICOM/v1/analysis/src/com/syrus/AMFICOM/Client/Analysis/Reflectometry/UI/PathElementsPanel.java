package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SOAnchorImpl;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceAndEvents;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObjectPool;
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
	
	SchemePath path;
	private PathElement startPathElement;
	private PathElement endPathElement;
//	private SchemeElement activeSchemeElement;
	private List<PathElement> activePathElements;

	private int textWidth;
	private int textHeight;
	Color movingColor = Color.RED.brighter();
	
	public PathElementsPanel(final PathElementsLayeredPanel panel,
			final Dispatcher dispatcher,
			final double y[],
			final double deltaX) {
		super(panel, dispatcher, y, deltaX);
		this.activePathElements = new LinkedList<PathElement>();
	}

	public void setPath(final SchemePath path) {
		this.path = path;
	}

	@Override
	protected void setGraphBounds(final int start, final int end) {
		super.setGraphBounds(start, end);
		if (this.path != null) {
			try {
				this.startPathElement = this.path.getPathElementByOpticalDistance(start * this.deltaX);
				if (this.path.hasPreviousPathElement(this.startPathElement)) {
					this.startPathElement = this.path.getPreviousPathElement(this.startPathElement);
				}
				this.endPathElement = this.path.getPathElementByOpticalDistance(end * this.deltaX);
				if (this.path.hasNextPathElement(this.endPathElement)) {
					this.endPathElement = this.path.getNextPathElement(this.endPathElement);
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}

	@Override
	protected void this_mousePressed(final MouseEvent e) {
		this.startpos = e.getPoint();
		this.currpos = e.getPoint();

		if (this.paint_path_elements && this.path != null) {
			if (this.currpos.y > 4 && this.currpos.y < 14) {
				this.setting_active_pe = true;
				updateDock();
				try {
					double distance = this.deltaX * coord2index(this.currpos.x);
					double distance1 = this.deltaX * coord2index(Math.max(0, this.currpos.x - GRAB_RANGE));
					double distance2 = this.deltaX * coord2index(this.currpos.x + GRAB_RANGE);
										
					PathElement activePathElement = this.path.getPathElementByOpticalDistance(distance);
					
					// get nearest PE which is SE
					if (activePathElement.getKind() != IdlKind.SCHEME_ELEMENT) {
						final double[] d = this.path.getOpticalDistanceFromStart(activePathElement);
						if (Math.abs(distance - d[0]) < Math.abs(distance - d[1])) {
							activePathElement = this.path.getPreviousPathElement(activePathElement);
							Log.debugMessage("Set previous pathElement : " + activePathElement.getName(),Level.FINEST);
						} else {
							activePathElement = this.path.getNextPathElement(activePathElement);
							Log.debugMessage("Set next pathElement : " + activePathElement.getName(), Level.FINEST);
						}
					}
					
					this.activePathElements.clear();
					this.activePathElements.add(activePathElement);

					// get all PE's in epsilon vicinity which is not CL
					PathElement currentPathElement = activePathElement;
					while (this.path.hasPreviousPathElement(currentPathElement)) {
						final double[] _d = this.path.getOpticalDistanceFromStart(currentPathElement);
						currentPathElement = this.path.getPreviousPathElement(currentPathElement);
						
						// if is CL - not including it
						if (currentPathElement.getKind() == IdlKind.SCHEME_CABLE_LINK) {
							break;
						}
						final double[] d = this.path.getOpticalDistanceFromStart(currentPathElement);
						if (((_d[0] - d[0]) / super.deltaX) * super.scaleX > GRAB_RANGE) {
							break;
						}
						this.activePathElements.add(0, currentPathElement);
					}
					currentPathElement = activePathElement;
					while (this.path.hasNextPathElement(currentPathElement)) {
						final double[] _d = this.path.getOpticalDistanceFromStart(currentPathElement);
						currentPathElement = this.path.getNextPathElement(currentPathElement);
						
						// if is CL - not including it
						if (currentPathElement.getKind() == IdlKind.SCHEME_CABLE_LINK) {
							break;
						}
						final double[] d = this.path.getOpticalDistanceFromStart(currentPathElement);
						if (((d[1] - _d[1]) / super.deltaX) * super.scaleX > GRAB_RANGE) {
							break;
						}
						this.activePathElements.add(currentPathElement);
					}
					
					/*
					PathElement pathElement1 = this.path.getPathElementByOpticalDistance(distance1);
					PathElement pathElement2 = this.path.getPathElementByOpticalDistance(distance2);
					
					final double[] d = this.path.getOpticalDistanceFromStart(this.activePathElement);
					
					if (this.activePathElement.getKind() != IdlKind.SCHEME_ELEMENT) {
						if (Math.abs(distance - d[0]) < Math.abs(distance - d[1])) {
							this.activePathElement = this.path.getPreviousPathElement(this.activePathElement);
							Log.debugMessage("Set previous pathElement : " + this.activePathElement.getName() + "(" + (3 / this.scaleX * this.deltaX) + ")",Level.FINEST);
						} else {
							this.activePathElement = this.path.getNextPathElement(this.activePathElement);
							Log.debugMessage("Set next pathElement : " + this.activePathElement.getName() + "(" + (3 / this.scaleX * this.deltaX) + ")", Level.FINER);
						}
					}
					
					// check for previous zero length element
					PathElement previousPE = this.path.getPreviousPathElement(this.activePathElement);
					while (previousPE != null && previousPE.getOpticalLength() == 0) {
						this.activePathElement = previousPE;
						previousPE = this.path.getPreviousPathElement(this.activePathElement);
					}*/
					
					this.parent.repaint();
//					this.activeSchemeElement = getSchemeElement(this.activePathElement);
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
				return;
			}
		}
		super.this_mousePressed(e);
	}

	@Override
	protected void this_mouseDragged(final MouseEvent e) {
		if (this.setting_active_pe) {
			if (!this.activePathElements.isEmpty()) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				Log.debugMessage("PathElement " + this.activePathElements.iterator().next().getName()
						+ " moved on " + ((this.currpos.x - this.startpos.x) / this.scaleX * this.deltaX) + " m", Level.FINER);
	
//				super.parent.repaint(this.startpos.x - 1, 0, 1, super.parent.getHeight());
//				super.parent.repaint(this.startpos.x + 1, 0, this.textWidth, this.textHeight);
				super.parent.repaint(this.currpos.x - 2, 0, 1, super.parent.getHeight());
				super.parent.repaint(this.currpos.x + 1, 0, this.textWidth, this.textHeight);
				
				Graphics g = getGraphics();
				g.setColor(this.movingColor);
				
				for (PathElement pe : this.activePathElements) {
					if (pe.getKind() == IdlKind.SCHEME_ELEMENT) {
						SchemeElement se = getSchemeElement(pe);
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
			if (!this.activePathElements.isEmpty() && Math.abs(change) > 0) {
				try {
					PathElement firstMovingPE = this.activePathElements.iterator().next();
					PathElement lastMovingPE = this.activePathElements.listIterator(this.activePathElements.size()).previous();
					
					double[] initial = this.path.getOpticalDistanceFromStart(firstMovingPE);
					double d = this.dockedX * this.deltaX - initial[0]; 
					
					PathElement lastNode = getPreviousAnchoredPE(firstMovingPE);
					PathElement nextNode = getNextAnchoredPE(lastMovingPE);
					
//					SchemeElement activeSE = getSchemeElement(activePathElement);
//					SchemeElement lastSE = getSchemeElement(lastNode);
//					SchemeElement nextSE = getSchemeElement(nextNode);
//					if (activeSE.equals(lastSE)) {
//						lastNode = this.path.getPreviousNode(lastNode);
//					}
//					if (activeSE.equals(nextSE)) {
//						nextNode = this.path.getNextNode(nextNode);
//					}

//					if (lastNode == null) {
//						lastNode = this.path.getPreviousPathElement(activePathElement);
//					}
//					if (nextNode == null) {
//						nextNode = this.path.getNextPathElement(activePathElement);
//					}
					
					if (lastNode != null && !firstMovingPE.equals(lastNode)) {
						this.path.changeOpticalLength(lastNode, firstMovingPE, d);
					}
					if (nextNode != null && !lastMovingPE.equals(nextNode)) {
						this.path.changeOpticalLength(lastMovingPE, nextNode, -d);
					}
				
					if (Heap.hasEtalon()) {
						ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
						updateAnchor(firstMovingPE, mtae);
					}
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
			}

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.setting_active_pe = false;
			this.parent.jLayeredPane.repaint();
			return;
		}
		super.this_mouseReleased(e);
	}
	
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
		return this.path.getPathMembers().first();
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
		return this.path.getPathMembers().last();
	}
	
	void validateAnchors(ModelTraceAndEvents mtae) {
		EventAnchorer ea = Heap.obtainAnchorer();
		boolean anchorerUpdated = false;
		try {
			for (int i = 0; i < mtae.getNEvents(); i++) {
				SOAnchorImpl soAnchor = ea.getEventAnchor(i);
				if (soAnchor.getValue() != SOAnchorImpl.VOID_ANCHOR.getValue()) {
					Identifier peId = Identifier.valueOf(soAnchor.getValue());
					PathElement pe = StorableObjectPool.getStorableObject(peId, true);
					if (pe == null) {
						ea.setEventAnchor(i, SOAnchorImpl.VOID_ANCHOR);
						anchorerUpdated = true;
						Log.debugMessage("Invalid pathElement with id '" + peId + "' Remove anchor for event " + i, Level.FINER);	
					}
				}
			}
			if (anchorerUpdated) {
				Heap.notifyAnchorerChanged();
			}
		} catch (ApplicationException e) {
			Log.errorMessage(e);
		}
	}
	
	void setPaintPathElements(boolean b) {
		this.paint_path_elements = b;
		if (b && Heap.hasEtalon()) {
			ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
			validateAnchors(mtae);
		}
	}
	
	void updateAnchor(PathElement pe, ModelTraceAndEvents mtae) {
		long id = pe.getId().getIdentifierCode();
		// create link to Etalon
		// определяем привязчика
		EventAnchorer ea = Heap.obtainAnchorer();
		
		boolean anchorerUpdated = false;
		
		// отвязываем от старого события
		for (int i = 0; i < mtae.getNEvents(); i++) {
			SOAnchorImpl soAnchor = ea.getEventAnchor(i);
			if (soAnchor.getValue() == id) {
				ea.setEventAnchor(i, SOAnchorImpl.VOID_ANCHOR);
				anchorerUpdated = true;
				Log.debugMessage("Removed anchor for event " + i, Level.FINER);
				break;
			}
		}
		
		// привязываем к новому событию в случае, если к нему не привязан кто-то другой
		int nEvent = this.dockedEvent;
		if (this.dockedEvent != -1) {
			SOAnchorImpl soAnchor = ea.getEventAnchor(nEvent);
			if (soAnchor.isVoid()) {
				ea.setEventAnchor(nEvent, new SOAnchorImpl(id));
				anchorerUpdated = true;
				Log.debugMessage("Create new anchor for event " + nEvent, Level.FINER);
			} else {
				Log.debugMessage("Already created anchor for event " + nEvent, Level.FINER);
			}
		}
		
		if (anchorerUpdated)
			Heap.notifyAnchorerChanged();
	}

	@Override
	public void paint(final Graphics g) {
		super.paint(g);

		if (this.paint_path_elements && this.path != null) {
			if (this.startPathElement == null || this.endPathElement == null)
				this.setGraphBounds(this.start, this.end);

			try {
				for (final PathElement pathElement : this.path.getPathMembers().tailSet(this.startPathElement)) {
					if (isNeedPaint(pathElement)) {
						
						final double d[] = this.path.getOpticalDistanceFromStart(pathElement);
						final int start1 = index2coord((int) Math.round(d[0] / this.deltaX));
						final int end1 = index2coord((int) Math.round(d[1] / this.deltaX));
						Log.debugMessage("PathElement " + pathElement.getName() + " from " + start1 + " to " + end1, Level.FINER);
						
						if (pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
							
							if (this.activePathElements.contains(pathElement)) {
								g.setColor(new Color(255, 0, 64));
							} else {
								g.setColor(new Color(0, 160, 0));
							}
							
							SchemeElement se = getSchemeElement(pathElement);
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
						}
					}
					
					if (pathElement.equals(this.endPathElement)) {
						break;
					}
				}
				
				if (setting_active_pe) {
					g.setColor(this.movingColor);
					for (PathElement pe : this.activePathElements) {
						if (pe.getKind() == IdlKind.SCHEME_ELEMENT) {
							SchemeElement se = getSchemeElement(pe);
							paintPathElement(g, this.currpos.x, se);						
						}
					}
				}
				
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}
	
	void paintPathElement(Graphics g, int coord, SchemeElement se) {
		EquipmentType type;
		try {
			type = se.getProtoEquipment().getType();
		} catch (ApplicationException e) {
			Log.errorMessage(e);
			type = EquipmentType.OTHER;
		}
		// if muff - paint small box and dashed line
		if (type.equals(EquipmentType.MUFF)) {
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
	 * @param pe
	 * @return true if pe need to be shown at field
	 * @throws ApplicationException
	 */
	private boolean isNeedPaint(PathElement pe) throws ApplicationException {
		if (pe.getOpticalLength() == 0) {
			PathElement previousPE = this.path.getPreviousPathElement(pe);
			if (previousPE != null && previousPE.getOpticalLength() == 0) {
				return false;
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
	
	private SchemeElement getSchemeElement(PathElement pathElement) {
		if (pathElement != null && pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
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
