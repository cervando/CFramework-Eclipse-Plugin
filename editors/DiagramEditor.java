package CPlugin.editors;



import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.gef.tools.CreationTool;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.WorkbenchPart;

import CPlugin.gefView.Policies.commands.ConnectionCreateCommand;
import CPlugin.gefView.Policies.commands.ConnectionDeleteCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityChangedLanguageCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityChangedNameCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityCreatedCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityDeletedCommand;
import CPlugin.gefView.Policies.commands.activity.ActivityTypeChangedCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangeConstraintCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedBlackBoxCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedNameCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedXCommand;
import CPlugin.gefView.Policies.commands.area.NodeChangedYCommand;
import CPlugin.gefView.Policies.commands.area.NodeCreateCommand;
import CPlugin.gefView.Policies.commands.area.NodeDeleteCommand;
import CPlugin.listener.GraphEditorSelectionListener;
import CPlugin.model.Area;
import CPlugin.model.Graph;
import CPlugin.utils.KMiddleProject;
import CPlugin.view.GraphEditPart;
import CPlugin.view.factories.AreaFactory;
import CPlugin.view.factories.ConnectionFactory;
import CPlugin.view.factories.GraphEditPartFactory;

public class DiagramEditor extends EditorPart {

	private EditDomain editDomain;
	private ActionRegistry actionRegistry;
	private CreationTool createNode;
	private ConnectionCreationTool createConnection;
	private boolean dirty = false;
	private GraphEditorSelectionListener graphEditorListener;
	private Label Tittle;
	private ScrollingGraphicalViewer viewer;
	private int level = 0;
	private final String[] LEVELS_NAME = new String[]{"Lobe", "Cortex", "Area" };
	private final int MAX_LEVELS = 3;
	private GraphEditPart[] graphs = new GraphEditPart[MAX_LEVELS];
	
	
	
