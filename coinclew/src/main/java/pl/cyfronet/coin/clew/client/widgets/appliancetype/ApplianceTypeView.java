package pl.cyfronet.coin.clew.client.widgets.appliancetype;

import pl.cyfronet.coin.clew.client.widgets.appliancetype.IApplianceTypeView.IApplianceTypePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class ApplianceTypeView extends Composite implements IApplianceTypeView, ReverseViewInterface<IApplianceTypePresenter> {
	private static ApplianceTypeViewUiBinder uiBinder = GWT.create(ApplianceTypeViewUiBinder.class);
	interface ApplianceTypeViewUiBinder extends UiBinder<Widget, ApplianceTypeView> {}
	
	private IApplianceTypePresenter presenter;

	@UiField HTML name;
	@UiField HTML description;
	@UiField ApplianceTypeMessages messages;
	@UiField Button start;
	
	public ApplianceTypeView() {
		initWidget(uiBinder.createAndBindUi(this));
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
}