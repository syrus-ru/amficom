package com.syrus.AMFICOM.Client.Schematics.Report;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.awt.event.*;

import javax.swing.JPanel;

import com.syrus.AMFICOM.Client.General.Report.RenderingObject;
import com.syrus.AMFICOM.Client.General.Report.CreateReportException;
import com.syrus.AMFICOM.Client.General.Scheme.SchemeGraph;

public class SchemeRenderPanel extends JPanel
{
	public RenderingObject reportsRO = null;
	SchemeGraph schemeGraph = null;
	public double formKoeff = 1;

	public SchemeRenderPanel(RenderingObject ro,SchemeGraph sg)
			throws CreateReportException
	{
		this.reportsRO = ro;
		this.schemeGraph = sg;
		schemeGraph.setBackground(new Color(220,220,220));

		try
		{
			Rectangle bounds = schemeGraph.getCellBounds(schemeGraph.getRoots());
			double scaleValue = this.getScale(bounds);

			formKoeff = (double)((bounds.width + 2 * bounds.x + 10) / (bounds.height + 2 * bounds.y + 20));
			this.setPreferredSize(new Dimension(reportsRO.width,(int)(reportsRO.width / formKoeff)));
		}
		catch (Exception e)
		{
			throw new CreateReportException(reportsRO.getReportToRender().getName(),CreateReportException.cantImplement);
		}

		this.addComponentListener(new ComponentAdapter()
		{
			public void componentHidden(ComponentEvent e)
			{

			}

			public void componentMoved(ComponentEvent e)
			{
			}

			public void componentResized(ComponentEvent e)
			{
				Rectangle bounds = schemeGraph.getCellBounds(schemeGraph.getRoots());
				double scaleValue = getScale(bounds);

				int curWidth = (int) ((bounds.width + 2 * bounds.x) * scaleValue);
				int newHeight = (int) ((bounds.height + 2 * bounds.y) * scaleValue);

				reportsRO.rendererPanel.setPreferredSize(new Dimension(curWidth, newHeight));
				reportsRO.rendererPanel.setSize(new Dimension(curWidth, newHeight));

				reportsRO.width = curWidth;
				reportsRO.height = newHeight;
			}

			public void componentShown(ComponentEvent e)
			{
			}
		});
	}

	private double getScale(Rectangle bounds)
	{
		double curWidth = 0;
		if (reportsRO.rendererPanel != null)
			curWidth = reportsRO.rendererPanel.getPreferredSize().getWidth();
		else
			curWidth = reportsRO.width;

		return (double) (curWidth / (bounds.width + 2 * bounds.x));
	}

	public void paint (Graphics g)
	{
		super.paint(g);

		Rectangle bounds = schemeGraph.getCellBounds(schemeGraph.getRoots());
		double scaleValue = this.getScale(bounds);

		schemeGraph.setScale(scaleValue);

		bounds.x *= scaleValue;
		bounds.y *= scaleValue;
		bounds.width *= scaleValue;
		bounds.height *= scaleValue;

		g.setClip(0,0,bounds.x + bounds.width,bounds.y + bounds.height);
		schemeGraph.getUI().paint(g,schemeGraph);
	}
}