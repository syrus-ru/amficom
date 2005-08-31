/*
 * $Id: ReportLayout.java,v 1.2 2005/08/31 10:32:54 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

/**
 * ����� �������� ��������� ��������� ����������� � ��������������
 * ���������� ������ ���, ����� ���������� ����� ������������ ����
 * ����� ���������� ����� ������������ �� ����� ������� ������.
 * @author $Author: peskovsky $
 * @version $Revision: 1.2 $, $Date: 2005/08/31 10:32:54 $
 * @module reportclient_v1
 */
public class ReportLayout {
	private RenderingComponentsContainer componentContainer = null;
	private final Map<RenderingComponent,Boolean> componentsSetUp = new HashMap<RenderingComponent,Boolean>();
	
	public void dolayout (RenderingComponentsContainer compContainer)
	{
		this.componentContainer = compContainer;
		//�������������� ������� "�������������" - � ��� �����������,
		//���������� ������� ����������� �� ��� ����� ��� ��� ���.
		for (RenderingComponent component : this.componentContainer.getRenderingComponents())
			this.componentsSetUp.put(component,Boolean.FALSE);
			
		//�������� ������������� ������ �������� X � Y ��� ����� � ������
		//��������� �����������
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		this.getAxisValuesMatrices(xs,ys);
		
		//���� �� ����� ��������� ���������
		while (!this.layoutIsFinished()){
			//�������� "�������" ��������� �������
			for (RenderingComponent component : this.componentContainer.getRenderingComponents()){
				try {
					int newY = this.checkToTopForElements(component,xs,ys);
					component.setY(newY);
				} catch (NonImplementedElementFoundException e) {
				}
			}
		}
	}

	/**
	 * ������������� ��� ���������� Y ���������� ��� �������� �����������, 
	 * � ��� ������, ����� ��������, ����������� ���� �� �����, ��� ��
	 * �����������.
	 * @author $Author: peskovsky $
	 * @version $Revision: 1.2 $, $Date: 2005/08/31 10:32:54 $
	 * @module reportclient_v1
	 */
	private class NonImplementedElementFoundException extends Exception
	{
		private static final long serialVersionUID = 3269694826007469180L;
	}
	
	/**
	 * ���������� �������� (y + height + dist) ��� ���������� � elem �������� ������?
	 * ��� dist - ���������� �� ����� �� elem �� ����� �������
	 * 0, � ������ ���� �������� ���� ���
	 * � -1, ���� ���� elem ���� ��������������� ��������
	 * @param component ��������������� ������� �����������
	 * @param xs ������ �������� x � x + width ��� ���� ��������
	 * @param ys ������ �������� y � y + height ��� ���� ��������
	 * @return �������� y ��� ���������� �������� �������
	 */
	private int checkToTopForElements(
			RenderingComponent component,
			List<Integer> xs,
			List<Integer> ys) throws NonImplementedElementFoundException{
		StorableElement element  = component.getElement();
		
		//������� ������� ��������� �� ������� �� ������� �� ��������� �������
		//�������� ������
		/**
		 * �������� Y ��� ������� ���� ��������, ���������� ������ � ����������
		 */
		int theLowestEdgeValue = -1;
		/**
		 * ������� �����������, ��������� ������ � ����������
		 */
		RenderingComponent theLowestComponent = null; 
		
		//�������� ����� � �������� ������ (� ������� "�����", ����������
		//xs,ys)
		for (int i = 0; i < xs.size() - 1; i++) {
			/**
			 * �������� ������� ������ ��������� ������
			 */
			int cellMiddleX = (xs.get(i).intValue() + xs.get(i + 1).intValue()) / 2;

			if (!	(	(element.getX() <= cellMiddleX)
					&&	(cellMiddleX <= element.getX() + element.getWidth())))
				//���� ����� ��������� �� ��� ��������� ��������� � ����������
				//"�������" ����� ����� 				
				continue;

			//���������� "����"
			for (int j = 0; j < ys.size() - 1; j++) {
				/**
				 * �������� �������� ������ ��������� ������
				 */
				int cellMiddleY = (ys.get(j).intValue() + ys.get(j + 1).intValue()) / 2;
				if (cellMiddleY >= element.getY())
					//���� ���������� ���� �� ������ �������� ��������
					break;

				//���� � ������ ������ �����-���� ������� �����������
				RenderingComponent componentFound = this.getComponentAtPoint(this.componentContainer,cellMiddleX, cellMiddleY);
				
				if (componentFound == null)
					//�� ����� - �������� ������ ����
					continue;
				
				//�����
				if (this.componentsSetUp.get(componentFound).equals(Boolean.FALSE))
					//���� ���� ������� ������� ��������� ��������������
					//������� �����������
					throw new NonImplementedElementFoundException();

				/**
				 * ���������� Y ������� ���� ���������� ����������
				 */
				int componentFoundBottomY = 0;

				if (componentFound instanceof DataRenderingComponent)
				{
					Rectangle compBounds = this.getDataComponentsClasterBounds(
							(DataRenderingComponent) componentFound);
					componentFoundBottomY = (int)compBounds.getMaxY();
				}
				else
					componentFoundBottomY = componentFound.getY() + componentFound.getHeight();
				
				if (componentFoundBottomY > theLowestEdgeValue) {
					theLowestEdgeValue = componentFoundBottomY;					
					theLowestComponent = componentFound;
				}
			}
		}

		//���� ���� �� ����� �� ������ �������
		if (theLowestEdgeValue == -1)
			return element.getY();

		StorableElement lowestElement = theLowestComponent.getElement();
		int yDistanceAtScheme = component.getElement().getY()
			- (lowestElement.getY() + lowestElement.getHeight());
		
		return theLowestEdgeValue + yDistanceAtScheme;
	}
	
