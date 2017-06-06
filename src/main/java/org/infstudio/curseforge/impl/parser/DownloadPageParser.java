package org.infstudio.curseforge.impl.parser;

import org.infstudio.curseforge.parser.CurseForgeDownloadPageParser;
import org.infstudio.curseforge.CurseForgeProjectArtifact;
import org.infstudio.curseforge.utils.DataSizeUnit;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ci010
 */
public class DownloadPageParser implements CurseForgeDownloadPageParser
{
	@Override
	public List<CurseForgeProjectArtifact> parseArtifact(Document document)
	{
		Elements elementsByClass = document.getElementsByClass("project-file-list-item");
		return elementsByClass.stream().map(element ->
		{
			String releaseType = element.getElementsByClass("tip").get(0).attr("title");
			Element tip = element.getElementsByClass("overflow-tip").get(0);
			String mcVersion = element.getElementsByClass("version-label").get(0).text();
			String downloadURL = "https://minecraft.curseforge.com" + tip.attr("href") + "/download";
			String fileName = tip.text();
			String size = element.getElementsByClass("project-file-size").get(0).text();
			DataSizeUnit unit = DataSizeUnit.of(size);
			size = size.replace(",", "");
			long fileSize;
			if (unit == null) fileSize = 0;
			else fileSize = unit.toByte(unit.fromString(size));
			Date date = null;
			try
			{
				date = new Date(Long.parseLong(element.getElementsByAttribute("data-epoch").get(0).attr("data-epoch")));
			}
			catch (Exception ignored) {}
			return new CurseForgeProjectArtifact(downloadURL, fileName, mcVersion, releaseType, fileSize, date);
		}).collect(Collectors.toList());
	}

	@Override
	public int parseMaxPage(Document document)
	{
		int maxPage = 1;
		Elements pages = document.getElementsByClass("b-pagination-item");
		List<String> pagings = pages.stream().map(Element::text).collect(Collectors.toList());
		for (String s : pagings)
		{
			try
			{
				Integer v = Integer.valueOf(s);
				if (maxPage < v)
					maxPage = v;
			}
			catch (Exception e) {}
		}
		return maxPage;
	}
}
