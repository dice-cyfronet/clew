package pl.cyfronet.coin.clew.client.widgets.initialconfigembed;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.cyfronet.coin.clew.client.widgets.initialconfigembed.IInitialConfigEmbedView.IInitialConfigEmbedPresenter;

import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.Form;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
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
				loadingProgress.addStyleName("icon-spin");
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
		
		FlowPanel panel = new FlowPanel();
		HTML header = new HTML(applianceTypeName + " (" + configName + ")");
		header.addStyleName(style.header());
		panel.add(header);
		
		if (parameters.size() > 0) {
			Form form = new Form();
			
			for (String parameter : parameters) {
				ControlLabel controlLabel = new ControlLabel(parameter);
				form.add(controlLabel);
				
				TextBox value = new TextBox();
				value.setPlaceholder(messages.parameterValuePlaceholder());
				value.addStyleName("input-block-level");
				result.put(parameter, value);
				form.add(value);
			}
			
			panel.add(form);
		} else {
			Label label = new Label(messages.noParametersLabel());
			panel.add(label);
		}
		
		parameterContainer.add(panel);
		
		return result;
	}
}