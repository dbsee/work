/**
 * =============================================================================
 * @copyright    Copyright 2011
 * @filename     ConnectMangerLabelProvider.java
 * @description  
 * @version      V001.00  
 * @history  	 日期      修改人     版本        操作
 *              2011-10-26   Slive     V001.00		创建文件
 *
 * ============================================================================= 
 */

package org.mydbsee.pic.views.providers;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.mydbsee.db.mode.ConnectModel;
import org.mydbsee.db.mode.DBModel;
import org.mydbsee.db.mode.FunModel;
import org.mydbsee.db.mode.FunModelManager;
import org.mydbsee.db.mode.PreModel;
import org.mydbsee.db.mode.PreModelManager;
import org.mydbsee.db.mode.TBModelManager;
import org.mydbsee.db.mode.TableModel;
import org.mydbsee.db.mode.ViewModel;
import org.mydbsee.db.mode.ViewModelManager;
import org.mydbsee.pic.sys.common.MyCacheImage;


/**
 * 连接管理treeLabelProvider
 */
public class ConnectManagerLabelProvider implements ILabelProvider
{
	private Image imageConn = MyCacheImage.getINSTANCE().getImage("icons/connect.gif");
	private Image imageDB = MyCacheImage.getINSTANCE().getImage("icons/database.gif");
	private Image imageTBManager = MyCacheImage.getINSTANCE().getImage("icons/tables.gif");
	private Image imageTable = MyCacheImage.getINSTANCE().getImage("icons/table.gif");
	private Image imageViewManger = MyCacheImage.getINSTANCE().getImage("icons/views.gif");
	private Image imageView = MyCacheImage.getINSTANCE().getImage("icons/view.gif");
	private Image imagePreManager = MyCacheImage.getINSTANCE().getImage("icons/procedures.gif");
	private Image imagePre = MyCacheImage.getINSTANCE().getImage("icons/procedure.gif");
	private Image imageFunManger = MyCacheImage.getINSTANCE().getImage("icons/functions.gif");
	private Image imageFun = MyCacheImage.getINSTANCE().getImage("icons/function.gif");
	
	@Override
	public Image getImage(Object element)
	{
	    if(element instanceof ConnectModel)
		{
			return imageConn;	// 返回连接图片
		}
		else if(element instanceof DBModel)
		{
			return imageDB;		// 返回数据库图片
		}
		else if(element instanceof TBModelManager)
		{
			return imageTBManager;
		}
		else if(element instanceof ViewModelManager)
		{
			return imageViewManger;
		}
		else if(element instanceof PreModelManager)
		{
			return imagePreManager;
		}
		else if(element instanceof FunModelManager)
		{
			return imageFunManger;
		}
		else if(element instanceof TableModel)
		{
			return imageTable;
		}
		else if(element instanceof ViewModel)
		{
			return imageView;
		}
		else if(element instanceof PreModel)
		{
			return imagePre;
		}
		else if(element instanceof FunModel)
		{
			return imageFun;
		}
		return null;
	}

	@Override
	public String getText(Object element)
	{
		if(element instanceof ConnectModel)
		{
			ConnectModel connectModel = (ConnectModel)element;
			return connectModel.getName();
		}
		else if(element instanceof DBModel)
		{
			DBModel dModel = (DBModel) element;
			return dModel.getName();
		}
		else if(element instanceof TBModelManager)
		{
			return ((TBModelManager)element).getName();
		}
		else if(element instanceof ViewModelManager)
		{
			return ((ViewModelManager)element).getName();
		}
		else if(element instanceof PreModelManager)
		{
			return ((PreModelManager)element).getName();
		}
		else if(element instanceof FunModelManager)
		{
			return ((FunModelManager)element).getName();
		}
		else if(element instanceof TableModel)
		{
			return ((TableModel)element).getName();
		}
		else if(element instanceof ViewModel)
		{
			return ((ViewModel)element).getName();
		}
		else if(element instanceof PreModel)
		{
			return ((PreModel)element).getName();
		}
		else if(element instanceof FunModel)
		{
			return ((FunModel)element).getName();
		}
		return null;
	}

	@Override
	public void addListener(ILabelProviderListener arg0)
	{
		
	}

	@Override
	public void dispose()
	{
		imageConn.dispose();
		imageDB.dispose();
		imageTable.dispose();
		imageView.dispose();
		imagePre.dispose();
		imageFun.dispose();
		imageTBManager.dispose();
		imageViewManger.dispose();
		imagePreManager.dispose();
		imageFunManger.dispose();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
