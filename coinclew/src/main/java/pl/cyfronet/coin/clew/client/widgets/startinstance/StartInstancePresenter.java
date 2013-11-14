package pl.cyfronet.coin.clew.client.widgets.startinstance;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.widgets.appliancetype.ApplianceTypePresenter;
import pl.cyfronet.coin.clew.client.widgets.startinstance.IStartInstanceView.IStartInstancePresenter;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = StartInstanceView.class)
public class StartInstancePresenter extends BasePresenter<IStartInstanceView, MainEventBus> implements IStartInstancePresenter {
	private CloudFacadeController cloudFacadeController;
	private List<ApplianceTypePresenter> applianceTypePresenters;

	@Inject
	public StartInstancePresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
		applianceTypePresenters = new ArrayList<ApplianceTypePresenter>();
	}
	
	public void onStart() {
		eventBus.addPopup(view);
	}
	
	public void onShowStartInstanceDialog() {
		view.clearApplianceTypeContainer();
		view.showProgressIndicator();
		view.show();
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				view.clearApplianceTypeContainer();
				
				if (applianceTypes.size() == 0) {
					view.addNoApplianceTypesLabel();
				} else {
					for (ApplianceType applianceType : applianceTypes) {
						ApplianceTypePresenter presenter = eventBus.addHandler(ApplianceTypePresenter.class);
						applianceTypePresenters.add(presenter);
						presenter.setApplianceType(applianceType);
						view.getApplianceTypeContainer().add(presenter.getView().asWidget());
					}
				}
			}
		});
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
			view.setStartSelectedBusyState(true);
			cloudFacadeController.startApplianceTypes(initialConfigurationIds, new Command() {
				@Override
				public void execute() {
					view.setStartSelectedBusyState(false);
					view.hide();
					eventBus.refreshInstanceList();
				}
			});
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