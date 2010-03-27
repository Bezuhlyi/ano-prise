package net.anotheria.anoprise;

import net.anotheria.anoprise.cache.CacheTestSuite;
import net.anotheria.anoprise.dualcrud.DualCrudTestSuite;
import net.anotheria.anoprise.eventservice.EventServiceTestSuite;
import net.anotheria.anoprise.fs.FSServiceTestSuite;
import net.anotheria.anoprise.metafactory.MetaFactoryTestSuite;
import net.anotheria.anoprise.mock.MockTestSuite;
import net.anotheria.anoprise.sessiondistributor.SessionDistributorTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(value=Suite.class)
@SuiteClasses(value={CacheTestSuite.class,DualCrudTestSuite.class,EventServiceTestSuite.class,FSServiceTestSuite.class, MetaFactoryTestSuite.class,
		MockTestSuite.class, SessionDistributorTestSuite.class} )
public class AllTests {

}