/*-
 * $Id: UCMSchemeExportCommand.java,v 1.22 2006/05/24 11:05:45 stas Exp $
 *
 * Copyright ї 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.impexp.unicablemap;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.xmlbeans.XmlOptions;

import com.syrus.AMFICOM.configuration.xml.XmlCableLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlCableLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlConfigurationLibrary;
import com.syrus.AMFICOM.configuration.xml.XmlEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlEquipmentSeq;
import com.syrus.AMFICOM.configuration.xml.XmlLinkType;
import com.syrus.AMFICOM.configuration.xml.XmlLinkTypeSeq;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipment;
import com.syrus.AMFICOM.configuration.xml.XmlProtoEquipmentSeq;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.AMFICOM.scheme.xml.SchemesDocument;
import com.syrus.AMFICOM.scheme.xml.XmlScheme;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLink;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeCableLinkSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElement;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeElementSeq;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeSeq;
import com.syrus.AMFICOM.scheme.xml.XmlAbstractSchemePort.DirectionType;
import com.syrus.AMFICOM.scheme.xml.XmlScheme.Kind;
import com.syrus.impexp.unicablemap.map.Block;
import com.syrus.impexp.unicablemap.map.Link;
import com.syrus.impexp.unicablemap.map.Site;
import com.syrus.impexp.unicablemap.objects.Cable;
import com.syrus.impexp.unicablemap.objects.CableThread;
import com.syrus.impexp.unicablemap.objects.CableType;
import com.syrus.impexp.unicablemap.objects.ChannelingItem;
import com.syrus.impexp.unicablemap.objects.Element;
import com.syrus.impexp.unicablemap.objects.Equipment;
import com.syrus.impexp.unicablemap.objects.LinkType;
import com.syrus.impexp.unicablemap.objects.MuffType;
import com.syrus.impexp.unicablemap.objects.Port;
import com.syrus.impexp.unicablemap.objects.SimplePort;
import com.syrus.impexp.unicablemap.objects.ThreadType;

/**
 * @author $Author: stas $
 * @version $Revision: 1.22 $, $Date: 2006/05/24 11:05:45 $
 * @module importUCM
 */

public class UCMSchemeExportCommand {
	protected static final String UCM_SCHEMED_EQT = "UCM_SCHEMED";
	protected static final String UCM_ODF_EQT = "UCM_ODF";
	protected static final String UCM_FRAME_EQT = "UCM_RACK";

	
	UniCableMapDatabase ucmDatabase;
	String filename;
	static final String configFileName = "_configUCM.xml"; 
	static final String schemeFileName = "_schemeUCM.xml";
	
	HashMap<Integer, CableType> cabletypes = new HashMap<Integer, CableType>();
	HashMap<Integer, LinkType> linktypes = new HashMap<Integer, LinkType>();
	HashMap<Integer, MuffType> eqtypes = new HashMap<Integer, MuffType>();
	HashSet<Equipment> equipments = new HashSet<Equipment>();
	HashMap<Integer, Element> muffs = new HashMap<Integer, Element>();
	HashMap<Integer, Element> buildings = new HashMap<Integer, Element>();
	HashMap<Integer, Element> floors = new HashMap<Integer, Element>();
	HashMap<Integer, Element> racks = new HashMap<Integer, Element>();
	HashMap<Integer, Element> vrms = new HashMap<Integer, Element>();
	
	HashMap<Integer, Cable> cables = new HashMap<Integer, Cable>();
	HashMap<String, Link> links = new HashMap<String, Link>();
	HashMap<String, Site> sites = new HashMap<String, Site>();
	
	Map<Cable, List<Site>> cableInlets = new HashMap<Cable, List<Site>>();
	Map<Port, Cable> portCableMapping = new HashMap<Port, Cable>();
	
	String defaultEqtId;
	
	public UCMSchemeExportCommand(UniCableMapDatabase ucmDatabase, String filename) {
		this.ucmDatabase = ucmDatabase;
		this.filename = filename;
	}
	
	public void processTypeObjects() {
		Collection<UniCableMapObject> muffTypes = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_MUFF_TYPE));
		createMuffTypes(muffTypes);

		Collection<UniCableMapObject> linkTypes = new LinkedList<UniCableMapObject>();
		linkTypes.add(this.ucmDatabase.getType(UniCableMapType.UCM_FIBRE));
		linkTypes.add(this.ucmDatabase.getType(UniCableMapType.UCM_PATCHCORD));
		createLinkTypes(linkTypes);

		Collection<UniCableMapObject> cableTypes = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_TYPE));
		createCableTypes(cableTypes);
	}
	
	public void processMapObjects() throws SQLException {
		Collection<UniCableMapObject> ucmCableInlets = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_INLET));
		createCableInlets(ucmCableInlets);

		Collection<UniCableMapObject> ucmTunnels = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_TUNNEL));
		createLinks(ucmTunnels);

		Collection<UniCableMapObject> ucmCollectorFragments = this.ucmDatabase.getObjects(
				this.ucmDatabase.getType(UniCableMapType.UCM_COLLECTOR_FRAGMENT));
		createLinks(ucmCollectorFragments);
	}
	
	public void processSchemeObjects(int cableNumber) throws SQLException {
		Collection<UniCableMapObject> buildings1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_BUILDING_PLAN));
		createBuildingsWithInternals(buildings1);
		
		Collection<UniCableMapObject> muffs1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_MUFF));
		createMuffs(muffs1);
		
		Collection<UniCableMapObject> vrms1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_FLOOR));
		createVrms(vrms1);
		
		Collection<UniCableMapObject> cables1 = this.ucmDatabase.getObjects(this.ucmDatabase.getType(UniCableMapType.UCM_CABLE_LINEAR));
		createCables(cables1, cableNumber);
		
