/**
 * $Id: MapPhysicalLinkElementStrategy.java,v 1.7 2005/02/01 16:16:13 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.Strategy;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapState;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.Selection;

import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

/**
 * Стратегия управления физической линией.
 * @author $Author: krupenn $
 * @version $Revision: 1.7 $, $Date: 2005/02/01 16:16:13 $
 * @module mapviewclient_v1
 */
public final class MapPhysicalLinkElementStrategy extends MapStrategy 
{
	/**
	 * Физическая линия.
	 */
	PhysicalLink link;

	/**
	 * Instance.
	 */
	private static MapPhysicalLinkElementStrategy instance = new MapPhysicalLinkElementStrategy();

	/**
	 * Private constructor.
	 */
	private MapPhysicalLinkElementStrategy()
	{
	}

	/**
	 * get instance.
	 * @return instance
	 */
	public static MapPhysicalLinkElementStrategy getInstance()
	{
		return instance;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setMapElement(MapElement me)
	{
		this.link = (PhysicalLink)me;
	}

	/**
	 * {@inheritDoc}
	 */
	public void doContextChanges(MouseEvent me)
	{
		Environment.log(Environment.LOG_LEVEL_FINER, "method call", getClass().getName(), "doContextChanges()");
		
		MapState mapState = logicalNetLayer.getMapState();

		int mouseMode = mapState.getMouseMode();
		int actionMode = mapState.getActionMode();

		if(SwingUtilities.isLeftMouseButton(me))
		{
			if(mouseMode == MapState.MOUSE_PRESSED)
			{
				if ((actionMode == MapState.SELECT_ACTION_MODE))
				{
					MapElement mel = logicalNetLayer.getCurrentMapElement();
					if(mel instanceof Selection)
					{
						Selection sel = (Selection)mel;
						sel.add(link);
					}
					else
					{
						Selection sel = new Selection(logicalNetLayer.getMapView().getMap());
						sel.addAll(logicalNetLayer.getSelectedElements());
						logicalNetLayer.setCurrentMapElement(sel);
					}
				}
				if ((actionMode != MapState.SELECT_ACTION_MODE) &&
					(actionMode != MapState.MOVE_ACTION_MODE) )
				{
					logicalNetLayer.deselectAll();
				}
				link.setSelected(true);
			}
		}
	}
}

