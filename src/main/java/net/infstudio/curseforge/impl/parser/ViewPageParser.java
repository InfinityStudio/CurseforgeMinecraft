package net.infstudio.curseforge.impl.parser;

import net.infstudio.curseforge.CurseForgeCategory;
import net.infstudio.curseforge.CurseForgeProject;
import net.infstudio.curseforge.CurseForgeProjectType;
import net.infstudio.curseforge.parser.CurseForgeViewPageParser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ci010
 */
public class ViewPageParser implements CurseForgeViewPageParser
{
	@Override
	public List<CurseForgeProject> parsePageItem(Document document, CurseForgeProjectType projectType, Map<String,
			CurseForgeCategory> pathToCategory)
	{
		Elements projectItems = document.getElementsByClass("project-list-item");
		return projectItems.stream().map(item ->
		{
			Element detail = item.child(1);
			Element infoName = detail.child(0);
			Element infoStat = detail.child(1);
			Element nameElement = infoName.child(0).child(0);
			return new CurseForgeProject(
					nameElement.text(),
					detail.child(3).child(0).text(),
					nameElement.attr("href"),
					item.getElementsByTag("img").get(0).attr("src"),
					detail.child(2).child(0).children().stream().map(e -> e.child(0).attr("href")).map(pathToCategory::get).collect(Collectors.toList()),
					infoName.child(1).child(0).text(),
					infoStat.child(0).text(),
					new Date(Long.parseLong(infoStat.child(1).child(0).attr("data-epoch"))),
					projectType);
		}).collect(Collectors.toList());
	}

	private Pattern matchPage = Pattern.compile("(page=)(\\d+)");

	@Override
	public int parseMaxPage(Document document)
	{
		Element pages = document.getElementsByClass("paging-list").get(0);
		String val = pages.child(pages.children().size() - 1).child(0).attr("href");
		Matcher matcher = matchPage.matcher(val);
		if (matcher.find())
			return Integer.valueOf(matcher.group(2));
		else throw new IllegalArgumentException("Cannot found the page.");
	}
}
