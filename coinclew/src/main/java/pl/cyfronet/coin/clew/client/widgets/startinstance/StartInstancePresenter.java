package pl.cyfronet.coin.clew.client.widgets.startinstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.ApplianceTypePresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.IStartInstanceView.IStartInstancePresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = StartInstanceView.class)
public class StartInstancePresenter extends BasePresenter<IStartInstanceView, MainEventBus> implements IStartInstancePresenter {
	private CloudFacadeController cloudFacadeController;
	private List<ApplianceTypePresenter> applianceTypePresenters;
	private boolean developmentMode;

	@Inject
	public StartInstancePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		applianceTypePresenters = new ArrayList<ApplianceTypePresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowStartInstanceDialog(boolean developmentMode) {
		this.developmentMode = developmentMode;
		setTitle();
		view.clearApplianceTypeContainer();
		view.showProgressIndicator();
		view.getFilter().setText("");
		view.show();
		
		if (developmentMode) {
			cloudFacadeController.getDevelopmentApplianceTypes(new ApplianceTypesCallback() {
				@Override
				public void processApplianceTypes(List<ApplianceType> applianceTypes) {
					displayApplianceTypes(applianceTypes);
				}
			});
		} else {
			cloudFacadeController.getProductionApplianceTypes(new ApplianceTypesCallback() {
				@Override
				public void processApplianceTypes(List<ApplianceType> applianceTypes) {
					displayApplianceTypes(applianceTypes);
				}
			});
		}
	}
	
	protected void displayApplianceTypes(List<ApplianceType> applianceTypes) {
		view.clearApplianceTypeContainer();
		
		if (applianceTypes.size() == 0) {
			view.addNoApplianceTypesLabel();
		} else {
			Collections.sort(applianceTypes, new Comparator<ApplianceType>() {
				@Override
				public int compare(ApplianceType o1, ApplianceType o2) {
					return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
				}
			});
			
			for (ApplianceType applianceType : applianceTypes) {
				ApplianceTypePresenter presenter = eventBus.addHandler(ApplianceTypePresenter.class);
				applianceTypePresenters.add(presenter);
				presenter.setApplianceType(applianceType, developmentMode);
				view.getApplianceTypeContainer().add(presenter.getView().asWidget());
			}
		}
	}

	private void setTitle() {
		if (developmentMode) {
			view.setDevelopmentModeTitle();
		} else {
			view.setPortalModeTitle();
		}
	}

	public void onHideStartInstanceModal() {
		view.hide();
		
		for (ApplianceTypePresenter presenter : applianceTypePresenters) {
			eventBus.removeHandler(presenter);
		}
	}

	@Override
	public void onStartSelected() {
		List<String> initialConfigurationIds = new ArrayList<String>();
		
		for (ApplianceTypePresenter presenter : applianceTypePresenters) {
			String initialConfigId = presenter.getSelectedInitialConfigId();
			
			if (initialConfigId != null) {
				initialConfigurationIds.add(initialConfigId);
			}
		}
		
		if (initialConfigurationIds.size() == 0) {
			view.showNoApplianceTypesSelected();
		} else {
			eventBus.startApplications(initialConfigurationIds, developmentMode);
			view.hide();
		}
	}

	@Override
	public void onFilterTextChanged() {
		String filterText = view.getFilter().getText();
		
		for (ApplianceTypePresenter presenter : applianceTypePresenters) {
			if (presenter.matchesFilter(filterText)) {
				presenter.getView().asWidget().setVisible(true);
			} else {
				presenter.getView().asWidget().setVisible(false);
			}
		}
	}
}