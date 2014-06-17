package pl.cyfronet.coin.clew.client.widgets.su;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController;
import pl.cyfronet.coin.clew.client.controller.CloudFacadeController.UsersCallback;
import pl.cyfronet.coin.clew.client.controller.cf.user.User;
import pl.cyfronet.coin.clew.client.widgets.su.ISuView.ISuPresenter;

import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = SuView.class)
public class SuPresenter extends BasePresenter<ISuView, MainEventBus> implements ISuPresenter {
	private static final String CLEW_NONE = "clew.none";
	
	private static String suUser;
	
	private CloudFacadeController cloudFacadeController;

	@Inject
	public SuPresenter(CloudFacadeController cloudFacadeController) {
		this.cloudFacadeController = cloudFacadeController;
	}
	
	public static String getSuUser() {
		return suUser;
	}
	
	public void onSwitchToSuView() {
		eventBus.setBody(view);
		loadUsers();
	}
	
	@Override
	public void onSwitchUser() {
		suUser = view.getSelectedUser();
		
		if(CLEW_NONE.equals(suUser)) {
			suUser = null;
		}
		
		view.setSuUser(suUser);
		eventBus.suUserChanged(suUser);
	}

	private void loadUsers() {
		view.showLoadingIndicator(true);
		view.clearUsers();
		cloudFacadeController.getUsers(new UsersCallback() {
			@Override
			public void processUsers(List<User> users) {
				view.showLoadingIndicator(false);
				Collections.sort(users, new Comparator<User>() {
					@Override
					public int compare(User u1, User u2) {
						return u1.getLogin().compareToIgnoreCase(u2.getLogin());
					}
				});
				
				view.addNoneOption(CLEW_NONE);
				
				for(User user : users) {
					view.addUserOption(user.getLogin(), user.getFullName());
				}
			}});
	}
}