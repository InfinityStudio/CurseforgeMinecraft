package net.infstudio.curseforge.parser;

import net.infstudio.curseforge.CurseForgeProjectArtifact;
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
