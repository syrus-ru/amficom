package com.syrus.AMFICOM.Client.Resource.Map;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.Strategy.MapStrategy;
import com.syrus.AMFICOM.Client.Map.Strategy.VoidStrategy;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

//A0A
public class MapEventMarker extends MapMarker
{
	static final public String typ = "mapeventmarker";

	public Object descriptor;

	public MapEventMarker(
			String myID, 
			MapContext myMapContext,
			Rectangle myBounds, 
			String myImageID,
			double mylen, 
			MapTransmissionPathElement path)
	{
		super(myID, myMapContext, myBounds, myImageID, mylen, path);

		setImageID("images/eventmarker.gif");
		this.name = "Событие";
	}

	public boolean isMovable()
	{
		return false;
	}

	public String getToolTipText()
	{
		String s1 = "Событие " + getName() + " (путь " + transmissionPath.getName() + ")";

		return s1;
	}

	public MapStrategy getMapStrategy(
			ApplicationContext aContext,
			LogicalNetLayer logicalNetLayer,
			MouseEvent me,
			Point sourcePoint)
	{
		int mode = getLogicalNetLayer().getMode();
		int actionMode = logicalNetLayer.getActionMode();

		MapStrategy strategy = new VoidStrategy();
		Point myPoint = me.getPoint();

		if(SwingUtilities.isLeftMouseButton(me))
		{
//A0A
			if ((actionMode != LogicalNetLayer.SELECT_ACTION_MODE) &&
				(actionMode != LogicalNetLayer.MOVE_ACTION_MODE) )
			{
				logicalNetLayer.getMapContext().deselectAll();
			}
			select();

		}//SwingUtilities.isLeftMouseButton(me)
		return strategy;
	}

	public String getTyp()
	{
		return typ;
	}

}