//		createInBuildingCommutation();
	}
	
	void createCables(Collection<UniCableMapObject> objects, int number) throws SQLException {
		int counter = 0;
		
		for (UniCableMapObject ucmObject : objects) {
			if (counter++ > number) {
				break;
			}
			
			Cable cable = new Cable(Integer.toString(ucmObject.un));
			this.cables.put(Integer.valueOf(ucmObject.un), cable);
			
			cable.setName(ucmObject.text);
			int fibers = 0;
			
			for(UniCableMapParameter param : ucmObject.buf.params) {
				if(param.realParameter.text.equals(UniCableMapParameter.UCM_FIBRES)) {
					fibers = Integer.parseInt(param.value);
				}
			}
			
			String startSiteId = "";
			String endSiteId = "";
			
			CableType type = null;
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_WELL_CABLE)) {
					if(ucmLink.parent.typ.text.equals(UniCableMapType.UCM_CABLE_INLET)) {
						List<Site> list = this.cableInlets.get(cable);
						if(list == null) {
							list = new LinkedList<Site>();
							this.cableInlets.put(cable, list);
						}
						final Site cableInlet = this.sites.get(String.valueOf(ucmLink.parent.un));
						if(cableInlet == null) {
							 System.err.println("cannot find cable inlet with id = " + ucmLink.parent.un
									 + " in preloaded cable inlets for cable " + cable.getId());
						}
						list.add(cableInlet);
					}
				}
			}

			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
					 cable.setTypeId(Integer.valueOf(ucmLink.parent.un));
					 if (fibers != 0) {
						 if (type == null) {
							 type = this.cabletypes.get(Integer.valueOf(ucmLink.parent.un));
						 }
						 if (type != null) {
							 type.setThreadNum(fibers);
						 } else {
							 System.err.println("type not found with id = " + ucmLink.parent.un);
						 }
					 }
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
					 cable.setCodenameId(Integer.valueOf(ucmLink.parent.un));
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
					UniCableMapObject startObj = ucmLink.parent;
					cable.setStartPortId(getFreePort(ucmObject, DirectionType.OUT, startObj));
					
					//add first CCI
					if (startObj.typ.text.equals(UniCableMapType.UCM_CABLE_INLET)) {
						ChannelingItem item = createStartChannelingItem(cable, startObj);
						cable.setFirstChannelingItem(item);
						startSiteId = item.getStartSiteId();
					}
					else if (startObj.typ.text.equals(UniCableMapType.UCM_ODF)) {
						UniCableMapObject inlet = getODFCableInlet(cable, startObj);
						if(inlet != null) {
							ChannelingItem item = createStartChannelingItem(cable, inlet);
							cable.setFirstChannelingItem(item);
							startSiteId = item.getStartSiteId();
						}
						else {
							startSiteId = getSiteId(startObj);
						}
					}
					else {
						startSiteId = getSiteId(startObj);
					}
					cable.setStartSiteId(startSiteId);
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
					UniCableMapObject endObj = ucmLink.parent;
					cable.setEndPortId(getFreePort(ucmObject, DirectionType.IN, endObj));
					
					// set last CCI
					if (endObj.typ.text.equals(UniCableMapType.UCM_CABLE_INLET)) {
						ChannelingItem item = createEndChannelingItem(cable, endObj);
						cable.setLastChannelingItem(item);
						endSiteId = item.getEndSiteId();
					}
					else if (endObj.typ.text.equals(UniCableMapType.UCM_ODF)) {
						UniCableMapObject inlet = getODFCableInlet(cable, endObj);
						if(inlet != null) {
							ChannelingItem item = createEndChannelingItem(cable, inlet);
							cable.setLastChannelingItem(item);
							endSiteId = item.getEndSiteId();
						}
						else {
							endSiteId = getSiteId(endObj);
						}
					}
					else {
						endSiteId = getSiteId(endObj);
					}
					cable.setEndSiteId(endSiteId);
				} 
			}
			
			if(startSiteId.length() == 0) {
				System.out.println("Не определен начальный узел для кабеля "
						+ cable.getName() + "' (" + cable.getId()
						+ ")");
			}
			if(endSiteId.length() == 0) {
				System.out.println("Не определен конечный узел для кабеля "
						+ cable.getName() + "' (" + cable.getId()
						+ ")");
			}

			// create cable threads
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CABLE_LAYOUT)) {
					UniCableMapObject razrez = ucmLink.child;  
					cable.setLayoutId(Integer.valueOf(razrez.un)); 
					if (cable.getThreads().isEmpty()) {
						createThreads(type, cable, razrez);
					}
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
					ChannelingItem item = createChannelingItem(cable, ucmLink.child);
					cable.addChannelingItem(item);
				}
			}
			cable.sortChannelingItems(getLinks(ucmObject));
		}
	}

//	void createInBuildingCommutation() {
//		// create ports in vrms and commutation beetwen them on level of floor
//		for (Element floor : this.floors.values()) {
//			List<Element> vrms = new LinkedList<Element>();
//			for (Element rack : floor.getElements()) {
//				vrms.addAll(rack.getElements());
//			}
//			for (Element vrm : vrms) {
//				for (Port cablePort : vrm.getAllPorts()) {
//					Cable cable = this.portCableMapping.get(cablePort);
//					
//				}
//			}
//		}
		
