package pl.cyfronet.coin.clew.client.widgets.instance;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Collapse;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Popover;
import org.gwtbootstrap3.client.ui.Tooltip;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.Placement;
import org.gwtbootstrap3.client.ui.constants.Toggle;
import org.gwtbootstrap3.client.ui.constants.Trigger;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.IHttpMappingView;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InstanceView extends Composite implements IInstanceView, ReverseViewInterface<IInstancePresenter> {
	private static InstanceViewUiBinder uiBinder = GWT.create(InstanceViewUiBinder.class);
	interface InstanceViewUiBinder extends UiBinder<Widget, InstanceView> {}
	interface Styles extends CssResource {
		String anchor();
		String descriptor();
		String service();
		String detailsName();
		String links();
	}
	
	private IInstancePresenter presenter;
	private Button shutdown;
	private Button externalInterfaces;
	private Button saveButton;
	private Label noServiceLabel;
	private Label noWebApplicationLabel;
	private Label noOtherServicesLabel;
	private boolean collapsed;
	private Button rebootButton;
	private Button saveInPlaceButton;
	
	@UiField HTML name;
	@UiField InstanceMessages messages;
	@UiField HTML ip;
	@UiField HTML location;
	@UiField FlowPanel status;
	@UiField ButtonGroup controls;
	@UiField Collapse collapse;
	@UiField FlowPanel webApplicationsContainer;
	@UiField FlowPanel serviceContainer;
	@UiField Styles style;
	@UiField FlowPanel otherServiceContainer;
	@UiField HTML description;
	@UiField Label bill;
	@UiField Button showDetails;
	@UiField Tooltip prepaidTooltip;
	@UiField FlowPanel details;
	@UiField FlowPanel noVmsLabel;

	public InstanceView() {
		initWidget(uiBinder.createAndBindUi(this));
		collapsed = true;
	}
	
	@UiHandler("showDetails")
	void showDetailsClicked(ClickEvent event) {
		collapse.toggle();
		collapsed = !collapsed;
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
	public String getSpecStanza(String cpu, String ram, String disk) {
		return messages.getSpec(cpu != null ? cpu : "0", ram != null ? ram : "0", disk != null ? disk : "0");
	}

	@Override
	public HasHTML getIp() {
		return ip;
	}

	@Override
	public HasHTML getLocation() {
		return location;
	}

	@Override
	public void setShutdownBusyState(boolean busy) {
		if(shutdown != null) {
			BootstrapHelpers.setButtonBusyState(shutdown, busy);
		}
	}

	@Override
	public boolean confirmInstanceShutdown() {
		return Window.confirm(messages.shutdownConfirmation());
	}

	@Override
	public void addShutdownControl() {
		if(shutdown == null) {
			shutdown = new Button();
			shutdown.setIcon(IconType.POWER_OFF);
			shutdown.setType(ButtonType.DANGER);
			shutdown.setSize(ButtonSize.EXTRA_SMALL);
			shutdown.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
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

	public void showNoServicesLabel(boolean show) {
		if(show) {
			if (noServiceLabel == null) {
				noServiceLabel = new Label(messages.noServicesLabel());
				serviceContainer.add(noServiceLabel);
			}
		} else {
			if (noServiceLabel != null) {
				serviceContainer.remove(noServiceLabel);
				noServiceLabel = null;
			}
		}
	}

	@Override
	public void showNoWebApplicationsLabel(boolean show) {
		if(show) {
			if (noWebApplicationLabel == null) {
				noWebApplicationLabel = new Label(messages.noWebApplicationsLabel());
				webApplicationsContainer.add(noWebApplicationLabel);
			}
		} else {
			if (noWebApplicationLabel != null) {
				webApplicationsContainer.remove(noWebApplicationLabel);
				noWebApplicationLabel = null;
			}
		}
	}

	@Override
	public void addExternalInterfacesControl() {
		if(externalInterfaces == null) {
			externalInterfaces = new Button();
			externalInterfaces.setIcon(IconType.COGS);
			externalInterfaces.setType(ButtonType.WARNING);
			externalInterfaces.setSize(ButtonSize.EXTRA_SMALL);
			externalInterfaces.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
			externalInterfaces.setTitle(messages.externalInterfacesTooltip());
			externalInterfaces.setEnabled(false);
			externalInterfaces.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onExternalInterfacesClicked();
				}
			});
			controls.insert(externalInterfaces, 0);
		}
	}

	@Override
	public void addSaveControl() {
		if(saveButton == null) {
			saveButton = new Button();
			saveButton.setIcon(IconType.COPY);
			saveButton.setType(ButtonType.WARNING);
			saveButton.setSize(ButtonSize.EXTRA_SMALL);
			saveButton.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
			saveButton.setTitle(messages.saveTooltip());
			saveButton.setEnabled(false);
			saveButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSave();
				}
			});
			controls.insert(saveButton, 0);
		}
	}

	@Override
	public void removeWebapp(IsWidget widget) {
		webApplicationsContainer.remove(widget);
	}

	@Override
	public void removeService(IsWidget widget) {
		serviceContainer.remove(widget);
	}

	@Override
	public IsWidget addOtherService(String serviceName, String publicIp, String port, String helpBlock) {
		FlowPanel panel = new FlowPanel();
		panel.addStyleName(style.service());
		
		FlowPanel namePanel = new FlowPanel();
		InlineHTML nameWidget = new InlineHTML(serviceName);
		nameWidget.addStyleName(style.detailsName());
		namePanel.add(nameWidget);
		panel.add(namePanel);
		
		HTML info = new HTML(publicIp + ":" + port);
		info.addStyleName(style.links());
		panel.add(info);
		
		if(helpBlock != null) {
			Button helpButton = new Button("");
			helpButton.setIcon(IconType.SIGN_IN);
			helpButton.setSize(ButtonSize.EXTRA_SMALL);
			helpButton.setDataToggle(Toggle.BUTTON);
			helpButton.setType(ButtonType.INFO);
			helpButton.addStyleName(style.descriptor());
			
			Popover helpPopover = new Popover();
			helpPopover.setContent(helpBlock);
			helpPopover.setIsHtml(true);
			helpPopover.setWidget(helpButton);
			helpPopover.setPlacement(Placement.LEFT);
			helpPopover.setTitle(messages.sshHelpHeader());
			helpPopover.setTrigger(Trigger.CLICK);
			
			panel.add(new InlineHTML("&nbsp;"));
			namePanel.add(helpPopover);
		}
		
		otherServiceContainer.add(panel);
		
		return panel;
	}

	@Override
	public void showNoOtherServicesLabel(boolean show) {
		if(show) {
			if(noOtherServicesLabel == null) {
				noOtherServicesLabel = new Label(messages.noOtherServices());
				otherServiceContainer.add(noOtherServicesLabel);
			}
		} else {
			if(noOtherServicesLabel != null) {
				otherServiceContainer.remove(noOtherServicesLabel);
				noOtherServicesLabel = null;
			}
		}
	}

	@Override
	public void removeOtherService(IsWidget widget) {
		otherServiceContainer.remove(widget);
	}

	@Override
	public void setUnsatisfiedState(String stateExplanation) {
		status.clear();
		
		Label unsatisfiedLabel = new Label(messages.unsatisfiedLabel());
		unsatisfiedLabel.setType(LabelType.DANGER);
		
		Tooltip tooltip = new Tooltip();
		tooltip.setText(stateExplanation);
		tooltip.add(unsatisfiedLabel);
		status.add(tooltip);
	}

	@Override
	public String getSshHelpBlock(String publicIp, String sourcePort) {
		return messages.sshHelpBlock(publicIp, sourcePort);
	}

	@Override
	public void setNoDescription() {
		description.setText(messages.getEmptyDescription());
	}

	@Override
	public HasText getDescription() {
		return description;
	}

	@Override
	public HasText getCost() {
		return bill;
	}

	@Override
	public void setStatus(String status) {
		Widget label = new Label(status);
		
		if(status != null) {
			if(status.equals("active")) {
				((Label) label).setType(LabelType.SUCCESS);
			} else {
				((Label) label).setType(LabelType.WARNING);
			}
		} else {
			label = new HTML("&nbsp;");
		}
		
		this.status.clear();
		this.status.add(label);
	}

	@Override
	public void enableSave(boolean enable) {
		if(saveButton != null) {
			saveButton.setEnabled(enable);
		}
	}

	@Override
	public void enableExternalInterfaces(boolean enable) {
		if(externalInterfaces != null) {
			externalInterfaces.setEnabled(enable);
		}
	}

	@Override
	public void enableCollapsable(boolean enable) {
		showDetails.setEnabled(enable);
	}

	@Override
	public void collapseDetails() {
		if(!collapsed) {
			showDetailsClicked(null);
			showDetails.setActive(false);
		}
	}

	@Override
	public void setFlavorDetails(String prepaidUntil, String flavorName, Float cpus, Float ram, Float hdd) {
		prepaidTooltip.setText(messages.prepaidUntil(prepaidUntil, flavorName, cpus, ram, hdd));
		prepaidTooltip.reconfigure();
	}

	@Override
	public void showDetailsPanel(boolean show) {
		details.setVisible(show);
	}

	@Override
	public void showNoVmsLabel(boolean show) {
		noVmsLabel.setVisible(show);
	}

	@Override
	public String getNoVmsLabel() {
		return messages.noVmsShortLabel();
	}

	@Override
	public void addRebootControl() {
		if(rebootButton == null) {
			rebootButton = new Button();
			rebootButton.setIcon(IconType.REFRESH);
			rebootButton.setType(ButtonType.DANGER);
			rebootButton.setSize(ButtonSize.EXTRA_SMALL);
			rebootButton.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
			rebootButton.setTitle(messages.rebootTooltip());
			rebootButton.setEnabled(false);
			rebootButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onReboot();
				}
			});
			controls.add(rebootButton);
		}
	}

	@Override
	public void enableReboot(boolean enable) {
		rebootButton.setEnabled(enable);
	}

	@Override
	public boolean confirmInstanceReboot() {
		return Window.confirm(messages.confirmReboot());
	}

	@Override
	public void setRebootBusyState(boolean busy) {
		if(rebootButton != null) {
			BootstrapHelpers.setButtonBusyState(rebootButton, busy);
		}
	}

	@Override
	public void addSaveInPlaceControl() {
		if(saveInPlaceButton == null) {
			saveInPlaceButton = new Button();
			saveInPlaceButton.setIcon(IconType.SAVE);
			saveInPlaceButton.setType(ButtonType.WARNING);
			saveInPlaceButton.setSize(ButtonSize.EXTRA_SMALL);
			saveInPlaceButton.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
			saveInPlaceButton.setTitle(messages.saveInPlaceTooltip());
			saveInPlaceButton.setEnabled(false);
			saveInPlaceButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSaveInPlace();
				}
			});
			controls.insert(saveInPlaceButton, 0);
		}
	}

	@Override
	public boolean saveInPlaceConfirmation() {
		return Window.confirm(messages.confirmSaveInPlace());
	}

	@Override
	public String missingApplianceType() {
		return messages.missingApplianceType();
	}

	@Override
	public void setSaveInPlaceBusyState(boolean state) {
		if(saveInPlaceButton != null) {
			BootstrapHelpers.setButtonBusyState(saveInPlaceButton, state);
		}
	}

	@Override
	public void enableSaveInPlace(boolean enable) {
		if(saveInPlaceButton != null) {
			saveInPlaceButton.setEnabled(enable);
		}
	}

	@Override
	public void addWebApplication(IsWidget view) {
		webApplicationsContainer.add(view);
	}

	@Override
	public void addService(IHttpMappingView view) {
		serviceContainer.add(view);
	}
}