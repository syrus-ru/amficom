/*-
 * $Id: DataSourceImage.java,v 1.9 2005/05/04 10:42:40 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.util.CacheLock;
import com.syrus.util.CacheLockObject;
import com.syrus.util.NIOCacheLock;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/05/04 10:42:40 $
 * @module generalclient_v1
 */
public class DataSourceImage {
	class ResourceDescriptor implements Serializable {
		private static final long serialVersionUID = 01L;

		String id;

		long modified;

		Serializable object;

		public ResourceDescriptor(String id, long modified,
				Serializable object) {
			this.id = id;
			this.modified = modified;
			this.object = object;
		}

		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException {
			id = (String) in.readObject();
			modified = in.readLong();
			object = (Serializable) in.readObject();
		}

		private void writeObject(java.io.ObjectOutputStream out)
				throws IOException {
			out.writeObject(id);
			out.writeLong(modified);
			out.writeObject(object);
		}
	}

	private static Hashtable catalog = new Hashtable();

	private static Hashtable loaded_catalog = new Hashtable();

	private static CacheLock lock = new NIOCacheLock();

	DataSourceInterface di;

	public DataSourceImage(DataSourceInterface di) {
		this.di = di;
		// пролучить ссылку на каталог объектов на локальном диске
	}

	private static ObjectInputStream createInputStream(InputStream f)
			throws Exception {
		InputStream f2 = new GZIPInputStream(f);
		return new ObjectInputStream(f2);
	}

	private static ObjectOutputStream createOutputStream(OutputStream f)
			throws Exception {
		OutputStream f2 = new GZIPOutputStream(f);
		return new ObjectOutputStream(f2);
	}

	// получить список идентификаторов объектов, отсутствующих в отображении,
	// которые, следовательно, необходимо загрузить с сервера
	protected Vector filter(String type,
			ResourceDescriptor_Transferable[] desc,
			boolean del_abscent) {
		ResourceDescriptor rd;
		Vector vec = new Vector();

		Hashtable cat = (Hashtable) catalog.get(type);
		Vector v = new Vector();// for del_abscent
		if (cat != null)
			for (Enumeration e = cat.keys(); e.hasMoreElements();)
				v.add(e.nextElement());

		if (desc != null)
			for (int i = 0; i < desc.length; i++) {
				if (desc[i] == null) {
					continue;
				}

				if (cat != null)
					rd = (ResourceDescriptor) cat
							.get(desc[i].resource_id);
				else
					rd = null;

				if (rd == null)
					vec.add(desc[i].resource_id);
				else if (rd.modified < desc[i].resource_modified)
					vec.add(desc[i].resource_id);
				v.remove(desc[i].resource_id);// for del_abscent
			}

		if (del_abscent) {
			for (int i = 0; i < v.size(); i++)
				remove(type, (String) v.get(i));
		}

		return vec;
	}

