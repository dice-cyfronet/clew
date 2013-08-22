package pl.cyfronet.coin.clew.client.widgets.dashboard;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DashboardPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showStartAppPopup();
		void clearAppsTable();
		void setAppsSpinnerVisible(boolean visible);
		void addStartButton(int i);
		void addCheckButton(int i);
		void addText(int i, int j, String text);
	}
	
	private View view;
	private CloudFacadeController cloudFacadeController;
	
	@Inject
	public DashboardPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
	}
	
	@Override
	public IsWidget getWidget() {
		return view;
	}

	@Override
	public void onShowStartAppModal() {
		view.showStartAppPopup();
		onStartAppModalShown();
	}

	@Override
	public void onStartAppModalShown() {
		view.clearAppsTable();
		view.setAppsSpinnerVisible(true);
		
		new Timer() {
			
			@Override
			public void run() {
				view.setAppsSpinnerVisible(false);
				int i = 0;
				
				for(AtomicService atomicService : cloudFacadeController.getAtomicServices()) {
					//TODO - refactor this!
					view.addStartButton(i);
					view.addCheckButton(i);
					view.addText(i, 2, atomicService.getName());
					view.addText(i, 3, atomicService.getDescription());
					i++;
				}
			}
		}.schedule(2000);
	}
}