package org.infstudio.curseforge.impl;

import org.infstudio.curseforge.*;
import org.infstudio.curseforge.parser.CurseForgeDownloadPageParser;
import org.infstudio.curseforge.parser.CurseForgeSearchPageParser;
import org.infstudio.curseforge.parser.CurseForgeViewPageParser;

import java.io.IOException;

/**
 * @author ci010
 */
public class CurseForgeServiceContainer implements CurseForgeService
{
	private CurseForgeViewPageParser pageParser;
	private CurseForgeDownloadPageParser downloadPageParser;
	private CurseForgeSearchPageParser searchPageParser;

	public CurseForgeServiceContainer(CurseForgeViewPageParser pageParser, CurseForgeDownloadPageParser downloadPageParser,
									  CurseForgeSearchPageParser searchPageParser)
	{
		this.pageParser = pageParser;
		this.downloadPageParser = downloadPageParser;
		this.searchPageParser = searchPageParser;
	}

	@Override
	public SearchSession search(String keyword) throws IOException
	{
		if (keyword == null) throw new IllegalArgumentException("Keyword cannot be null.");
		SearchSessionImpl session = new SearchSessionImpl(searchPageParser, keyword);
		session.refresh();
		return session;
	}

	@Override
	public ViewSession view(CurseForgeProjectType projectType) throws IOException
	{
		if (projectType == null) projectType = CurseForgeProjectType.Mods;
		ViewSessionImpl session = new ViewSessionImpl(pageParser, projectType);
		session.refresh();
		return session;
	}

	@Override
	public ArtifactSession artifact(CurseForgeProject project) throws IOException
	{
		ArtifactSessionImpl session = new ArtifactSessionImpl(downloadPageParser);
		session.setProject(project);
		session.refresh();
		return session;
	}
}
