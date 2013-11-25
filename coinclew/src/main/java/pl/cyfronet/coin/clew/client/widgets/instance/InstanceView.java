package pl.cyfronet.coin.clew.client.widgets.instance;

import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InstanceView extends Composite implements IInstanceView, ReverseViewInterface<IInstancePresenter> {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}
	interface Styles extends CssResource {
		String anchor();
		String descriptor();
		String service();
		String name();
	}
	
	private IInstancePresenter presenter;
	private Button shutdown;
	private Button externalInterfaces;
	
	@UiField HTML name;
	@UiField HTML spec;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField HTML status;
	@UiField ButtonGroup controls;
	@UiField Collapse collapse;
	@UiField FlowPanel webApplicationsContainer;
	@UiField FlowPanel serviceContainer;
	@UiField Styles style;

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
		if (shutdown == null) {
			shutdown = new Button();
			shutdown.setIcon(IconType.OFF);
			shutdown.setType(ButtonType.DANGER);
			shutdown.setSize(ButtonSize.MINI);
			shutdown.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			shutdown.setTitle(messages.shutdownTooltip());
			shutdown.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onShutdownClicked();
				}
			});
			controls.add(shutdown);
		}
	}

	@Override
	public void addService(String name, String httpUrl, String httpsUrl, String descriptor) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		HTML nameWidget = new HTML(name);
		panel.addStyleName(style.name());
		panel.add(nameWidget);
		
		if (httpUrl != null) {
			Anchor http = new Anchor(httpUrl, httpUrl);
			http.addStyleName(style.anchor());
			panel.add(http);
		}
		
		if (httpsUrl != null) {
			Anchor https = new Anchor(httpsUrl, httpsUrl);
			https.addStyleName(style.anchor());
			panel.add(https);
		}
		
		Button descriptorButton = new Button();
		descriptorButton.setIcon(IconType.FILE);
		descriptorButton.setType(ButtonType.INFO);
		descriptorButton.addStyleName(style.descriptor());
		descriptorButton.setSize(ButtonSize.MINI);
		
		if (descriptor != null && !descriptor.trim().isEmpty()) {
			final Modal modal = new Modal();
			modal.setTitle(messages.descriptorModalTitle());
			modal.add(new HTML(descriptor));
			panel.add(modal);
			descriptorButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					modal.show();
				}
			});
		} else {
			descriptorButton.setEnabled(false);
		}
		
		panel.add(descriptorButton);
		serviceContainer.add(panel);
	}

	@Override
	public void addWebApplication(String name, String httpUrl, String httpsUrl) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		HTML nameWidget = new HTML(name);
		panel.addStyleName(style.name());
		panel.add(nameWidget);
		
		if (httpUrl != null) {
			Anchor http = new Anchor(httpUrl, httpUrl);
			http.addStyleName(style.anchor());
			panel.add(http);
		}
		
		if (httpsUrl != null) {
			Anchor https = new Anchor(httpsUrl, httpsUrl);
			https.addStyleName(style.anchor());
			panel.add(https);
		}
		
		webApplicationsContainer.add(panel);
	}

	@Override
	public void addNoServicesLabel() {
		Label label = new Label(messages.noServicesLabel());
		serviceContainer.add(label);
	}

	@Override
	public void addNoWebApplicationsLabel() {
		Label label = new Label(messages.noWebApplicationsLabel());
		webApplicationsContainer.add(label);
	}

	@Override
	public void addExternalInterfacesControl() {
		if (externalInterfaces == null) {
			externalInterfaces = new Button();
			externalInterfaces.setIcon(IconType.ANCHOR);
			externalInterfaces.setType(ButtonType.WARNING);
			externalInterfaces.setSize(ButtonSize.MINI);
			externalInterfaces.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			externalInterfaces.setTitle(messages.externalInterfacesTooltip());
			externalInterfaces.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onExternalInterfacesClicked();
				}
			});
			controls.insert(externalInterfaces, 1);
		}
	}
}