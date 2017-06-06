package net.infstudio.curseforge.impl;

import net.infstudio.curseforge.CurseForgeService;
import net.infstudio.curseforge.CurseForgeProject;
import net.infstudio.curseforge.SessionBase;
import net.infstudio.curseforge.parser.CurseForgeSearchPageParser;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author ci010
 */
public class SearchSessionImpl extends SessionBase<CurseForgeProject> implements CurseForgeService.SearchSession,
																				 CurseForgeService.LinearRequester<CurseForgeProject>
{
	private CurseForgeSearchPageParser parser;
	private String keyword;

	private boolean hasNext;
	private int page;

	public SearchSessionImpl(CurseForgeSearchPageParser parser, String keyword)
	{
		this.parser = parser;
		this.keyword = keyword;
	}

	@Override
	public void refresh() throws IOException
	{
		String url = ROOT + "/search";
		Document document = request(url, "GET", Collections.singletonMap("search", keyword));
		if (document.getElementsByClass("tabbed-container").size() == 0) return;

		List<CurseForgeProject> projects = parser.parseSearchPage(document);
		this.cache.clear();
		this.cache.addAll(projects);

		this.hasNext = true;
	}

	@Override
	public boolean growContent() throws IOException
	{
		if (!hasNext) return false;
		String url = ROOT + "/search";
		Map<String, Object> args = new TreeMap<>();
		args.put("search", keyword);
		args.put("page", ++page);
		Document document = request(url, "GET", args);
		List<CurseForgeProject> projects = parser.parseSearchPage(document);
		this.cache.addAll(projects);

		return true;
	}

	@Override
	public String getKeyword()
	{
		return keyword;
	}

	@Override
	public void setKeyword(String key)
	{
		this.keyword = key;
	}

	@Override
	public List<CurseForgeProject> requestContent(int page) throws IOException
	{
		String url = ROOT + "/search";
		Map<String, Object> args = new TreeMap<>();
		args.put("search", keyword);
		args.put("page", page);
		Document document = request(url, "GET", args);
		return parser.parseSearchPage(document);
	}

	@Override
	public int getPage()
	{
		return page;
	}

	@Override
	public int getMaxPage()
	{
		return -1;
	}
}
