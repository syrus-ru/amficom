/**
 * $Id: PlaceSchemePathCommandTestCase.java,v 1.3 2005/09/14 10:18:01 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.test;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.client.map.command.action.PlaceSchemeElementCommand;
import com.syrus.AMFICOM.client.map.command.action.PlaceSchemePathCommand;
import com.syrus.AMFICOM.client.map.command.action.UnPlaceSchemeElementCommand;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.MeasurementPath;
import com.syrus.AMFICOM.scheme.SchemeSampleData;


public class PlaceSchemePathCommandTestCase extends SchemeBindingTestCase {

	public void test1() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 0);
		assertEquals(measurementPaths.size(), 1);

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 0);
		
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

	public void test2() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand intercommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		intercommand.setNetMapViewer(METS.netMapViewer);
		intercommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		METS.logicalNetLayer.getMapViewController().scanCables(SchemeSampleData.scheme1);
		
		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 1);
		assertEquals(measurementPaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 1);
		assertTrue(sortedCablePaths.contains(cablePath));
		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

	public void test3() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		PlaceSchemeElementCommand intercommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		intercommand.setNetMapViewer(METS.netMapViewer);
		intercommand.execute();

		METS.logicalNetLayer.getMapViewController().scanCables(SchemeSampleData.scheme1);
		
		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 1);
		assertEquals(measurementPaths.size(), 1);

		CablePath cablePath = (CablePath)cablePaths.iterator().next();

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 1);
		assertTrue(sortedCablePaths.contains(cablePath));
		assertSame(cablePath.getStartNode(), this.building1); 
		assertSame(cablePath.getEndNode(), this.well1);
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

	public void test4() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand intercommand1 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		intercommand1.setNetMapViewer(METS.netMapViewer);
		intercommand1.execute();

		PlaceSchemeElementCommand intercommand2 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element2, this.well3location);
		intercommand2.setNetMapViewer(METS.netMapViewer);
		intercommand2.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		METS.logicalNetLayer.getMapViewController().scanCables(SchemeSampleData.scheme1);
		
		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 3);
		assertEquals(measurementPaths.size(), 1);

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 3);

		Iterator<CablePath> iterator = sortedCablePaths.iterator();
		CablePath cablePath1 = iterator.next();
		CablePath cablePath2 = iterator.next();
		CablePath cablePath3 = iterator.next();

		assertSame(cablePath1.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath2.getSchemeCableLink(), SchemeSampleData.scheme1clink1);
		assertSame(cablePath3.getSchemeCableLink(), SchemeSampleData.scheme1clink2);
		
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

	public void test5() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand intercommand2 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element2, this.well3location);
		intercommand2.setNetMapViewer(METS.netMapViewer);
		intercommand2.execute();

		PlaceSchemeElementCommand intercommand1 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		intercommand1.setNetMapViewer(METS.netMapViewer);
		intercommand1.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		METS.logicalNetLayer.getMapViewController().scanCables(SchemeSampleData.scheme1);
		
		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 3);
		assertEquals(measurementPaths.size(), 1);

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 3);

		Iterator<CablePath> iterator = sortedCablePaths.iterator();
		CablePath cablePath1 = iterator.next();
		CablePath cablePath2 = iterator.next();
		CablePath cablePath3 = iterator.next();

		assertSame(cablePath1.getSchemeCableLink(), SchemeSampleData.scheme1clink0);
		assertSame(cablePath2.getSchemeCableLink(), SchemeSampleData.scheme1clink1);
		assertSame(cablePath3.getSchemeCableLink(), SchemeSampleData.scheme1clink2);
		
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

	public void test6() {
		PlaceSchemeElementCommand startcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element0, this.building1location);
		startcommand.setNetMapViewer(METS.netMapViewer);
		startcommand.execute();

		PlaceSchemeElementCommand intercommand1 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element1, this.well1location);
		intercommand1.setNetMapViewer(METS.netMapViewer);
		intercommand1.execute();

		PlaceSchemeElementCommand intercommand2 = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element2, this.well3location);
		intercommand2.setNetMapViewer(METS.netMapViewer);
		intercommand2.execute();

		PlaceSchemeElementCommand endcommand = new PlaceSchemeElementCommand(SchemeSampleData.scheme1element3, this.building2location);
		endcommand.setNetMapViewer(METS.netMapViewer);
		endcommand.execute();

		PlaceSchemePathCommand command = new PlaceSchemePathCommand(SchemeSampleData.scheme1path0);
		command.setNetMapViewer(METS.netMapViewer);
		command.execute();

		UnPlaceSchemeElementCommand unplaceCommand = new UnPlaceSchemeElementCommand(this.well1, SchemeSampleData.scheme1element1);
		unplaceCommand.setNetMapViewer(METS.netMapViewer);
		unplaceCommand.execute();
		
		METS.logicalNetLayer.getMapViewController().scanCables(SchemeSampleData.scheme1);
		
		Collection cablePaths = METS.mapView.getCablePaths();
		Collection measurementPaths = METS.mapView.getMeasurementPaths();

		assertEquals(cablePaths.size(), 1);
		assertEquals(measurementPaths.size(), 1);

		MeasurementPath measurementPath = (MeasurementPath) measurementPaths.iterator().next();
		
		List<CablePath> sortedCablePaths = measurementPath.getSortedCablePaths();

		assertEquals(sortedCablePaths.size(), 1);

		CablePath cablePath = sortedCablePaths.iterator().next();

		assertSame(cablePath.getSchemeCableLink(), SchemeSampleData.scheme1clink2);
		
		assertSame(measurementPath.getStartNode(), this.building1); 
		assertSame(measurementPath.getEndNode(), this.building2);
	}

}
