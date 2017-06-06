package org.infstudio.curseforge;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/**
 * @author ci010
 */
public class CurseForgeServicesTest
{
	@Test
	public void createDefault() throws Exception
	{
		CurseForgeServices.createDefault();
	}

	@Test
	public void view() throws Exception
	{
		CurseForgeService aDefault = CurseForgeServices.createDefault();
		CurseForgeService.ViewSession view = aDefault.view(CurseForgeProjectType.Mods);
		System.out.println(view.getContent().stream().map(CurseForgeProject::getName).collect(Collectors.toList()));
		view.growContent();
		System.out.println(view.getContent().stream().map(CurseForgeProject::getName).collect(Collectors.toList()));
		System.out.println(view.getFilterTypes());
		view.setFilterType(view.getFilterTypes().get(2));
		System.out.println(view.getFilterType());
		view.refresh();
		System.out.println(view.getContent().stream().map(CurseForgeProject::getName).collect(Collectors.toList()));
	}

	Pattern
			p2 = Pattern.compile("<li class=\"project-list-item\">\n*" +
			" *<div class=\"avatar\">\n*" +
			" *<a class=\"e-avatar64 \" href=\"([^\"]*)\">\n*" +
			" *<img src=\"([^\"]*)\">\n*" +
			" *</a>\n*" +
			" *</div>\n*" +
			" *<div class=\"details\">\n*" +
			" *<div class=\"info name\">\n*" +
			" *<div class=\"name-wrapper overflow-tip\">\n*" +
			" *<a href=\"([^\"]*)\">([^<][^/][^a][^>].)*</a>\n*" +
			" *</div>\n*" +
			" *<span class=\"byline\"> by <a href=\"([^\"]*)\">techbrew</a>\n*" +
			" *</span>\n*" +
			" *</div>\n*" +
			" *<div class=\"info stats\">\n*" +
			" *<p class=\"e-download-count\">18,998,788</p>\n*" +
			" *<p class=\"e-update-date" +
			"\"><abbr class=\"tip standard-date standard-datetime\" title=\"([^\"]*)\" data-epoch=\"([^\"]*)\" time-processed=\"(true|false)\">Feb 17, 2017</abbr></p>\n*" +
			" *</div>\n*" +
			" *<div class=\"categories-box\">\n*" +
			" *<div class=\"category-icon-wrapper\">\n*" +
			" *<div class=\"category-icons\">\n*" +
			" *<a href=\"([^\"]*)\" class=\"j-tooltip e-avatar32\">\n*" +
			" *<img src=\"([^\"]*)\">\n*" +
			" *</a>\n*" +
			" *</div>\n*" +
			" *</div>\n*" +
			" *</div>\n*" +
			" *<div class=\"description\">\n*" +
			" *<p>Real-time mapping in-game or your browser as you explore.</p>\n*" +
			" *</div>\n*" +
			" *</div>\n*" +
			" *</li>\n*");

	@Test
	public void testMatch() throws Exception
	{
		String request;
//		request = request("https://minecraft.curseforge.com/mc-mods");
		request = "<li class=\"project-list-item\">\n" +
				"    <div class=\"avatar\">\n" +
				"        <a class=\"e-avatar64 \" href=\"https://minecraft.curseforge.com/projects/journeymap-32274\">\n" +
				"            <img src=\"./Mods - Projects - Minecraft CurseForge_files/635421614078544069.png\">\n" +
				"    </a>\n" +
				"    </div>\n" +
				"    <div class=\"details\">\n" +
				"        <div class=\"info name\">\n" +
				"            <div class=\"name-wrapper overflow-tip\">\n" +
				"                <a href=\"https://minecraft.curseforge.com/projects/journeymap-32274\">JourneyMap</a>\n" +
				"            </div>\n" +
				"            <span class=\"byline\"> by <a href=\"https://minecraft.curseforge.com/members/techbrew\">techbrew</a></span>\n" +
				"        </div>\n" +
				"        <div class=\"info stats\">\n" +
				"            <p class=\"e-download-count\">18,998,788</p>\n" +
				"            <p class=\"e-update-date\"><abbr class=\"tip standard-date standard-datetime\" title=\"2/17/2017 10:28 PM\" data-epoch=\"1487402894\" time-processed=\"true\">Feb 17, 2017</abbr></p>\n" +
				"        </div>\n" +
				"        <div class=\"categories-box\">\n" +
				"            <div class=\"category-icon-wrapper\">\n" +
				"                <div class=\"category-icons\">\n" +
				"                    <a href=\"https://minecraft.curseforge.com/mc-mods/map-information\" class=\"j-tooltip e-avatar32\">\n" +
				"                        <img src=\"./Mods - Projects - Minecraft CurseForge_files/635351497437388438(1).png\">\n" +
				"                    </a>\n" +
				"                </div>\n" +
				"            </div>\n" +
				"        </div>\n" +
				"        <div class=\"description\">\n" +
				"            <p>Real-time mapping in-game or your browser as you explore.</p>\n" +
				"        </div>\n" +
				"    </div>\n" +
				"</li>";
		Matcher matcher = p2.matcher(request);
		while (matcher.find())
		{
			for (int i = 0; i < matcher.groupCount(); i++)
			{
				System.out.println(i);
				System.out.println(matcher.group(i));
				System.out.println();
			}
		}
//		System.out.println(request);
	}

	protected String request(String url) throws IOException
	{
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(15000);
		connection.setRequestProperty("Accept-Charset", "UTF-8");
		try
		{
			connection.connect();
			try (InputStream in = connection.getInputStream())
			{
				ReadableByteChannel readableByteChannel = Channels.newChannel(in);
				ByteBuffer allocate = ByteBuffer.allocate(640000);
				int pos;
				do { pos = allocate.position(); readableByteChannel.read(allocate); }
				while (pos != allocate.position());
				allocate.flip();
				CharBuffer decode = Charset.forName("utf-8").decode(allocate);
				return decode.toString();
			}
		}
		finally
		{
			connection.disconnect();
		}
	}

	@Test
	public void search() throws Exception
	{
		CurseForgeService aDefault = CurseForgeServices.createDefault();
		CurseForgeService.SearchSession test = aDefault.search("test");
		System.out.println(test);
	}
}
