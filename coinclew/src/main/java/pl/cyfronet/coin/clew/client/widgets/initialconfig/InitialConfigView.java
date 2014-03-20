package pl.cyfronet.coin.clew.client.widgets.initialconfig;

import pl.cyfronet.coin.clew.client.widgets.initialconfig.IInitialConfigView.IInitialConfigPresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InitialConfigView extends Composite implements IInitialConfigView, ReverseViewInterface<IInitialConfigPresenter> {
	private static InitialConfigViewUiBinder uiBinder = GWT.create(InitialConfigViewUiBinder.class);
	interface InitialConfigViewUiBinder extends UiBinder<Widget, InitialConfigView> {}
	interface Styles extends CssResource {
		String param();
	}
	
	private IInitialConfigPresenter presenter;
	
	@UiField HTML name;
	@UiField FlowPanel parameters;
	@UiField Styles style;
	@UiField InitialConfigMessages messages;
	@UiField Button edit;

	public InitialConfigView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("remove")
	void onRemoveClicked(ClickEvent event) {
		getPresenter().onRemove();
	}
	
	@UiHandler("edit")
	void onEditClicked(ClickEvent event) {
		getPresenter().onEdit();
	}
	
	@Override
	public void setPresenter(IInitialConfigPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInitialConfigPresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public void addParameter(String parameter) {
		Label label = new Label(parameter);
		label.setType(LabelType.INFO);
		label.addStyleName(style.param());
		parameters.add(label);
	}

	@Override
	public void addNoParametersLabel() {
		Label label = new Label(messages.noParametersLabel());
		parameters.add(label);
	}

	@Override
	public void clearParameterContainer() {
		parameters.clear();
	}

	@Override
	public void cancelEdit() {
		edit.setActive(false);
	}
}