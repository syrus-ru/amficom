package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.Component;

import javax.swing.JPanel;

import com.ofx.component.SxSymbolCanvas;
import com.ofx.mapViewer.SxHighlightBin;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.mapViewer.SxRendererInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxSymbology;
import com.syrus.AMFICOM.client.map.SpatialLayer;

public class OfxSpatialLayer implements SpatialLayer 
{
	protected String className = null;
	protected String binName = null;
	protected SxMapViewer sxMapViewer = null;
	protected SxSymbology sym = null;
	protected SxClass sc;
	protected SxHighlightBin bin;
	protected SxRendererInterface ri;
	protected JPanel imagePanel;

	public OfxSpatialLayer(SxMapViewer sxMapViewer, String className, String binName)
	{
		this.sxMapViewer = sxMapViewer;
		this.className = className;
		this.binName = binName;

		try
		{
			this.sc = SxClass.retrieve(className, sxMapViewer.getQuery());
			this.bin = sxMapViewer.getBin(binName);
			this.ri = this.bin.getRenderer(className);
			this.sym = this.bin.getSymbology(className);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		setLabel();
	}
	
	public OfxSpatialLayer(SxMapViewer sxMapViewer, String className)
	{
		this.sxMapViewer = sxMapViewer;
		this.className = className;

		try
		{
			this.sc = SxClass.retrieve(className, sxMapViewer.getQuery());
			this.ri = this.sc.getRenderer();
			this.sym = this.sc.getSymbology();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
		
		setLabel();
	}
	
	private void setLabel()
	{
		try
		{
			int dim = this.sc.getDimension();
			
			this.imagePanel = new JPanel();
			SxSymbolCanvas sxsymbolcanvas = new SxSymbolCanvas(this.sym);
			sxsymbolcanvas.setRenderer(this.ri);
			sxsymbolcanvas.setDimension(dim);
			
			this.imagePanel.add(sxsymbolcanvas);
		}
		catch(Exception exception)
		{
			this.imagePanel = null;
			exception.printStackTrace();
		}
	}

	public boolean isVisible()
	{
		SxMapLayerInterface sxmaplayerinterface = this.sxMapViewer.getNamedLayer(this.className);
		if(sxmaplayerinterface != null)
			return sxmaplayerinterface.isEnabled();
		return false;
	}

	public boolean isLabelVisible()
	{
		SxMapLayerInterface sxmaplayerinterface = this.sxMapViewer.getNamedLayer(this.className + "LABELS");
		if(sxmaplayerinterface != null)
			return sxmaplayerinterface.isEnabled();
		return false;
	}

	public void setVisible(boolean visible)
	{
		SxMapLayerInterface sxmaplayerinterface = this.sxMapViewer.getNamedLayer(this.className);
		if(sxmaplayerinterface != null)
			sxmaplayerinterface.setEnabled(visible);
			
		this.sxMapViewer.postPaintEvent();
	}


	public void setLabelVisible(boolean visible)
	{
		SxMapLayerInterface sxmaplayerinterface = this.sxMapViewer.getNamedLayer(this.className + "LABELS");
		if(sxmaplayerinterface != null)
			sxmaplayerinterface.setEnabled(visible);
	}

	public Component getLayerImage()
	{
		return this.imagePanel;
	}
	
	public String getName()
	{
		if(this.binName != null)
			return this.binName;
		return this.className;
	}

	public boolean isVisibleAtScale(double scale) {
		return true;
	}
}
