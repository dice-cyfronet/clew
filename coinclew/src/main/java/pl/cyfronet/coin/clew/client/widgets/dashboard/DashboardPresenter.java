package pl.cyfronet.coin.clew.client.widgets.dashboard;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

import pl.cyfronet.coin.clew.client.common.BasePresenter;

public class DashboardPresenter extends BasePresenter implements Presenter {
	public interface View extends IsWidget {
		
	}
	
	private View view;
	
	@Inject
	public DashboardPresenter(View view) {
		this.view = view;
	}
	
	@Override
	public IsWidget getWidget() {
		return view;
	}
}