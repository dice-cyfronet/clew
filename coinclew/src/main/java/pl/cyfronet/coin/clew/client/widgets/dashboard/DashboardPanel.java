package pl.cyfronet.coin.clew.client.widgets.dashboard;

import org.fusesource.restygwt.client.JsonCallback;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;

import pl.cyfronet.coin.clew.client.widgets.dashboard.DashboardPresenter.View;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.event.ShownEvent;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class DashboardPanel extends Composite implements View {
	private static DashboardPanelUiBinder uiBinder = GWT.create(DashboardPanelUiBinder.class);
	interface DashboardPanelUiBinder extends UiBinder<Widget, DashboardPanel> {}
	
	private Provider<Presenter> presenter;

	@UiField Modal startAppPopup;
	@UiField FlexTable appsTable;
	@UiField Icon appListSpinner;

	@Inject
	public DashboardPanel(Provider<Presenter> presenter) {
		this.presenter = presenter;
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showStartAppModal")
	void onShowStartAppModal(ClickEvent event) {
		presenter.get().onShowStartAppModal();
	}
	
	@UiHandler("startSelectedApps")
	void onStartApp(ClickEvent event) {
		Window.alert("TODO");
	}
	
	@UiHandler("startAppPopup")
	void onStartAppModalShown(ShownEvent event) {
		presenter.get().onStartAppModalShown();
	}
	
	@Deprecated
	void onCors(ClickEvent event) {
		Resource r = new Resource("http://localhost:3000/api/v1/appliance_sets");
		r.get().send(new JsonCallback() {
			@Override
			public void onFailure(Method method, Throwable exception) {
				Window.alert(exception.getMessage());
			}

			@Override
			public void onSuccess(Method method, JSONValue response) {
				Window.alert(response.toString());
			}
		});
	}

	@Override
	public void showStartAppPopup() {
		startAppPopup.show();
	}

	@Override
	public void clearAppsTable() {
		appsTable.clear();
	}

	@Override
	public void setAppsSpinnerVisible(boolean visible) {
		appListSpinner.setVisible(visible);
	}

	@Override
	public void addStartButton(int i) {
		Button startButton = new Button("", IconType.PLAY);
		startButton.setType(ButtonType.SUCCESS);
		startButton.setSize(ButtonSize.MINI);
		appsTable.setWidget(i, 0, startButton);
	}

	@Override
	public void addCheckButton(int i) {
		CheckBox checkBox = new CheckBox();
		appsTable.setWidget(i, 1, checkBox);
	}

	@Override
	public void addText(int i, int j, String text) {
		appsTable.setText(i, j, text);
	}
}