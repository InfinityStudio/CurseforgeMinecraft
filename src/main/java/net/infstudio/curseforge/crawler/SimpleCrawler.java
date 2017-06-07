package net.infstudio.curseforge.crawler;

import net.infstudio.curseforge.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author ci010
 */
public class SimpleCrawler
{
	public void start(File root, int thread) throws IOException
	{
		if (root.isFile()) throw new IOException();
		root.mkdirs();
		ExecutorService executorService = Executors.newFixedThreadPool(thread);
		CurseForgeService service = CurseForgeServices.createDefault();
		CurseForgeService.LinearRequester<CurseForgeProject> requester = service.view(CurseForgeProjectType.Mods);
		Collection<Callable<Void>> tasks = new ArrayList<>();
		for (int i = 0; i < requester.getMaxPage(); i++)
			tasks.add(new ProjectIterate(root, service, requester, i));
		try
		{
			List<Future<Void>> futures = executorService.invokeAll(tasks);
			for (Future<Void> future : futures) {future.get();}
		}
		catch (InterruptedException | ExecutionException e)
		{
			e.printStackTrace();
		}
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
				File parent = new File(this.root, project.getName());
				if (!parent.exists()) parent.mkdirs();
				for (int j = 0; j < artifact.getMaxPage(); j++)
					for (CurseForgeProjectArtifact art : artifact.requestContent(j))
					{
						ReadableByteChannel rbc = Channels.newChannel(new URL(art.getDownloadURL()).openStream());
						String fileName = art.getFileName();
						if (!fileName.endsWith(".jar") || !fileName.endsWith(".litemod"))
							fileName = fileName.concat(".jar");
						FileOutputStream fos = new FileOutputStream(new File(parent, fileName));
						fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
					}
			}
			return null;
		}
	}
}
