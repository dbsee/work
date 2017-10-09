package org.mydbsee.pic.actions.header;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.mydbsee.pic.commands.ICommandIds;


public class SysMessagePopupAction extends Action {

    private final IWorkbenchWindow window;

    public SysMessagePopupAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
        setImageDescriptor(org.mydbsee.pic.MyDbSeeActivator.getImageDescriptor("/icons/sample3.gif"));
    }

    @Override
    public void run() {
        MessageDialog.openInformation(window.getShell(), "Open", "Open Message Dialog!");
    }
}