/**
 * $Id: MotionDescriptor.java,v 1.1 2004/11/10 15:58:30 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

import java.awt.Point;

/**
 * startPoint, endPoint - концевые узлы nodelink, по которому движется маркер
 * (метка)
 * thisPoint - текущее местоположение маркера на nodeLink
 * mousePoint - точка, куда передвинут указатель мышки. в соответстии с этим
 * перемещением маркер должен переместиться вдоль nodeLink.
 * Рисование о пределение координат маркера происходит путм проецирования 
 * координат курсора мыши на линию, на которой маркер находится
 * 
 * 
 *                                           *(endPoint)
 *  
 *                         *(mousePoint)
 *                           *(предполагаемое новое местоположение маркера)
 *                      *(thisPoint)
 *  
 *  
 *     *(startPoint)
 * 
 * 	B - угол наклона вектора (startPoint, endPoint)
 *  A - угол медлу векторами (startPoint, endPoint) и  (thisPoint, mousePoint)
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/11/10 15:58:30 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MotionDescriptor 
{
	public double cosB;
	public double sinB;
	public double lengthThisToMousePoint;
	public double cosA;
	public double lengthFromStartNode;
	public double nodeLinkLength;

	public MotionDescriptor(
			double cb, 
			double sb, 
			double lt, 
			double ca, 
			double lf, 
			double nl)
	{
		cosB = cb;
		sinB = sb;
		lengthThisToMousePoint = lt;
		cosA = ca;
		lengthFromStartNode = lf;
		nodeLinkLength = nl;
	}

	public MotionDescriptor(
		Point startPoint,
		Point endPoint,
		Point thisPoint,
		Point mousePoint)
	{

		nodeLinkLength = Math.sqrt( 
			(endPoint.x - startPoint.x) * (endPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (endPoint.y - startPoint.y) );

		lengthFromStartNode = Math.sqrt( 
			(thisPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(thisPoint.y - startPoint.y) * (thisPoint.y - startPoint.y) );

		sinB =  (endPoint.y - startPoint.y) / nodeLinkLength;

		cosB =  (endPoint.x - startPoint.x) / nodeLinkLength;

		lengthThisToMousePoint = Math.sqrt( 
			(mousePoint.x - thisPoint.x) * (mousePoint.x - thisPoint.x) +
			(mousePoint.y - thisPoint.y) * (mousePoint.y - thisPoint.y) );

		cosA = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endPoint.x - startPoint.x) * (mousePoint.x - thisPoint.x) + 
				(endPoint.y - startPoint.y) * (mousePoint.y - thisPoint.y) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		lengthFromStartNode = lengthFromStartNode + cosA * lengthThisToMousePoint;
	}
}