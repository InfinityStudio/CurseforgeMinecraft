package net.infstudio.curseforge;

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
	/**
	 * The session of the curse forge service.
	 *
	 * @param <T> The data it contained.
	 */
	interface Session<T>
	{
		List<T> getContent();

		/**
		 * Refresh the session with current setting. The content will be all clear out and replaced by the new things.
		 */
		void refresh() throws IOException;

		/**
		 * Grow the content, if there are more.
		 *
		 * @return If this operation success. True for there are more content and this grow. False for there is no more.
		 */
		boolean growContent() throws IOException;
	}

	interface LinearRequester<T>
	{
		List<T> requestContent(int page) throws IOException;

		int getPage();

		int getMaxPage();
	}

	interface ViewSession extends Session<CurseForgeProject>
	{
		/**
		 * @return The project type of this session.
		 */
		CurseForgeProjectType getProjectType();

		/**
		 * @return Current filter. Might be null.
		 */
		String getFilterType();

		/**
		 * @param filterType The filter for the view. Get from {@link #getFilterTypes()}.
		 */
		void setFilterType(String filterType);

		/**
		 * @return Current version code. Might be null.
		 */
		VersionCode getVersionCode();

		/**
		 * @param versionCode The version constrain for this view. Get from {@link #getVersionCodes()}.
		 */
		void setVersionCode(VersionCode versionCode);

		CurseForgeCategory getCategory();

		void setCategory(CurseForgeCategory category);

		List<String> getFilterTypes();

		List<VersionCode> getVersionCodes();

		Map<String, CurseForgeCategory> getCategories();
	}

	interface SearchSession extends Session<CurseForgeProject>
	{
		String getKeyword();

		void setKeyword(String key);
	}

	interface ArtifactSession extends Session<CurseForgeProjectArtifact>
	{
		CurseForgeProject getProject();

		void setProject(CurseForgeProject project);
	}

	<R extends SearchSession & LinearRequester<CurseForgeProject>> R search(String keyword) throws IOException;

	<R extends ViewSession & LinearRequester<CurseForgeProject>> R view(CurseForgeProjectType projectType) throws IOException;

	<R extends ArtifactSession & LinearRequester<CurseForgeProjectArtifact>> R artifact(CurseForgeProject project) throws IOException;

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
