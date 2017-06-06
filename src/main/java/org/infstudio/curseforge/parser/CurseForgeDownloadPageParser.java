package org.infstudio.curseforge.parser;

import org.infstudio.curseforge.CurseForgeProjectArtifact;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * @author ci010
 */
public interface CurseForgeDownloadPageParser
{
	List<CurseForgeProjectArtifact> parseArtifact(Document document);

	int parseMaxPage(Document document);
}
