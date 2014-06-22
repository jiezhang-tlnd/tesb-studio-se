// ============================================================================
//
// Copyright (C) 2006-2012 Talend Inc. - www.talend.com
//
// This source code is available under agreement available at
// %InstallDIR%\features\org.talend.rcp.branding.%PRODUCTNAME%\%PRODUCTNAME%license.txt
//
// You should have received a copy of the agreement
// along with this program; if not, write to Talend SA
// 9 rue Pages 92150 Suresnes, France
//
// ============================================================================
package org.talend.repository.services.action;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.talend.commons.ui.runtime.image.ImageProvider;
import org.talend.core.model.repository.ERepositoryObjectType;
import org.talend.core.model.repository.RepositoryManager;
import org.talend.core.repository.model.ProxyRepositoryFactory;
import org.talend.core.repository.ui.actions.metadata.AbstractCreateAction;
import org.talend.repository.model.IProxyRepositoryFactory;
import org.talend.repository.model.IRepositoryNode.EProperties;
import org.talend.repository.model.RepositoryNode;
import org.talend.repository.services.model.services.util.EServiceCoreImage;
import org.talend.repository.services.ui.OpenWSDLWizard;
import org.talend.repository.services.utils.ESBRepositoryNodeType;

/**
 * DOC hwang class global comment. Detailled comment
 */
public class OpenWSDLAction extends AbstractCreateAction {

    private String createLabel = "Assign WSDL";

    private ERepositoryObjectType currentNodeType;

    private boolean creation = false;

    protected static final int WIZARD_WIDTH = 500;

    protected static final int WIZARD_HEIGHT = 140;

    public OpenWSDLAction() {
        super();

        this.setText(createLabel);
        this.setToolTipText(createLabel);

        this.setImageDescriptor(ImageProvider.getImageDesc(EServiceCoreImage.SERVICE_ICON));

        currentNodeType = ESBRepositoryNodeType.SERVICES;
    }

    public OpenWSDLAction(boolean isToolbar) {
        this();
        setToolbar(isToolbar);

        this.setText(createLabel);
        this.setToolTipText(createLabel);

        this.setImageDescriptor(ImageProvider.getImageDesc(EServiceCoreImage.SERVICE_ICON));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.talend.repository.ui.actions.metadata.AbstractCreateAction#init(org.talend.repository.model.RepositoryNode)
     */
    @Override
    protected void init(RepositoryNode node) {
        ERepositoryObjectType nodeType = (ERepositoryObjectType) node.getProperties(EProperties.CONTENT_TYPE);
        if (!currentNodeType.equals(nodeType)) {
            return;
        }
        this.setText(createLabel);
        this.setImageDescriptor(ImageProvider.getImageDesc(EServiceCoreImage.SERVICE_ICON));
        IProxyRepositoryFactory factory = ProxyRepositoryFactory.getInstance();
        switch (node.getType()) {
        case REPOSITORY_ELEMENT:
            // if (factory.isPotentiallyEditable(node.getObject())) {
            //
            // collectSiblingNames(node);
            // }
            // collectSiblingNames(node);
            creation = false;
            break;
        default:
            return;
        }
        setEnabled(isLastVersion(node));
    }

    protected void doRun() {
        if (repositoryNode == null) {
            repositoryNode = getCurrentRepositoryNode();
        }
        if (isToolbar()) {
            if (repositoryNode != null && repositoryNode.getContentType() != currentNodeType) {
                repositoryNode = null;
            }
            if (repositoryNode == null) {
                repositoryNode = getRepositoryNodeForDefault(currentNodeType);
            }
        }

        WizardDialog wizardDialog;
        if (isToolbar()) {
            init(repositoryNode);
            OpenWSDLWizard openWizard = new OpenWSDLWizard(PlatformUI.getWorkbench(), repositoryNode);
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), openWizard);
        } else {
            // selection = getSelection();
            wizardDialog = new WizardDialog(Display.getCurrent().getActiveShell(), new OpenWSDLWizard(PlatformUI.getWorkbench(),
                    repositoryNode));
        }

        if (!creation) {
            RepositoryManager.refreshSavedNode(repositoryNode);
        }

        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();

        wizardDialog.open();
        RepositoryManager.refreshCreatedNode(ESBRepositoryNodeType.SERVICES);
    }
}