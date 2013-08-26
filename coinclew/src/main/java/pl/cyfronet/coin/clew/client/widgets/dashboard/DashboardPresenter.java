package pl.cyfronet.coin.clew.client.widgets.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.cyfronet.coin.clew.client.common.BasePresenter;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.beans.cf.AtomicService;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

public class DashboardPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		void showStartAppPopup();
		void clearAppsTable();
		void setAppsSpinnerVisible(boolean visible);
		void addStartButton(int i);
		HasValue<Boolean> addCheckButton(int i);
		void addText(int i, int j, String text);
		void setAppVisibility(int i, boolean b);
		void setStartSelectedWidgetBusyState(boolean b);
		void hideStartAppPopup();
		void setStartAppWidgetBusyState(int i, boolean b);
	}
	
	private final static Logger log = LoggerFactory.getLogger(DashboardPresenter.class);
	
	private View view;
	private CloudFacadeController cloudFacadeController;
	private List<AtomicService> atomicServices;
	private List<HasValue<Boolean>> appChecks;
	
	@Inject
	public DashboardPresenter(View view, CloudFacadeController cloudFacadeController) {
		this.view = view;
		this.cloudFacadeController = cloudFacadeController;
		appChecks = new ArrayList<HasValue<Boolean>>();
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
		appChecks.clear();
		
		for(AtomicService atomicService : atomicServices) {
			view.addStartButton(i);
			appChecks.add(view.addCheckButton(i));
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

	@Override
	public void onStartSelected() {
		List<String> startIds = new ArrayList<String>();
		int i = 0;
		
		for (HasValue<Boolean> selected : appChecks) {
			if (selected.getValue()) {
				startIds.add(atomicServices.get(i).getId());
			}
			
			i++;
		}
		
		log.info("Starting atomic services with ids {}", startIds);
		view.setStartSelectedWidgetBusyState(true);
		cloudFacadeController.startAtomicServices(startIds, new Command() {
			@Override
			public void execute() {
				view.setStartSelectedWidgetBusyState(false);
				view.hideStartAppPopup();
			}
		});
	}

	@Override
	public void onStartSingle(final int i) {
		log.info("Starting single atomic service {}", atomicServices.get(i).getId());
		view.setStartAppWidgetBusyState(i, true);
		cloudFacadeController.startAtomicServices(
				Arrays.asList(atomicServices.get(i).getId()), new Command() {
					@Override
					public void execute() {
						view.setStartAppWidgetBusyState(i, false);
					}
				});
	}
}