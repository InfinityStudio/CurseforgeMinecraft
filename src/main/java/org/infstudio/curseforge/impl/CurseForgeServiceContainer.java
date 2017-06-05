package org.infstudio.curseforge.impl;

import org.infstudio.curseforge.*;

import java.io.IOException;

/**
 * @author ci010
 */
public class CurseForgeServiceContainer implements CurseForgeService
{
	private CurseForgeViewPageParser pageParser;
	private CurseForgeDownloadPageParser downloadPageParser;

	public CurseForgeServiceContainer(CurseForgeViewPageParser pageParser, CurseForgeDownloadPageParser downloadPageParser)
	{
		this.pageParser = pageParser;
		this.downloadPageParser = downloadPageParser;
	}

	@Override
	public SearchSession search(String keyword) throws IOException
	{
		return null;
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
	public Session<CurseForgeProjectArtifact> artifact(CurseForgeProject project) throws IOException
	{
		ArtifactSessionImpl session = new ArtifactSessionImpl(downloadPageParser);
		session.setProject(project);
		session.refresh();
		return session;
	}
}
