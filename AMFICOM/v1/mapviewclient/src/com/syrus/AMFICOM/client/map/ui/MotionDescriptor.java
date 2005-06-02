/**
 * $Id: MotionDescriptor.java,v 1.3 2005/02/10 11:48:40 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import java.awt.Point;

/**
 * startPoint, endPoint - �������� ���� nodelink, �� �������� �������� ������
 * (�����)
 * thisPoint - ������� �������������� ������� �� nodeLink
 * mousePoint - �����, ���� ���������� ��������� �����. � ����������� � ����
 * ������������ ������ ������ ������������� ����� nodeLink.
 * ��������� � ���������� ��������� ������� ���������� ���� ������������� 
 * ��������� ������� ���� �� �����, �� ������� ������ ���������
 * 
 * 
 *                                           *(endPoint)
 *  
 *                         *(mousePoint)
 *                           *(�������������� ����� �������������� �������)
 *                      *(thisPoint)
 *  
 *  
 *     *(startPoint)
 * 
 * 	B - ���� ������� ������� (startPoint, endPoint)
 *  A - ���� ����� ��������� (startPoint, endPoint) �  (thisPoint, mousePoint)
 * 
 * 
 * @version $Revision: 1.3 $, $Date: 2005/02/10 11:48:40 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public class MotionDescriptor 
{
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

		// ��������� ������������ �������� ��������� � lengthFromStartNode
		double scalar = (endPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (thisPoint.y - startPoint.y);

		if(scalar < 0)
			this.lengthFromStartNode = -this.lengthFromStartNode;

		this.lengthFromStartNode = this.lengthFromStartNode + this.cosA * this.lengthThisToMousePoint;
	}
}
