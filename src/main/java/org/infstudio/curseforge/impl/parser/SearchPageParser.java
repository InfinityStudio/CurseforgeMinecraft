package org.infstudio.curseforge.impl.parser;

import org.infstudio.curseforge.CurseForgeProject;
import org.infstudio.curseforge.CurseForgeProjectType;
import org.infstudio.curseforge.parser.CurseForgeSearchPageParser;
import org.jsoup.nodes.Document;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author ci010
 */
public class SearchPageParser implements CurseForgeSearchPageParser
{
	private Pattern pattern = Pattern.compile("(gameCategorySlug=)([^&]*)(&?)");

	@Override
	public List<CurseForgeProject> parseSearchPage(Document document)
	{
		return document.getElementsByClass("results").stream()
				.map(e ->
				{
					String path = e.child(1).child(0).child(0).attr("href");
					Matcher matcher = pattern.matcher(path);
					CurseForgeProjectType type = null;
					if (matcher.find())
						for (CurseForgeProjectType projectType : CurseForgeProjectType.values())
							if (projectType.getId().equals(matcher.group(2)))
								type = projectType;
					if (type == null) throw new IllegalStateException();
					return new CurseForgeProject(
							e.child(1).child(0).child(0).text(),
							e.child(1).child(1).text(),
							type.getPath(),
							e.child(0).child(0).child(0).attr("src"),
							Collections.emptyList(),
							e.child(2).child(0).text(),
							"-1",
							new Date(Long.parseLong(e.child(3).child(0).attr("data-epoch"))),
							type);
				})
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	@Override
	public boolean hasNext(Document document)
	{
		//TODO implement this
		return false;
	}
}
