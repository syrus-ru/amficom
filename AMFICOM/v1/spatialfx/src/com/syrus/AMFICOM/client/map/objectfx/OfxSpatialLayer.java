package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.component.SxSymbolCanvas;
import com.ofx.mapViewer.SxHighlightBin;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.mapViewer.SxRendererInterface;
import com.ofx.repository.SxClass;
import com.ofx.repository.SxSymbology;

import com.syrus.AMFICOM.Client.Map.SpatialLayer;

import java.awt.Component;

import javax.swing.JPanel;

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
			sc = SxClass.retrieve(className, sxMapViewer.getQuery());
			bin = sxMapViewer.getBin(binName);
			ri = bin.getRenderer(className);
			sym = bin.getSymbology(className);
		}
		catch(java.lang.Exception exception)
		{
			exception.printStackTrace();;
		}
		setLabel();
	}
	
	public OfxSpatialLayer(SxMapViewer sxMapViewer, String className)
	{
		this.sxMapViewer = sxMapViewer;
		this.className = className;

		try
		{
			sc = SxClass.retrieve(className, sxMapViewer.getQuery());
			ri = sc.getRenderer();
			sym = sc.getSymbology();
		}
		catch(Exception exception)
		{
			exception.printStackTrace();;
		}
		
		setLabel();
	}
	
	private void setLabel()
	{
		try
		{
			int dim = sc.getDimension();
			
			imagePanel = new JPanel();
			SxSymbolCanvas sxsymbolcanvas = new SxSymbolCanvas(sym);
			sxsymbolcanvas.setRenderer(ri);
			sxsymbolcanvas.setDimension(dim);
			
			imagePanel.add(sxsymbolcanvas);
		}
		catch(java.lang.Exception exception)
		{
			imagePanel = null;
			exception.printStackTrace();
		}
	}

	public boolean isVisible()
	{
		SxMapLayerInterface sxmaplayerinterface = sxMapViewer.getNamedLayer(className);
		if(sxmaplayerinterface != null)
			return sxmaplayerinterface.isEnabled();
		else
			return false;
	}

	public boolean isLabelVisible()
	{
		SxMapLayerInterface sxmaplayerinterface = sxMapViewer.getNamedLayer(className + "LABELS");
		if(sxmaplayerinterface != null)
			return sxmaplayerinterface.isEnabled();
		else
			return false;
	}

	public void setVisible(boolean visible)
	{
		SxMapLayerInterface sxmaplayerinterface = sxMapViewer.getNamedLayer(className);
		if(sxmaplayerinterface != null)
			sxmaplayerinterface.setEnabled(visible);
			
		sxMapViewer.postPaintEvent();
	}


	public void setLabelVisible(boolean visible)
	{
		SxMapLayerInterface sxmaplayerinterface = sxMapViewer.getNamedLayer(className + "LABELS");
		if(sxmaplayerinterface != null)
			sxmaplayerinterface.setEnabled(visible);
	}

	public Component getLayerImage()
	{
		return imagePanel;
	}
	
	public String getName()
	{
		if(binName != null)
			return binName;
		return className;
	}
}
