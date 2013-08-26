package pl.cyfronet.coin.clew.client.widgets.dashboard;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;

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
		void setAppVisibility(int i, boolean b);
	}
	
	private final static Logger log = LoggerFactory.getLogger(DashboardPresenter.class);
	
	private View view;
	private CloudFacadeController cloudFacadeController;
	private List<AtomicService> atomicServices;
	
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
		
		atomicServices = cloudFacadeController.getAtomicServices();
		view.setAppsSpinnerVisible(false);
		
		int i = 0;
		
		for(AtomicService atomicService : atomicServices) {
			view.addStartButton(i);
			view.addCheckButton(i);
			view.addText(i, 2, atomicService.getName());
			view.addText(i, 3, atomicService.getDescription());
			i++;
		}
	}

	@Override
	public void onFilter(String text) {
		log.debug("Filtering apps for {}", text);
		int i = 0;
		
		for (AtomicService atomicService : atomicServices) {
			if (atomicService.getName() != null && atomicService.getName().contains(text) ||
					atomicService.getDescription() != null && atomicService.getDescription().contains(text)) {
				view.setAppVisibility(i, true);
			} else {
				view.setAppVisibility(i, false);
			}
			
			i++;
		}
	}
}