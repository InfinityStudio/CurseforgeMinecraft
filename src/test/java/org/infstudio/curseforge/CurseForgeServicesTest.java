package org.infstudio.curseforge;

import org.junit.Test;

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

}
