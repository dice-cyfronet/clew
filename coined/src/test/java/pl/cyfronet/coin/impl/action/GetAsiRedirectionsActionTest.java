package pl.cyfronet.coin.impl.action;

import java.util.List;

import org.testng.annotations.Test;

public class GetAsiRedirectionsActionTest extends ActionTest {

	private String asiId = "";
	private String ctxId = "";
	private List redirections;
	
	@Test
	public void shouldGetAsiRedirections() {
		givenAsiRedirectionsInAIR();
		whenGetAsiRedirections();
		thenAsiRedirectionsReturned();
	}

	private void givenAsiRedirectionsInAIR() {
		
	}

	private void whenGetAsiRedirections() {
		// TODO Auto-generated method stub
		
	}

	private void thenAsiRedirectionsReturned() {
		// TODO Auto-generated method stub
		
	}

}
