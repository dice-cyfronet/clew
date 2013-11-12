package pl.cyfronet.coin.clew.client.widgets.applications;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationsView extends Composite implements IApplicationsView {
	private static ApplicationsViewUiBinder uiBinder = GWT.create(ApplicationsViewUiBinder.class);
	interface ApplicationsViewUiBinder extends UiBinder<Widget, ApplicationsView> {}
	
	@UiField FlowPanel instanceContainer;
	@UiField ApplicationsMessages messages;

	public ApplicationsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public HasWidgets getInstanceContainer() {
		return instanceContainer;
	}

	@Override
	public void showLoadingInicator() {
		Icon icon = new Icon(IconType.SPINNER);
		icon.setSpin(true);
		instanceContainer.add(icon);
	}

	@Override
	public void clearInstanceContainer() {
		instanceContainer.clear();
	}

	@Override
	public void addNoInstancesLabel() {
		instanceContainer.add(new Label(messages.noInstancesLabel()));
	}
}