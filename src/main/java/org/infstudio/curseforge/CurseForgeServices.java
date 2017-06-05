package org.infstudio.curseforge;

import org.infstudio.curseforge.impl.CurseForgeServiceContainer;
import org.infstudio.curseforge.impl.parser.DownloadPageParser;
import org.infstudio.curseforge.impl.parser.ViewPageParser;

/**
 * @author ci010
 */
public class CurseForgeServices
{
	public static CurseForgeService create(CurseForgeViewPageParser viewPageParser,
										   CurseForgeDownloadPageParser downloadPageParser)
	{
		return new CurseForgeServiceContainer(viewPageParser, downloadPageParser);
	}

	public static CurseForgeService createDefault()
	{
		return new CurseForgeServiceContainer(new ViewPageParser(), new DownloadPageParser());
	}

	private CurseForgeServices() {}
}
