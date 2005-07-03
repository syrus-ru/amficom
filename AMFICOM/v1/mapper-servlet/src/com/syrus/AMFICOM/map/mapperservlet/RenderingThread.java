package com.syrus.AMFICOM.map.mapperservlet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.mapinfo.util.DoublePoint;

/**
 * $Id: RenderingThread.java,v 1.2 2005/05/10 07:08:44 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

class RenderingThread extends Thread
{
	
	/**
	 * Буфер для хранения готового изображения
	 */
	private ByteArrayOutputStream readyImageOutputStream = null;
	
	/**
	 * Буфер для рендеринга
	 */
	private ByteArrayOutputStream currentlyRenderingOutputStream = null;
	
	
	/**
	 * Флаг показывает - происходит ли рендеринг в текущий момент
	 */
	private boolean isProcessing = false;

	/**
	 * Флаг показывает есть ли в буфере отображённая карта
	 */
	boolean isMapRendered = false;

	private SessionState sessionState = null;
	
	private MapJRenderer mapJRenderer;
	
	public RenderingThread (
			SessionState sessionState,
			MapJRenderer mapJRenderer)
	{
		this.sessionState = sessionState;
		this.mapJRenderer = mapJRenderer;
	}
	
	public void run()
	{
		//Отображаем карту и записывает её в поток данных
		Logger.log("RenderingThread - run - Rendering map.");
		
		this.currentlyRenderingOutputStream = new ByteArrayOutputStream();
		
		try
		{
			this.mapJRenderer.renderToStream(this.currentlyRenderingOutputStream);
		}
		catch (Exception e)
		{
			Logger.log(e.getMessage());
		}

		this.readyImageOutputStream = this.currentlyRenderingOutputStream;			
		
		this.isProcessing = false;
		this.isMapRendered = true;
		
		Logger.log("RenderingThread - run - Map rendered.");

		synchronized(this.sessionState)
		{
			this.sessionState.setOutputStream(this.readyImageOutputStream);
			MapJRendererPool.unlockRenderer(this.mapJRenderer);
			this.sessionState.setState(State.STATE_RENDERED);
		}
	}
	
	public void cancel()
	{
//		this.toBreak = true;
	}
	
	public void cancelRendering()
	{
		Logger.log("RunningThread - cancelRendering - Stopping the rendering of map.");		
		try
		{
			this.mapJRenderer.cancelRendering();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Logger.log(e.getMessage());
		}
		Logger.log("RunningThread - cancelRendering - Rendering stopped.");		
	}

	public boolean isMapRendered()
	{
		return this.isMapRendered;
	}
	
	public void getMapRenditionInto(OutputStream out)
	{
		Logger.log("RunningThread - getMapRendition - Writing the rendition to stream.");		
		try
		{
			this.readyImageOutputStream.writeTo(out);
			Logger.log("RunningThread - getMapRendition - Succesfully written.");			
			this.isMapRendered = false;
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			Logger.log(e.getMessage());			
		}
	}
	
	public void startProcessing(
			int width,
			int height,
			double scale,
			double centerX,
			double centerY,
			int layersCount,
			boolean[] layersVisibilities)
	{
		while (this.isProcessing == true)
			try
			{
				Thread.sleep(10);
			} catch (InterruptedException e1)
			{
				// TODO Auto-generated catch block
				Logger.log(e1.getMessage());				
			}

		this.isProcessing = true;
		
		//Setting size, zoom and center point	
		this.mapJRenderer.setSize(width, height);
		this.mapJRenderer.setCenter(new DoublePoint(centerX, centerY));
		this.mapJRenderer.setScale(scale);
		
		for (int i = 0; i < layersCount; i++)
			this.mapJRenderer.setLayerVisibility(
					i,
					layersVisibilities[2*i],
					layersVisibilities[2*i + 1]);
		
		Logger.log("Starting RenderingThread.");			
		this.start();
		Logger.log("RenderingThread started.");			
	}
	
	public String toString()
	{
		return "RenderingThread exists.";
	}
	
	public SessionState getSessionState()
	{
		return this.sessionState;
	}
	
	public void setSessionState(SessionState sessionState)
	{
		this.sessionState = sessionState;
	}
}