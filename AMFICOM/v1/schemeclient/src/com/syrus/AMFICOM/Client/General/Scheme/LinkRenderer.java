package com.syrus.AMFICOM.Client.General.Scheme;

import java.awt.Rectangle;

import com.jgraph.graph.EdgeRenderer;

public class LinkRenderer extends EdgeRenderer
{
	public void repaint()
	{
		super.repaint();
	}

	public void repaint(Rectangle r)
	{
		super.repaint(r);
	}

/*	public void paint(Graphics g) {
	//	System.out.println("LinkView = " + view.hashCode() + " LinkRenderer = " + hashCode());

		Shape edgeShape = createShape();
		// Sideeffect: beginShape, lineShape, endShape
		if (edgeShape != null) {
			Graphics2D g2 = (Graphics2D) g;
			int c = BasicStroke.CAP_BUTT;
			int j = BasicStroke.JOIN_MITER;
			g2.setStroke(new BasicStroke(lineWidth, c, j));
			g.translate(-getX(), -getY());

			if (alarmed)
				g.setColor(Color.red);
			else
				g.setColor(getForeground());

			if (beginShape != null) {
				if (beginFill)
					g2.fill(beginShape);
				g2.draw(beginShape);
			}
			if (endShape != null) {
				if (endFill)
					g2.fill(endShape);
				g2.draw(endShape);
			}
			if (lineDash != null) // Dash For Line Only
				g2.setStroke(
					new BasicStroke(lineWidth, c, j, 10.0f, lineDash, 0.0f));
			g2.draw(lineShape);
			if (selected) { // Paint Selected
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g2.setColor(graph.getHighlightColor());
				if (beginShape != null)
					g2.draw(beginShape);
				g2.draw(lineShape);
				if (endShape != null)
					g2.draw(endShape);
			}
			if (graph.getEditingCell() != view.getCell()) {
				Object label = graph.convertValueToString(view);
				if (label != null) {
					g2.setStroke(new BasicStroke(1));
					g.setFont(getFont());
					paintLabel(g, label.toString());
				}
			}
		}
	}*/
}

