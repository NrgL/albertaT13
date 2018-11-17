package tutorial.q1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.repodriller.scm.GitRemoteRepository;
import org.repodriller.scm.SCMRepository;

public class GitRemoteRepositoryTest {

    private static GitRemoteRepository git1;
    private static GitRemoteRepository git2;
    private static String url;
    private static String REMOTE_GIT_TEMP_DIR = "remoteGitTempDir";

    @BeforeClass
    public static void readPath() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
//        url = "https://github.com/mauricioaniche/repodriller";
//        url = "https://github.com/iluwatar/java-design-patterns";
//          url = "https://github.com/ReactiveX/RxJava";
        url="https://github.com/elastic/elasticsearch";

		/* git1: Clone to a unique temp dir */
        git1 = new GitRemoteRepository(url);

		/* git2: Clone to relative dir REMOTE_GIT_TEMP_DIR somewhere in the RepoDriller tree.
		 *       Make sure it doesn't exist when we try to create it. */
        FileUtils.deleteDirectory(new File(REMOTE_GIT_TEMP_DIR));
        git2 = GitRemoteRepository.hostedOn(url).inTempDir(REMOTE_GIT_TEMP_DIR).asBareRepos().build();
    }

    @Test
    public void shouldGetInfoFromARepo() {
        SCMRepository repo = git1.info();
        Assert.assertEquals("54d54097ebfba82ac3b28adb21826f10fec01965", repo.getFirstCommit());
    }

    @Test
    public void shouldGetSameOriginURL() {
        SCMRepository repo = git1.info();
        System.out.println("@@@@@@@@ "+repo.toString());
        String origin = repo.getOrigin();
        Assert.assertEquals(url, origin);
    }

    @Test
    public void shouldInitWithGivenTempDir() {
        Path expectedStart = Paths.get(REMOTE_GIT_TEMP_DIR).toAbsolutePath();
        Path absPath = Paths.get(git2.info().getPath()).toAbsolutePath();

        Assert.assertTrue("Directory " + REMOTE_GIT_TEMP_DIR + " not honored. Path is " + absPath,
                absPath.startsWith(expectedStart));

        File bareRepositoryRefDir = new File(git2.info().getPath() + File.separator + "refs");
        Assert.assertTrue("A bare repository should have a refs directory",
                bareRepositoryRefDir.exists());
    }

    @AfterClass
    public static void deleteTempResource() throws IOException {
        Collection<GitRemoteRepository> repos = new ArrayList<GitRemoteRepository>();
        if (git1 != null)
            repos.add(git1);
        if (git2 != null)
            repos.add(git2);

        for (GitRemoteRepository repo : repos) {
            String repoPath = repo.info().getPath();
//            System.out.println("@@@ "+repoPath);
//            repo.deleteTempGitPath();
			/* close() should delete its path. */
            File dir = new File(repoPath);
            try {
                Assert.assertFalse("Remote repo's directory should be deleted: " + repoPath,
                        dir.exists());
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}