package com.syrus.jASMIN.General.Panels;

import javax.swing.*;
import java.awt.*;
import oracle.jdeveloper.layout.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

import javax.swing.text.View;

/**
 * A Swing-based dialog class.
 * <P>
 * @author Andrei Kroupennikov
 */

class MyBasicTabbedPaneUI extends BasicTabbedPaneUI
{
/*
			protected void paintTab(
					Graphics g,
					int tabPlacement,
					Rectangle[] rects,
					int tabIndex,
					Rectangle iconRect,
					Rectangle textRect)
			{
				Rectangle tabRect = rects[tabIndex];
				int selectedIndex = tabPane.getSelectedIndex();
				boolean isSelected = selectedIndex == tabIndex;

//				if(tabPlacement == LEFT && not_rotated)
//				{
//					rotateInsets();
//					not_rotated = false;
//				}

				paintTabBackground(
						g,
						tabPlacement,
						tabIndex,
						tabRect.x,
						tabRect.y,
						tabRect.width,
						tabRect.height,
						isSelected);
				paintTabBorder(
						g,
						tabPlacement,
						tabIndex,
						tabRect.x,
						tabRect.y,
						tabRect.width,
						tabRect.height,
						isSelected);

				String title = tabPane.getTitleAt(tabIndex);
				Font font = tabPane.getFont();
				FontMetrics metrics = g.getFontMetrics(font);
				Icon icon = getIconForTab(tabIndex);

				layoutLabel(tabPlacement, metrics, tabIndex, title, icon,
							tabRect, iconRect, textRect, isSelected);

				paintText(g, tabPlacement, font, metrics,
						  tabIndex, title, textRect, isSelected);

				paintIcon(g, tabPlacement, tabIndex, icon, iconRect, isSelected);

				paintFocusIndicator(g, tabPlacement, rects, tabIndex,
				  iconRect, textRect, isSelected);
	}
*/
	public static ComponentUI createUI(JComponent c)
	{
		System.out.println("createUI ");
		return new MyBasicTabbedPaneUI();
	}

	protected FontMetrics getFontMetrics()
	{
		System.out.println("getFontMetrics ");
		Font font = tabPane.getFont();
		return Toolkit.getDefaultToolkit().getFontMetrics(font);
	}

	protected LayoutManager createLayoutManager()
	{
		System.out.println("createLayoutManager ");
		return new MyTabbedPaneLayout(tabPane);
	}

	private void ensureCurrentLayout()
	{
		System.out.println("ensureCurrentLayout ");
		if (tabPane.getTabCount() != rects.length)
		{
			MyTabbedPaneLayout layout = (MyTabbedPaneLayout)tabPane.getLayout();
			layout.calculateLayoutInfo();
		}
	}

	protected void assureRectsCreated(int tabCount)
	{
		super.assureRectsCreated(tabCount);	}	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics)	{		return super.calculateTabWidth(tabPlacement, tabIndex, metrics);	}
	protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight)	{
		return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);	}
	protected int calculateMaxTabWidth(int tabPlacement) {		FontMetrics metrics = getFontMetrics();
		int tabCount = tabPane.getTabCount();
		int result = 0;
		for(int i = 0; i < tabCount; i++) {
			result = Math.max(calculateTabWidth(tabPlacement, i, metrics), result);
		}
		return result;
	}

	protected int calculateMaxTabHeight(int tabPlacement) {
		FontMetrics metrics = getFontMetrics();
		int tabCount = tabPane.getTabCount();
		int result = 0;
		int fontHeight = metrics.getHeight();
		for(int i = 0; i < tabCount; i++) {
			result = Math.max(calculateTabHeight(tabPlacement, i, fontHeight), result);
		}
		return result;
    }