	// получить с сервера список описателей для объектов фиксированного типа	
	protected ResourceDescriptor_Transferable[] GetDescriptors(String type) {
		if (!(di.getSession() instanceof RISDSessionInfo))
			return new ResourceDescriptor_Transferable[]{new ResourceDescriptor_Transferable(
					"id", System.currentTimeMillis())};

		RISDSessionInfo si = (RISDSessionInfo) di.getSession();

		if (si == null)
			return null;
		if (!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try {
			ecode = si.ci.getServer().GetResourceDescriptors(
					si.getAccessIdentity(), type, rdh);
		} catch (Exception ex) {
			System.err.print("Error getting descriptors: "
					+ ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != ErrorCode._ERROR_NO_ERROR) {
			System.out.println("Failed GetDescriptors! status = "
					+ ecode);
			return null;
		}
		return rdh.value;
	}

	// получить каталог сераилизованных объектов фиксированного типа
	protected void load(String type) {
		loadFromPool(type);

		Boolean lc = (Boolean) loaded_catalog.get(type);
		if (lc != null && lc.booleanValue())
			return;
		loaded_catalog.put(type, Boolean.FALSE);

		Hashtable hc = (Hashtable) catalog.get(type);

		String name = "resources/directory/"
				+ di.getSession().getConnectionInterface()
						.getServerName().replace(':',
								'-').replace(
								'/', '_') + "/"
				+ type + "s.dsi";

		Object obj = null;

		CacheLockObject lock_result = null;

		try {
			lock_result = lock.lockRead(name);
			ObjectInputStream in = createInputStream((InputStream) lock_result
					.getResource());
			obj = in.readObject();
			in.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} catch (NullPointerException ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} catch (StreamCorruptedException ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} catch (Exception ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} finally {
			if (lock_result != null)
				lock.releaseRead(lock_result);
		}
		Hashtable h2 = (Hashtable) obj;
		if (h2 == null)
			h2 = new Hashtable();

		Vector vec = new Vector();
		for (Enumeration e = hc.elements(); e.hasMoreElements();) {
			ResourceDescriptor rd = (ResourceDescriptor) e
					.nextElement();
			vec.add(rd.id);
		}

		for (Enumeration e = h2.elements(); e.hasMoreElements();) {
			ResourceDescriptor rd = (ResourceDescriptor) e
					.nextElement();
			ObjectResource or = (ObjectResource) rd.object;
			if (!vec.contains(rd.id)) {
				hc.put(or.getId(), rd);
				Pool.put(type, or.getId(), or);
			}
		}

		Map map = Pool.getMap("serverimage");
		if (map != null)
			for (Iterator iterator = map.values().iterator(); iterator
					.hasNext();)
				((ObjectResource) (iterator.next()))
						.updateLocalFromTransferable();
		Pool.removeMap("serverimage");
		loaded_catalog.put(type, Boolean.TRUE);
	}

	// сохранить каталог сераилизованных объектов фиксированного типа
	protected void save(String type) {
		Map map = Pool.getMap(type);
		if (map == null)
			map = new Hashtable();
		Hashtable h2 = (Hashtable) catalog.get(type);
		if (h2 == null) {
			h2 = new Hashtable();
			catalog.put(type, h2);
		}
		for (Iterator iterator = map.values().iterator(); iterator
				.hasNext();) {
			ObjectResource or = (ObjectResource) (iterator.next());
			h2.put(or.getId(), new ResourceDescriptor(or.getId(),
					or.getModified(), (Serializable) or));
		}
		String name = "resources/directory/"
				+ di.getSession().getConnectionInterface()
						.getServerName().replace(':',
								'-').replace(
								'/', '_') + "/"
				+ type + "s.dsi";
		CacheLockObject lock_result = null;
		ObjectOutputStream out;

		try {
			lock_result = lock.lockWrite(name);
			out = createOutputStream((OutputStream) lock_result
					.getResource());
			out.writeObject(h2);
			out.flush();
			out.close();
		} catch (NullPointerException ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} catch (Exception ex) {
			System.out.println("Catalogue: " + type + ": "
					+ ex.getMessage());
		} finally {
			if (lock_result != null)
				lock.releaseWrite(lock_result);
		}
	}

	// получить каталог сераилизованных объектов фиксированного типа
	private void loadFromPool(String type) {
		Map map = Pool.getMap(type);
		if (map == null)
			map = new Hashtable();
		Hashtable hashtable = new Hashtable();
		for (Iterator iterator = map.values().iterator(); iterator
				.hasNext();) {
			ObjectResource or = (ObjectResource) (iterator.next());
			hashtable.put(or.getId(), new ResourceDescriptor(or
					.getId(), or.getModified(),
					(Serializable) or));
		}
		catalog.put(type, hashtable);
	}

	// удалить из каталога объект
	private void remove(String type, String id) {
		Hashtable h = (Hashtable) catalog.get(type);
		if (h == null)
			return;
		h.remove(id);

		Pool.remove(type, id);
	}
}
