package pl.cyfronet.coin.clew.client.widgets.instance;

import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InstanceView extends Composite implements IInstanceView, ReverseViewInterface<IInstancePresenter> {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}
	
	private IInstancePresenter presenter;
	private Button shutdown;
	
	@UiField HTML name;
	@UiField HTML spec;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField HTML status;
	@UiField ButtonGroup controls;
	@UiField Collapse collapse;

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("showDetails")
	void showDetailsClicked(ClickEvent event) {
		collapse.toggle();
	}
	
	@Override
	public void setPresenter(IInstancePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInstancePresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public HasText getSpec() {
		return spec;
	}

	@Override
	public String getSpecStanza(String cpu, String ram, String disk) {
		return messages.getSpec(cpu != null ? cpu : "0", ram != null ? ram : "0", disk != null ? disk : "0");
	}

	@Override
	public HasText getIp() {
		return ip;
	}

	@Override
	public HasText getLocation() {
		return location;
	}

	@Override
	public HasText getStatus() {
		return status;
	}

	@Override
	public void setShutdownBusyState(boolean busy) {
		if (shutdown != null) {
			if (busy) {
				shutdown.state().loading();
			} else {
				shutdown.state().reset();
			}
		}
	}

	@Override
	public boolean confirmInstanceShutdown() {
		return Window.confirm(messages.shutdownConfirmation());
	}

	@Override
	public void addShutdownControl() {
		shutdown = new Button();
		shutdown.setIcon(IconType.OFF);
		shutdown.setType(ButtonType.DANGER);
		shutdown.setSize(ButtonSize.MINI);
		shutdown.setLoadingText("&lt;i class='icon-spinner icon-spin'&gt;&lt;/i&gt;");
		shutdown.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getPresenter().onShutdownClicked();
			}
		});
		controls.add(shutdown);
	}
}