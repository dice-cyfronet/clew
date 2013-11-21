package pl.cyfronet.coin.clew.client.widgets.menu;

import pl.cyfronet.coin.clew.client.widgets.menu.IMenuView.IMenuPresenter;

import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.ReverseViewInterface;

public class MenuView extends Composite implements IMenuView , ReverseViewInterface<IMenuPresenter> {
	private static MenuViewUiBinder uiBinder = GWT.create(MenuViewUiBinder.class);
	interface MenuViewUiBinder extends UiBinder<Widget, MenuView> {}
	
	private IMenuPresenter presenter;
	
	@UiField NavLink appsMenuItem;
	@UiField NavLink wfsMenuItem;
	@UiField NavLink devMenuItem;
	
	public MenuView() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("appsMenuItem")
	void appsMenuItemClicked(ClickEvent event) {
		getPresenter().onApplicationsMenuItemClicked();
	}
	
	@UiHandler("wfsMenuItem")
	void wfsMenuItemClicked(ClickEvent event) {
		getPresenter().onWorkflowsMenuItemClicked();
	}
	
	@UiHandler("devMenuItem")
	void devMenuItemClicked(ClickEvent event) {
		getPresenter().onDevelopmentMenuItemClicked();
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public void setPresenter(IMenuPresenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public IMenuPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void activateWorkflowsMenuItem(boolean activate) {
		wfsMenuItem.setActive(activate);
	}

	@Override
	public void activateApplicationsMenuItem(boolean activate) {
		appsMenuItem.setActive(activate);
	}

	@Override
	public void activateDevelopmentMenuItem(boolean activate) {
		devMenuItem.setActive(activate);
	}

	@Override
	public void showDevTab(boolean show) {
		if (show) {
			devMenuItem.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		} else {
			devMenuItem.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		}
	}
}