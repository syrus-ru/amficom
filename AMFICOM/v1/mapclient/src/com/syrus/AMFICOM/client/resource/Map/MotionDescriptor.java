/**
 * $Id: MotionDescriptor.java,v 1.2 2004/11/16 17:30:26 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Resource.Map;

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
 * @version $Revision: 1.2 $, $Date: 2004/11/16 17:30:26 $
 * @module
 * @author $Author: krupenn $
 * @see
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

	public MotionDescriptor(
		Point startPoint,
		Point endPoint,
		Point thisPoint,
		Point mousePoint)
	{

		nodeLinkLength = Math.sqrt( 
			(endPoint.x - startPoint.x) * (endPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (endPoint.y - startPoint.y) );

		sinB = (endPoint.y - startPoint.y) / nodeLinkLength;

		cosB = (endPoint.x - startPoint.x) / nodeLinkLength;

		lengthFromStartNode = Math.sqrt( 
			(thisPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(thisPoint.y - startPoint.y) * (thisPoint.y - startPoint.y) );
			
		lengthThisToMousePoint = Math.sqrt( 
			(mousePoint.x - thisPoint.x) * (mousePoint.x - thisPoint.x) +
			(mousePoint.y - thisPoint.y) * (mousePoint.y - thisPoint.y) );

		cosA = (lengthThisToMousePoint == 0 ) ? 0.0 :
			(	(endPoint.x - startPoint.x) * (mousePoint.x - thisPoint.x) + 
				(endPoint.y - startPoint.y) * (mousePoint.y - thisPoint.y) ) /
			( nodeLinkLength * lengthThisToMousePoint );

		// ��������� ������������ �������� ��������� � lengthFromStartNode
		double scalar = (endPoint.x - startPoint.x) * (thisPoint.x - startPoint.x) +
			(endPoint.y - startPoint.y) * (thisPoint.y - startPoint.y);

		if(scalar < 0)
			lengthFromStartNode = -lengthFromStartNode;

		lengthFromStartNode = lengthFromStartNode + cosA * lengthThisToMousePoint;
	}
}