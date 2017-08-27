package org.mydbsee.pic;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.mydbsee.pic.common.sys.CacheImage;
import org.mydbsee.pic.common.sys.HookSysTray;
import org.mydbsee.pic.common.sys.IAppConstants;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {
	private HookSysTray hookSysTray;
    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    @Override
    public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(800,600));
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);
		configurer.setShellStyle(SWT.MIN | SWT.CLOSE);
		configurer.setTitle(IAppConstants.APPLICATION_TITLE);
    }

	private void createSystemTray() {
		hookSysTray = new HookSysTray();
		hookSysTray.createSysTray(getWindowConfigurer().getWindow());
	}

	@Override
	public void postWindowOpen() {
		// TODO Auto-generated method stub
		// 设置窗口自动居中
		Shell shell = getWindowConfigurer().getWindow().getShell();
		Rectangle screenSize = Display.getDefault().getClientArea();
		Rectangle frameSize = shell.getBounds();
		shell.setLocation((screenSize.width - frameSize.width) / 2,
				(screenSize.height - frameSize.height) / 2);
		createSystemTray();
		String[] args=Platform.getApplicationArgs();
		if(args.length==1&&args[0].equals("system")) 
			getWindowConfigurer().getWindow().getShell().setMinimized(true);
	}

	@Override
	public boolean preWindowShellClose() {
		// TODO Auto-generated method stub
		//hookSysTray.windowMinimized(getWindowConfigurer().getWindow().getShell());
		PlatformUI.getWorkbench().close();
		return false;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		hookSysTray.Dispose();
		CacheImage.getINSTANCE().dispose();
	}
}
