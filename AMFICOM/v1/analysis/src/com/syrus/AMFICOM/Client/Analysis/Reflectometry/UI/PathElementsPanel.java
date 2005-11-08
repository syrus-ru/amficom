package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.scheme.PathElement;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemePath;
import com.syrus.AMFICOM.scheme.corba.IdlPathElementPackage.IdlDataPackage.IdlKind;
import com.syrus.util.Log;

public final class PathElementsPanel extends AnalysisPanel {
	private static final long serialVersionUID = 5067614728945559108L;

	protected boolean paint_path_elements = false;
	private boolean setting_active_pe = false;

	SchemePath path;
	private PathElement startPathElement;
	private PathElement endPathElement;
	private PathElement activePathElement;

	public PathElementsPanel(final PathElementsLayeredPanel panel,
			final Dispatcher dispatcher,
			final double y[],
			final double deltaX) {
		super(panel, dispatcher, y, deltaX);
	}

	public void setPath(final SchemePath path) {
		this.path = path;
		// if (events != null)
		// decompositor.setTotalOpticalLength(events[events.length - 1].last_point *
		// deltaX);
		// else if (ep != null)
		// decompositor.setTotalOpticalLength(ep[ep.length - 1].end * deltaX);
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

					if (this.activePathElement.getKind() != IdlKind.SCHEME_ELEMENT
							&& Math.abs(distance - d[0]) < 3 / this.scaleX * this.deltaX
							&& this.path.hasPreviousPathElement(this.activePathElement)) {
						this.activePathElement = this.path.getPreviousPathElement(this.activePathElement);
						Log.debugMessage("Set previous pathElement : " + this.activePathElement.getName() + "(" + (3 / this.scaleX * this.deltaX) + ")",
								Level.FINER);
					} else if (this.activePathElement.getKind() != IdlKind.SCHEME_ELEMENT
							&& Math.abs(distance - d[1]) < 3 / this.scaleX * this.deltaX
							&& this.path.hasNextPathElement(this.activePathElement)) {
						this.activePathElement = this.path.getNextPathElement(this.activePathElement);
						Log.debugMessage("Set next pathElement : " + this.activePathElement.getName() + "(" + (3 / this.scaleX * this.deltaX) + ")",
								Level.FINER);
					}
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
			if (this.activePathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
				Log.debugMessage("PathElement " + this.activePathElement.getName()
						+ " moved on " + ((this.currpos.x - this.startpos.x) / this.scaleX * this.deltaX) + " m", Level.FINER);
				this.upd_currpos(e);
				this.paintMovingPE(getGraphics().create());
			}
			return;
		}
		super.this_mouseDragged(e);
	}

	@Override
	protected void this_mouseReleased(final MouseEvent e) {
		if (this.setting_active_pe) {
			final double d = ((this.currpos.x - this.startpos.x) / this.scaleX * this.deltaX);
			if (Math.abs(d) > this.deltaX) {
				try {
					PathElement lastNode = this.path.getPreviousNode(this.activePathElement);
					PathElement nextNode = this.path.getNextNode(this.activePathElement);

					if (lastNode == null) {
						lastNode = this.path.getPreviousPathElement(this.activePathElement);
					}
					if (nextNode == null) {
						nextNode = this.path.getNextPathElement(this.activePathElement);
					}
					if (lastNode != null) {
						this.path.changeOpticalLength(lastNode, this.activePathElement, d);
					}
					if (nextNode != null) {
						this.path.changeOpticalLength(this.activePathElement, nextNode, -d);
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

	@Override
	public void paint(final Graphics g) {
		super.paint(g);

		if (this.paint_path_elements && this.path != null) {
			if (this.startPathElement == null || this.endPathElement == null)
				this.setGraphBounds(this.start, this.end);

			Set<SchemeElement> paintedSchemeElements = new HashSet<SchemeElement>();
			try {
				for (final PathElement pathElement : this.path.getPathMembers().tailSet(this.startPathElement)) {

					if (pathElement.getKind() == IdlKind.SCHEME_CABLE_LINK) {
						g.setColor(new Color(0.5f, 0.5f, 1f, 0.5f));
					} else if (pathElement.getKind() == IdlKind.SCHEME_LINK) {
						g.setColor(new Color(0.5f, 1.0f, 0.5f, 0.5f));
					} else if (pathElement == this.activePathElement) {
						g.setColor(new Color(255, 0, 64));
					} else {
						g.setColor(new Color(0, 160, 0));
					}

					final double d[] = this.path.getOpticalDistanceFromStart(pathElement);
					final int start1 = index2coord((int) Math.round(d[0] / this.deltaX));
					final int end1 = index2coord((int) Math.round(d[1] / this.deltaX));
					Log.debugMessage("PathElement " + pathElement.getName() + " from " + start1 + " to " + end1, Level.FINER);
					if (pathElement.getKind() == IdlKind.SCHEME_ELEMENT) {
						SchemeElement se = pathElement.getSchemeElement();
						SchemeElement parent = se.getParentSchemeElement(); 
						while (parent != null) {
							se = parent;
							parent = se.getParentSchemeElement();
						}
						
						if (!paintedSchemeElements.contains(se)) {
							paintedSchemeElements.add(se);
						// if muff - paint only small box and dashed line

						EquipmentType type;
						try {
							type = se.getProtoEquipment().getType();
						} catch (ApplicationException e) {
							Log.errorMessage(e);
							type = EquipmentType.OTHER;
						}
						if (type.equals(EquipmentType.MUFF)) {
							g.fill3DRect(start1 - 1, 6, 2, 8, true);
							((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
							g.drawLine(start1, 6, start1, getHeight());
							((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
						} else { // paint large box, dashed line and name of SE
							g.fill3DRect(start1 - 4, 6, 8, 8, true);
							((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
							g.drawLine(start1, 6, start1, getHeight());
							((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);

							final String text = pathElement.getName();
							final FontMetrics fm = this.parent.getFontMetrics(this.parent.getFont());
							final int height = fm.stringWidth(text);
							final int width = fm.getHeight();
							final int y1 = 6;
							final Graphics2D g2 = (Graphics2D) g;
							final AffineTransform t = g2.getTransform();
							g2.translate(0, height + 10);
							g2.rotate(Math.toRadians(270), start1 + width - 5, y1);
							g2.drawString(text, start1 + width - 5, y1);
							g2.setTransform(t);
						}
						}
					} else {
//						g.drawLine(start1, 3, start1, 5);
						g.fillRect(start1, 5, end1 - start1, 9);
//						g.drawLine(start1, 5, end1, 5);
//						g.drawLine(end1, 5, end1, 3);
					}
				}
			} catch (ApplicationException e) {
				Log.errorMessage(e);
			}
		}
	}

	void paintMovingPE(final Graphics g) {
		g.setXORMode(Color.CYAN);
		g.drawRect(this.currpos.x - 4, 6, 8, 8);
		g.drawRect(this.tmppos.x - 4, 6, 8, 8);
		((Graphics2D) g).setStroke(ScaledGraphPanel.DASHED_STROKE);
		g.drawLine(this.currpos.x, 16, this.currpos.x, getHeight());
		g.drawLine(this.tmppos.x, 16, this.tmppos.x, getHeight());
		((Graphics2D) g).setStroke(ScaledGraphPanel.DEFAULT_STROKE);
	}
}
