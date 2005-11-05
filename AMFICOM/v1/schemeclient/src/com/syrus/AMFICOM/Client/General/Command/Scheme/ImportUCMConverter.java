/*-
 * $Id: ImportUCMConverter.java,v 1.13 2005/11/05 13:43:20 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Command.Scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ObjectEntities.CHARACTERISTIC_TYPE_CODE;
import static com.syrus.AMFICOM.general.StorableObjectWrapper.COLUMN_CODENAME;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import com.jgraph.graph.DefaultGraphCell;
import com.syrus.AMFICOM.Client.General.Event.SchemeEvent;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.client_.scheme.graph.ElementsTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeGraph;
import com.syrus.AMFICOM.client_.scheme.graph.SchemeTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.UgoTabbedPane;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateBlockPortAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.CreateTopLevelSchemeAction;
import com.syrus.AMFICOM.client_.scheme.graph.actions.GraphActions;
import com.syrus.AMFICOM.client_.scheme.graph.actions.SchemeActions;
import com.syrus.AMFICOM.client_.scheme.graph.objects.CablePortCell;
import com.syrus.AMFICOM.client_.scheme.utils.ClientUtils;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.ProtoEquipment;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.CharacteristicType;
import com.syrus.AMFICOM.general.CharacteristicTypeCodenames;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DataType;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectWrapper;
import com.syrus.AMFICOM.general.TypicalCondition;
import com.syrus.AMFICOM.general.corba.IdlCharacteristicTypePackage.CharacteristicTypeSort;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectConditionPackage.IdlTypicalConditionPackage.OperationSort;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.AMFICOM.scheme.SchemeCablePort;
import com.syrus.AMFICOM.scheme.SchemeCableThread;
import com.syrus.AMFICOM.scheme.SchemeDevice;
import com.syrus.AMFICOM.scheme.SchemeElement;
import com.syrus.AMFICOM.scheme.SchemeLink;
import com.syrus.AMFICOM.scheme.SchemePort;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeElementPackage.IdlSchemeElementKind;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;
import com.syrus.util.Log;

public class ImportUCMConverter {
	private Map<Integer, SchemeProtoElement> straightMuffs;
	private Map<Integer, SchemeProtoElement> inVrms;
	private Map<Integer, SchemeProtoElement> outVrms;
	private Set<Identifier> placedObjectIds;
	private Set<ProtoEquipment> muffProtoTypes;
	private Set<Identifiable> objectsToDelete;
	
	private Identifier userId;
	private ApplicationContext aContext;
	private SchemeTabbedPane pane;
	
	ImportUCMConverter(ApplicationContext aContext, SchemeTabbedPane pane) {
		this.aContext = aContext;
		this.pane = pane;
		this.userId = LoginManager.getUserId();
	}
	
	void parseSchemeCableLinks(Scheme scheme) throws ApplicationException {
		EquivalentCondition condition1 = new EquivalentCondition(ObjectEntities.CABLELINK_TYPE_CODE);
		Set<CableLinkType> cableLinkTypes1 = StorableObjectPool.getStorableObjectsByCondition(condition1, true);

		if (cableLinkTypes1.size() == 0) {
			Log.debugMessage("no CableLinkTypes found", Level.WARNING);
			return;
		}
		
		Map<CableLinkType, List<CableThreadType>> cableToThreadsMapping = new HashMap<CableLinkType, List<CableThreadType>>();
		for (CableLinkType type : cableLinkTypes1) {
			cableToThreadsMapping.put(type, ClientUtils.getSortedThreadTypes(type));	
		}
		Map<Integer, CableLinkType> cableLinkTypes = new HashMap<Integer, CableLinkType>();
		for (CableLinkType type : cableToThreadsMapping.keySet()) {
			List<CableThreadType> threadTypes = cableToThreadsMapping.get(type);
			cableLinkTypes.put(Integer.valueOf(threadTypes.size()), type);
		}
		
		final StorableObjectCondition condition = new TypicalCondition(CharacteristicTypeCodenames.COMMON_COLOUR, OperationSort.OPERATION_EQUALS, CHARACTERISTIC_TYPE_CODE, COLUMN_CODENAME);
		final Set<CharacteristicType> characteristicTypes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		final CharacteristicType characteristicType = characteristicTypes.isEmpty()
				? CharacteristicType.createInstance(this.userId, CharacteristicTypeCodenames.COMMON_COLOUR,
						"", "color", DataType.INTEGER, CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL)
				: characteristicTypes.iterator().next();
		assert characteristicType != null : NON_NULL_EXPECTED;
		final String name = characteristicType.getName();
		final String description = characteristicType.getDescription();

		for (SchemeCableLink schemeCableLink : scheme.getSchemeCableLinks(false)) {
			CableLinkType cableLinkType = schemeCableLink.getAbstractLinkType();
			if (cableLinkType == null) {
				Log.debugMessage("No real CableLinkType for " + schemeCableLink.getName(), Level.FINEST);
				CableLinkType suitableType;
				Integer fibers = Integer.valueOf(schemeCableLink.getSchemeCableThreads(false).size());
				suitableType = cableLinkTypes.get(fibers);
				if (suitableType == null) {
					for (Integer i : cableLinkTypes.keySet()) {
						if (i.compareTo(fibers) > 0) {
							suitableType = cableLinkTypes.get(i);
							break;
						}
					}
					if (suitableType == null) {
						suitableType = cableLinkTypes.values().iterator().next();
					}
				}
				schemeCableLink.setAbstractLinkType(suitableType);
			} else { // fix colors
				Iterator<CableThreadType> it1 = cableToThreadsMapping.get(cableLinkType).iterator();
				Iterator<SchemeCableThread> it2 = ClientUtils.getSortedCableThreads(schemeCableLink).iterator();
				for(; it1.hasNext() && it2.hasNext();) {
					final CableThreadType cableThreadType = it1.next();
					final SchemeCableThread schemeCableThread = it2.next();

					if (ClientUtils.getCharacteristic(schemeCableThread.getCharacteristics(false), 
							CharacteristicTypeCodenames.COMMON_COLOUR) == null) {
						Characteristic.createInstance(this.userId,
								characteristicType,
								name,
								description,
								Integer.toString(cableThreadType.getColor()),
								schemeCableThread,
								true,
								true);
					}
				}
			}
		}
	}
	
	void parseSchemeElements(Scheme scheme) throws ApplicationException {
		this.objectsToDelete = new HashSet<Identifiable>();
//		initMuffs();
		initVrms();
		initPlacedObjects(scheme);
		
		ApplicationContext internalContext =  new ApplicationContext();
		internalContext.setDispatcher(new Dispatcher());
		SchemeGraph invisibleGraph = new UgoTabbedPane(internalContext).getGraph();
		invisibleGraph.setMakeNotifications(false);
		Identifier domainId = LoginManager.getDomainId();
	
		for (SchemeElement schemeElement : scheme.getSchemeElements(false)) {
			if (!this.placedObjectIds.contains(schemeElement.getId())) {
			try{
				if (schemeElement.getKind() == IdlSchemeElementKind.SCHEME_CONTAINER) {
				// if no real Scheme associated create new scheme with internal VRM's
					Scheme internalScheme = Scheme.createInstance(this.userId, schemeElement.getName(), IdlKind.BUILDING, domainId);
					internalScheme.setDescription(schemeElement.getDescription());
					internalScheme.setLabel(schemeElement.getLabel());
					internalScheme.setSymbol(schemeElement.getSymbol());
					schemeElement.setScheme(internalScheme, false);
					// graph for schemeCell
					ElementsTabbedPane schemePane = new ElementsTabbedPane(internalContext);
					SchemeGraph schemeGraph = schemePane.getGraph();
					schemePane.getCurrentPanel().getSchemeResource().setScheme(internalScheme);
					int grid = schemeGraph.getGridSize();
					schemeGraph.setMakeNotifications(false);
					SchemeImageResource res = internalScheme.getSchemeCell();
					if (res == null) {
						res = SchemeObjectsFactory.createSchemeImageResource();
						internalScheme.setSchemeCell(res);
					}
					
					//	next create vrm for every cable port
					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively(false);
					int inCount = 0, outCount = 0;
					for (SchemeCablePort cablePort : existingCablePorts) {
						int fibers = 8;
						SchemeCableLink cable = cablePort.getAbstractSchemeLink();
						if (cable != null) {
							fibers = cable.getSchemeCableThreads(false).size();
						} else {
							Log.debugMessage("No connected cable found", Level.FINE);
						}
						boolean isInput = cablePort.getDirectionType() == IdlDirectionType._IN;
						SchemeProtoElement suitableVrm = getSuitableProto(isInput ? this.inVrms : this.outVrms, Integer.valueOf(fibers));
						
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(internalScheme, suitableVrm);
						// substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, Collections.singleton(cablePort));
						
//						substituteSchemeElement(clonedIds, newSchemeElement, schemeElement);

						// write it to cell
						SchemeImageResource res1 = newSchemeElement.getSchemeCell();
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						res1.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						// place to scheme cell
						Point p = isInput 
								?	new Point(grid * 15, grid * (10 + 20 * inCount))
								: new Point(grid * 45, grid * (10 + 20 * outCount));
						clonedObjects = SchemeActions.openSchemeImageResource(schemeGraph, res1, true, p, false);
						// add top level port
						CablePortCell portCell = SchemeActions.findCablePortCellById(schemeGraph, cablePort.getId());
						CreateBlockPortAction.create(schemeGraph, portCell);

						if (isInput) {
							inCount++;
						} else {
							outCount++;
						}
					}
					
					for (SchemeDevice dev : new HashSet<SchemeDevice>(schemeElement.getSchemeDevices(false))) {
						dev.setParentSchemeElement(null, false);
					}
					res.setData((List)schemeGraph.getArchiveableState());
					// create UGO
					if (existingCablePorts.size() > 0) {
						new CreateTopLevelSchemeAction(schemePane).execute();
						schemeElement.setUgoCell(internalScheme.getUgoCell().clone());
					}
				} else if (schemeElement.getKind() == IdlSchemeElementKind.SCHEME_ELEMENT_CONTAINER) {
					
					SchemeActions.generateImage(invisibleGraph, schemeElement);
					
					
					
//					ProtoEquipment protoEquipment = schemeElement.getProtoEquipment();
/*					Set<SchemeCablePort> existingCablePorts = schemeElement.getSchemeCablePortsRecursively(false);
					if (existingCablePorts.size() < 3) { // straight muff
						// count how many threads in connected fibers
						int maxFibers = 8;
						for (SchemeCablePort cablePort : existingCablePorts) {
							SchemeCableLink cable = cablePort.getAbstractSchemeLink();
							if (cable != null) {
								maxFibers = Math.max(maxFibers, cable.getSchemeCableThreads(false).size());
							} else {
								Log.debugMessage("No connected cable found", Level.FINE);
							}
						}
						SchemeProtoElement suitableMuff = getSuitableProto(this.straightMuffs, Integer.valueOf(maxFibers));
						
						// next create SchemeElement from suitableMuff
						SchemeElement newSchemeElement = SchemeObjectsFactory.createSchemeElement(schemeElement, suitableMuff);

						SchemeImageResource schemeCell = SchemeObjectsFactory.createSchemeImageResource();  
						schemeCell.setImage(newSchemeElement.getSchemeCell().getImage());
						schemeElement.setSchemeCell(schemeCell);
						
						SchemeImageResource ugoCell = SchemeObjectsFactory.createSchemeImageResource();  
						ugoCell.setImage(newSchemeElement.getUgoCell().getImage());
						schemeElement.setUgoCell(ugoCell);
						
						// and substitute existing cable ports
						Map<Identifier, Identifier>clonedIds = newSchemeElement.getClonedIdMap();
						substituteExistingPorts(clonedIds, existingCablePorts);
						
						substituteSchemeElement(clonedIds, newSchemeElement, schemeElement);
						
						// write it to cell
						Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, schemeCell, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						schemeCell.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);
						
						clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, ugoCell, true);
						SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
						ugoCell.setData((List)invisibleGraph.getArchiveableState());
						GraphActions.clearGraph(invisibleGraph);

					} else { // split muff
						// graph for schemeCell
						ElementsTabbedPane schemePane = new ElementsTabbedPane(internalContext);
						SchemeGraph schemeGraph = schemePane.getGraph();
						schemePane.getCurrentPanel().getSchemeResource().setSchemeElement(schemeElement);
						int grid = schemeGraph.getGridSize();
						schemeGraph.setMakeNotifications(false);
						SchemeImageResource res = schemeElement.getSchemeCell();
						if (res == null) {
							res = SchemeObjectsFactory.createSchemeImageResource();
							schemeElement.setSchemeCell(res);
						}
						
						//	next create vrm for every cable port
						int inCount = 0, outCount = 0;
						for (SchemeCablePort cablePort : existingCablePorts) {
//							SchemeCableLink cableLink = cablePort.getAbstractSchemeLink();
							int fibers = 8;
							SchemeCableLink cable = cablePort.getAbstractSchemeLink();
							if (cable != null) {
								fibers = cable.getSchemeCableThreads(false).size();
							} else {
								Log.debugMessage("No connected cable found", Level.FINE);
							}
							boolean isInput = cablePort.getDirectionType() == IdlDirectionType._IN;
							SchemeProtoElement suitableVrm = getSuitableProto(isInput ? this.inVrms : this.outVrms, Integer.valueOf(fibers));
							
							SchemeElement newVrm = SchemeObjectsFactory.createSchemeElement(schemeElement, suitableVrm);
							// substitute existing cable ports
							Map<Identifier, Identifier>clonedIds = newVrm.getClonedIdMap();
							substituteExistingPorts(clonedIds, Collections.singleton(cablePort));

//							substituteSchemeElement(clonedIds, newVrm, schemeElement);
							
							// write it to cell
							SchemeImageResource res1 = newVrm.getSchemeCell();
							Map<DefaultGraphCell, DefaultGraphCell> clonedObjects = SchemeActions.openSchemeImageResource(invisibleGraph, res1, true);
							SchemeObjectsFactory.assignClonedIds(clonedObjects, clonedIds);
							res1.setData((List)invisibleGraph.getArchiveableState());
							GraphActions.clearGraph(invisibleGraph);
							
							// place to scheme cell
							Point p = isInput 
									?	new Point(grid * 15, grid * (10 + 20 * inCount))
									: new Point(grid * 45, grid * (10 + 20 * outCount));
							clonedObjects = SchemeActions.openSchemeImageResource(schemeGraph, res1, true, p, false);
							// add top level port
							CablePortCell portCell = SchemeActions.findCablePortCellById(schemeGraph, cablePort.getId());
							CreateBlockPortAction.create(schemeGraph, portCell);

							if (isInput) {
								inCount++;
							} else {
								outCount++;
							}
						}
						for (SchemeDevice dev : new HashSet<SchemeDevice>(schemeElement.getSchemeDevices(false))) {
							dev.setParentSchemeElement(null, false);
						}
						res.setData((List)schemeGraph.getArchiveableState());
						// create UGO
						if (existingCablePorts.size() > 0) {
							new CreateTopLevelSchemeAction(schemePane).execute();
						}
					}*/ 
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
			StorableObjectPool.flush(this.objectsToDelete, this.userId, true);
			this.objectsToDelete.clear();
		}
		
	}
	
	private SchemeProtoElement getSuitableProto(Map<Integer, SchemeProtoElement> mapping, Integer num) {
		//  search for proto with corresponding number of ports, if nothing found search with larger number, 
		//  if not again - get any
		SchemeProtoElement suitableProto = mapping.get(num);
		if (suitableProto == null) {
			for (Integer i : mapping.keySet()) {
				if (i.compareTo(num) > 0) {
					suitableProto = mapping.get(i);
					break;
				}
			}
			if (suitableProto == null) {
				suitableProto = mapping.values().iterator().next();
			}
		}
		return suitableProto;
	}

	private void substituteSchemeElement(Map<Identifier, Identifier>clonedIds, SchemeElement seToRemove, SchemeElement seToAdd) throws ApplicationException {
		for (SchemeDevice dev : seToAdd.getSchemeDevices(false)) {
			if (!dev.getSchemeCablePorts(false).isEmpty()) {
				Log.errorMessage("Try to delete device with non empty cable ports");
			}
			if (!dev.getSchemePorts(false).isEmpty()) {
				Log.errorMessage("Try to delete device with non empty ports");
			}
			Log.debugMessage("Remove device " + dev.getId(), Level.FINE);
			StorableObjectPool.delete(dev.getId());
			this.objectsToDelete.add(dev);
		}
		Set<SchemeDevice> devices = new HashSet<SchemeDevice>(seToRemove.getSchemeDevices(false));
		for (SchemeDevice child : devices) {
			child.setParentSchemeElement(seToAdd, false);
		}
		Set<SchemeElement> elements = new HashSet<SchemeElement>(seToRemove.getSchemeElements(false));
		for (SchemeElement child : elements) {
			child.setParentSchemeElement(seToAdd, false);
		}
		Set<SchemeLink> links = new HashSet<SchemeLink>(seToRemove.getSchemeLinks(false));
		for (SchemeLink child : links) {
			child.setParentSchemeElement(seToAdd, false);
		}
		substituteIds(clonedIds, seToRemove, seToAdd);
		
		StorableObjectPool.delete(seToRemove.getId());
		
		this.objectsToDelete.add(seToRemove);
	}
	
	private void substituteIds(Map<Identifier, Identifier>clonedIds, SchemeElement seToRemove, SchemeElement seToAdd) {
		for (Identifier id : clonedIds.keySet()) {
			Identifier value = clonedIds.get(id);
			if (id.equals(seToRemove.getId())) {
				clonedIds.remove(id);
				id = seToAdd.getId();
				clonedIds.put(id, value);
			}
			if (value.equals(seToRemove.getId())) {
				clonedIds.put(id, seToAdd.getId());
			}
		}
	}
	
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, Set<SchemeCablePort> existingCablePorts) throws ApplicationException {
		Map<SchemeCablePort, Identifier> seCablePortReversedMap = new HashMap<SchemeCablePort, Identifier>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false); // old port
				seCablePortReversedMap.put(cport, id1);
			}
		}

		Map<SchemeCablePort, SchemeCablePort>existingPortsMapping = new HashMap<SchemeCablePort, SchemeCablePort>(); // oldId to existing
		for (SchemeCablePort existingPort : existingCablePorts) {
			for (Iterator<SchemeCablePort> it = seCablePortReversedMap.keySet().iterator(); it.hasNext();) {
				SchemeCablePort seCablePort = it.next();
				if (seCablePort.getDirectionType() == existingPort.getDirectionType() && !existingPortsMapping.containsKey(seCablePort)) {
					existingPortsMapping.put(seCablePort, existingPort);
					break;
				}
			}
			if (!existingPortsMapping.containsValue(existingPort)) {
				for (Iterator<SchemeCablePort> it = seCablePortReversedMap.keySet().iterator(); it.hasNext();) {
					SchemeCablePort seCablePort = it.next();
					if (!existingPortsMapping.containsKey(seCablePort)) {
						existingPortsMapping.put(seCablePort, existingPort);
						break;
					}
				}
			}
		}
		
		
		/*Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>(); // oldId to existing 
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false); // old port
				for (SchemeCablePort existingPort : existingCablePorts) {
					if (cport.getDirectionType() == existingPort.getDirectionType() && !existingPortsMapping.containsValue(existingPort)) {
						existingPortsMapping.put(id1, existingPort);
					}
				}
			}
		}*/
		int counter = 0;
		for (SchemeCablePort portToRemove : existingPortsMapping.keySet()) {
			SchemeCablePort portToAdd = existingPortsMapping.get(portToRemove);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			Log.debugMessage((counter++) + ":Add port " + portToAdd.getId() + " (parentId = '" + parent.getId() + "')", Level.FINE);
			portToAdd.setParentSchemeDevice(parent, false);
			portToAdd.setPortType(portToRemove.getPortType());
			clonedIds.put(seCablePortReversedMap.get(portToRemove), portToAdd.getId());
			Log.debugMessage("Remove port " + portToRemove.getId() + " (parentId = '" + parent.getId() + "')", Level.FINE);
			StorableObjectPool.delete(portToRemove.getId());
			this.objectsToDelete.add(portToRemove);
			Log.debugMessage("\tparent schemeElement " + parent.getParentSchemeElement().getId(), Level.FINE);
			for (SchemeCablePort child : parent.getSchemeCablePorts(false)) {
				Log.debugMessage("\tchild cablePort " + child.getId(), Level.FINE);
			}
		}
	}
	/*
	private void substituteExistingPorts(Map<Identifier, Identifier>clonedIds, SchemeCablePort existingCablePort) throws ApplicationException {
		Map<Identifier, SchemeCablePort>existingPortsMapping = new HashMap<Identifier, SchemeCablePort>();
		for (Identifier id1 : clonedIds.keySet()) {
			if (id1.getMajor() == ObjectEntities.SCHEMECABLEPORT_CODE) {
				SchemeCablePort cport = StorableObjectPool.getStorableObject(clonedIds.get(id1), false);
				if (cport.getDirectionType() == existingCablePort.getDirectionType()) {
					existingPortsMapping.put(id1, existingCablePort);
					break;
				}
			}
		}
		for (Identifier id : existingPortsMapping.keySet()) {
			SchemeCablePort portToRemove = StorableObjectPool.getStorableObject(clonedIds.get(id), false);
			SchemeCablePort portToAdd = existingPortsMapping.get(id);
			
			SchemeDevice parent = portToRemove.getParentSchemeDevice();
			portToAdd.setParentSchemeDevice(parent);
			portToAdd.setPortType(portToRemove.getPortType());
			portToAdd.setName(portToRemove.getName());
			clonedIds.put(id, portToAdd.getId());
			Log.debugMessage("Remove port " + portToRemove.getId(), Level.FINE);
			StorableObjectPool.delete(portToRemove.getId());
			StorableObjectPool.flush(portToRemove.getId(), this.userId, false);
		}
	}
	*/

	private void initPlacedObjects(Scheme scheme) {
		Dispatcher internalDispatcher = this.aContext.getDispatcher();
		internalDispatcher.firePropertyChange(new SchemeEvent(this, scheme.getId(), SchemeEvent.OPEN_SCHEME));
		this.placedObjectIds = SchemeActions.getPlacedObjects(this.pane.getGraph());
	}
	
	/*
	private void initMuffs() throws ApplicationException {
		final TypicalCondition condition1 = new TypicalCondition(EquipmentType.MUFF, 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.PROTOEQUIPMENT_CODE, 
				StorableObjectWrapper.COLUMN_TYPE_CODE);
		
		this.muffProtoTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> muffTypeIds = new HashSet<Identifier>();
		for (ProtoEquipment protoEq : this.muffProtoTypes) {
			muffTypeIds.add(protoEq.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(muffTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> muffs = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (muffs.size() == 0) {
			Log.debugMessage("No muffs found", Level.WARNING);
			throw new CreateObjectException("No muffs found");
		}

		// put <number of ports, muff>
		this.straightMuffs = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement muff : muffs) {
			if (muff.getParentSchemeProtoElement() == null) {
				Set<SchemeCablePort> cablePorts = muff.getSchemeCablePortsRecursively(false);
				if (cablePorts.size() == 2) {
					this.straightMuffs.put(Integer.valueOf(muff.getSchemePortsRecursively(false).size() / 2), muff);
				}
			}
		}
		if (this.straightMuffs.size() == 0) {
			Log.debugMessage("No straight muffs found", Level.WARNING);
			throw new CreateObjectException("No straight muffs found");
		}
	}*/
	
	private void initVrms() throws ApplicationException {
		final TypicalCondition condition1 = new TypicalCondition(EquipmentType.CABLE_PANEL, 
				OperationSort.OPERATION_EQUALS, 
				ObjectEntities.PROTOEQUIPMENT_CODE, 
				StorableObjectWrapper.COLUMN_TYPE_CODE);
		
		Set<ProtoEquipment> vrmTypes = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
		Set<Identifier> vrmTypeIds = new HashSet<Identifier>();
		for (ProtoEquipment protoEq : vrmTypes) {
			vrmTypeIds.add(protoEq.getId());
		}
		LinkedIdsCondition condition2 = new LinkedIdsCondition(vrmTypeIds, ObjectEntities.SCHEMEPROTOELEMENT_CODE);
		Set<SchemeProtoElement> vrms = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
		if (vrms.size() == 0) {
			Log.debugMessage("No vrms found", Level.WARNING);
			return;
		}

		// put <number of ports, vrm>
		this.inVrms = new HashMap<Integer, SchemeProtoElement>();
		this.outVrms = new HashMap<Integer, SchemeProtoElement>();
		for (SchemeProtoElement vrm : vrms) {
			if (vrm.getParentSchemeProtoElement() == null) {
				Set<SchemePort> ports = vrm.getSchemePortsRecursively(false);
				if (ports.size() > 0) {
					SchemePort port = ports.iterator().next();
					if (port.getDirectionType() == IdlDirectionType._OUT) {
						this.inVrms.put(Integer.valueOf(ports.size()), vrm);
					} else {
						this.outVrms.put(Integer.valueOf(ports.size()), vrm);
					}
				}
			}
		}
	}
}
