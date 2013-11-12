package pl.cyfronet.coin.clew.client.widgets.root;

import pl.cyfronet.coin.clew.client.ErrorCode;
import pl.cyfronet.coin.clew.client.MainEventBus;
import pl.cyfronet.coin.clew.client.widgets.root.IRootView.IRootPresenter;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

@Presenter(view = RootView.class)
public class RootPresenter extends BasePresenter<IRootView, MainEventBus> implements IRootPresenter {
	private RootMessages messages;
	private Timer errorTimer;

	@Inject
	public RootPresenter(RootMessages messages) {
		this.messages = messages;
	}
	
	public void onSetMenu(IsWidget widget) {
		view.setMenu(widget);
	}
	
	public void onSetBody(IsWidget widget) {
		view.setBody(widget);
	}
	
	public void onAddPopup(IsWidget widget) {
		view.addPopup(widget);
	}
	
	public void onDisplayError(ErrorCode errorCode) {
		String errorMessage = messages.getString(errorCode.name());
		view.getErrorLabel().setText(errorMessage);
		view.setErrorLabelVisible(true);
		
		if (errorTimer != null) {
			errorTimer.cancel();
		}
		
		errorTimer = new Timer() {
			@Override
			public void run() {
				view.getErrorLabel().setText("");
				view.setErrorLabelVisible(false);
				errorTimer = null;
			}
		};
		errorTimer.schedule(5000);
	}
}