//	Cable cable = this.cables.get(Integer.valueOf(ucmCable.un));
//	vrm.initCounter();
//	for (CableThread thread : cable.getThreads()) {
//		vrm.addOutputPort(thread.getId() + "^" + vrm.getId(), SimplePort.OPTICAL_PORT_TYPE);
//	}
//	}
	
	private List<Link> getLinks(UniCableMapObject ucmObject) throws SQLException {
		List<Link> foundLinks = new LinkedList<Link>(); 
		
		for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_TUNNEL_CABLE)) {
				UniCableMapObject tunnel = ucmLink.parent;
				final Link link = this.links.get(Integer.toString(tunnel.un));
				foundLinks.add(link);
			}
		}
		return foundLinks;
	}

	void createThreads(CableType type, Cable cable, UniCableMapObject razrez) throws SQLException {
		int fiberCount = 0;
		
		for(UniCableMapLink ucmLink2 : this.ucmDatabase.getChildren(razrez)) {
			if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject fiber = ucmLink2.child;

				CableThread thread = new CableThread(cable.getId() + "thread" + (++fiberCount));
				thread.setParentId(cable.getId());
//				thread.setName(fiber.text);
				thread.setName(Integer.toString(fiberCount));
				cable.addCableThread(thread);
				
//				for(UniCableMapLink ucmLink4 : this.ucmDatabase.getChildren(fiber)) {
//					if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
//						UniCableMapObject nextFiber = ucmLink4.child;
//						thread.setSourcePortId(Integer.valueOf(nextFiber.un));
//					} else if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
//						UniCableMapObject previosFiber = ucmLink4.child;
//						thread.setTargetPortId(Integer.valueOf(previosFiber.un));
//					}
//				}
				
				if (type != null) {
					ThreadType tt = new ThreadType(type.getId() + "threadType" + fiberCount);
					tt.setCableTypeId(type.getId());
					type.addThreadType(tt);
					
					for(UniCableMapParameter param : fiber.buf.params) {
						if(param.realParameter.text.equals(UniCableMapParameter.UCM_MUNBER_RO_MANDATORY)) {
							String codename = param.value;
							tt.setCodename(codename);
							break;
						}
					}
					for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(fiber)) {
						if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_TYPE_REALIZATION)) {
							tt.setLinkTypeId(String.valueOf(ucmLink4.parent.un));
							break;
						}
					}
					thread.setLinkTypeId(tt.getLinkTypeId());
				} else {
					System.err.println("cable type not found. can't create CTT's");
				}
			}
		}
	}
	
	ChannelingItem createStartChannelingItem(Cable cable, UniCableMapObject inlet) throws SQLException {
		ChannelingItem item = new ChannelingItem("cci" + inlet.un + "start" + cable.getId());
		item.setRowX(0);
		item.setPlaceY(0);
		item.setLength(0);
		item.setEndSiteId(Integer.toString(inlet.un));
		item.setParentId(cable.getId());
				
		for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(inlet)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject plan = ucmLink.parent;
				item.setTunnelId("site" + plan.un + "indoor"+inlet.un);
				item.setStartSiteId("site" + plan.un);
			}
		}
		return item;
	}
	
	UniCableMapObject getODFCableInlet(Cable cable, UniCableMapObject odf) throws SQLException {
		String building = getSiteId(odf);
		final List<Site> list = this.cableInlets.get(cable);
		if(list != null) {
			for(Site site : list) {
				if(site.getAttachmentSiteNodeId().equals(building)) {
					UniCableMapObject cableInlet = this.ucmDatabase.getObject(
							Integer.parseInt(site.getId()));
					return cableInlet;
				}
			}
		}
		System.out.println("Не найден кабельный ввод для ODF " + odf.un);
		return null;
	}
	
	ChannelingItem createEndChannelingItem(Cable cable, UniCableMapObject inlet) throws SQLException {
		ChannelingItem item = new ChannelingItem("cci" + inlet.un + "end" + cable.getId());
		item.setRowX(0);
		item.setPlaceY(0);
		item.setLength(0);
		item.setStartSiteId(Integer.toString(inlet.un));
		item.setParentId(cable.getId());
				
		for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(inlet)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject plan = ucmLink.parent;
				item.setTunnelId("site" + plan.un + "indoor"+inlet.un);
				item.setEndSiteId("site" + plan.un);
			}
		}
		return item;
	}
	
	ChannelingItem createChannelingItem(Cable cable, UniCableMapObject ucmObject) throws SQLException {  // место кабеля
		ChannelingItem item = new ChannelingItem(Integer.toString(ucmObject.un));
		item.setParentId(cable.getId());
		
		for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
						
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
				UniCableMapObject truba = ucmLink.parent;
				for(UniCableMapLink ucmLink1 : this.ucmDatabase.getParents(truba)) {
					if(ucmLink1.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
						UniCableMapObject ucmBlock = ucmLink1.parent;
						int pox = 0;
						int poy = 0;
						boolean leftToRight = true;
						boolean topToBottom = false;
						for(UniCableMapParameter param : ucmBlock.buf.params) {
							if (param.realParameter.text.equals(UniCableMapParameter.UCM_X)) {
								pox = Integer.parseInt(param.value);
							} else if (param.realParameter.text.equals(UniCableMapParameter.UCM_Y)) {
								poy = Integer.parseInt(param.value);
							} else if(param.realParameter.text.equals(UniCableMapParameter.UCM_FROM_TOP)) {
								topToBottom = Boolean.parseBoolean(param.value);
							} else if(param.realParameter.text.equals(UniCableMapParameter.UCM_FROM_RIGHT)) {
								leftToRight = ! Boolean.parseBoolean(param.value);
							}
						}

						for(UniCableMapLink ucmLink2 : this.ucmDatabase.getParents(ucmBlock)) {
							if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
								UniCableMapObject razrez = ucmLink2.parent;
								for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(razrez)) {
									if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
										UniCableMapObject tunnel = ucmLink3.parent;
										item.setTunnelId(Integer.toString(tunnel.un));
										
										for(UniCableMapParameter param : tunnel.buf.params) {
											if(param.realParameter.text.equals(UniCableMapParameter.UCM_MAP_LENGTH)) {
												String dval = param.value.replace(',', '.');
												item.setLength(Double.parseDouble(dval));
											}
										}
										
										for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(tunnel)) {
											if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
												item.setStartSiteId(Integer.toString(ucmLink4.parent.un));
											} else if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
												item.setEndSiteId(Integer.toString(ucmLink4.parent.un));
											}
										}
										break;
									}
								}
								break;
							}
						}

