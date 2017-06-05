package org.infstudio.curseforge;

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
