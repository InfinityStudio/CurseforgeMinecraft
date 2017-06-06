package net.infstudio.curseforge.crawler;

import net.infstudio.curseforge.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ci010
 */
public class SimpleCrawler
{
	public void start(File root) throws IOException
	{
		if (root.isFile()) throw new IOException();
		root.mkdirs();
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		CurseForgeService service = CurseForgeServices.createDefault();
		CurseForgeService.LinearRequester<CurseForgeProject> requester = service.view(CurseForgeProjectType.Mods);
		for (int i = 0; i < requester.getMaxPage(); i++)
			executorService.submit(new ProjectIterate(root, service, requester, i));
	}

	private class ProjectIterate implements Callable<Void>
	{
		File root;
		CurseForgeService service;
		CurseForgeService.LinearRequester<CurseForgeProject> requester;
		int i;

		ProjectIterate(File root, CurseForgeService service, CurseForgeService.LinearRequester<CurseForgeProject>
				requester, int i)
		{
			this.root = root;
			this.service = service;
			this.requester = requester;
			this.i = i;
		}

		@Override
		public Void call() throws Exception
		{
			List<CurseForgeProject> projects = requester.requestContent(i);
			for (CurseForgeProject project : projects)
			{
				CurseForgeService.LinearRequester<CurseForgeProjectArtifact> artifact = service.artifact(project);
				for (int j = 0; j < artifact.getMaxPage(); j++)
					for (CurseForgeProjectArtifact art : artifact.requestContent(j))
					{
						ReadableByteChannel rbc = Channels.newChannel(new URL(art.getDownloadURL()).openStream());
						FileOutputStream fos = new FileOutputStream(new File(root, art.getFileName()));
						fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					}
			}
			return null;
		}
	}
}
