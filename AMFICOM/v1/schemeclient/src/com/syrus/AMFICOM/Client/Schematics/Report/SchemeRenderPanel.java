package com.syrus.AMFICOM.Client.Schematics.Report;

import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.Report.*;
import com.syrus.AMFICOM.Client.General.Scheme.*;

public class SchemeRenderPanel extends SchemePanelNoEdition
{
	public RenderingObject reportsRO = null;
	public SchemeGraph sGraph = null;
//	public double formKoeff = 1;

	public SchemeRenderPanel (ApplicationContext aContext)
	{
		super(aContext);
	}

	public void initializeSize(RenderingObject ro)
		throws CreateReportException
	{
		this.reportsRO = ro;
		sGraph = this.getGraph();

		try
		{
//			Rectangle bounds = sGraph.getCellBounds(sGraph.getRoots());
//			double scaleValue = this.getScale(bounds);

/*			double formKoeff = ((double)((bounds.width + 2 * bounds.x + 10)) / ((double)(bounds.height + 2 * bounds.y + 20)));
			this.setPreferredSize(new Dimension(reportsRO.width,(int)(reportsRO.width / formKoeff)));*/

			srpResized();
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
				srpResized();
			}

			public void componentShown(ComponentEvent e)
			{
			}
		});
	}

	public void srpResized()
	{
		Rectangle bounds = sGraph.getCellBounds(sGraph.getRoots());
		double scaleValue = getScale(bounds);

		int curWidth = (int) ((bounds.width + 2 * bounds.x) * scaleValue);
		int newHeight = (int) ((bounds.height + 2 * bounds.y) * scaleValue);

		/*		reportsRO.rendererPanel.setPreferredSize(new Dimension(curWidth, newHeight));
		 reportsRO.rendererPanel.setSize(new Dimension(curWidth, newHeight));*/
		this.setSize(new Dimension(curWidth, newHeight));
		this.setPreferredSize(this.getSize());
		sGraph.setActualSize(new Dimension(curWidth - 15, newHeight - 15));

		reportsRO.width = curWidth;
		reportsRO.height = newHeight;

		sGraph.setScale(scaleValue);
	}

	private double getScale(Rectangle bounds)
	{
		double curWidth = 0;
		if (reportsRO.rendererPanel != null)
			curWidth = reportsRO.rendererPanel.getPreferredSize().getWidth();
		else
			curWidth = reportsRO.width;

		return (curWidth / (bounds.width + 2 * bounds.x));
	}

/*	public void paint (Graphics g)
	{
		g.setColor(Color.white);
//		Dimension prefSize = reportsRO.rendererPanel.getPreferredSize();
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		g.setColor(Color.black);
		g.drawRect(0,0,this.getWidth() - 1,this.getHeight() - 1);

		schemeGraph.setBackground(Color.white);

		Rectangle bounds = schemeGraph.getCellBounds(schemeGraph.getRoots());
		double scaleValue = this.getScale(bounds);

		schemeGraph.setScale(scaleValue);

		bounds.x *= scaleValue;
		bounds.y *= scaleValue;
		bounds.width *= scaleValue;
		bounds.height *= scaleValue;

		g.setClip(0,0,bounds.x + bounds.width,bounds.y + bounds.height);
		schemeGraph.getUI().paint(g,schemeGraph);
	}*/
}

