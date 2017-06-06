package net.infstudio.curseforge.impl;

import net.infstudio.curseforge.*;
import net.infstudio.curseforge.parser.CurseForgeViewPageParser;
import org.infstudio.curseforge.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ci010
 */
public class ViewSessionImpl extends SessionBase<CurseForgeProject> implements CurseForgeService.ViewSession
{
	private CurseForgeProjectType projectType;
	private String filter;
	private CurseForgeService.VersionCode versionCode;
	private CurseForgeCategory category;

	private List<String> filters;
	private List<CurseForgeService.VersionCode> versionCodes;
	private Map<String, CurseForgeCategory> categoryMap;

	private int page = 1;
	private int maxPage = 1;

	private CurseForgeViewPageParser parser;

	public ViewSessionImpl(CurseForgeViewPageParser parser,
						   CurseForgeProjectType projectType)
	{
		this.parser = parser;
		this.projectType = projectType;
		this.filters = new ArrayList<>();
		this.versionCodes = new ArrayList<>();
		this.categoryMap = new TreeMap<>();
	}

	protected void checkCache(Document document)
	{
		if (!filters.isEmpty()) return;

		Element filter = document.getElementById("filter-sort");

		List<String> filterTypesCache = filter.children().stream().map(e -> e.attr("value")).collect(Collectors
				.toList());
		filters.clear();
		filters.addAll(filterTypesCache);


		List<CurseForgeService.VersionCode> gameVersionConstrains = document.getElementById("filter-game-version")
				.children().stream().map
						(element -> new CurseForgeService.VersionCode(element.text(), element.val())).collect(Collectors.toList());
		versionCodes.clear();
		versionCodes.addAll(gameVersionConstrains);


		List<CurseForgeCategory> categoriesCacheList =
				document.getElementsByClass("level-categories-nav").stream()
						.map(e -> e.child(0))
						.map(e ->
						{
							try
							{
								return new CurseForgeCategory(e.attr("href"), e.child(1).text(),
										e.child(0).attr("src"));
							}
							catch (Exception ex) {return null;}
						})
						.filter(Objects::nonNull).collect(Collectors.toList());
		categoryMap = categoriesCacheList.stream().collect(Collectors.toMap
				(CurseForgeCategory::getPath, Function.identity()));

	}

	protected String buildURL()
	{
		return ROOT + (category != null ? category.getPath() : projectType.getPath());
	}

	protected void buildArgs(Map<String, Object> header)
	{
		if (versionCode != null)
			header.put("filter-game-version", versionCode.getCode());
		if (filter != null)
			header.put("filter-sort", filter);
		header.put("page", page);
	}

	@Override
	public void refresh() throws IOException
	{
		String url = buildURL();
		this.page = 1;
		Map<String, Object> args = new TreeMap<>();
		buildArgs(args);
		Document document = request(url, "GET", args);
		if (document == null) throw new IllegalStateException("Error");
		checkCache(document);
		List<CurseForgeProject> proj = parser.parsePageItem(document, projectType, this.categoryMap);
		this.cache.clear();
		this.cache.addAll(proj);
		this.maxPage = this.parser.parseMaxPage(document);
	}

	@Override
	public boolean growContent() throws IOException
	{
		if ((page + 1) > this.maxPage) return false;
		++page;
		String url = buildURL();
		Map<String, Object> args = new TreeMap<>();
		buildArgs(args);
		Document document = request(url, "GET", args);
		List<CurseForgeProject> proj = parser.parsePageItem(document, projectType, this.categoryMap);
		cache.addAll(proj);
		return true;
	}

	@Override
	public CurseForgeProjectType getProjectType()
	{
		return projectType;
	}

	@Override
	public String getFilterType()
	{
		return filter;
	}

	@Override
	public void setFilterType(String filterType)
	{
		this.filter = filterType;
	}

	@Override
	public CurseForgeService.VersionCode getVersionCode()
	{
		return versionCode;
	}

	@Override
	public void setVersionCode(CurseForgeService.VersionCode versionCode)
	{
		this.versionCode = versionCode;
	}

	@Override
	public CurseForgeCategory getCategory()
	{
		return category;
	}

	@Override
	public void setCategory(CurseForgeCategory category)
	{
		this.category = category;
	}

	@Override
	public List<String> getFilterTypes()
	{
		if (filters == null) return Collections.emptyList();
		return filters;
	}

	@Override
	public List<CurseForgeService.VersionCode> getVersionCodes()
	{
		if (versionCodes == null) return Collections.emptyList();
		return versionCodes;
	}

	@Override
	public Map<String, CurseForgeCategory> getCategories()
	{
		if (categoryMap == null) Collections.emptyMap();
		return categoryMap;
	}
}
