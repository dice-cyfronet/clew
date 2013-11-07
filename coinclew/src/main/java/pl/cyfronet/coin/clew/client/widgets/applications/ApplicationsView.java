package pl.cyfronet.coin.clew.client.widgets.applications;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationsView extends Composite implements IApplicationsView {
	private static ApplicationsViewUiBinder uiBinder = GWT.create(ApplicationsViewUiBinder.class);
	interface ApplicationsViewUiBinder extends UiBinder<Widget, ApplicationsView> {}

	public ApplicationsView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
}