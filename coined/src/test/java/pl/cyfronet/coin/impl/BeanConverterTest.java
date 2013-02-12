package pl.cyfronet.coin.impl;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import pl.cyfronet.coin.api.beans.AtomicService;
import pl.cyfronet.coin.impl.air.client.ApplianceType;

public class BeanConverterTest {

	private ApplianceType applianceType;
	private AtomicService atomicService;

	@Test
	public void shouldConvertATIntoAS() throws Exception {
		givenAT();
		whenConvertToAS();
		thenAllFieldsFromATConvertedIntoAS();
	}

	private void givenAT() {
		applianceType = new ApplianceType();
		applianceType.setId("id");
		applianceType.setAuthor("author");
		applianceType.setDescription("description");
		applianceType.setDevelopment(true);
		applianceType.setHttp(true);
		applianceType.setIn_proxy(true);
		applianceType.setName("name");
		applianceType.setPublished(true);
		applianceType.setScalable(true);
		applianceType.setShared(true);
		applianceType.setVnc(true);
	}

	private void whenConvertToAS() {
		atomicService = BeanConverter.getAtomicService(applianceType);
	}

	private void thenAllFieldsFromATConvertedIntoAS() {
		assertEquals(atomicService.getAtomicServiceId(), applianceType.getId());
		assertEquals(atomicService.getDescription(),
				applianceType.getDescription());
		assertEquals(atomicService.getName(), applianceType.getName());
		assertEquals(atomicService.isDevelopment(),
				applianceType.isDevelopment());
		assertEquals(atomicService.isHttp(), applianceType.isHttp()
				&& applianceType.isIn_proxy());
		assertEquals(atomicService.isInProxy(), applianceType.isIn_proxy());
		assertEquals(atomicService.isPublished(), applianceType.isPublished());
		assertEquals(atomicService.isScalable(), applianceType.isScalable());
		assertEquals(atomicService.isShared(), applianceType.isShared());
		assertEquals(atomicService.isVnc(), applianceType.isVnc());
	}
}
