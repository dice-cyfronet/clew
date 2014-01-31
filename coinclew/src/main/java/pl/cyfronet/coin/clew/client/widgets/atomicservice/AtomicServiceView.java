package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ButtonGroup;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.constants.LabelType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
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
	@UiField InlineHTML author;
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
	public void updateAuthor(String authorName) {
		author.setText(messages.authorLabel(authorName));
	}

	@Override
	public void addRemoveButton() {
		if (removeButton == null) {
			removeButton = new Button();
			removeButton.setLoadingText("<i class='icon-spinner icon-spin'></i>");
			removeButton.setType(ButtonType.DANGER);
			removeButton.setIcon(IconType.REMOVE);
			removeButton.setSize(ButtonSize.MINI);
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
				inactiveLabel.setType(LabelType.IMPORTANT);
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
}