//						if(ucmObject.un == 604919L) {
//							// in a link which has 2 blocks
//							int a = 0;
//						}
						Link link = this.links.get(item.getTunnelId());
						if(link != null) {
							Block block = link.getBindingBlocks().get(String.valueOf(ucmBlock.un));
							int seq = block.getPipeNumbers().get(String.valueOf(truba.un)).intValue();
							if(seq > pox * poy) {
								System.out.println("seq " + seq
										+ " is greater than " + pox
										+ " * " + poy
										+ " for truba " + truba.un
										+ " (" + truba.text + ") "
										+ " in block " + ucmBlock.un
										+ " (" + ucmBlock.text + ") ");
							}
							int rowX = (pox == 0) ? 0
									: (leftToRight) ? ((seq % pox) - 1) 
											: (pox - (seq % pox));
							if(rowX < 0) {
								rowX += pox;
							}
							int placeY = (pox == 0) ? 0
									: (topToBottom) ? ((seq - 1) / pox) 
											: (poy - ((seq - 1) / pox) - 1);
							item.setRowX(rowX);
							item.setPlaceY(placeY);
							item.setBlockId(block.getId());
							if(rowX == -1 || placeY == -1) {
								System.out.println("row " + rowX
										+ ", place " + placeY
										+ " for seq " + seq
										+ " at tunnel dimensions (" + pox
										+ ", " + poy + ")");
							}
						}
						else {
							item.setRowX(-1);
							item.setPlaceY(-1);
						}
						break;
					}
				}
				break;
			}
		}
		return item;
	}
	
	void createMuffTypes(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject ucmObject : objects) {
			MuffType type = new MuffType(ucmObject.un);
			type.setName(ucmObject.text);
			this.eqtypes.put(Integer.valueOf(ucmObject.un), type);
		}
		this.defaultEqtId = this.eqtypes.values().iterator().next().getId();
	}

	void createLinks(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject ucmObject : objects) {
			Link link = Link.parseLink(this.ucmDatabase, ucmObject, "dummy");
			this.links.put(Integer.toString(ucmObject.un), link);
		}
	}
	
	void createCableInlets(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject ucmObject : objects) {
			Site cableInlet = Site.parseSite(this.ucmDatabase, ucmObject, "dummy");
			this.sites.put(Integer.toString(ucmObject.un), cableInlet);
		}
	}
	
	void createCableTypes(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject ucmObject : objects) {
			CableType type = new CableType(String.valueOf(ucmObject.un));
			type.setName(ucmObject.text);
			this.cabletypes.put(Integer.valueOf(ucmObject.un), type);
		}
	}
	
	void createLinkTypes(Collection<UniCableMapObject> objects) {
		for (UniCableMapObject ucmObject : objects) {
			LinkType type = new LinkType(ucmObject.un);
			type.setName(ucmObject.text);
			this.linktypes.put(Integer.valueOf(ucmObject.un), type);
		}
	}
	
	void createMuffs(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject ucmObject : objects) {
			Element muf = new Element(ucmObject.un);
			muf.setName(ucmObject.text);
			muf.setLabel("MO");
			muf.setKind(Element.KIND_EQUIPMENTED);
			Equipment eq = new Equipment("eq" + ucmObject.un);
			eq.setName(ucmObject.text);
			eq.setLatitude((float)ucmObject.y0);
			eq.setLongitude((float)ucmObject.x0);
			muf.setEquipment(eq);
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(ucmObject)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_TYPE_REALIZATION)) {
//					 muf.setEqtId(ucmLink.parent.un);
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_KIND_HAS_KIND)) {
										
//					muf.setEquipmentTypeId(this.eqtypes.values().iterator().next().getId());
					eq.setTypeId(this.defaultEqtId);
//					 muf.setCodename(Integer.toString(ucmLink.parent.un));
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject wellLarge = ucmLink.parent;
					for(UniCableMapLink ucmLink2 : this.ucmDatabase.getParents(wellLarge)) {
						if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
							muf.setWellId(Integer.toString(ucmLink2.parent.un));
						}
					}
				}
			}

			int inCablePorts = 0, outCablePorts = 0;
			Map<UniCableMapObject, SimplePort> vol2port = new HashMap<UniCableMapObject, SimplePort>();
