/*-
 * $$Id: MoveSelectionCommandBundle.java,v 1.26 2005/10/26 14:17:34 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.command.action;

import java.awt.Point;
import java.util.Iterator;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapException;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.AMFICOM.map.Mark;
import com.syrus.AMFICOM.resource.DoublePoint;
import com.syrus.util.Log;

/**
 * ѕеремещение объектов по карте.  оманда €вл€етс€ пучком команд 
 * (CommandBundle), передвгающих отдельные элементы.
 * 
 * @version $Revision: 1.26 $, $Date: 2005/10/26 14:17:34 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MoveSelectionCommandBundle extends MapActionCommandBundle {
	/** конечна€ точка сдвига */
	public static final String END_POINT = "endpoint"; //$NON-NLS-1$
	/** начальга€ точка сдвига */
	public static final String START_POINT = "startpoint"; //$NON-NLS-1$
	/** абсолютное смещение по оси абсцисс */
	public static final String DELTA_X = "deltax"; //$NON-NLS-1$
	/** абсолютное смещение по оси ординат */
	public static final String DELTA_Y = "deltay"; //$NON-NLS-1$

	/**
	 * начальна€ точка смещени€
	 */
	Point startPoint;
	
	/**
	 * конечна€ точка смещени€
	 */
	Point endPoint;
	
	/**
	 * абсолютное смещение по оси абсцисс
	 */
	double deltaX = 0.0D;
	
	/**
	 * абсолютное смещение по оси ординат
	 */
	double deltaY = 0.0D;

	public MoveSelectionCommandBundle(Point point) {
		this.startPoint = point;
	}

	public MoveSelectionCommandBundle(NetMapViewer netMapViewer) {
		super();
		setNetMapViewer(netMapViewer);
	}

	/**
	 * при установке параметров перемещени€ параметры передаютс€ всем командам в
	 * пучке
	 */
	@Override
	public void setParameter(String field, Object value) {
		if(field.equals(DELTA_X)) {
			this.deltaX = Double.parseDouble((String) value);
			super.setParameter(field, value);
		} else if(field.equals(DELTA_Y)) {
			this.deltaY = Double.parseDouble((String) value);
			super.setParameter(field, value);
		} else if(field.equals(START_POINT)) {
			this.startPoint = (Point) value;
			this.endPoint = (Point) value;
			// пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
		} else if(field.equals(END_POINT)) {
			this.endPoint = (Point) value;
			// пересчитать смещение
			this.setShift();
			super.setParameter(DELTA_X, String.valueOf(this.deltaX));
			super.setParameter(DELTA_Y, String.valueOf(this.deltaY));
		}

	}

	/**
	 * при установке логического сетевого сло€ оздаютс€ команды на перемещение
	 * выбранных объектов. выполнение удалени€ осуществл€етс€ только при вызове
	 * execute()
	 */
	@Override
	public void setNetMapViewer(NetMapViewer netMapViewer) {
		super.setNetMapViewer(netMapViewer);

		this.setElements();
	}

	/**
	 * обновить абсолютное смещение по начальной и конечной точкам сдвига
	 */
	protected void setShift() {
		try {
			DoublePoint sp = this.logicalNetLayer.getConverter().convertScreenToMap(this.startPoint);
			DoublePoint ep = this.logicalNetLayer.getConverter().convertScreenToMap(this.endPoint);
			this.deltaX = ep.getX() - sp.getX();
			this.deltaY = ep.getY() - sp.getY();
		} catch(MapException e) {
			// TODO Auto-generated catch block
			Log.debugException(e, Level.SEVERE);
		}
	}
	
	/**
	 * создать отдельные команды на перемещение дл€ всех выделенных
	 * точечных объектов
	 */
	protected void setElements() {
		Map map = this.logicalNetLayer.getMapView().getMap();
		Iterator iter = map.getSelectedElements().iterator();

		while (iter.hasNext() ) {
			MapElement mapElement = (MapElement )iter.next();
			if(mapElement instanceof AbstractNode) {
				AbstractNode node = (AbstractNode)mapElement;
				if(node instanceof Mark) {
					Mark mme = (Mark)node;
					super.add(new MoveMarkCommand(mme));
				}
				// do not move external nodes
				else if(!map.getExternalNodes().contains(node))
					super.add(new MoveNodeCommand(node));
			}
		}
	}
	
	@Override
	public void execute() {
		super.execute();
	}
}
