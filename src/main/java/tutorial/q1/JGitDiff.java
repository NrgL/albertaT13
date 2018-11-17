package tutorial.q1;

/**
 * Created by na-salehnia on 11/10/2018.
 */

import java.io.ByteArrayOutputStream;
        import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.api.Git;
        import org.eclipse.jgit.diff.DiffEntry;
        import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

public class JGitDiff
{
    public static void main(String[] args) throws Exception
    {
        File gitWorkDir = new File("C:\\Users\\na-salehnia\\AppData\\Local\\Temp\\RxJava\\");
//        File gitWorkDir = new File("C:\\Users\\na-salehnia\\AppData\\Local\\Temp\\elasticsearch\\.git");
        Git git = Git.open(gitWorkDir);

//        String oldHash = "697fd66aae9beed107e13f49a741455f1d9d8dd9";


        //************************************************************

        /*Repository repo = new FileRepository("C:\\\\Users\\\\na-salehnia\\\\AppData\\\\Local\\\\Temp\\\\RxJava\\\\.git");
        Git git = new Git(repo);
        RevWalk walk = new RevWalk(repo);
        Iterable<RevCommit> commits = git.log().all().call();
        System.out.println("*********************** :)) "+commits.toString());

        List<Ref> branches = git.branchList().call();

        for (Ref branch : branches) {
            String branchName = branch.getName();
        for (RevCommit commit : commits) {
            boolean foundInThisBranch = false;

            RevCommit targetCommit = walk.parseCommit(repo.resolve(
                    commit.getName()));
            for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
                if (e.getKey().startsWith(Constants.R_HEADS)) {
                    if (walk.isMergedInto(targetCommit, walk.parseCommit(
                            e.getValue().getObjectId()))) {
                        String foundInBranch = e.getValue().getName();
                        if (branchName.equals(foundInBranch)) {
                            foundInThisBranch = true;
                            break;
                        }
                    }
                }
            }

            if (foundInThisBranch) {
                System.out.println(commit.getName());
                System.out.println(commit.getAuthorIdent().getName());
                System.out.println(new Date(commit.getCommitTime() * 1000L));
                System.out.println(commit.getFullMessage());
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@ "+commit.getParents().toString());
            }
        }
    }


*/


        //***********************************************************
        ObjectId headId = git.getRepository().resolve("HEAD^{tree}");
//        ObjectId oldId = git.getRepository().resolve(oldHash + "^{tree}");
        ObjectId oldId = git.getRepository().resolve( "HEAD~1^{tree}" );

        ObjectReader reader = git.getRepository().newObjectReader();

        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        oldTreeIter.reset(reader, oldId);
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        newTreeIter.reset(reader, headId);

        List<DiffEntry> diffs= git.diff()
                .setNewTree(newTreeIter)
                .setOldTree(oldTreeIter)
                .call();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter df = new DiffFormatter(out);
        df.setRepository(git.getRepository());

        List<DiffEntry> modifyList= new ArrayList();

        for(DiffEntry diff : diffs)
        {
            System.out.println("-------------------START------------------");
            System.out.println("@@@@@@@@@@@@@@@ "+diff.getChangeType().toString());
            System.out.println("************* "+diff.toString());
//            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~");


            if(diff.getChangeType()== DiffEntry.ChangeType.MODIFY){
                modifyList.add(diff);
            }
//            System.out.println("^^^^^^^^^^^^^ "+diff.);

            df.format(diff);
            diff.getOldId();
            String diffText = out.toString("UTF-8");
            System.out.println("~~~~~~~~~~~~~~st~~~~~~~~~ \n"+diffText+"\n ~~~~~~~~~~~~~~~fi~~~~~~~~~~~~~");
            out.reset();
            System.out.println("-------------------FINISH------------------");
        }

        System.out.println("&&&&&&&&&&&& Final: "+modifyList.size());
        for (int i=0; i<modifyList.size();i++){
            System.out.println("!!!!!!!!!!!!!! "+modifyList.get(i).getOldMode().toString());
            System.out.println("%%%%%%%%%%%%% "+modifyList.get(i).toString());
        }
    }
}