package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import java.util.ArrayList;
import java.util.List;

import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.RadioButton;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceTypeView extends Composite implements IApplianceTypeView, ReverseViewInterface<IApplianceTypePresenter> {
	private static ApplianceTypeViewUiBinder uiBinder = GWT.create(ApplianceTypeViewUiBinder.class);
	interface ApplianceTypeViewUiBinder extends UiBinder<Widget, ApplianceTypeView> {}
	
	private IApplianceTypePresenter presenter;
	private List<RadioButton> checkBoxes;

	@UiField HTML name;
	@UiField HTML description;
	@UiField ApplianceTypeMessages messages;
	@UiField Button start;
	@UiField FlowPanel initialConfigsContainer;
	
	public ApplianceTypeView() {
		initWidget(uiBinder.createAndBindUi(this));
		checkBoxes = new ArrayList<RadioButton>();
	}
	
	@UiHandler("start")
	void startClicked(ClickEvent event) {
		presenter.onStartApplianceType();
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getDescription() {
		description.removeStyleName("muted");
		
		return description;
	}

	@Override
	public void setEmptyDescription() {
		description.setText(messages.emptyDescriptionLabel());
		description.addStyleName("muted");
	}

	@Override
	public void setPresenter(IApplianceTypePresenter presenter) {
		this.presenter = presenter;
		
	}

	@Override
	public IApplianceTypePresenter getPresenter() {
		return presenter;
	}

	@Override
	public void setStartButtonBusyState(boolean busy) {
		if (busy) {
			start.state().loading();
		} else {
			start.state().reset();
		}
	}

	@Override
	public void addInitialConfigsProgressIndicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.addStyleName("icon-spin");
		initialConfigsContainer.add(icon);
	}

	@Override
	public void clearInitialConfigsContainer() {
		initialConfigsContainer.clear();
	}

	@Override
	public void addNoInitialConfigsLabel() {
		Label label = new Label(messages.noInitialConfigLabel());
		label.setType(LabelType.WARNING);
		initialConfigsContainer.add(label);
	}

	@Override
	public HasValue<Boolean> addInitialConfigRadioBox(String radioName, String name) {
		RadioButton radio = new RadioButton(radioName);
		radio.setHTML(name);
		initialConfigsContainer.add(radio);
		checkBoxes.add(radio);
		
		return radio;
	}

	@Override
	public void enableControls(boolean enable) {
		start.setEnabled(enable);
		
		for (CheckBox checkbox : checkBoxes) {
			checkbox.setEnabled(enable);
		}
	}
}