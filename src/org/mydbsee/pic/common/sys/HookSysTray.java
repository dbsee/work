package org.mydbsee.pic.common.sys;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.mydbsee.pic.Activator;
import org.mydbsee.pic.actions.sys.SysLogoffAction;

public class HookSysTray {

	private TrayItem trayItem;

	public HookSysTray() {

	}

	public void createSysTray(final IWorkbenchWindow window) {
		trayItem = initTrayItem(window);
		if (trayItem != null) {
			trayPopupMenu(window);
			trayMinimize(window);
		}
	}

	// ��С�����򴰿�
	public void windowMinimized(final Shell shell) {
		shell.setMinimized(true);
		shell.setVisible(false);
	}

	// ��С����������
	private void trayMinimize(final IWorkbenchWindow window) {
		window.getShell().addShellListener(new ShellAdapter() {
			public void shellIconified(ShellEvent e) {
				window.getShell().setVisible(false);
			}
		});
		trayItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				Shell shell = window.getShell();
				if (!shell.isVisible()) {
					shell.setVisible(true);
					window.getShell().setMinimized(false);
				}
			}
		});
	}

	// ���̵����˵�
	private void trayPopupMenu(final IWorkbenchWindow window) {
		trayItem.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				MenuManager trayMenu = new MenuManager();
				Menu menu = trayMenu.createContextMenu(window.getShell());
				fillTrayItem(trayMenu, window);
				menu.setVisible(true);
			}
		});
	}

	// �������̲˵���
	private void fillTrayItem(IMenuManager trayItem,
			final IWorkbenchWindow window) {
		Action exitSystem = new Action("�˳�ϵͳ[&E]", AbstractUIPlugin
				.imageDescriptorFromPlugin(IAppConstants.APPLICATION_ID,
						IImageKey.EXIT_SYSTEM)) {
			public void run() {
				PlatformUI.getWorkbench().close();
			}
		};
		trayItem.add(new SysLogoffAction());
		trayItem.add(exitSystem);
	}

	// ��ʼ��������Ŀ�����ֺ�ͼ��
	private TrayItem initTrayItem(final IWorkbenchWindow window) {
		final Tray tray = window.getShell().getDisplay().getSystemTray();
		if (tray == null)
			return null;
		trayItem = new TrayItem(tray, SWT.NONE);
		trayItem.setImage(CacheImage.getINSTANCE().getImage(
				Activator.PLUGIN_ID, IImageKey.WINDOW_IMAGE));
		// ��ʱ��ʾ������ʾ�ı�
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				window.getShell().getDisplay().syncExec(new Runnable() {
					public void run() {
						ToolTip tip = new ToolTip(window.getShell(),
								SWT.BALLOON | SWT.ICON_INFORMATION);
						tip.setAutoHide(true);
						tip.setMessage(IAppConstants.APPLICATION_TITLE);
						tip.setText("��ӭʹ��");
						trayItem.setToolTip(tip);
						tip.setVisible(true);
					}
				});
			}
		}, 0, 30 * 60 * 1000);
		return trayItem;
	}

	public void Dispose() {
		if (trayItem != null)
			trayItem.dispose();
	}
}