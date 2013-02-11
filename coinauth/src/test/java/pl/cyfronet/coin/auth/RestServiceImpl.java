package pl.cyfronet.coin.auth;

import pl.cyfronet.coin.auth.annotation.Public;
import pl.cyfronet.coin.auth.annotation.Role;


public class RestServiceImpl implements RestService {

	@Public
	@Override
	public void publicMethod() {

	}

	@Public
	@Role(values = "myRole")
	@Override
	public void publicFirst() {

	}

	@Override
	public void withoutAnnotations() {

	}

	@Role(values = "developer")
	@Override
	public void withDeveloperRole() {
		
	}
}
