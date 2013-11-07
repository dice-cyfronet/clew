package pl.cyfronet.coin.clew.client.widgets.root;

import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.root.IRootView.IRootPresenter;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, MainEventBus> implements IRootPresenter {
	public void onSetMenu(IsWidget widget) {
		view.setMenu(widget);
	}
	
	public void onSetBody(IsWidget widget) {
		view.setBody(widget);
	}
}