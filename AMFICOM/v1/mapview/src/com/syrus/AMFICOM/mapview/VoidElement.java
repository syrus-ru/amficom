/**
 * $Id: VoidElement.java,v 1.3 2005/02/02 15:17:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.mapview;

import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.MapElementState;
import com.syrus.AMFICOM.mapview.MapView;

import java.util.List;

/**
 * Пустой элемент.
 * 
 * @author $Author: krupenn $
 * @version $Revision: 1.3 $, $Date: 2005/02/02 15:17:30 $
 * @module mapviewclient_v1
 */
public final class VoidElement implements MapElement 
{
	/**
	 * Вид карты.
	 */
	protected MapView mapView;
	
	/**
	 * Instance.
	 */
	protected static VoidElement singleton = new VoidElement();

	/**
	 * Protected constructor.
	 */
	protected VoidElement()
	{
	}

	/**
	 * Get instance.
	 * @param mapView вид карты
	 * @return instance
	 */
	public static VoidElement getInstance(MapView mapView)
	{
		singleton.mapView = mapView;
		return singleton;
	}

	/**
	 * {@inheritDoc} 
	 */
	public Map getMap()
	{
		return this.mapView.getMap();
	}

	/**
	 * Получить вид карты.
	 * @return вид карты
	 */
	public MapView getMapView()
	{
		return this.mapView;
	}

	/**
	 * {@inheritDoc} 
	 */
	public String getToolTipText()
	{
		return getMapView().getName();
	}

	/**
	 * {@inheritDoc} 
	 */
	public boolean isSelected()
	{
		return true;
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public MapElementState getState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setSelected(boolean selected)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setAlarmState(boolean alarmState)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public boolean getAlarmState()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public Identifier getId()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public String getName()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setName(String name)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public DoublePoint getLocation()
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setMap(Map mc)
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public List getCharacteristics() 
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void addCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void removeCharacteristic(Characteristic ch)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void revert(MapElementState state)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public boolean isRemoved()
	{
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public void setRemoved(boolean removed)
	{
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * Suppress since this class is transient 
	 */
	public java.util.Map getExportMap()
	{
		throw new UnsupportedOperationException();
	}
}