	/**
	 * ���������� ������������� ������ xs � ys ����� � ������
	 * ��������� ����������� �� x,y.
	 * @param xs ������ �������� x � x + width ��� ���� ��������
	 * @param ys ������ �������� y � y + height ��� ���� ��������
	 */
	private void getAxisValuesMatrices(
			List<Integer> xs,
			List<Integer> ys) {
		//��� �������� �������� ������ ������������ ������, ��������� �� ��
		//����� ������� ����� ������� ���������, � ����� ��� (�� ��������
		//��������� ������ ���������� ������������� ��������) - �� ����� �������
		//�������� ����� � �������.
		for (RenderingComponent component : this.componentContainer.getRenderingComponents()){
			StorableElement element = component.getElement();
			
			if (element instanceof AttachedTextStorableElement)
			{
				//�� ��������� ���������� ������ ������������� � �������� ��������
				AttachedTextStorableElement atElement =
					(AttachedTextStorableElement) element;
				
				if (	!atElement.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)
					||	!atElement.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT))
					continue;
				
				xs.add(new Integer(element.getX()));
				ys.add(new Integer(element.getY()));

				xs.add(new Integer(element.getX() + element.getWidth()));
				ys.add(new Integer(element.getY() + element.getHeight()));
			}
			else if (element instanceof DataStorableElement)
			{
				Rectangle borders = this.componentContainer.getReportTemplate().
					getElementClasterBounds((DataStorableElement)element);
				
				xs.add(new Integer(borders.x));
				ys.add(new Integer(borders.y));

				xs.add(new Integer(borders.x + borders.width));
				ys.add(new Integer(borders.y + borders.height));
			}
			else if (element instanceof ImageStorableElement)
			{
				xs.add(new Integer(element.getX()));
				ys.add(new Integer(element.getY()));

				xs.add(new Integer(element.getX() + element.getWidth()));
				ys.add(new Integer(element.getY() + element.getHeight()));
			}
			
			Collections.sort(xs);
			Collections.sort(ys);			
		}
	}

	/**
	 * �������������� ����� �������� ������� � �������� �����.
	 * @param x �������� �� x ��������������� �����
	 * @param y �������� �� y ��������������� �����
	 * @return ���������� 
	 *  -1, ���� � ���� ����� ��� �������� �������;
	 *  -2, ���� ����, �� �� ��� �� ���������� � ���������������� ��� �����.
	 */
	private RenderingComponent getComponentAtPoint(
			RenderingComponentsContainer componentsContainer,
			int x,
			int y) {
		ReportTemplate reportTemplate = componentsContainer.getReportTemplate();
		List<RenderingComponent> reportComponents = componentsContainer.getRenderingComponents();
		
		for (RenderingComponent component : reportComponents) {
			StorableElement element = component.getElement();
			if (element instanceof AttachedTextStorableElement)
			{
				//����������� ������� �� ���������
				AttachedTextStorableElement atElement =
					(AttachedTextStorableElement) element;
				
				if (	!atElement.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)
					||	!atElement.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT))
					continue;
				
				if (element.hasPoint(x,y))
					return component;
			}
			else if (element instanceof DataStorableElement)
			{
				//�������: ����������� �� ����� �������� - �������, � �������
				//������� ������� ����������� ������ � ����������� � ���� �������
				DataStorableElement dsElement =	(DataStorableElement) element;
				if (reportTemplate.clasterContainsPoint(dsElement,x,y))
					return component;
			}
			else if (element instanceof ImageStorableElement)
			{
				if (element.hasPoint(x,y))
					return component;
			}
		}
		return null;
	}
	
	/**
	 * ���������� ������� �������� ��� ��� �������������� �������� �������
	 * � ���������.
	 * @param component
	 * @return �������� ��������
	 */
	private Rectangle getDataComponentsClasterBounds(DataRenderingComponent component)
	{
		Rectangle bounds = this.componentContainer.getReportTemplate().
			getElementClasterBounds((DataStorableElement)component.getElement());
		
		bounds.x = component.getX();
		bounds.y = component.getY();
		bounds.height += component.getHeight() - component.getElement().getHeight();
		return bounds;
	}
	
	/**
	 * @return true, ���� ��������� ����������� ���������.
	 */
	private boolean layoutIsFinished(){
		for (Boolean value : this.componentsSetUp.values())
			if (value.equals(Boolean.FALSE))
				return false;
		
		return true;
	}
}
