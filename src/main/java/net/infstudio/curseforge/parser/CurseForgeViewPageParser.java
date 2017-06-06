package net.infstudio.curseforge.parser;

import net.infstudio.curseforge.CurseForgeCategory;
import net.infstudio.curseforge.CurseForgeProject;
import net.infstudio.curseforge.CurseForgeProjectType;
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
