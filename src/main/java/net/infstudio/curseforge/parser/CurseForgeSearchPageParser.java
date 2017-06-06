package net.infstudio.curseforge.parser;

import net.infstudio.curseforge.CurseForgeProject;
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
