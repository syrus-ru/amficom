package com.syrus.AMFICOM.Client.Map;

import java.awt.Point;
import java.awt.geom.Point2D;

public interface MapCoordinatesConverter 
{
	Point convertMapToScreen(Point2D.Double point);
	Point2D.Double convertScreenToMap(Point point);
	double distance(Point2D.Double from, Point2D.Double to);
}
