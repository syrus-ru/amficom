/**
 * $Id: MotionDescriptor.java,v 1.3 2005/02/10 11:48:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

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
 * @version $Revision: 1.3 $, $Date: 2005/02/10 11:48:40 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MotionDescriptor 
{
	/**
	 * косинус угла наклона фрагмента линии в экранной плоскости
	 */
	public double cosB;
	/**
	 * синус угла наклона фрагмента линии в экранной плоскости
	 */
	public double sinB;
	/**
	 * расстояние от текущего положения маркера до точки перемещения 
	 * указателя мыши в экранных координатах
	 */
	public double lengthThisToMousePoint;
	/**
	 * косинус угла можду фрагментом линии и вектором перемещения маркера
	 * (вектор между текущим положением маркера и точкой перемещения 
	 * указателя мыши) в экранной плоскости
	 */
	public double cosA;
	/**
	 * расстояние от начального узла фрагмента до предполагаемого нового
	 * положения маркера на фрагмента в экранных координатах
	 */
	public double lengthFromStartNode;
	/**
	 * длина фрагмента в экранных координатах
	 */
	public double nodeLinkLength;
/*
	private MotionDescriptor(
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
*/
	public MotionDescriptor(
		Point startPoint,
		Point endPoint,
		Point thisPoint,
		Point mousePoint)
	{

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

		// скалярное произведение векторов фрагмента и lengthFromStartNode
		double scalar = (endPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (thisPoint.y - startPoint.y);

		if(scalar < 0)
			this.lengthFromStartNode = -this.lengthFromStartNode;

		this.lengthFromStartNode = this.lengthFromStartNode + this.cosA * this.lengthThisToMousePoint;
	}
}
