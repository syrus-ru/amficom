/*
 * $Id: ReportLayout.java,v 1.9 2005/11/16 18:53:17 max Exp $
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

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.report.AbstractDataStorableElement;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.ReportTemplate;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

/**
 * ����� �������� ��������� ��������� ����������� � ��������������
 * ���������� ������ ���, ����� ���������� ����� ������������ ����
 * ����� ���������� ����� ������������ �� ����� ������� ������.
 * @author $Author: max $
 * @version $Revision: 1.9 $, $Date: 2005/11/16 18:53:17 $
 * @module reportclient
 */
public class ReportLayout {
	private List<RenderingComponent> componentContainer = null;
	private ReportTemplate reportTemplate = null;
	
	private final Map<RenderingComponent,Boolean> componentsSetUp = new HashMap<RenderingComponent,Boolean>();
	
	public void dolayout (List<RenderingComponent> container,ReportTemplate template) throws ApplicationException
	{
		this.componentContainer = container;
		this.reportTemplate = template;		
		
		//�������������� ������� "�������������" - � ��� �����������,
		//���������� ������� ����������� �� ��� ����� ��� ��� ���.
		for (RenderingComponent component : this.componentContainer)
			this.componentsSetUp.put(component,Boolean.FALSE);
			
		//�������� ������������� ������ �������� X � Y ��� ����� � ������
		//��������� �����������
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		this.getAxisValuesMatrices(xs,ys);
		
		//���� �� ����� ��������� ���������
		while (!this.layoutIsFinished()){
			//�������� "�������" ��������� �������
			for (RenderingComponent component : this.componentContainer){
				if (this.componentsSetUp.get(component).equals(Boolean.FALSE))
					try {
						int newY = -1;
						if (component instanceof AttachedTextComponent) {
							//����������� ������� ������ ����� ����, ��� ������������
							//�������, � ������� ��� ���������
							AttachedTextStorableElement element =
								(AttachedTextStorableElement)component.getElement();
							TextAttachingType vertAttachingType = element.getVerticalAttachType();
							
							if (!vertAttachingType.equals(TextAttachingType.TO_FIELDS_TOP)) {
								RenderingComponent vertAttacherComponent =
									this.getComponentForElement(element.getVerticalAttacher());
								if (this.componentsSetUp.get(vertAttacherComponent).equals(Boolean.FALSE))
									continue;
								if (vertAttachingType.equals(TextAttachingType.TO_TOP))
									newY = vertAttacherComponent.getY() + element.getDistanceY();
								else
									newY = vertAttacherComponent.getY()
										+ vertAttacherComponent.getHeight() + element.getDistanceY();
							}
							else
								newY = this.checkToTopForElements(component,xs,ys);
						}
						else
							newY = this.checkToTopForElements(component,xs,ys);
						
						component.setY(newY);
						this.componentsSetUp.put(component,Boolean.TRUE);
					} catch (NonImplementedElementFoundException e) {
					}
			}
		}
	}

