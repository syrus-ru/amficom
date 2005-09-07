/**
 * $Id: AMFICOMSxMapLayer.java,v 1.2 2005/09/07 15:53:36 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.objectfx;

import java.awt.Graphics;

import com.ofx.mapViewer.SxMapLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;

class AMFICOMSxMapLayer extends SxMapLayer
{
	private final OfxConnection connection;
	
	public AMFICOMSxMapLayer(OfxConnection connection)
	{
		super();
		this.connection = connection;
	}
	
	@Override
	public void paint(Graphics g)
	{
		try
		{
			// TODO this is really bad!!
			OfxNetMapViewer viewer = this.connection.getOfxLayerPainter();
			viewer.getLogicalNetLayer().paint(g, viewer.getVisibleBounds());
		}
		catch (MapConnectionException e)
		{
			System.out.println("Жопа");
		}
		catch (MapDataException e)
		{
			System.out.println("Жопа");
		}
	}
}