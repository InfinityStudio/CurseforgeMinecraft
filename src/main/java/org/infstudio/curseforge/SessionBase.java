package org.infstudio.curseforge;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Indicate a simple cache for a listStr of projects
 *
 * @param <T>
 */
public abstract class SessionBase<T> implements CurseForgeService.Session<T>
{
	public static final String ROOT = "https://minecraft.curseforge.com";

	protected List<T> cache;
	protected int readTimeout = 15000, connectionTimeout = 15000;

	protected SessionBase()
	{
		cache = new ArrayList<>();
	}

	@Override
	public List<T> getContent()
	{
		return cache;
	}

	@Override
	public String toString()
	{
		return "Session{" +
				"cache=" + cache +
				'}';
	}

	protected Document request(String url, String method, Map<String, Object> header) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		if (header != null)
			for (Map.Entry<String, Object> h : header.entrySet())
				connection.setRequestProperty(h.getKey(), h.getValue().toString());
		connection.setRequestMethod(method);
		connection.setConnectTimeout(connectionTimeout);
		connection.setReadTimeout(readTimeout);

		try
		{
			connection.connect();
			try (InputStream in = connection.getInputStream())
			{
				return Jsoup.parse(in, "utf-8", url);
			}
		}
		finally
		{
			connection.disconnect();
		}
	}
}