	/**
	 * ������������� ��� ���������� Y ���������� ��� �������� �����������, 
	 * � ��� ������, ����� ��������, ����������� ���� �� �����, ��� ��
	 * �����������.
	 * @author $Author: max $
	 * @version $Revision: 1.9 $, $Date: 2005/11/16 18:53:17 $
	 * @module reportclient
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
	 * @throws ApplicationException 
	 */
	private int checkToTopForElements(
			RenderingComponent component,
			List<Integer> xs,
			List<Integer> ys) throws NonImplementedElementFoundException, ApplicationException{
		StorableElement element  = component.getElement();
		int elemMinX = element.getX();
		int elemMaxX = element.getX() + element.getWidth();
		int elementMinY = element.getY();
		if (element instanceof AbstractDataStorableElement) {
			//�������: ����������� �� ����� �������� - �������, � �������
			//������� ������� ����������� ������ � ����������� � ���� �������
			AbstractDataStorableElement dsElement =	(AbstractDataStorableElement) element;
			Rectangle bounds = this.reportTemplate.getElementClasterBounds(dsElement);
			elementMinY = bounds.y;
		}
		
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

			if (!((elemMinX <= cellMiddleX) &&	(cellMiddleX <= elemMaxX)))
				//���� ����� ��������� �� ��� ��������� ��������� � ����������
				//"�������" ����� ����� 				
				continue;

			//���������� "����"
			for (int j = 0; j < ys.size() - 1; j++) {
				/**
				 * �������� �������� ������ ��������� ������
				 */
				int cellMiddleY = (ys.get(j).intValue() + ys.get(j + 1).intValue()) / 2;
				if (cellMiddleY >= elementMinY)
					//���� ���������� ���� �� ������ �������� ��������
					break;

				//���� � ������ ������ �����-���� ������� �����������
				RenderingComponent componentFound = this.getComponentAtPoint(cellMiddleX, cellMiddleY);
				
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

				if (	(componentFound instanceof DataRenderingComponent)
					&& !(componentFound instanceof ImageRenderingComponent))
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
			return elementMinY;

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
	 * @throws ApplicationException 
	 */
	private void getAxisValuesMatrices(
			List<Integer> xs,
			List<Integer> ys) throws ApplicationException {
		//��� �������� �������� ������ ������������ ������, ��������� �� ��
		//����� ������� ����� ������� ���������, � ����� ��� (�� ��������
		//��������� ������ ���������� ������������� ��������) - �� ����� �������
		//�������� ����� � �������.
		for (RenderingComponent component : this.componentContainer) {
			StorableElement element = component.getElement();
			
			if (element instanceof AttachedTextStorableElement) {
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
			else if (element instanceof AbstractDataStorableElement) {
				Rectangle borders = this.reportTemplate.getElementClasterBounds(
						(AbstractDataStorableElement)element);
				
				xs.add(new Integer(borders.x));
				ys.add(new Integer(borders.y));

				xs.add(new Integer(borders.x + borders.width));
				ys.add(new Integer(borders.y + borders.height));
			}
			else if (element instanceof ImageStorableElement) {
				xs.add(new Integer(element.getX()));
				ys.add(new Integer(element.getY()));

				xs.add(new Integer(element.getX() + element.getWidth()));
				ys.add(new Integer(element.getY() + element.getHeight()));
			}
		}
		Collections.sort(xs);
		Collections.sort(ys);			
	}

	/**
	 * �������������� ����� �������� ������� � �������� �����.
	 * @param x �������� �� x ��������������� �����
	 * @param y �������� �� y ��������������� �����
	 * @return ���������� 
	 *  -1, ���� � ���� ����� ��� �������� �������;
	 *  -2, ���� ����, �� �� ��� �� ���������� � ���������������� ��� �����.
	 * @throws ApplicationException 
	 */
	private RenderingComponent getComponentAtPoint(int x,int y) throws ApplicationException {
		for (RenderingComponent component : this.componentContainer) {
			StorableElement element = component.getElement();
			if (element instanceof AttachedTextStorableElement)	{
				//����������� ������� �� ���������
				AttachedTextStorableElement atElement =
					(AttachedTextStorableElement) element;
				
				if (	!atElement.getVerticalAttachType().equals(TextAttachingType.TO_FIELDS_TOP)
					||	!atElement.getHorizontalAttachType().equals(TextAttachingType.TO_FIELDS_LEFT))
					continue;
				
				if (element.hasPoint(x,y))
					return component;
			}
			else if (element instanceof AbstractDataStorableElement) {
				//�������: ����������� �� ����� �������� - �������, � �������
				//������� ������� ����������� ������ � ����������� � ���� �������
				AbstractDataStorableElement dsElement =	(AbstractDataStorableElement) element;
				if (this.reportTemplate.clasterContainsPoint(dsElement,x,y))
					return component;
			}
			else if (element instanceof ImageStorableElement) {
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
	 * @throws ApplicationException 
	 */
	private Rectangle getDataComponentsClasterBounds(DataRenderingComponent component) throws ApplicationException {
		Rectangle bounds = this.reportTemplate.getElementClasterBounds(
				(AbstractDataStorableElement)component.getElement());
		
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
	
	private RenderingComponent getComponentForElement(StorableElement element) {
		for (RenderingComponent component : this.componentContainer) {
			if (component.getElement().equals(element))
				return component;
		}
		return null;
	}
}
