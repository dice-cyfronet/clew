package pl.cyfronet.coin.clew.client.widgets.atomicservice;

import pl.cyfronet.coin.clew.client.widgets.atomicservice.IAtomicServiceView.IAtomicServicePresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.i18n.client.Messages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class AtomicServiceView extends Composite implements IAtomicServiceView, ReverseViewInterface<IAtomicServicePresenter> {
	private static AtomicServiceViewUiBinder uiBinder = GWT.create(AtomicServiceViewUiBinder.class);
	interface AtomicServiceViewUiBinder extends UiBinder<Widget, AtomicServiceView> {}
	
	private IAtomicServicePresenter presenter;
	
	@UiField InlineHTML name;
	@UiField InlineHTML author;
	@UiField AtomicServiceMessages messages;

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
}