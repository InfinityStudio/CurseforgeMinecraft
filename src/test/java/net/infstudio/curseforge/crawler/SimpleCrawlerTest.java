package net.infstudio.curseforge.crawler;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

/**
 * @author ci010
 */
public class SimpleCrawlerTest
{
	@Test
	public void start() throws Exception
	{
		new SimpleCrawler().start(new File("C:\\Users\\cijhn\\Workspace\\down"));
	}

}
