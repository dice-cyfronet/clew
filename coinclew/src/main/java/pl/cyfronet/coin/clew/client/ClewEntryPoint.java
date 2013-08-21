package pl.cyfronet.coin.clew.client;

import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.Resource;
import org.fusesource.restygwt.client.TextCallback;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

public class ClewEntryPoint implements EntryPoint {
	@Override
	public void onModuleLoad() {
		RootPanel.get().add(new Button("Go!", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Resource resource = new Resource("http://0.0.0.0:3000/api/v1/security_policies");
				resource.get().send(new TextCallback() {
					@Override
					public void onSuccess(Method method, String response) {
						Window.alert(response);
					}
					
					@Override
					public void onFailure(Method method, Throwable exception) {
						Window.alert(exception.getMessage());
					}
				});
			}
		}));
	}
}