	Button newConnecionButton;
	Button newModuleButton;
	Button returnViewButton;
	
	
	
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		IFile original= (input instanceof IFileEditorInput) ? ((IFileEditorInput) input).getFile() : null;
		if ( original == null || !original.exists()  ){
			/** TODO close window **/
			System.out.println("close this window");
		}
		setSite(site);
		setInput(input);
		initEditDomain();
		initActionRegistry();
		initGraphEditorListener();
		showPropertiesWindow();
	}
	
	private void showPropertiesWindow() throws PartInitException{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.showView(org.eclipse.ui.IPageLayout.ID_PROP_SHEET);
		
	}
	
	
	
	public void createPartControl(Composite parent) {
		
		//Make Parent a Two Column griid
		GridLayout f = new GridLayout();
		f.marginLeft = 0;
		f.marginTop = 0;
		parent.setLayout(f);
		
		//Grid Data For bottons at top
		//GridData gridData = new GridData();
		//gridData.verticalAlignment = GridData.BEGINNING;
		
		//Layout for bottons container
		RowLayout r = new RowLayout();
		r.wrap = true;
		r.pack = true;
		r.justify = false;
		r.type = SWT.HORIZONTAL;
		r.marginBottom = 0;
		r.marginHeight = 0;
		r.spacing = 0;
		
	
		
		//Bottons container
		Composite compositeBar = new Composite(parent, PROP_TITLE);
		compositeBar.setLayout(r);
		
		
		
		Tittle = new Label(compositeBar, PROP_TITLE);
		Tittle.setFont(new Font( Tittle.getDisplay(), new FontData( "Arial", 20, SWT.BOLD ) ));
		Tittle.setText("Lobe View  ");
		Tittle.setSize(300, 20);
		//Tittle.setLayoutData(new GridData().verticalAlignment = GridData.CENTER );
		//Tittle.setLayoutData( gd );
		
		
		newModuleButton = new Button(compositeBar, PROP_TITLE);
		newModuleButton.setText("New " + LEVELS_NAME[level]); 
		//newModuleButton.setSize(20,20);
		createNode = new CreationTool(new AreaFactory());
		newModuleButton.addListener(SWT.Selection, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		    	editDomain.setActiveTool(createNode);
		    }
		});
		//newModuleButton.setImage(image);
		
		
		newConnecionButton = new Button(compositeBar, PROP_TITLE);
		newConnecionButton.setText("New Connection");
		//newConnecionButton.setSize(20,20);
		createConnection = new ConnectionCreationTool(new ConnectionFactory());
		newConnecionButton.addListener(SWT.Selection, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		    	editDomain.setActiveTool(createConnection);
		    }
		});
		
		
		returnViewButton = new Button(compositeBar, PROP_TITLE);
		returnViewButton.setText("Return to Previos Level");
		returnViewButton.setVisible(false);
		returnViewButton.addListener(SWT.Selection, new Listener()
		{
		    @Override
		    public void handleEvent(Event event)
		    {
		    	level--;
		    	//Save actual GRAPH
		    	
		    	//Load higher GRAPH
		    	viewer.setContents(graphs[level]);
		    	refreshButtons();
		    }
		});
		
		
		
		
		
		/*
		try {
			newAreaActionTool.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
						getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
			newConnectionActionTool.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
						getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		} catch (Exception e) {
			System.out.println(e);
		}*/
		
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		
		Composite compositeViewer = new Composite(parent, PROP_TITLE);
		compositeViewer.setLayout(new FillLayout());
		compositeViewer.setBackground(ColorConstants.black);
		compositeViewer.setLayoutData(gridData);
		
		
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(compositeViewer);
		viewer.setEditPartFactory(new GraphEditPartFactory());
		viewer.getControl().setBackground(ColorConstants.white);
		
		
		
		
		
		editDomain.addViewer(viewer);
		getSite().setSelectionProvider(viewer);
		loadFile(viewer);
		
		
		//viewer.setRootEditPart(new RulerRootEditPart(true));
		//GridData gridData = new GridData();
		//gridData.horizontalAlignment = GridData.FILL;
		//gridData.horizontalSpan = 7;
		//	coviewer.getControl().setLayoutData(gridData);
		/**
		 * Its supose to set a Background image, but it does not, also it doesnt crash
		 */
		/*
		ImageDescriptor imgDesc = Activator.getImageDescriptor("icons/editorBG.png");
		Image img = new Image(
				(Display.getCurrent() == null )? Display.getDefault():Display.getCurrent(),
				imgDesc.getImageData()
		);
		viewer.getControl().setBackgroundImage(img);
		viewer.getControl().update();
		 */
		//GridData gridData = new GridData();
		//gridData.horizontalAlignment = GridData.FILL;
		//gridData.horizontalSpan = 7;
		//viewer.getControl().setLayoutData(gridData);
		//
				
	}
	
	
	
	public void openElement( String name ) {
		//Save graph
		graphs[level] = (GraphEditPart) viewer.getContents();
		level ++ ;
		viewer.setContents(new Graph());
		refreshButtons();
	}
	
	
	public void loadFile(ScrollingGraphicalViewer viewer){
		if ( getEditorInput() instanceof IFileEditorInput ){
			IProject project = ((IFileEditorInput) getEditorInput()).getFile().getProject();
			KMiddleProject kmp = new KMiddleProject(project);
			Graph g = kmp.getGraph();
			viewer.setContents(g);
			try {
				//Needed cause the descriptor delete some invalid character every time is open
				((IFileEditorInput) getEditorInput()).getFile().refreshLocal(IResource.DEPTH_ZERO,  null);
			} catch (Exception e) {
				e.printStackTrace();
			}
			setDirty(false);
		}else{
			viewer.setContents(new Graph());
		}
	}
	
	
	private void refreshButtons() {

    	newModuleButton.setText("New " + LEVELS_NAME[level]); 
    	Tittle.setText(LEVELS_NAME[level] + " View  ");
    	if (level == 0 )
    		returnViewButton.setVisible(false);
    	else
    		returnViewButton.setVisible(true);
	}
	
	
	public void createToolbarActions(Composite parent){
		createNode = new CreationTool(new AreaFactory());
		createConnection = new ConnectionCreationTool(new ConnectionFactory());
		
		Action newAreaActionTool = new Action(){public void run(){editDomain.setActiveTool(createNode);}};
		Action newConnectionActionTool = new Action(){public void run(){editDomain.setActiveTool(createConnection);}};
		
		try {
			newAreaActionTool.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
						getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT));
			newConnectionActionTool.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
						getImageDescriptor(ISharedImages.IMG_TOOL_FORWARD));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		IToolBarManager mgr = getEditorSite().getActionBars().getToolBarManager();
		mgr.add(newAreaActionTool);
		mgr.add(newConnectionActionTool);
		mgr.update(true);
		
