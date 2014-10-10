package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class AtomicServiceView extends Composite implements IAtomicServiceView, ReverseViewInterface<IAtomicServicePresenter> {
	private static AtomicServiceViewUiBinder uiBinder = GWT.create(AtomicServiceViewUiBinder.class);
	interface AtomicServiceViewUiBinder extends UiBinder<Widget, AtomicServiceView> {}
	
	private IAtomicServicePresenter presenter;
	private Button removeButton;
	private Label inactiveLabel;
	
	@UiField InlineHTML name;
	@UiField AtomicServiceMessages messages;
	@UiField ButtonGroup buttons;
	@UiField InlineHTML description;
	@UiField FlowPanel inactiveContainer;
	@UiField Button startInstance;

	public AtomicServiceView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("editInitialConfigs")
	void editInitialConfigsClicked(ClickEvent event) {
		getPresenter().onEditInitialConfigs();
	}
	
	@UiHandler("editProperties")
	void editPropertiesClicked(ClickEvent event) {
		getPresenter().onEditProperties();
	}
	
	@UiHandler("editExternalInterfaces")
	void editExternalInterfaces(ClickEvent event) {
		getPresenter().onEditExternalInterfaces();
	}
	
	@UiHandler("startInstance")
	void startInstance(ClickEvent event) {
		getPresenter().onStartInstance();
	}

	@Override
	public void setPresenter(IAtomicServicePresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IAtomicServicePresenter getPresenter() {
		return presenter;
	}

	@Override
	public HasText getName() {
		return name;
	}

	@Override
	public void addRemoveButton() {
		if (removeButton == null) {
			removeButton = new Button();
			removeButton.setDataLoadingText("<i class='fa fa-spinner fa-spin'></i>");
			removeButton.setType(ButtonType.DANGER);
			removeButton.setIcon(IconType.TIMES);
			removeButton.setSize(ButtonSize.EXTRA_SMALL);
			removeButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					getPresenter().onRemove();
				}
			});
			buttons.add(removeButton);
		}
	}

	@Override
	public void setRemoveBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(removeButton, busy);
	}

	@Override
	public boolean confirmRemoval() {
		return Window.confirm(messages.confirmRemoval());
	}

	@Override
	public void showInactiveLabel(boolean active) {
		if (active) {
			if (inactiveLabel == null) {
				inactiveLabel = new Label(messages.inactiveLabel());
				inactiveLabel.setType(LabelType.DANGER);
				inactiveContainer.add(inactiveLabel);
			}
		} else {
			if (inactiveLabel != null) {
				inactiveContainer.clear();
				inactiveContainer.add(new InlineHTML("&nbsp;"));
				inactiveLabel = null;
			}
		}
	}

	@Override
	public HasHTML getDescription() {
		return description;
	}

	@Override
	public void setStartInstanceBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(startInstance, busy);
	}

	@Override
	public void showNoInitialConfigurationsMessage() {
		Window.alert(messages.noInitialConfigsMessage());
	}

	@Override
	public void enableStartButton(boolean enable) {
		startInstance.setEnabled(enable);
	}
}