//			Map<UniCableMapObject, SimplePort> vol2portOut = new HashMap<UniCableMapObject, SimplePort>();
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(ucmObject)) {
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
//					 muf.addOutputCablePort(String.valueOf(ucmLink.child.un + "^" + muf.getId()));
//					 outPorts++;
//				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
//					 muf.addInputCablePort(String.valueOf(ucmLink.child.un + "^" + muf.getId()));
//					 inPorts++;
//				} else 
					if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
					UniCableMapObject raspaika = ucmLink.child;
					for(UniCableMapLink ucmLink2 : this.ucmDatabase.getChildren(raspaika)) {
						if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
							// для каждого разреза создаем внутренний ВРМ
							UniCableMapObject razrez = ucmLink2.child;
							Element vrm = new Element(razrez.un);
							vrm.setEquipmentTypeId(UCM_ODF_EQT);
							vrm.setKind(Element.KIND_EQUIPMENTED);
							vrm.setName("(ВРМ" + (inCablePorts + outCablePorts + 1) + ") " + muf.getName());
							vrm.setLabel("КП");
							muf.addElement(vrm);
							
							boolean isSource = false;
							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(razrez)) {
								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CABLE_LAYOUT)) {
									UniCableMapObject cable = ucmLink3.parent;
									for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(cable)) {
										if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
											UniCableMapObject muff = ucmLink4.parent;
											if (muff.un == ucmObject.un) {
												isSource = false;
												//	создаем кабельный ввод
												Port port = vrm.addInputCablePort(Integer.toString(ucmObject.un) + "^" + vrm.getId());
												inCablePorts++;
												port.setName(inCablePorts + "i");
											}
										}
										if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
											UniCableMapObject muff = ucmLink4.parent;
											if (muff.un == ucmObject.un) {
												isSource = true;
												//	создаем кабельный ввод
												Port port = vrm.addOutputCablePort(Integer.toString(ucmObject.un) + "^" + vrm.getId());
												outCablePorts++;
												port.setName(outCablePorts + "o");
											}
										}
									}
								}
							}

							vrm.initCounter();
							// для каждого волокна создаем порт 
							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getChildren(razrez)) {
								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
									UniCableMapObject volokno = ucmLink3.child;
									if (isSource) {
										SimplePort p = vrm.addInputPort(Integer.toString(volokno.un) + "^" + vrm.getId(), SimplePort.SPLICE_PORT_TYPE);
										vol2port.put(volokno, p);
									} else {
										SimplePort p = vrm.addOutputPort(Integer.toString(volokno.un) + "^" + vrm.getId(), SimplePort.SPLICE_PORT_TYPE);
										vol2port.put(volokno, p);
//										vol2portOut.put(volokno, p);
									}
								}
							}
						}
					}
				}
			}
			// create Links
		
			for(UniCableMapObject vol : new HashSet<UniCableMapObject>(vol2port.keySet())) {
				SimplePort inPort = vol2port.get(vol);
				if (inPort != null) {
					
					
					SimplePort outPort = null;
					for(UniCableMapLink ucmLink3 : this.ucmDatabase.getChildren(vol)) {
						if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_SOURCE_TARGET)) {
							UniCableMapObject volokno = ucmLink3.child;
//							outPort = vol2portOut.get(volokno);
							outPort = vol2port.get(volokno);
							if (outPort != null) {
								vol2port.remove(vol);
								vol2port.remove(volokno);
								break;
							}
						}
					}
					
					Integer typeId =  this.linktypes.keySet().iterator().next();
					if (outPort != null) {
						com.syrus.impexp.unicablemap.objects.Link link = 
							new com.syrus.impexp.unicablemap.objects.Link(outPort.getId() + "-" + inPort.getId());
						link.setEndPortId(inPort.getId());
						link.setStartPortId(outPort.getId());
						link.setName("OЛ" + inPort.getName());
						link.setTypeId(typeId);
						muf.addLink(link);
					} else if (vol2port.containsKey(vol)) {
						System.err.println("Complimentary port not found can't create internal link for " + ucmObject);
					} 
				} 
			}
				
			
			if (inCablePorts + outCablePorts > 0) {
				this.equipments.add(eq);
				this.muffs.put(Integer.valueOf(ucmObject.un), muf);
			}
			if (inCablePorts == 0) {
				System.out.println("Муфта " + ucmObject.text + " [" + ucmObject.un + "]" +  " не имеет конечного кабеля");
			}
			if (outCablePorts == 0) {
				System.out.println("Муфта " + ucmObject.text + " [" + ucmObject.un + "]" +  " не имеет начального кабеля");
			}
		}
	}
	
	void createVrms(Collection<UniCableMapObject> ucmFloors) throws SQLException {
		for (UniCableMapObject parentFloor : ucmFloors) {
			Element floor = this.floors.get(Integer.valueOf(parentFloor.un));
			//			Set<UniCableMapObject> currentVrms = new HashSet<UniCableMapObject>();
			Integer inCablePorts = new Integer(0), outCablePorts = new Integer(0);
			Map<UniCableMapObject, SimplePort> vol2port = new HashMap<UniCableMapObject, SimplePort>();
						
			for(UniCableMapLink ucmLink_1 : this.ucmDatabase.getChildren(parentFloor)) {
				if(ucmLink_1.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject obj = ucmLink_1.child;
					if (obj.typ.text.equals(UniCableMapType.UCM_FRAME)) {
						UniCableMapObject parent = obj;
						for(UniCableMapLink ucmLink_2 : this.ucmDatabase.getChildren(obj)) {
							if(ucmLink_2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
								UniCableMapObject odf = ucmLink_2.child;
								Element rack = this.racks.get(Integer.valueOf(parent.un));
								createVrm(odf, rack, vol2port, inCablePorts, outCablePorts);
							}
						}
					} else if (obj.typ.text.equals(UniCableMapType.UCM_ODF)) {
						UniCableMapObject parent = parentFloor;
						UniCableMapObject odf = obj;
						Element rack = this.racks.get(Integer.valueOf(parent.un));
						createVrm(odf, rack, vol2port, inCablePorts, outCablePorts);
					}
				}
			}
			// create Links
		
			for(UniCableMapObject vol : new HashSet<UniCableMapObject>(vol2port.keySet())) {
				SimplePort inPort = vol2port.get(vol);
				if (inPort != null) {
					
					
					SimplePort outPort = null;
					for(UniCableMapLink ucmLink3 : this.ucmDatabase.getChildren(vol)) {
						if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_SOURCE_TARGET)) {
							UniCableMapObject volokno = ucmLink3.child;
//							outPort = vol2portOut.get(volokno);
							outPort = vol2port.get(volokno);
							if (outPort != null) {
								vol2port.remove(vol);
								vol2port.remove(volokno);
								break;
							}
						}
					}
					
					Integer typeId =  this.linktypes.keySet().iterator().next();
					if (outPort != null) {
						com.syrus.impexp.unicablemap.objects.Link link = 
							new com.syrus.impexp.unicablemap.objects.Link(outPort.getId() + "-" + inPort.getId());
						link.setEndPortId(inPort.getId());
						link.setStartPortId(outPort.getId());
						link.setName("OЛ" + inPort.getName());
						link.setTypeId(typeId);
						floor.addLink(link);
					} else if (vol2port.containsKey(vol)) {
						System.err.println("Complimentary port not found can't create internal link for " + parentFloor);
					}
				}
			}
		}
	}
	
	private void createVrm(UniCableMapObject odf, Element rack, Map<UniCableMapObject, SimplePort> vol2portIn, Integer inCablePorts, Integer outCablePorts) throws SQLException {
		for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(odf)) {
			if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_GENERALIATION_DETALIZATION)) {
				UniCableMapObject planODF = ucmLink.child;
				for(UniCableMapLink ucmLink2 : this.ucmDatabase.getChildren(planODF)) {
					if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
						// для каждого разреза создаем внутренний ВРМ
						UniCableMapObject razrez = ucmLink2.child;
						if (razrez.typ.text.equals(UniCableMapType.UCM_CABLE_PROFILE)) {
							Element vrm = new Element(razrez.un);
							vrm.setKind(Element.KIND_EQUIPMENTED);
							vrm.setEquipmentTypeId(UCM_ODF_EQT);
							vrm.setName("ВРМ " + (inCablePorts.intValue() + outCablePorts.intValue() + 1) + odf.text);
							vrm.setLabel("КП");
							rack.addElement(vrm);
							
							boolean isSource = false;
							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(razrez)) {
								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CABLE_LAYOUT)) {
									UniCableMapObject cable = ucmLink3.parent;
									for(UniCableMapLink ucmLink4 : this.ucmDatabase.getParents(cable)) {
										if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
											UniCableMapObject odf1 = ucmLink4.parent;
											if (odf1.un == odf.un) {
												isSource = false;
												//	создаем кабельный ввод
												Port port = vrm.addInputCablePort(Integer.toString(odf.un) + "^" + vrm.getId());
												inCablePorts = new Integer(inCablePorts.intValue() + 1);
												port.setName(inCablePorts + "i");
											}
										}
										if(ucmLink4.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
											UniCableMapObject odf1 = ucmLink4.parent;
											if (odf1.un == odf.un) {
												isSource = true;
												//	создаем кабельный ввод
												Port port = vrm.addOutputCablePort(Integer.toString(odf.un) + "^" + vrm.getId());
												outCablePorts = new Integer(outCablePorts.intValue() + 1);
												port.setName(outCablePorts + "o");
											}
										}
									}
								}
							}
							
