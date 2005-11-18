package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.logging.Level;

import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.analysis.EventAnchorer;
import com.syrus.AMFICOM.analysis.SOAnchorImpl;
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
	private static final int DOCK_RANGE = 15;
	
	SchemePath path;
	private PathElement startPathElement;
	private PathElement endPathElement;
//	private SchemeElement activeSchemeElement;
	private PathElement activePathElement;

	public PathElementsPanel(final PathElementsLayeredPanel panel,
			final Dispatcher dispatcher,
			final double y[],
			final double deltaX) {
		super(panel, dispatcher, y, deltaX);
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
				try {
					double distance = this.deltaX * coord2index(this.currpos.x);
					this.activePathElement = this.path.getPathElementByOpticalDistance(distance);
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
					}
					
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
			if (this.activePathElement != null) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				Log.debugMessage("PathElement " + this.activePathElement.getName()
						+ " moved on " + ((this.currpos.x - this.startpos.x) / this.scaleX * this.deltaX) + " m", Level.FINER);
	
				super.parent.repaint(this.startpos.x - 1, 0, 20, super.parent.getHeight());
				
				int prev_pos = this.currpos.x;
				
				SchemeElement se = getSchemeElement(this.activePathElement);
				Graphics g = getGraphics().create();
				g.setXORMode(new Color(0, 255, 191));
				
//				super.parent.repaint(prev_pos - 1, 0, 20, super.parent.getHeight());
				paintPathElement(g, this.currpos.x, se);
				
				this.upd_currpos(e);
				updateDock();
				
				this.currpos = new Point(index2coord(this.dockedX), this.currpos.y);
				
				
				
//				super.parent.repaint(this.currpos.x - 1, 0, 20, super.parent.getHeight());
				
				paintPathElement(g, this.currpos.x, se);

				
//				this.paintMovingPE(getGraphics().create());
			}
			return;
		}
		super.this_mouseDragged(e);
	}

	@Override
	protected void this_mouseReleased(final MouseEvent e) {
		if (this.setting_active_pe) {
			final double d = ((this.currpos.x - this.startpos.x) / this.scaleX * this.deltaX);
			if (this.activePathElement != null && Math.abs(d) > this.deltaX) {
				try {
					PathElement lastNode = this.path.getPreviousNode(this.activePathElement);
					PathElement nextNode = this.path.getNextNode(this.activePathElement);
					
					SchemeElement activeSE = getSchemeElement(this.activePathElement);
					SchemeElement lastSE = getSchemeElement(lastNode);
					SchemeElement nextSE = getSchemeElement(nextNode);
					if (activeSE.equals(lastSE)) {
						lastNode = this.path.getPreviousNode(lastNode);
					}
					if (activeSE.equals(nextSE)) {
						nextNode = this.path.getNextNode(nextNode);
					}

					if (lastNode == null) {
						lastNode = this.path.getPreviousPathElement(this.activePathElement);
					}
					if (nextNode == null) {
						nextNode = this.path.getNextPathElement(this.activePathElement);
					}
					if (lastNode != null && !this.activePathElement.equals(lastNode)) {
						this.path.changeOpticalLength(lastNode, this.activePathElement, d);
					}
					if (nextNode != null && !this.activePathElement.equals(nextNode)) {
						this.path.changeOpticalLength(this.activePathElement, nextNode, -d);
					}
				} catch (ApplicationException e1) {
					Log.errorMessage(e1);
				}
				
				long id = this.activePathElement.getId().getIdentifierCode();
				// create link to Etalon
				if (Heap.hasEtalon()) {
					ModelTraceAndEvents mtae = Heap.getMTMEtalon().getMTAE();
					
					// определяем привязчика
					EventAnchorer ea = Heap.obtainAnchorer();
					
					// отвязываем от старого события
					for (int i = 0; i < mtae.getNEvents(); i++) {
						SOAnchorImpl soAnchor = ea.getEventAnchor(i);
						if (soAnchor.getValue() == id) {
							ea.setEventAnchor(i, SOAnchorImpl.VOID_ANCHOR);
							Log.debugMessage("Removed anchor for event " + i, Level.FINER);
							break;
						}
					}
					
					// привязываем к новому событию в случае, если к нему не привязан кто-то другой
					int nEvent = mtae.getEventByCoord(coord2index(this.currpos.x));
					SOAnchorImpl soAnchor = ea.getEventAnchor(nEvent);
					if (soAnchor.isVoid()) {
						ea.setEventAnchor(nEvent, new SOAnchorImpl(id));
						Log.debugMessage("Create new anchor for event " + nEvent, Level.FINER);
					} else {
						Log.debugMessage("Already created anchor for event " + nEvent, Level.FINER);
					}
				}
			}

			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			this.setting_active_pe = false;
			this.parent.jLayeredPane.repaint();
			return;
		}
		super.this_mouseReleased(e);
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
							
							if (pathElement.equals(this.activePathElement)) {
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
		final int height = fm.stringWidth(text);
		final int width = fm.getHeight();
		final int y1 = 6;
		final Graphics2D g2 = (Graphics2D) g;
		final AffineTransform t = g2.getTransform();
		g2.translate(0, height + 10);
		g2.rotate(Math.toRadians(270), coord + width - 5, y1);
		g2.drawString(text, coord + width - 5, y1);
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
			
			// get current event
			if (event.getEventType() == SimpleReflectogramEvent.LINEAR || 
					event.getEventType() == SimpleReflectogramEvent.NOTIDENTIFIED) { // may not be anchored
				int currCoord = coord2index(this.currpos.x);
				if (currCoord - event.getBegin() < DOCK_RANGE) {
					this.dockedEvent = nEvent - 1;
					this.dockedX = event.getBegin() - 1;
					
				} else if (event.getEnd() - currCoord < DOCK_RANGE) {
					this.dockedEvent = nEvent + 1;
					this.dockedX = event.getEnd() + 1;
				} else {
					this.dockedEvent = -1;
					this.dockedX = coord2index(this.currpos.x);
				}
			} else { // may be anchored
				this.dockedEvent = nEvent;
				this.dockedX = coord2index(this.currpos.x);
			}
		} else {
			this.dockedEvent = -1;
			this.dockedX = coord2index(this.currpos.x);
		}
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

//	void paintMovingPE(final Graphics g) {
//
//		g.drawRect(this.currpos.x - 4, 6, 8, 8);
//		g.drawRect(this.tmppos.x - 4, 6, 8, 8);
//		((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
//		g.drawLine(this.currpos.x, 16, this.currpos.x, getHeight());
//		g.drawLine(this.tmppos.x, 16, this.tmppos.x, getHeight());
//		((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
//	}
}
