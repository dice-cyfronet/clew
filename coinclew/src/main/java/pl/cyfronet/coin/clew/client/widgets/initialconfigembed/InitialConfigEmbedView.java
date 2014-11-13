package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FieldSet;
import org.gwtbootstrap3.client.ui.Form;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Icon;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Legend;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.InputType;

import pl.cyfronet.coin.clew.client.widgets.BootstrapHelpers;
import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.IInitialConfigEmbedView.IInitialConfigEmbedPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class InitialConfigEmbedView extends Composite implements IInitialConfigEmbedView, ReverseViewInterface<IInitialConfigEmbedPresenter> {
	private static InitialConfigEmbedViewUiBinder uiBinder = GWT.create(InitialConfigEmbedViewUiBinder.class);
	interface InitialConfigEmbedViewUiBinder extends UiBinder<Widget, InitialConfigEmbedView> {}
	interface Styles extends CssResource {
		String header();
	}
	
	private Icon loadingProgress;
	private IInitialConfigEmbedPresenter presenter;
	
	@UiField Modal modal;
	@UiField FlowPanel parameterContainer;
	@UiField InitialConfigEmbedMessages messages;
	@UiField Styles style;
	@UiField Button start;

	public InitialConfigEmbedView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("close")
	void onCloseClicked(ClickEvent event) {
		modal.hide();
	}
	
	@UiHandler("start")
	void onStartClicked(ClickEvent event) {
		getPresenter().onStartApplications();
	}
	
	@Override
	public void setPresenter(IInitialConfigEmbedPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IInitialConfigEmbedPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showModal(boolean show) {
		if (show) {
			modal.show();
		} else {
			modal.hide();
		}
	}

	@Override
	public void showLoadingProgress(boolean show) {
		if (show) {
			if (loadingProgress == null) {
				loadingProgress = new Icon(IconType.SPINNER);
				loadingProgress.addStyleName("fa-spin");
				parameterContainer.add(loadingProgress);
			}
		} else {
			if (loadingProgress != null) {
				parameterContainer.remove(loadingProgress);
				loadingProgress = null;
			}
		}
	}

	@Override
	public Map<String, HasText> addParameters(String applianceTypeName, String configName, List<String> parameters) {
		Map<String, HasText> result = new HashMap<String, HasText>();
		
		Form panel = new Form();
		FieldSet fieldSet = new FieldSet();
		panel.add(fieldSet);
		
		Legend legend = new Legend(applianceTypeName + " (" + configName + ")");
		fieldSet.add(legend);
		
		if (parameters.size() > 0) {
			for (String parameter : parameters) {
				FormGroup group = new FormGroup();
				FormLabel controlLabel = new FormLabel();
				controlLabel.setText(parameter);
				group.add(controlLabel);
				
				Input value = null;
				
				if(parameter.toLowerCase().endsWith("password")) {
					value = new Input(InputType.PASSWORD);
				} else {
					value = new Input(InputType.TEXT);
				}
				
				value.setPlaceholder(messages.parameterValuePlaceholder());
				result.put(parameter, value);
				group.add(value);
				fieldSet.add(group);
			}
		} else {
			Label label = new Label(messages.noParametersLabel());
			panel.add(label);
		}
		
		parameterContainer.add(panel);
		
		return result;
	}

	@Override
	public void clearParameters() {
		parameterContainer.clear();
	}

	@Override
	public void setStartBusyState(boolean busy) {
		BootstrapHelpers.setButtonBusyState(start, busy);
	}
}