/*	protected int calculateMaxTabHeight(int tabPlacement)
	{
		return super.calculateMaxTabHeight(tabPlacement);	}	protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics)	{		return super.calculateTabWidth(tabPlacement, tabIndex, metrics);	}*/  void setMaxTabHeight(int maxTabHeight) {
    this.maxTabHeight = maxTabHeight;  }      int getMaxTabHeight() {    return maxTabHeight;  }    Rectangle[] getRects() {    return rects;  }      JTabbedPane getTabbedPane() {    return tabPane;  }	protected Insets getSelectedTabPadInsets(int tabPlacement) {
		Insets currentInsets = new Insets(0,0,0,0);
		rotateInsets(selectedTabPadInsets, currentInsets, tabPlacement);
		return currentInsets;
	}

	protected Insets getTabAreaInsets(int tabPlacement)
	{
		Insets currentInsets = new Insets(0,0,0,0);		rotateInsets(tabAreaInsets, currentInsets, tabPlacement);		return currentInsets;
	}
	protected static void rotateInsets(
			Insets topInsets,
			Insets targetInsets,
			int targetPlacement)
	{
		System.out.println("rotating " + topInsets + " to " + targetInsets
				+ " rule " + targetPlacement);

		switch(targetPlacement)
		{
			case LEFT:
			  targetInsets.top = topInsets.left;
			  targetInsets.left = topInsets.top;
			  targetInsets.bottom = topInsets.right;
			  targetInsets.right = topInsets.bottom;
			  break;
		  case BOTTOM:
			  targetInsets.top = topInsets.bottom;
			  targetInsets.left = topInsets.right;
			  targetInsets.bottom = topInsets.top;
			  targetInsets.right = topInsets.left;
			  break;
		  case RIGHT:
			  targetInsets.top = topInsets.left;
			  targetInsets.left = topInsets.bottom;
			  targetInsets.bottom = topInsets.right;
			  targetInsets.right = topInsets.top;
			  break;
		  case TOP:
		  default:
			  targetInsets.top = topInsets.top;
			  targetInsets.left = topInsets.left;
			  targetInsets.bottom = topInsets.bottom;
			  targetInsets.right = topInsets.right;
		}
	}

	class MyTabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout
	{
		JTabbedPane tabPane;

		MyTabbedPaneLayout(JTabbedPane tabPane)
		{
			super();
			this.tabPane = tabPane;		}
	public void layoutContainer(Container parent)
	{
	  super.layoutContainer(parent);	}	public void calculateLayoutInfo() {	  int tabCount = tabPane.getTabCount();	  assureRectsCreated(tabCount);	  calculateTabWidths(tabPane.getTabPlacement(), tabCount);	  calculateTabRects(tabPane.getTabPlacement(), tabCount);	}	protected void calculateTabWidths(int tabPlacement, int tabCount) {	  if (tabCount == 0) {		return;	  }	  FontMetrics metrics = getFontMetrics();	  int fontHeight = metrics.getHeight();	  int maxTabHeight = calculateMaxTabHeight(tabPlacement);	  setMaxTabHeight(maxTabHeight);	  Rectangle[] rects = getRects();	  for (int i = 0; i < tabCount; i++) {		rects[i].width  = calculateTabWidth(tabPlacement, i, metrics);		rects[i].height = maxTabHeight;	  }	}		protected void calculateTabRects(int tabPlacement, int tabCount)
		{
			System.out.println("calculate start" + tabCount + " as " + tabPlacement);

			FontMetrics metrics = getFontMetrics();
			Dimension size = tabPane.getSize();
			Insets insets = tabPane.getInsets();
			Insets tabAreaInsets = getTabAreaInsets(tabPlacement);
			int fontHeight = metrics.getHeight();
			int selectedIndex = tabPane.getSelectedIndex();
			int tabRunOverlay;
			int i, j;
			int x, y;
			int returnAt;
			boolean verticalTabRuns = (tabPlacement == LEFT || tabPlacement == RIGHT);

			System.out.println("calculate " + tabCount + " as " + tabPlacement);

			//
			// Calculate bounds within which a tab run must fit
            //
            switch(tabPlacement) {
              case LEFT:
                  maxTabWidth = calculateMaxTabWidth(tabPlacement);
                  x = insets.left + tabAreaInsets.left;
                  y = insets.top + tabAreaInsets.top;
				  returnAt = size.height - (insets.bottom + tabAreaInsets.bottom);
                  break;
			  case RIGHT:
                  maxTabWidth = calculateMaxTabWidth(tabPlacement);
                  x = size.width - insets.right - tabAreaInsets.right - maxTabWidth;
                  y = insets.top + tabAreaInsets.top;
                  returnAt = size.height - (insets.bottom + tabAreaInsets.bottom);
                  break;
			  case BOTTOM:
				  maxTabHeight = calculateMaxTabHeight(tabPlacement);
                  x = insets.left + tabAreaInsets.left;
				  y = size.height - insets.bottom - tabAreaInsets.bottom - maxTabHeight;
                  returnAt = size.width - (insets.right + tabAreaInsets.right);
                  break;
              case TOP:
			  default:
				  maxTabHeight = calculateMaxTabHeight(tabPlacement);
                  x = insets.left + tabAreaInsets.left;
                  y = insets.top + tabAreaInsets.top;
                  returnAt = size.width - (insets.right + tabAreaInsets.right);
            }

            tabRunOverlay = getTabRunOverlay(tabPlacement);

            runCount = 0;
			selectedRun = -1;

            if (tabCount == 0) {
                return;
            }

			// Run through tabs and partition them into runs
            Rectangle rect;
			for (i = 0; i < tabCount; i++) {
				rect = rects[i];

                if (!verticalTabRuns) {
                    // Tabs on TOP or BOTTOM....
					if (i > 0) {
						rect.x = rects[i-1].x + rects[i-1].width;
					} else {
                        tabRuns[0] = 0;
                        runCount = 1;
                        maxTabWidth = 0;
                        rect.x = x;
                    }
					rect.width = calculateTabWidth(tabPlacement, i, metrics);
                    maxTabWidth = Math.max(maxTabWidth, rect.width);

                    // Never move a TAB down a run if it is in the first column. 
                    // Even if there isn't enough room, moving it to a fresh 
                    // line won't help.
                    if (rect.x != 2 + insets.left && rect.x + rect.width > returnAt) {
                        if (runCount > tabRuns.length - 1) {
							expandTabRunsArray();
                        }
                        tabRuns[runCount] = i;
						runCount++;
                        rect.x = x;
                    }
                    // Initialize y position in case there's just one run
					rect.y = y;
					rect.height = maxTabHeight/* - 2*/;

				} else {
                    // Tabs on LEFT or RIGHT...
                    if (i > 0) {
                        rect.y = rects[i-1].y + rects[i-1].height;
                    } else {
						tabRuns[0] = 0;
                        runCount = 1;
						maxTabHeight = 0;
                        rect.y = y;
                    }
                    rect.height = calculateTabHeight(tabPlacement, i, fontHeight);
                    maxTabHeight = Math.max(maxTabHeight, rect.height);

                    // Never move a TAB over a run if it is in the first run. 
                    // Even if there isn't enough room, moving it to a fresh 
                    // column won't help.
					if (rect.y != 2 + insets.top && rect.y + rect.height > returnAt) {
                        if (runCount > tabRuns.length - 1) {
                            expandTabRunsArray();
                        }
						tabRuns[runCount] = i;
						runCount++;
                        rect.y = y;
                    }
                    // Initialize x position in case there's just one column
                    rect.x = x;
                    rect.width = maxTabWidth/* - 2*/;

				}
                if (i == selectedIndex) {
					selectedRun = runCount - 1;
				}
            }


            if (runCount > 1) {
                // Re-distribute tabs in case last run has leftover space
                normalizeTabRuns(tabPlacement, tabCount, verticalTabRuns? y : x, returnAt);

				selectedRun = getRunForTab(tabCount, selectedIndex);

                // Rotate run array so that selected run is first
                if (shouldRotateTabRuns(tabPlacement)) {
					rotateTabRuns(tabPlacement, selectedRun);
				}
            }

            // Step through runs from back to front to calculate
            // tab y locations and to pad runs appropriately
            for (i = runCount - 1; i >= 0; i--) {
                int start = tabRuns[i];
				int next = tabRuns[i == (runCount - 1)? 0 : i + 1];
				int end = (next != 0? next - 1 : tabCount - 1);
				if (!verticalTabRuns) {
                    for (j = start; j <= end; j++) {
                        rect = rects[j];
                        rect.y = y;
                        rect.x += getTabRunIndent(tabPlacement, i);
                    }
                    if (shouldPadTabRun(tabPlacement, i)) {
                        padTabRun(tabPlacement, start, end, returnAt);
                    }
					if (tabPlacement == BOTTOM) {
                        y -= (maxTabHeight - tabRunOverlay);
                    } else {
                        y += (maxTabHeight - tabRunOverlay);
					}
				} else {
                    for (j = start; j <= end; j++) {
						rect = rects[j];
                        rect.x = x;
                        rect.y += getTabRunIndent(tabPlacement, i);
                    }
                    if (shouldPadTabRun(tabPlacement, i)) {
                        padTabRun(tabPlacement, start, end, returnAt);
                    }
					if (tabPlacement == RIGHT) {
						x -= (maxTabWidth - tabRunOverlay);
					} else {
						x += (maxTabWidth - tabRunOverlay);
					}
				}
			}

			// Pad the selected tab so that it appears raised in front
			padSelectedTab(tabPlacement, selectedIndex);

		}

	}// MyTabbedPaneLayout

}//MyVasicTabbedPaneUI

