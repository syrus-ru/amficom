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
 * startPoint, endPoint - �������� ���� nodelink, �� �������� �������� ������
 * (�����)
 * thisPoint - ������� �������������� ������� �� nodeLink
 * mousePoint - �����, ���� ���������� ��������� �����. � ����������� � ����
 * ������������ ������ ������ ������������� ����� nodeLink.
 * ��������� � ���������� ��������� ������� ���������� ���� ������������� 
 * ��������� ������� ���� �� �����, �� ������� ������ ���������
 * <pre>
 * 
 *                                           *(endPoint)
 *  
 *                         *(mousePoint)
 *                           *(�������������� ����� �������������� �������)
 *                      *(thisPoint)
 *  
 *  
 *     *(startPoint)
 * </pre>
 * 	B - ���� ������� ������� (startPoint, endPoint)
 *  A - ���� ����� ��������� (startPoint, endPoint) �  (thisPoint, mousePoint)
 * 
 * @version $Revision: 1.6 $, $Date: 2005/09/30 16:08:42 $
 * @author $Author: krupenn $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public class MotionDescriptor  {
	/**
	 * ������� ���� ������� ��������� ����� � �������� ���������
	 */
	public double cosB;
	/**
	 * ����� ���� ������� ��������� ����� � �������� ���������
	 */
	public double sinB;
	/**
	 * ���������� �� �������� ��������� ������� �� ����� ����������� 
	 * ��������� ���� � �������� �����������
	 */
	public double lengthThisToMousePoint;
	/**
	 * ������� ���� ����� ���������� ����� � �������� ����������� �������
	 * (������ ����� ������� ���������� ������� � ������ ����������� 
	 * ��������� ����) � �������� ���������
	 */
	public double cosA;
	/**
	 * ���������� �� ���������� ���� ��������� �� ��������������� ������
	 * ��������� ������� �� ��������� � �������� �����������
	 */
	public double lengthFromStartNode;
	/**
	 * ����� ��������� � �������� �����������
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

		// ��������� ������������ �������� ��������� � lengthFromStartNode
		double scalar = (endPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (thisPoint.y - startPoint.y);

		if(scalar < 0)
			this.lengthFromStartNode = -this.lengthFromStartNode;

		this.lengthFromStartNode = this.lengthFromStartNode + this.cosA * this.lengthThisToMousePoint;
	}
}
