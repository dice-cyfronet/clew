package pl.cyfronet.coin.clew.client.widgets.startinstance;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.IconType;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.startinstance.IStartInstanceView.IStartInstancePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class StartInstanceView extends Composite implements IStartInstanceView, ReverseViewInterface<IStartInstancePresenter> {
	private static StartInstanceViewUiBinder uiBinder = GWT.create(StartInstanceViewUiBinder.class);
	interface StartInstanceViewUiBinder extends UiBinder<Widget, StartInstanceView> {}
	
	private IStartInstancePresenter presenter;
	private Timer filterTimer;
	
	@UiField Modal startInstanceModal;
	@UiField FlowPanel applianceTypeContainer;
	@UiField StartInstanceMessages messages;
	@UiField Button startSelected;
	@UiField TextBox filterAppsBox;

	public StartInstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
		startInstanceModal.setWidth("1000px");
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		getPresenter().onHide();
	}
	
	@UiHandler("startSelected")
	void startSelectedClicked(ClickEvent event) {
		getPresenter().onStartSelected();
	}
	
	@UiHandler("filterAppsBox")
	void filterTextTyped(KeyUpEvent event) {
		if (filterTimer != null) {
			filterTimer.cancel();
		} else {
			filterTimer = new Timer() {
				@Override
				public void run() {
					getPresenter().onFilterTextChanged();
					filterTimer = null;
				}
			};
		}
		
		filterTimer.schedule(500);
	}
	
	@Override
	public void setPresenter(IStartInstancePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IStartInstancePresenter getPresenter() {
		return presenter;
	}

	@Override
	public void show() {
		startInstanceModal.show();
	}

	@Override
	public void showProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("fa-spin");
		applianceTypeContainer.add(icon);
	}

	@Override
	public void clearApplianceTypeContainer() {
		applianceTypeContainer.clear();
	}

	@Override
	public void addNoApplianceTypesLabel() {
		applianceTypeContainer.add(new Label(messages.noApplianceTypesLabel()));
	}

	@Override
	public HasWidgets getApplianceTypeContainer() {
		return applianceTypeContainer;
	}

	@Override
	public void hide() {
		startInstanceModal.hide();
	}

	@Override
	public void showNoApplianceTypesSelected() {
		Window.alert(messages.noSelectedApplianceTypes());
	}

	@Override
	public void setStartSelectedBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(startSelected, busy);
	}

	@Override
	public HasText getFilter() {
		return filterAppsBox;
	}

	@Override
	public void setDevelopmentModeTitle() {
		startInstanceModal.setTitle(messages.startDevInstanceModalHeader());
	}

	@Override
	public void setPortalModeTitle() {
		startInstanceModal.setTitle(messages.startInstanceModalHeader());
	}
}