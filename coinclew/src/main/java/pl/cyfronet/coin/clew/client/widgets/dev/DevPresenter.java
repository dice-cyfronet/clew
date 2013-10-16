package pl.cyfronet.coin.clew.client.widgets.dev;

import java.util.List;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.ApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.ApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.appliancetype.NewApplianceType;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DevPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showNewApplianceModal(boolean show);
		void showApplianceType(String name, String description);
		HasText getNewApplianceTypeName();
		HasText getNewApplianceTypeDescription();
	}
	
	private CloudFacadeController cloudFacadeController;
	private View view;

	@Inject
	public DevPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
		
	}
	
	public void load() {
		cloudFacadeController.getApplianceTypes(new ApplianceTypesCallback() {
			@Override
			public void processApplianceTypes(List<ApplianceType> applianceTypes) {
				for (ApplianceType applianceType : applianceTypes) {
					view.showApplianceType(applianceType.getName(), applianceType.getDescription());
				}
			}
		});
	}

	@Override
	public IsWidget getWidget() {
		return view;
	}

	@Override
	public void onShowNewApplianceModal() {
		view.showNewApplianceModal(true);
	}

	@Override
	public void onAddApplianceType() {
		NewApplianceType newApplianceType = new NewApplianceType();
		newApplianceType.setName(view.getNewApplianceTypeName().getText());
		newApplianceType.setDescription(view.getNewApplianceTypeDescription().getText());
		cloudFacadeController.addApplianceType(newApplianceType, new Command() {
			@Override
			public void execute() {
				view.showNewApplianceModal(false);
			}});
	}
}