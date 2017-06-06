package net.infstudio.curseforge;

import net.infstudio.curseforge.impl.CurseForgeServiceContainer;
import net.infstudio.curseforge.impl.parser.ViewPageParser;
import net.infstudio.curseforge.parser.CurseForgeDownloadPageParser;
import net.infstudio.curseforge.parser.CurseForgeViewPageParser;
import net.infstudio.curseforge.impl.parser.DownloadPageParser;
import net.infstudio.curseforge.impl.parser.SearchPageParser;
import net.infstudio.curseforge.parser.CurseForgeSearchPageParser;

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
