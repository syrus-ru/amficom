/*-
 * $$Id: MotionDescriptor.java,v 1.6 2005/09/30 16:08:42 krupenn Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Point;

/**
 * startPoint, endPoint - концевые узлы nodelink, по которому движетс€ маркер
 * (метка)
 * thisPoint - текущее местоположение маркера на nodeLink
 * mousePoint - точка, куда передвинут указатель мышки. в соответстии с этим
 * перемещением маркер должен переместитьс€ вдоль nodeLink.
 * –исование о пределение координат маркера происходит путм проецировани€ 
 * координат курсора мыши на линию, на которой маркер находитс€
 * <pre>
 * 
 *                                           *(endPoint)
 *  
 *                         *(mousePoint)
 *                           *(предполагаемое новое местоположение маркера)
 *                      *(thisPoint)
 *  
 *  
 *     *(startPoint)
 * </pre>
 * 	B - угол наклона вектора (startPoint, endPoint)
 *  A - угол медлу векторами (startPoint, endPoint) и  (thisPoint, mousePoint)
 * 
 * @version $Revision: 1.6 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MotionDescriptor  {
	/**
	 * косинус угла наклона фрагмента линии в экранной плоскости
	 */
	public double cosB;
	/**
	 * синус угла наклона фрагмента линии в экранной плоскости
	 */
	public double sinB;
	/**
	 * рассто€ние от текущего положени€ маркера до точки перемещени€ 
	 * указател€ мыши в экранных координатах
	 */
	public double lengthThisToMousePoint;
	/**
	 * косинус угла можду фрагментом линии и вектором перемещени€ маркера
	 * (вектор между текущим положением маркера и точкой перемещени€ 
	 * указател€ мыши) в экранной плоскости
	 */
	public double cosA;
	/**
	 * рассто€ние от начального узла фрагмента до предполагаемого нового
	 * положени€ маркера на фрагмента в экранных координатах
	 */
	public double lengthFromStartNode;
	/**
	 * длина фрагмента в экранных координатах
	 */
	public double nodeLinkLength;

	public MotionDescriptor(
		Point startPoint,
		Point endPoint,
		Point thisPoint,
		Point mousePoint) {

		this.nodeLinkLength = Math.sqrt( 
			(endPoint.x - startPoint.x) * (endPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (endPoint.y - startPoint.y) );

		this.sinB = (endPoint.y - startPoint.y) / this.nodeLinkLength;

		this.cosB = (endPoint.x - startPoint.x) / this.nodeLinkLength;

		this.lengthFromStartNode = Math.sqrt( 
			(thisPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(thisPoint.y - startPoint.y) * (thisPoint.y - startPoint.y) );
			
		this.lengthThisToMousePoint = Math.sqrt( 
			(mousePoint.x - thisPoint.x) * (mousePoint.x - thisPoint.x) +
			(mousePoint.y - thisPoint.y) * (mousePoint.y - thisPoint.y) );

		this.cosA = (this.lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endPoint.x - startPoint.x) * (mousePoint.x - thisPoint.x) + 
				(endPoint.y - startPoint.y) * (mousePoint.y - thisPoint.y) ) /
			( this.nodeLinkLength * this.lengthThisToMousePoint );

		// скал€рное произведение векторов фрагмента и lengthFromStartNode
		double scalar = (endPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (thisPoint.y - startPoint.y);

		if(scalar < 0)
			this.lengthFromStartNode = -this.lengthFromStartNode;

		this.lengthFromStartNode = this.lengthFromStartNode + this.cosA * this.lengthThisToMousePoint;
	}
}
