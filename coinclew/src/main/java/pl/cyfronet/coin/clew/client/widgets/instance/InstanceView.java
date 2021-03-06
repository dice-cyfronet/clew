package pl.cyfronet.coin.clew.client.widgets.instance;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.httpmapping.IHttpMappingView;
import pl.cyfronet.coin.clew.client.widgets.instance.IInstanceView.IInstancePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Popover;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.constants.Placement;
import com.github.gwtbootstrap.client.ui.constants.Trigger;
import com.github.gwtbootstrap.client.ui.constants.VisibilityChange;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
	
	private Button shutdown, externalInterfaces, saveButton, rebootButton, saveInPlaceButton, pauseButton;
	
	private Label noServiceLabel, noWebApplicationLabel, noOtherServicesLabel;
	
	private boolean collapsed;
	
	@UiField
	InstanceMessages messages;
	
	@UiField
	HTML name, ip, location, description;
	
	@UiField
	FlowPanel status, webApplicationsContainer, serviceContainer, otherServiceContainer, details, noVmsLabel;
	
	@UiField
	ButtonGroup controls;
	
	@UiField
	Collapse collapse;
	
	@UiField
	Styles style;
	
	@UiField
	Label bill;
	
	@UiField
	Button showDetails;
	
	@UiField
	Tooltip prepaidTooltip;

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
			externalInterfaces.setSize(ButtonSize.MINI);
			externalInterfaces.setLoadingText("<i class='icon-spinner icon-spin'></i>");
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
			saveButton.setSize(ButtonSize.MINI);
			saveButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
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
		
		HTML info = new HTML(SafeHtmlUtils.fromString(publicIp + ":" + port));
		info.addStyleName(style.links());
		panel.add(info);
		
		if(helpBlock != null) {
			Button helpButton = new Button("", IconType.SIGNIN);
			helpButton.setSize(ButtonSize.MINI);
			helpButton.setToggle(true);
			helpButton.setType(ButtonType.INFO);
			helpButton.addStyleName(style.descriptor());
			helpButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Popover.changeVisibility(event.getRelativeElement(), VisibilityChange.TOGGLE.get());
				}
			});
			
			Popover helpPopover = new Popover();
			helpPopover.setText(helpBlock);
			helpPopover.setHtml(true);
			helpPopover.setWidget(helpButton);
			helpPopover.setPlacement(Placement.LEFT);
			helpPopover.setHeading(messages.sshHelpHeader());
			helpPopover.setTrigger(Trigger.MANUAL);
			
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
		unsatisfiedLabel.setType(LabelType.IMPORTANT);
		
		Tooltip tooltip = new Tooltip(stateExplanation);
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
			rebootButton.setSize(ButtonSize.MINI);
			rebootButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
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
			saveInPlaceButton.setSize(ButtonSize.MINI);
			saveInPlaceButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			saveInPlaceButton.setTitle(messages.saveInPlaceElaboratedTooltip());
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
	public void addPauseControl() {
		if(pauseButton == null) {
			pauseButton = new Button();
			pauseButton.setIcon(IconType.PAUSE);
			pauseButton.setType(ButtonType.DANGER);
			pauseButton.setSize(ButtonSize.MINI);
			pauseButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			pauseButton.setTitle(messages.pauseTooltip());
			pauseButton.setEnabled(false);
			pauseButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onSuspend();
				}
			});
			controls.add(pauseButton);
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
			
			if(enable) {
				saveInPlaceButton.setTitle(messages.saveInPlaceTooltip());
			}
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

	@Override
	public String getInitializingLabel() {
		return messages.initializingLabel();
	}

	@Override
	public void enablePause(boolean enable) {
		pauseButton.setEnabled(enable);
	}

	@Override
	public void switchPauseButton(boolean paused) {
		if(paused) {
			pauseButton.setIcon(IconType.PLAY);
			pauseButton.setTitle(messages.resumeTooltip());
		} else {
			pauseButton.setIcon(IconType.PAUSE);
			pauseButton.setTitle(messages.pauseTooltip());
		}
	}

	@Override
	public void setSuspendBusyState(boolean busy) {
		if(pauseButton != null) {
			BootstrapHelpers.setButtonBusyState(pauseButton, busy);
		}
	}
}