//		GridData gridData = new GridData();
//		gridData.horizontalAlignment = GridData.FILL;
//		gridData.horizontalSpan = 1;
//		viewer.getControl().setLayoutData(gridData);
//		m_toolbar.setSize(250, 400);
//		m_toolbar.setBackground(ColorConstants.white);
//		FillLayout fillLayout = new FillLayout();
//		fillLayout.type = SWT.VERTICAL;
//		m_toolbar.setLayout(fillLayout);
		
//		ToolBar m_toolbar  = new ToolBar(parent, SWT.VERTICAL);
//		ToolBarManager m_toolBarManager = new ToolBarManager(m_toolbar);
//	    m_toolBarManager.add(newAreaActionTool);
//	    m_toolBarManager.add(newConnectionActionTool);
//	    m_toolBarManager.update(true);
	}
	
	
	
	private void initEditDomain() {
		 editDomain = new DefaultEditDomain(this);
	}
	
	private void initActionRegistry() {
		actionRegistry = new ActionRegistry();
		actionRegistry.registerAction(new UndoAction(this));
		actionRegistry.registerAction(new RedoAction(this));
		actionRegistry.registerAction(new DeleteAction((WorkbenchPart)this));
	}
	
	private void initGraphEditorListener() {		 
		graphEditorListener = new GraphEditorSelectionListener(actionRegistry);
		ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
		editDomain.getCommandStack().addCommandStackEventListener(graphEditorListener);
		selectionService.addSelectionListener(graphEditorListener);
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object getAdapter(Class adapter) {
		 if (adapter == CommandStack.class) {
			 return editDomain.getCommandStack();
		 }
		 return super.getAdapter(adapter);
	}
	
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void doSave(IProgressMonitor monitor) {
		if ( getEditorInput() instanceof IFileEditorInput ){
			IProject projectName = ((IFileEditorInput) getEditorInput()).getFile().getProject();
			KMiddleProject kmp = new KMiddleProject(projectName);
			
			Object[] commands = editDomain.getCommandStack().getCommands();
			for ( int i = 0; i < commands.length; i++){
				System.out.println(commands[i].toString());
				if ( commands[i] instanceof CompoundCommand){
					CompoundCommand cCommand = (CompoundCommand)commands[i];
					List cCommands = cCommand.getCommands();
					for ( int c = 0; c < cCommands.size(); c++ ){
						System.out.println("\t" + cCommands.get(c).toString());
						execCommand(kmp, cCommands.get(c));
					}	
				}else{
					execCommand(kmp, commands[i]);
				}	
			}
			kmp.createAreaNamePython();
			try {
				((IFileEditorInput) getEditorInput()).getFile().refreshLocal(IResource.DEPTH_ZERO,  null);
				editDomain.getCommandStack().flush();
				setDirty(false);
			} catch (Exception e) {
				System.out.println("Error updating saved file");
			}
		}else{
			System.out.println("Could not get project name");
		}
	}
	
	public void execCommand(KMiddleProject kmp, Object command){
		//--------- Node modifications
		if ( command instanceof NodeDeleteCommand ){
			Area a = ((NodeDeleteCommand) command).getNode();
			boolean isBB =  ((NodeDeleteCommand) command).isBlackBox();
			kmp.deleteArea(a.getName(), isBB);
			
		} else if ( command instanceof NodeCreateCommand ){
			Area a = ((NodeCreateCommand) command).getNode();
			Rectangle r = a.getConstraint();
			kmp.addArea("", a, ((NodeCreateCommand) command).getAreaName());
			kmp.setAreaXY(((NodeCreateCommand) command).getAreaName(), r.x, r.y,r.width,r.height);
			
		} else if ( command instanceof NodeChangedNameCommand ){
			String oldAreaName = ((NodeChangedNameCommand) command).getOldName();
			String newAreaName = ((NodeChangedNameCommand) command).getNewName();
			boolean isBB	   = ((NodeChangedNameCommand) command).isBlackBox();
			
			kmp.setAreaName(oldAreaName, newAreaName, isBB);
			
		} else if ( command instanceof NodeChangeConstraintCommand ){
			Rectangle r = ((NodeChangeConstraintCommand) command).getNewConstraint();
			kmp.setAreaXY(((NodeChangeConstraintCommand) command).getAreaName(), r.x, r.y,r.width,r.height);
		
		} else if ( command instanceof NodeChangedXCommand ){
			NodeChangedXCommand ncX = ((NodeChangedXCommand) command);
			kmp.setAreaXY(ncX.getAreaName(), ncX.getNewX(), ncX.getY(), ncX.getWidth(), ncX.getHeigh());
			
		} else if ( command instanceof NodeChangedYCommand ){
			NodeChangedYCommand ncY = ((NodeChangedYCommand) command);
			kmp.setAreaXY(ncY.getAreaName(), ncY.getX(), ncY.getNewY(), ncY.getWidth(), ncY.getHeigh());
		
		} else if ( command instanceof NodeChangedBlackBoxCommand ) {
			
			NodeChangedBlackBoxCommand ncbb = ((NodeChangedBlackBoxCommand) command);
			kmp.setAreaBlackbox(ncbb.getName(), ncbb.isBlackBox() );
			
			
			
			
					
		//--------- Connection modifications
		} else if ( command instanceof ConnectionDeleteCommand ){
			ConnectionDeleteCommand c = (ConnectionDeleteCommand) command;
			kmp.deleteConection(c.getSourceName(), c.getTargetName());
			
		} else if ( command instanceof ConnectionCreateCommand ){
			ConnectionCreateCommand c = (ConnectionCreateCommand) command;
			kmp.addConnection(c.getSourceName(), c.getTargetName());
			
			
		//--------- Activity modifications
		} else if ( command instanceof ActivityCreatedCommand ){
			CPlugin.model.Activity p =  ((ActivityCreatedCommand) command).getProccess();
			kmp.addActivity(((ActivityCreatedCommand) command).getAreaName(), p,((ActivityCreatedCommand) command).getProcessName());
		
		} else if ( command instanceof ActivityChangedNameCommand ){
			ActivityChangedNameCommand pcnc = ((ActivityChangedNameCommand) command);
			kmp.setActivityName(pcnc.getAreaName(), pcnc.getOldName(), pcnc.getNewName(), pcnc.getLanguage());
		
		} else if ( command instanceof ActivityDeletedCommand ){
			CPlugin.model.Activity p =  ((ActivityDeletedCommand) command).getProccess();
			kmp.deleteActivity(((ActivityDeletedCommand) command).getAreaName(), p.getName(), p.getLanguage());		
			
		} else if ( command instanceof ActivityTypeChangedCommand ){
			ActivityTypeChangedCommand c = (ActivityTypeChangedCommand) command;
			kmp.setActivityType(c.getAreaName(), c.getProcessName(), c.getNewValue());
			
		} else if ( command instanceof ActivityChangedLanguageCommand ){
			ActivityChangedLanguageCommand c = (ActivityChangedLanguageCommand) command;
			kmp.setActivityLanguage(c.getAreaName(), c.getProcessName(), c.getOldLanguage(), c.getNewLanguage());
			
		} 
	}

	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean dirty){
		this.dirty = dirty;
		firePropertyChange(PROP_DIRTY);
	}
	
	public boolean isSaveAsAllowed() {return false;}
	public void setFocus() {}
	public void doSaveAs() {}
	
	@Override
	public void dispose() {
		ISelectionService selectionService = getSite().getWorkbenchWindow().getSelectionService();
		selectionService.removeSelectionListener(graphEditorListener);
	}
	

}