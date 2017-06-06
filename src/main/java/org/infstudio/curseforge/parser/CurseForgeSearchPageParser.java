package org.infstudio.curseforge.parser;

import org.infstudio.curseforge.CurseForgeProject;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author ci010
 */
public interface CurseForgeSearchPageParser
{
	List<CurseForgeProject> parseSearchPage(Document document);

	boolean hasNext(Document document);
}
