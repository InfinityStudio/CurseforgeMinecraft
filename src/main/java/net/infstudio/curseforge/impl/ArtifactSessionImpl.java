package net.infstudio.curseforge.impl;

import net.infstudio.curseforge.CurseForgeProject;
import net.infstudio.curseforge.CurseForgeProjectArtifact;
import net.infstudio.curseforge.CurseForgeService;
import net.infstudio.curseforge.SessionBase;
import net.infstudio.curseforge.parser.CurseForgeDownloadPageParser;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * @author ci010
 */
public class ArtifactSessionImpl extends SessionBase<CurseForgeProjectArtifact> implements CurseForgeService
																								   .ArtifactSession,
																						   CurseForgeService
																								   .LinearRequester<CurseForgeProjectArtifact>
{
	private CurseForgeProject project;
	private CurseForgeDownloadPageParser parser;

	public ArtifactSessionImpl(CurseForgeDownloadPageParser parser)
	{
		this.parser = parser;
	}

	private int page = 1;
	private int maxPage = 1;

	private String directToDownload()
	{
		return project.getProjectPath() + "/files";
	}

	private void buildHeader(Map<String, Object> header)
	{
		header.put("page", page);
	}

	@Override
	public void refresh() throws IOException
	{
		if (project == null) throw new IllegalStateException();
		String files = directToDownload();
		Map<String, Object> header = new TreeMap<>();
		buildHeader(header);
		Document doc = request(ROOT + files, "GET", header);
		List<CurseForgeProjectArtifact> artifact = parser.parseArtifact(doc);

		this.cache.clear();
		this.cache.addAll(artifact);

		this.maxPage = parser.parseMaxPage(doc);
	}

	@Override
	public boolean growContent() throws IOException
	{
		if (project == null) throw new IllegalStateException();
		if (page + 1 >= maxPage) return false;
		String url = ROOT + directToDownload();
		Map<String, Object> header = new TreeMap<>();
		++this.page;
		buildHeader(header);
		Document doc = request(url, "GET", header);

		cache.addAll(parser.parseArtifact(doc));
		return true;
	}

	@Override
	public CurseForgeProject getProject()
	{
		return project;
	}

	@Override
	public void setProject(CurseForgeProject project)
	{
		Objects.requireNonNull(project);
		this.project = project;
	}

	@Override
	public List<CurseForgeProjectArtifact> requestContent(int page) throws IOException
	{
		String url = ROOT + directToDownload();
		Map<String, Object> header = new TreeMap<>();
		buildHeader(header);
		header.put("page", page);
		Document doc = request(url, "GET", header);
		return parser.parseArtifact(doc);
	}

	@Override
	public int getPage()
	{
		return page;
	}

	@Override
	public int getMaxPage()
	{
		return maxPage;
	}
}
