package pl.cyfronet.coin.clew.client.widgets.startinstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.auth.MiTicketReader;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.AggregateApplianceTypesCallback;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UserCallback;
import pl.cyfronet.coin.clew.client.controller.cf.CloudFacadeError;
import pl.cyfronet.coin.clew.client.controller.cf.aggregates.appliancetype.AggregateApplianceType;
import pl.cyfronet.coin.clew.client.controller.cf.user.User;
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
	
	/**
	 * postponedQuery and presentersLoaded are used to handle filter input before the appliances are loaded and
	 * carry out filtering afterwards. 
	 */
	private String postponedQuery;
	private boolean presentersLoaded;
	private MiTicketReader ticketReader;

	@Inject
	public StartInstancePresenter(CloudFacadeController cloudFacadeController, MiTicketReader ticketReader) {
		this.cloudFacadeController = cloudFacadeController;
		this.ticketReader = ticketReader;
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
		cloudFacadeController.aggregateApplianceTypes(developmentMode ? "development" : "production", new AggregateApplianceTypesCallback() {
			@Override
			public void onError(CloudFacadeError error) {
				eventBus.displayError(error);
			}
			
			@Override
			public void processApplianceTypes(final List<AggregateApplianceType> applianceTypes) {
				cloudFacadeController.getUserWithLogin(ticketReader.getUserLogin(), new UserCallback() {
					@Override
					public void processUser(User user) {
						if(user != null) {
							displayApplianceTypes(applianceTypes, user);
						}
					}
				});
			}
		});
	}
	
	protected void displayApplianceTypes(List<AggregateApplianceType> applianceTypes, User user) {
		view.clearApplianceTypeContainer();
		
		if(applianceTypes.size() == 0) {
			view.addNoApplianceTypesLabel();
		} else {
			Collections.sort(applianceTypes, new Comparator<AggregateApplianceType>() {
				@Override
				public int compare(AggregateApplianceType o1, AggregateApplianceType o2) {
					return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
				}
			});
			
			for(AggregateApplianceType applianceType : applianceTypes) {
				ApplianceTypePresenter presenter = eventBus.addHandler(ApplianceTypePresenter.class);
				applianceTypePresenters.add(presenter);
				presenter.setApplianceType(applianceType, developmentMode, user.getTeams());
				view.getApplianceTypeContainer().add(presenter.getView().asWidget());
			}
			
			presentersLoaded = true;
			
			if(postponedQuery != null) {
				onFilterTextChanged();
				postponedQuery = null;
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
		
		applianceTypePresenters.clear();
	}

	@Override
	public void onStartSelected() {
		List<String> initialConfigurationIds = new ArrayList<String>();
		Map<String, List<String>> computeSiteIds = new HashMap<String, List<String>>();
		Map<String, String> teams = new HashMap<String, String>();
		
		for(ApplianceTypePresenter presenter : applianceTypePresenters) {
			String initialConfigId = presenter.getSelectedInitialConfigId();
			
			if (initialConfigId != null) {
				initialConfigurationIds.add(initialConfigId);
			}
			
			String computeSiteId = presenter.getSelectedComputeSiteId();
			
			if(computeSiteId != null && !computeSiteId.equals("0")) {
				computeSiteIds.put(initialConfigId, Arrays.asList(new String[] {computeSiteId}));
			}
			
			String teamId = presenter.getTeamId();
			teams.put(initialConfigId, teamId);
		}
		
		if(initialConfigurationIds.size() == 0) {
			view.showNoApplianceTypesSelected();
		} else {
			onHideStartInstanceModal();
			eventBus.startApplications(initialConfigurationIds, computeSiteIds, developmentMode, teams);
		}
	}

	@Override
	public void onFilterTextChanged() {
		String filterText = view.getFilter().getText();
		
		if(!presentersLoaded) {
			postponedQuery = filterText;
		} else {
			for (ApplianceTypePresenter presenter : applianceTypePresenters) {
				if (presenter.matchesFilter(filterText)) {
					presenter.getView().asWidget().setVisible(true);
				} else {
					presenter.getView().asWidget().setVisible(false);
				}
			}
		}
	}

	@Override
	public void onHide() {
		onHideStartInstanceModal();
	}
}