package org.infstudio.curseforge.parser;

import org.infstudio.curseforge.CurseForgeCategory;
import org.infstudio.curseforge.CurseForgeProject;
import org.infstudio.curseforge.CurseForgeProjectType;
import org.jsoup.nodes.Document;

import java.util.List;
import java.util.Map;

/**
 * @author ci010
 */
public interface CurseForgeViewPageParser
{
	List<CurseForgeProject> parsePageItem(Document document, CurseForgeProjectType projectType,
										  Map<String, CurseForgeCategory> pathToType);

	int parseMaxPage(Document document);
}
