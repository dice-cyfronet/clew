package pl.cyfronet.coin.clew.client.widgets.appliancedetails;

import pl.cyfronet.coin.clew.client.widgets.appliancedetails.IApplianceDetailsView.IApplianceDetailsPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceDetailsView extends Composite implements IApplianceDetailsView, ReverseViewInterface<IApplianceDetailsPresenter> {
	private static ApplianceDetailsViewUiBinder uiBinder = GWT.create(ApplianceDetailsViewUiBinder.class);
	interface ApplianceDetailsViewUiBinder extends UiBinder<Widget, ApplianceDetailsView> {}
	
	private IApplianceDetailsPresenter presenter;
	
	@UiField Modal modal;
	@UiField Button start;
	@UiField ApplianceDetailsMessages messages;
	@UiField FlowPanel container;
	@UiField FlowPanel nameContainer;

	public ApplianceDetailsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void closeClicked(ClickEvent event) {
		modal.hide();
	}
	
	@UiHandler("start")
	void startClicked(ClickEvent event) {
		getPresenter().onStartInstance();
	}
	
	@Override
	public void setPresenter(IApplianceDetailsPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IApplianceDetailsPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			modal.show();
		} else {
			modal.hide();
		}
	}

	@Override
	public HasValue<Boolean> addKey(String id, String name) {
		RadioButton button = new RadioButton("key", name);
		button.setFormValue(id);
		container.add(button);
		
		return button;
	}

	@Override
	public void setStartBusyState(boolean busy) {
		if (busy) {
			start.state().loading();
		} else {
			start.state().reset();
		}
	}

	@Override
	public HasText addName(String name) {
		ControlLabel label = new ControlLabel(messages.nameLabel(name));
		TextBox nameBox = new TextBox();
		nameBox.setPlaceholder(messages.namePlaceholder());
		nameContainer.add(label);
		nameContainer.add(nameBox);
		
		return nameBox;
	}

	@Override
	public HasWidgets getContainer() {
		return container;
	}

	@Override
	public HasWidgets getNameContainer() {
		return nameContainer;
	}
}