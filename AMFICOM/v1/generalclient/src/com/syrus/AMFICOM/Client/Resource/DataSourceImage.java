//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс хранения отображения БД на клиентскую часть          * //
// *           задача модуля - для минимизации трафика клиент-сервер      * //
// *           хранить подгружаемые с сервера объекты, так что при        * //
// *           последующем запуске клиентской части проверяется образ     * //
// *           на наличие необходимых объектов, и в случае их отсутствия  * //
// *           они подгружаются с сервера                                 * //
// *                                                                      * //
// * Тип: Java 1.4.0                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 24 mar 2003                                                      * //
// * Расположение: ISM\prog\java\AMFICOM\com\syrus\AMFICOM\Client\        * //
// *        Resource\DataSourceImage.java                                 * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 9.0.3.9.93                       * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.4.0)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptorSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ResourceDescriptor_Transferable;
import com.syrus.AMFICOM.Client.General.ConnectionInterface;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;

import com.syrus.util.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.io.FileNotFoundException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DataSourceImage
{
	protected DataSourceInterface di;
	protected static Hashtable catalog = new Hashtable();
	protected static Hashtable loaded_catalog = new Hashtable();

	protected static CacheLock lock = new NIOCacheLock();

	protected DataSourceImage()
	{
	}
	
	public DataSourceImage(DataSourceInterface di)
	{
		this.di = di;
		// пролучить ссылку на каталог объектов на локальном диске
//		load();
	}

	// получить список идентификаторов объектов, отсутствующих в отображении,
	// которые, следовательно, необходимо загрузить с сервера
	protected Vector filter(String type, ResourceDescriptor_Transferable[] desc, boolean del_abscent)
	{
		ResourceDescriptor rd;
		Vector vec = new Vector();

		Hashtable cat = (Hashtable )catalog.get(type);
		Vector v = new Vector();// for del_abscent
		if(cat != null)
			for(Enumeration e = cat.keys(); e.hasMoreElements();)
				v.add(e.nextElement());

		if(desc != null)
			for(int i = 0; i < desc.length; i++)
			{
				if(desc[i] == null)
				{
					continue;
				}

				if(cat != null)
					rd = (ResourceDescriptor )cat.get(desc[i].resource_id);
				else
					rd = null;

				if(rd == null)
					vec.add(desc[i].resource_id);
				else
				if(rd.modified < desc[i].resource_modified)
					vec.add(desc[i].resource_id);
				v.remove(desc[i].resource_id);// for del_abscent
			}

		if(del_abscent)
		{
			for(int i = 0; i < v.size(); i++)
				remove(type, (String )v.get(i));
		}

		return vec;
	}

	protected Vector filter(String type, ResourceDescriptor_Transferable[] desc)
	{
		return filter(type, desc, false);
	}
	
	// получить каталог сераилизованных объектов
	protected void load()
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();

//		catalog = new Hashtable();
	}

	// получить каталог сераилизованных объектов фиксированного типа
	protected void load2(String type)
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();
		String s = ci.getServiceURL();
		String s1 = s.replace(':', '-');
		String s2 = s1.replace('/', '_');
		String name = "resources/directory/" + s2 + "/" + type + "s.dsi";

        Object obj = null;
		
		CacheLockObject lock_result = null;
		
        try
        {
			lock_result = lock.lockRead(name);
//            ObjectInputStream in = createInputStream(name);
            ObjectInputStream in = createInputStream((InputStream )lock_result.getResource());
            obj = in.readObject();
//            in.close();
        }
        catch (NullPointerException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
		}
        catch (Exception ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
        }
		finally
		{
			if(lock_result != null)
				lock.releaseRead(lock_result);
		}
		Hashtable h2 = (Hashtable )obj;
		if(h2 == null)
			h2 = new Hashtable();
			
		catalog.put(type, h2);

		for(Enumeration e = h2.elements(); e.hasMoreElements();)
		{
			ResourceDescriptor rd = (ResourceDescriptor )e.nextElement();
			ObjectResource or = (ObjectResource )rd.object;
			Pool.put(type, or.getId(), or);
		}

		Hashtable ht3 = (Hashtable )Pool.getHash("serverimage");
		if(ht3 != null)
		for(Enumeration e = ht3.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			or.updateLocalFromTransferable();
		}
		Pool.removeHash("serverimage");
	}
	
	// получить каталог сераилизованных объектов фиксированного типа
	protected void load(String type)
	{
		loadFromPool(type);

		Boolean lc = (Boolean )loaded_catalog.get(type);
		if(lc != null)
			if(lc.equals(Boolean.TRUE))
				return;
		loaded_catalog.put(type, Boolean.FALSE);
		
		Hashtable hc = (Hashtable )catalog.get(type);
		
		ConnectionInterface ci = di.getSession().getConnectionInterface();
		String s = ci.getServiceURL();
		String s1 = s.replace(':', '-');
		String s2 = s1.replace('/', '_');
		String name = "resources/directory/" + s2 + "/" + type + "s.dsi";

        Object obj = null;

		CacheLockObject lock_result = null;

        try
        {
			lock_result = lock.lockRead(name);
//            ObjectInputStream in = createInputStream(name);
            ObjectInputStream in = createInputStream((InputStream )lock_result.getResource());
            obj = in.readObject();
            in.close();
        }
        catch (FileNotFoundException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
		}
        catch (NullPointerException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
		}
        catch (StreamCorruptedException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
        }
        catch (Exception ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
//			ex.printStackTrace();
        }
		finally
		{
			if(lock_result != null)
				lock.releaseRead(lock_result);
		}
		Hashtable h2 = (Hashtable )obj;
		if(h2 == null)
			h2 = new Hashtable();
			
		Vector vec = new Vector();
		for(Enumeration e = hc.elements(); e.hasMoreElements();)
		{
			ResourceDescriptor rd = (ResourceDescriptor )e.nextElement();
			vec.add(rd.id);
		}

		for(Enumeration e = h2.elements(); e.hasMoreElements();)
		{
			ResourceDescriptor rd = (ResourceDescriptor )e.nextElement();
			ObjectResource or = (ObjectResource )rd.object;
			if(!vec.contains(rd.id))
			{
				hc.put(or.getId(), rd);
				Pool.put(type, or.getId(), or);
			}
		}

		Hashtable ht3 = (Hashtable )Pool.getHash("serverimage");
		if(ht3 != null)
		for(Enumeration e = ht3.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			or.updateLocalFromTransferable();
		}
		Pool.removeHash("serverimage");
		loaded_catalog.put(type, Boolean.TRUE);
	}
	
	// получить каталог сераилизованных объектов фиксированного типа
	protected void loadFromPool(String type)
	{
		Hashtable h2 = (Hashtable )Pool.getHash(type);
		if(h2 == null)
			h2 = new Hashtable();

		Hashtable h = new Hashtable();

		for(Enumeration e = h2.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			ResourceDescriptor rd = new ResourceDescriptor(or.getId(), or.getModified(), (Serializable )or);
			h.put(or.getId(), rd);
		}

		catalog.put(type, h);
	}
	
	// получить каталог сераилизованных объектов фиксированного типа
	protected void load(String type, Hashtable h)
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();
		String s = ci.getServiceURL();
		String s1 = s.replace(':', '-');
		String s2 = s1.replace('/', '_');
		String name = "resources/directory/" + s2 + "/" + type + "s.dsi";

        Object obj = null;

		CacheLockObject lock_result = null;

        try
        {
			lock_result = lock.lockRead(name);
//            ObjectInputStream in = createInputStream(name);
            ObjectInputStream in = createInputStream((InputStream )lock_result.getResource());
            obj = in.readObject();
            in.close();
        }
        catch (NullPointerException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
		}
        catch (Exception ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
        }
		finally
		{
			if(lock_result != null)
				lock.releaseRead(lock_result);
		}
		Hashtable h2 = (Hashtable )obj;
		if(h2 == null)
			h2 = new Hashtable();
			
		catalog.put(type, h2);

		for(Enumeration e = h2.elements(); e.hasMoreElements();)
		{
			ResourceDescriptor rd = (ResourceDescriptor )e.nextElement();
			ObjectResource or = (ObjectResource )rd.object;
			h.put(or.getId(), or);
		}

		Hashtable ht3 = (Hashtable )Pool.getHash("serverimage");
		if(ht3 != null)
		for(Enumeration e = ht3.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			or.updateLocalFromTransferable();
		}
		Pool.removeHash("serverimage");
	}
	
	// удалить из каталога объект
	protected void remove(String type, String id)
	{
		Hashtable h = (Hashtable )catalog.get(type);
		if(h == null)
			return;
		h.remove(id);

		Pool.remove(type, id);
/*
		Hashtable h2 = (Hashtable )Pool.getHash(type);
		if(h2 == null)
			return;
		h2.remove(id);
*/
		// удалить сериализованный объект
//		ConnectionInterface ci = di.getSession().getConnectionInterface();
	}
	
	// сохранить каталог сераилизованных объектов
	protected void save()
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();
	}
	
	// сохранить каталог сераилизованных объектов фиксированного типа
	protected void save(String type)
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();
		Hashtable h = (Hashtable )Pool.getHash(type);
		if(h == null)
			h = new Hashtable();
			
		Hashtable h2 = (Hashtable )catalog.get(type);
		if(h2 == null)
		{
			h2 = new Hashtable();
			catalog.put(type, h2);
		}

		for(Enumeration e = h.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			ResourceDescriptor rd = new ResourceDescriptor(or.getId(), or.getModified(), (Serializable )or);
			h2.put(or.getId(), rd);
		}

		String s = ci.getServiceURL();
		String s1 = s.replace(':', '-');
		String s2 = s1.replace('/', '_');
		String name = "resources/directory/" + s2 + "/" + type + "s.dsi";
		
		CacheLockObject lock_result = null;
		ObjectOutputStream out;

        try
        {
			lock_result = lock.lockWrite(name);
//            out = createOutputStream(name);
            out = createOutputStream((OutputStream )lock_result.getResource());
            out.writeObject(h2);
            out.flush();
            out.close();
        }
        catch (NullPointerException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
//			ex.printStackTrace();
		}
        catch (Exception ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
//			ex.printStackTrace();
        }
		finally
		{
			if(lock_result != null)
				lock.releaseWrite(lock_result);
		}
	}

	// сохранить каталог сераилизованных объектов фиксированного типа
	protected void save(String type, Hashtable h)
	{
		ConnectionInterface ci = di.getSession().getConnectionInterface();
		if(h == null)
			h = new Hashtable();
			
		Hashtable h2 = (Hashtable )catalog.get(type);
		if(h2 == null)
		{
			h2 = new Hashtable();
			catalog.put(type, h2);
		}

		for(Enumeration e = h.elements(); e.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )e.nextElement();
			ResourceDescriptor rd = new ResourceDescriptor(or.getId(), or.getModified(), (Serializable )or);
			h2.put(or.getId(), rd);
		}

		String s = ci.getServiceURL();
		String s1 = s.replace(':', '-');
		String s2 = s1.replace('/', '_');
		String name = "resources/directory/" + s2 + "/" + type + "s.dsi";
		
		CacheLockObject lock_result = null;
		ObjectOutputStream out;
		
        try
        {
			lock_result = lock.lockWrite(name);
//          out = createOutputStream(name);
            out = createOutputStream((OutputStream )lock_result.getResource());
            out.writeObject(h2);
            out.flush();
            out.close();
        }
        catch (NullPointerException ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
//			ex.printStackTrace();
		}
        catch (Exception ex)
        {
			System.out.println("Catalogue: " + type + ": " + ex.getMessage());
//			ex.printStackTrace();
        }
		finally
		{
			if(lock_result != null)
				lock.releaseWrite(lock_result);
		}
	}

    static protected ObjectInputStream createInputStream(String filename)
			throws Exception
    {
        InputStream f = new FileInputStream(filename);
        f = new GZIPInputStream(f);
		return new ObjectInputStream(f);
    }

    static protected ObjectInputStream createInputStream(InputStream f)
			throws Exception
    {
        InputStream f2 = new GZIPInputStream(f);
		return new ObjectInputStream(f2);
    }

    static protected ObjectOutputStream createOutputStream(String filename)
			throws Exception
    {
        File file = new File(filename);
		file.mkdirs();
        if (file.exists())
            file.delete();
        file.createNewFile();

        OutputStream f = new FileOutputStream(filename);
        f = new GZIPOutputStream(f);
        return new ObjectOutputStream(f);
    }

    static protected ObjectOutputStream createOutputStream(OutputStream f)
			throws Exception
    {
        OutputStream f2 = new GZIPOutputStream(f);
        return new ObjectOutputStream(f2);
    }

	// получить с сервера список описателей для объектов фиксированного типа	
	protected ResourceDescriptor_Transferable[] GetDescriptors(String type)
	{
		if(!(di.getSession() instanceof RISDSessionInfo))
			return new ResourceDescriptor_Transferable[] {
					new ResourceDescriptor_Transferable(
							"id", System.currentTimeMillis())
				};
			
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();
		
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetResourceDescriptors(si.accessIdentity, type, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptors! status = " + ecode);
			return null;
		}
		return rdh.value;
	}

	// получить с сервера список описателей для объектов фиксированного типа
	// с фильтрацией по текущему домену
	protected ResourceDescriptor_Transferable[] GetDomainDescriptors(String type)
	{
		if(!(di.getSession() instanceof RISDSessionInfo))
			return new ResourceDescriptor_Transferable[] {
					new ResourceDescriptor_Transferable(
							"id", System.currentTimeMillis())
				};
			
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();
		
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetDomainResourceDescriptors(si.accessIdentity, type, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptors! status = " + ecode);
			return null;
		}
		return rdh.value;
	}

	// получить с сервера список описателей для объектов фиксированного типа	
	protected ResourceDescriptor_Transferable[] GetDescriptorsByIds(String type, String[] ids)
	{
		if(!(di.getSession() instanceof RISDSessionInfo))
			return new ResourceDescriptor_Transferable[] {
					new ResourceDescriptor_Transferable(
							"id", System.currentTimeMillis())
				};
			
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();
		
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptorSeq_TransferableHolder rdh = new ResourceDescriptorSeq_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetResourceDescriptorsByIds(si.accessIdentity, type, ids, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptorsByIds! status = " + ecode);
			return null;
		}
		return rdh.value;
	}

	// получить с сервера описатель для объекта фиксированного типа
	protected ResourceDescriptor_Transferable GetDescriptor(String type, String id)
	{
		if(!(di.getSession() instanceof RISDSessionInfo))
			return new ResourceDescriptor_Transferable(
							"id", System.currentTimeMillis());
			
		RISDSessionInfo si = (RISDSessionInfo )di.getSession();
		
		if(si == null)
			return null;
		if(!si.isOpened())
			return null;

		int ecode = 0;
		ResourceDescriptor_TransferableHolder rdh = new ResourceDescriptor_TransferableHolder();

		try
		{
			ecode = si.ci.server.GetResourceDescriptor(si.accessIdentity, type, id, rdh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting descriptor: " + ex.getMessage());
			ex.printStackTrace();
			return null;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetDescriptor! status = " + ecode);
			return null;
		}
		return rdh.value;
	}

	class ResourceDescriptor implements Serializable
	{
		private static final long serialVersionUID = 01L;
		String id;
		long modified;
		Serializable object;

		public ResourceDescriptor(
				String id,
				long modified,
				Serializable object)
		{
			this.id = id;
			this.modified = modified;
			this.object = object;
		}

		private void writeObject(java.io.ObjectOutputStream out) throws IOException
		{
			out.writeObject(id);
			out.writeLong(modified);
			out.writeObject(object);
		}
     
		private void readObject(java.io.ObjectInputStream in)
				throws IOException, ClassNotFoundException
		{
			id = (String )in.readObject();
			modified = in.readLong();
			object = (Serializable )in.readObject();
		}
	}

	public void LoadImages()
	{
		ResourceDescriptor_Transferable[] desc = GetDescriptors(ImageResource.typ);
		ImageCatalogue.removeAll();
		load(ImageResource.typ, ImageCatalogue.getHash());
		Vector ids = filter(ImageResource.typ, desc, true);
		if(	ids.size() > 0)
		{
			String[] imids = new String[ids.size()];
			ids.copyInto(imids);
			di.LoadImages(imids);
			if(ids.size() > 0)
				save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}

	public void LoadImages(String[] id)
	{
		Hashtable cat = (Hashtable )catalog.get(ImageResource.typ);
		Vector v = new Vector();
		if(cat != null)
			for(Enumeration e = cat.keys(); e.hasMoreElements();)
				v.add(e.nextElement());

		Vector l = new Vector();
		for(int i = 0; i < id.length; i++)
			if(!v.contains(id[i]))
				if(!l.contains(id[i]))
					l.add(id[i]);

		if(	l.size() > 0)
		{
			String[] imids = new String[l.size()];
			l.copyInto(imids);
			di.LoadImages(imids);
			if(l.size() > 0)
				save(ImageResource.typ, ImageCatalogue.getHash());
		}
	}
}
