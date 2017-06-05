package org.infstudio.curseforge;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * The service for the CurseForge project.
 * <p>Notice that these functions are all blocking.
 *
 * @author ci010
 */
public interface CurseForgeService
{
	interface Session<T>
	{
		List<T> getContent();

		void refresh() throws IOException;

		boolean growContent() throws IOException;
	}

	interface ViewSession extends Session<CurseForgeProject>
	{
		CurseForgeProjectType getProjectType();

		String getFilterType();

		void setFilterType(String filterType);

		VersionCode getVersionCode();

		void setVersionCode(VersionCode versionCode);

		CurseForgeCategory getCategory();

		void setCategory(CurseForgeCategory category);

		List<String> getFilterTypes();

		List<VersionCode> getVersionCodes();

		Map<String, CurseForgeCategory> getCategories();
	}

	interface SearchSession extends Session<CurseForgeProject>
	{

	}

	interface ArtifactSession extends Session<CurseForgeProjectArtifact>
	{
		CurseForgeProject getProject();

		void setProject(CurseForgeProject project);
	}

	SearchSession search(String keyword) throws IOException;

	ViewSession view(CurseForgeProjectType projectType) throws IOException;

	Session<CurseForgeProjectArtifact> artifact(CurseForgeProject project) throws IOException;

	class VersionCode
	{
		private String versionString;
		private String code;

		public VersionCode(String versionString, String code)
		{
			this.versionString = versionString;
			this.code = code;
		}

		public String getVersionString() {return versionString;}

		public String getCode() {return code;}

		@Override
		public String toString()
		{
			return versionString;
		}
	}
}
