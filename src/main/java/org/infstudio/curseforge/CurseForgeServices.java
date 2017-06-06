package org.infstudio.curseforge;

import org.infstudio.curseforge.impl.CurseForgeServiceContainer;
import org.infstudio.curseforge.impl.parser.DownloadPageParser;
import org.infstudio.curseforge.impl.parser.SearchPageParser;
import org.infstudio.curseforge.impl.parser.ViewPageParser;
import org.infstudio.curseforge.parser.CurseForgeDownloadPageParser;
import org.infstudio.curseforge.parser.CurseForgeSearchPageParser;
import org.infstudio.curseforge.parser.CurseForgeViewPageParser;

/**
 * @author ci010
 */
public class CurseForgeServices
{
	public static CurseForgeService create(CurseForgeViewPageParser viewPageParser,
										   CurseForgeDownloadPageParser downloadPageParser,
										   CurseForgeSearchPageParser searchPageParser)
	{
		return new CurseForgeServiceContainer(viewPageParser, downloadPageParser, searchPageParser);
	}

	public static CurseForgeService createDefault()
	{
		return new CurseForgeServiceContainer(new ViewPageParser(), new DownloadPageParser(), new SearchPageParser());
	}

	private CurseForgeServices() {}
}