//							if (inCablePorts + outCablePorts == 0) {
//								if (inCablePorts == 0) {
//									System.out.println("ВРМ " + odf.text + " [" + odf.un + "]" +  " не имеет конечного кабеля");
//								} else {
//									System.out.println("ВРМ " + odf.text + " [" + odf.un + "]" +  " не имеет начального кабеля");
//								}
//							}
							
							vrm.initCounter();
							// для каждого волокна создаем порт 
							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getChildren(razrez)) {
								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
									UniCableMapObject volokno = ucmLink3.child;
									if (isSource) {
										SimplePort p = vrm.addInputPort(Integer.toString(volokno.un) + "^" + vrm.getId(), SimplePort.OPTICAL_PORT_TYPE);
										vol2portIn.put(volokno, p);
									} else {
										SimplePort p = vrm.addOutputPort(Integer.toString(volokno.un) + "^" + vrm.getId(), SimplePort.OPTICAL_PORT_TYPE);
										vol2portIn.put(volokno, p);
//										vol2portOut.put(volokno, p);
									}
								}
							}
							
							if (vrm.getAllPorts().size() > 0) {
								this.vrms.put(Integer.valueOf(odf.un), vrm);
							}
						}
					}
				}
			}
		}
	}
		
	void createBuildingsWithInternals(Collection<UniCableMapObject> objects) throws SQLException {
		for (UniCableMapObject plan : objects) {
			Element building = new Element(plan.un);
			Equipment eq = new Equipment("eq" + plan.un);
			eq.setName(plan.text);
			eq.setLatitude((float)plan.y0);
			eq.setLongitude((float)plan.x0);
			eq.setTypeId(UCM_SCHEMED_EQT);
			building.setEquipment(eq);
			this.equipments.add(eq);
			
			building.setWellId("site" + plan.un);
			building.setName(plan.text);
			building.setLabel(plan.text);
			this.buildings.put(Integer.valueOf(plan.un), building);
			building.setKind(Element.KIND_SCHEMED);
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(plan)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject floor = ucmLink.child;
					if (floor.typ.text.equals(UniCableMapType.UCM_FLOOR)) {
						Element floorElement = new Element(floor.un);
						floorElement.setName(floor.text);
						floorElement.setLabel(floor.text);
						floorElement.setKind(Element.KIND_SCHEMED);
						floorElement.setEquipmentTypeId(UCM_SCHEMED_EQT);
						
						building.addElement(floorElement);
						this.floors.put(Integer.valueOf(floor.un), floorElement);
						
						for(UniCableMapLink ucmLink2 : this.ucmDatabase.getChildren(floor)) {
							if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
								UniCableMapObject rack = ucmLink2.child;
								
								if (rack.typ.text.equals(UniCableMapType.UCM_FRAME)) {
									Element frame = new Element(rack.un);
									frame.setName(rack.text);
									frame.setLabel(rack.text);
									frame.setKind(Element.KIND_EQUIPMENTED);
									frame.setEquipmentTypeId(UCM_FRAME_EQT);
									
									floorElement.addElement(frame);
									this.racks.put(Integer.valueOf(rack.un), frame);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private String getFreePort(UniCableMapObject cable, DirectionType.Enum directionType, UniCableMapObject obj) throws SQLException {
		if (obj.typ.text.equals(UniCableMapType.UCM_MUFF)) {
			/*Element muf = (Element)this.muffs.get(obj.un);
			for(UniCableMapLink ucmLink : this.ucmDatabase.getChildren(obj)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS)) {
					 return muf.addInputPort(cable.getId() + "on" + obj.un).getId();
				} else if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
					 return muf.addOutputPort(cable.getId() + "on" + obj.un).getId();
				}
			}*/
			Element muff = this.muffs.get(Integer.valueOf(obj.un));
			Collection<Port> outPorts = muff.getPorts(directionType);
			for (Port port : outPorts) {
				if (!port.isConnected()) {
					port.setConnected(true);
					return port.getId();
				}
				System.out.println("Port already connected");
			}
		} else if (obj.typ.text.equals(UniCableMapType.UCM_CABLE_INLET)) {
			
			UniCableMapObject odf = null;
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(cable)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_START_STARTS) || 
					ucmLink.mod.text.equals(UniCableMapLinkType.UCM_END_ENDS)) {
					UniCableMapObject child = ucmLink.parent;
					if (child.typ.text.equals(UniCableMapType.UCM_ODF)) {
						odf = child;
						break;
					}
				}
			}
			Element vrm = this.vrms.get(Integer.valueOf(odf.un));
			Collection<Port> outPorts = vrm.getPorts(directionType);
			for (Port port : outPorts) {
				if (!port.isConnected()) {
					port.setConnected(true);
					return port.getId();
				}
				System.out.println("Port already connected");
			}
			
			/*for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(odf)) {
				
				
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject stoika = ucmLink.parent;
					Element rack = this.racks.get(Integer.valueOf(stoika.un));
					// add vrm to rack
					String portId = createVrmForPort(obj, directionType, odf, rack, cable);
					return portId;
				}
			}*/

//			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(obj)) {
//				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
//					 Element building = this.buildings.get(Integer.valueOf(ucmLink.parent.un));
//					 Port port;
//					 if (directionType.equals(DirectionType.IN)) {
//						 port = building.addInputCablePort(cable.un + "^" + obj.un);
//						 port.setDescription(obj.text);
//					 } else {
//						 port = building.addOutputCablePort(cable.un + "^" + obj.un);
//						 port.setDescription(obj.text);
//					 }
//					 return port.getId();
//				}
//			}
		} else if (obj.typ.text.equals(UniCableMapType.UCM_ODF)) {
			Element vrm = this.vrms.get(Integer.valueOf(obj.un));
			Collection<Port> outPorts = vrm.getPorts(directionType);
			for (Port port : outPorts) {
				if (!port.isConnected()) {
					port.setConnected(true);
					return port.getId();
				}
				System.out.println("Port already connected");
			}

			
			/*Cable c = this.cables.get(Integer.valueOf(cable.un));
			UniCableMapObject inlet = getODFCableInlet(c, obj);
			
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(obj)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject stoika = ucmLink.parent;
					
					Element rack = this.racks.get(Integer.valueOf(stoika.un));
					// add vrm to rack
					String portId = createVrmForPort(inlet, directionType, obj, rack, cable);
					return portId;*/
					
//					for(UniCableMapLink ucmLink2 : this.ucmDatabase.getParents(stoika)) {
//						if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
//							UniCableMapObject floor = ucmLink2.parent;
//							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(floor)) {
//								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
//									UniCableMapObject plan = ucmLink3.parent;
//									Element building = this.buildings.get(Integer.valueOf(plan.un));
//									 Port port;
//									 if (directionType.equals(DirectionType.IN)) {
//										 port = building.addInputCablePort(cable.un + "^" + obj.un);
//										 port.setDescription(obj.text);
//									 } else {
//										 port = building.addOutputCablePort(cable.un + "^" + obj.un);
//										 port.setDescription(obj.text);
//									 }
//									 return port.getId();
//								}
//							}
//						}
//					}
//				}
//			}
		} else {
			System.out.println("Кабель " + cable.text + " присоединен к " + obj.typ.text + ": " + obj.text + " [" + obj.un + "]");
			return null;
		}
		System.err.println("No free port found for " + obj.text);
		return null;
	}
	/*
	private String createVrmForPort(UniCableMapObject inlet, DirectionType.Enum directionType, 
			UniCableMapObject odf, Element rack, UniCableMapObject ucmCable) {
		Element vrm = new Element(odf.un);
		vrm.setName(odf.text);
		vrm.setLabel(odf.text);
		vrm.setKind(Element.KIND_EQUIPMENTED);
		vrm.setEquipmentTypeId(UCM_ODF_EQT);
		rack.addElement(vrm);
		
		Port port;
		int un = (inlet == null) ? odf.un : inlet.un;
		String text = (inlet == null) ? odf.text : inlet.text;
		
		if (directionType.equals(DirectionType.IN)) {
			port = vrm.addInputCablePort(ucmCable.un + "^" + un);
			port.setDescription(text);
		} else {
			port = vrm.addOutputCablePort(ucmCable.un + "^" + un);
			port.setDescription(text);
		}
		Cable cable = this.cables.get(Integer.valueOf(ucmCable.un));
		this.portCableMapping.put(port, cable);
		return port.getId();
	}*/
	
	private String getSiteId(UniCableMapObject obj) throws SQLException {
		if (obj.typ.text.equals(UniCableMapType.UCM_MUFF)) {
			Element muff = this.muffs.get(Integer.valueOf(obj.un));
			return muff.getWellId();
		} else if (obj.typ.text.equals(UniCableMapType.UCM_ODF)) {
			for(UniCableMapLink ucmLink : this.ucmDatabase.getParents(obj)) {
				if(ucmLink.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
					UniCableMapObject stoika = ucmLink.parent;
					for(UniCableMapLink ucmLink2 : this.ucmDatabase.getParents(stoika)) {
						if(ucmLink2.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
							UniCableMapObject floor = ucmLink2.parent;
							for(UniCableMapLink ucmLink3 : this.ucmDatabase.getParents(floor)) {
								if(ucmLink3.mod.text.equals(UniCableMapLinkType.UCM_CONTAINS_INSIDE)) {
									UniCableMapObject plan = ucmLink3.parent;
									Element building = this.buildings.get(Integer.valueOf(plan.un));
									return building.getWellId();
								}
							}
						}
					}
				}
			}
		}
		return "";
	}
	
	@SuppressWarnings("unchecked")
	private void saveConfigXML(String fileName) {
		System.out.println("Start saving config XML");
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/config/xml", "config");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		XmlConfigurationLibrary doc = XmlConfigurationLibrary.Factory.newInstance(xmlOptions);
		XmlIdentifier uid = doc.addNewId();
		uid.setStringValue("ucm_config_library");
		doc.setName("UCM types");
		doc.setCodename("UCM types");
		doc.setImportType("ucm");

		XmlLinkTypeSeq xmlLinkTypes = doc.addNewLinkTypes();
		XmlCableLinkTypeSeq xmlCableLinkTypes = doc.addNewCableLinkTypes();
		XmlProtoEquipmentSeq xmlProtoEquipments = doc.addNewProtoEquipments();
		XmlEquipmentSeq xmlEquipments = doc.addNewEquipments();

		Collection<XmlLinkType> lts1 = new ArrayList<XmlLinkType>(this.linktypes.size());
		for (LinkType linkType : this.linktypes.values()) {
			lts1.add(linkType.toXMLObject());
		}
		xmlLinkTypes.setLinkTypeArray(lts1.toArray(new XmlLinkType[lts1.size()]));

		Collection<XmlCableLinkType> lts = new ArrayList<XmlCableLinkType>(this.cabletypes.size());
		for (CableType cableType : this.cabletypes.values()) {
			lts.add(cableType.toXMLObject());
		}
		xmlCableLinkTypes.setCableLinkTypeArray(lts.toArray(new XmlCableLinkType[lts.size()]));
		
		Collection<XmlProtoEquipment> eqts = new ArrayList<XmlProtoEquipment>(this.eqtypes.size());
		for (MuffType eqType : this.eqtypes.values()) {
			eqts.add(eqType.toXMLObject());
		}
		xmlProtoEquipments.setProtoEquipmentArray(eqts.toArray(new XmlProtoEquipment[eqts.size()]));
		
		Collection<XmlEquipment> eqs = new ArrayList<XmlEquipment>(this.equipments.size());
		for (Equipment eq : this.equipments) {
			eqs.add(eq.toXMLObject());
		}
		xmlEquipments.setEquipmentArray(eqs.toArray(new XmlEquipment[eqs.size()]));

		System.out.println("Check if XML valid...");
		boolean isXmlValid = UCMParser.validateXml(doc);
		if(isXmlValid) {
			System.out.println("Done successfully");
			File f = new File(fileName + configFileName);

			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
			} catch(IOException e) {
				e.printStackTrace();
			}
			System.out.println("\nXML Instance Document saved at : " + f.getPath());
		} else {
			System.out.println("Done with errors");
		}
	}
	
	@SuppressWarnings("unchecked")
	private void saveSchemeXML(String fileName, int number) {
		System.out.println("Start saving scheme XML");
		
		XmlOptions xmlOptions = new XmlOptions();
		xmlOptions.setSavePrettyPrint();
		java.util.Map prefixes = new HashMap();
		prefixes.put("http://syrus.com/AMFICOM/scheme/xml", "scheme");
		xmlOptions.setSaveSuggestedPrefixes(prefixes);
		
		SchemesDocument doc = SchemesDocument.Factory.newInstance(xmlOptions);
		
		XmlSchemeSeq xmlSchemes = doc.addNewSchemes();
		XmlScheme xmlScheme = xmlSchemes.addNewScheme();
		XmlIdentifier uid = xmlScheme.addNewId();
		uid.setStringValue("ucm_top_scheme");
		
		xmlScheme.setName("UCM top scheme");
//		xmlScheme.setDescription("");
		xmlScheme.setKind(Kind.NETWORK);
		xmlScheme.setWidth(840);
		xmlScheme.setHeight(1190);
		xmlScheme.setImportType("ucm");
		
		XmlSchemeElementSeq xmlSchemeElements = xmlScheme.addNewSchemeElements();
		XmlSchemeCableLinkSeq xmlSchemeCableLinks = xmlScheme.addNewSchemeCableLinks();
//		SchemeLinks xmlSchemeLinks = xmlScheme.addNewSchemelinks();

		int counter = 0;
		Collection<XmlSchemeElement> ses = new ArrayList<XmlSchemeElement>(this.muffs.size() + this.buildings.size());
		for (Element muff : this.muffs.values()) {
			if (counter++ > number) {
				break;
			}
			ses.add(muff.toXMLObject(xmlScheme.getId(), true));
		}
		counter = 0;
		for (Element building : this.buildings.values()) {
			if (counter++ > number) {
				break;
			}
			if (!building.getElements().isEmpty()) {
				ses.add(building.toXMLObject(xmlScheme.getId(), true));
			} else {
				System.out.println("Элемент " + building.getName() + " не содержит внутренностей. Пропускаю запись в XML");
			}
		}
		xmlSchemeElements.setSchemeElementArray(ses.toArray(new XmlSchemeElement[ses.size()]));

		counter = 0;
		Collection<XmlSchemeCableLink> cls = new ArrayList<XmlSchemeCableLink>(this.cables.size());
		for (Cable cable : this.cables.values()) {
			if (counter++ > number) {
				break;
			}
			cls.add(cable.toXMLObject(xmlScheme.getId()));
		}
		xmlSchemeCableLinks.setSchemeCableLinkArray(cls.toArray(new XmlSchemeCableLink[cls.size()]));
		
		
		System.out.println("Check if XML valid...");
		boolean isXmlValid = UCMParser.validateXml(doc);
		if(isXmlValid) {
			System.out.println("Done successfully");
			File f = new File(fileName + schemeFileName);

			try {
				// Writing the XML Instance to a file.
				doc.save(f, xmlOptions);
				System.out.println("\nXML Instance Document saved at : " + f.getPath());
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Done with errors");
		}
	}

	public void execute() {
		this.cabletypes.clear();
		this.linktypes.clear();
		this.eqtypes.clear();
		this.equipments.clear();
		this.muffs.clear();
		this.buildings.clear();
		this.cables.clear();
		this.links.clear();
		this.sites.clear();
		this.cableInlets.clear();

		try {
			processTypeObjects();
			processMapObjects();
			processSchemeObjects(Integer.MAX_VALUE);
			
			int pos = this.filename.lastIndexOf(".");
			if (pos != -1) {
				this.filename = this.filename.substring(0, pos);
			}
			saveConfigXML(this.filename);
			saveSchemeXML(this.filename, Integer.MAX_VALUE);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
	}
}
