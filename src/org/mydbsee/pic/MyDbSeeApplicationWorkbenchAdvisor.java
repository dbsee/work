package org.mydbsee.pic;

import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

/**
 * This workbench advisor creates the window advisor, and specifies
 * the perspective id for the initial window.
 */
public class MyDbSeeApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	
	@Override
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new MyDbSeeApplicationWorkbenchWindowAdvisor(configurer);
    }
    
    @Override
	public String getInitialWindowPerspectiveId() {
		return MyDbSeePerspective.ID;
	}

	@Override
	public void initialize(IWorkbenchConfigurer configurer) {
		// TODO Auto-generated method stub
        configurer.setSaveAndRestore(false);
        //PlatformUI.getPreferenceStore().setValue(IWorkbenchPreferenceConstants.SHOW_INTRO, true);
	} 
    
	
}
