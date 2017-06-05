package org.infstudio.curseforge;

import org.junit.Test;

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
		System.out.println(view);
	}

}
