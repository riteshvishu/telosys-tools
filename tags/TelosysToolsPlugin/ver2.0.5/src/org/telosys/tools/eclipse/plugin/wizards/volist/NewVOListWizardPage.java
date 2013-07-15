package org.telosys.tools.eclipse.plugin.wizards.volist;

import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.telosys.tools.commons.JavaClassUtil;
import org.telosys.tools.commons.config.ClassNameProvider;
import org.telosys.tools.eclipse.plugin.commons.JModel;
import org.telosys.tools.eclipse.plugin.config.ProjectConfig;
import org.telosys.tools.eclipse.plugin.wizards.common.StandardNewJavaClassWizardPage;
import org.telosys.tools.eclipse.plugin.wizards.common.VOBeanField;
import org.telosys.tools.eclipse.plugin.wizards.common.WizardTools;

public class NewVOListWizardPage extends StandardNewJavaClassWizardPage {

	private final static String NAME = "Page1" ; // Page id in the wizard

	private final static String TITLE = "V.O. List";

	private final static String DESCRIPTION = "Create a new Value Object List class";

	private final static int FIELD_VO_CLASS = 1;

//	private final static Status STATUS1_OK = WizardTools.getStatusOK();
	private final static Status STATUS1_ERR = WizardTools.getStatusError("VO bean class name is empty") ;

//	private Status _status1 = STATUS1_OK;

//	private CtxBean _ctxBean = null;

	public NewVOListWizardPage(IStructuredSelection selection) {
		super(true, NAME, TITLE, DESCRIPTION, selection);
	}

	private String getVOListClassName(String sBeanClassName) {
		if (sBeanClassName != null) {
			ClassNameProvider classNameProvider = getClassNameProvider() ;
			if ( classNameProvider != null )
			{
				return classNameProvider.getVOListClassName(sBeanClassName);
			}
		}
		return "";
	}

	private void initFields() {
		log("initFields()...");
		initStandardFields();

//		ProjectConfig projectConfig = getProjectConfig() ;
//
//		//--- Set the package of the new class
//		String s = projectConfig.getPackageForVOList();
//		if (s != null) {
//			log("initFields() : package =  " + s);
//			setPackageFieldValue(s); // The package must exist
//		}
		
		//--- Set the VO class
		IJavaElement je = getJavaElementSelected();
		if (je != null) {
			IType type = JModel.getJavaType(je);
			if (type != null) {
				log("initFields() : selected type =  " + type.getFullyQualifiedName());
				setClassToUseFieldValue(type.getFullyQualifiedName());

				String sVOClass = type.getElementName();

				setClassNameFieldValue(getVOListClassName(sVOClass));
			}
		}
	}

	/**
	 * Create the wizard IHM
	 */
	public void createControl(Composite pageComposite) {
		initMainComposite(pageComposite);

		createStandardControl();
		createSeparator();
		
		ProjectConfig projectConfig = getProjectConfig() ;
		String sVOClassMask = projectConfig.getVOClassMask();
		VOBeanField field = new VOBeanField(FIELD_VO_CLASS, this, getShell(),
				getJavaProjectSelected(), sVOClassMask);
		createClassToUseControl(field);

		createSeparator();

		setControl(getGridComposite());

		initFields();
		doStatusUpdate();
	}

//	/**
//	 * Updates status when a field changes.
//	 * 
//	 * @param fieldName
//	 *            the name of the field that had change
//	 */
//	protected void handleFieldChanged(String sField) {
//		log("handleFieldChanged(" + sField + ")");
//		super.handleFieldChanged(sField); // Set the standard status
//										  // (fPackageStatus, fTypeNameStatus,
//										  // ... )
//		doStatusUpdate();
//	}

	public String getVOBeanClassFieldValue() {
		return getClassToUseFieldValue();
	}

//	public JavaBeanAttribute[] getVOBeanAttributes() {
//		log("getVOBeanAttributes()");
//		if (_ctxBean != null) {
//			return _ctxBean.getAttributes();
//		}
//		return new JavaBeanAttribute[0];
//	}

	/*
	 * Event called each time a specific field is modified ( via keyboard or
	 * selection )
	 * 
	 * @see org.objectweb.telosys.plugin.wizards.common.IWizardPageEvents#specificFieldsChanged(int)
	 */
	public void specificFieldsChanged(int iFieldId, String sNewValue) {
		log("specificFieldsChanged(" + iFieldId + ")");
		if (iFieldId == FIELD_VO_CLASS) {
			log("specificFieldsChanged(" + iFieldId
					+ ") : check ScreenData class : '" + sNewValue + "'");
			//--- Check the ScreenData field
			//_status1 = STATUS1_ERR;
			setStatus(STATUS1_ERR);
			if (sNewValue != null) {
				if (sNewValue.trim().length() > 0) {
					//_status1 = STATUS1_OK;
					setStatusOK();
					log("specificFieldsChanged(" + iFieldId
							+ ") : check ScreenData class : '" + sNewValue
							+ "' : Status OK");
					String sShortName = JavaClassUtil.shortName(sNewValue);
					setClassNameFieldValue(getVOListClassName(sShortName));
				}
			}
		}
		doStatusUpdate();
	}

//	/**
//	 * Updates status.
//	 */
//	private void doStatusUpdate() {
//
//		log("doStatusUpdate...");
//
//		log("_status1 : " + _status1);
//
//		//--- Update the status line and the "OK" button according to the given
//		// status collection
//		// ( only the most severe message is displayed )
//		IStatus[] status = new IStatus[] { this.fContainerStatus,
//				this.fPackageStatus, this.fTypeNameStatus, _status1 };
//
//		updateStatus(status);
//	}

}