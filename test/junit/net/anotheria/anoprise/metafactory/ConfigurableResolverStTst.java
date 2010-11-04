package net.anotheria.anoprise.metafactory;

import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.*;

public class ConfigurableResolverStTst {
	
	private static ConfigurableResolver resolver;
	
	@BeforeClass public static void setUp(){
		resolver = ConfigurableResolver.create();
	}
	
	@Test public void resolveAliasTest(){
		assertEquals("foo.bar.XxxService", resolver.resolveAlias("XxxService"));
		assertEquals("XxxService", resolver.resolveAlias("DomainXxxService"));
		assertEquals("XxxService", resolver.resolveAlias("CmsXxxService"));
		assertNull(resolver.resolveAlias("UknownXxxService"));
	}
}