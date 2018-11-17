package tutorial.q5;;
        import org.repodriller.domain.Commit;
        import org.repodriller.domain.Modification;
        import org.repodriller.persistence.PersistenceMechanism;
        import org.repodriller.scm.CommitVisitor;
        import org.repodriller.scm.SCMRepository;


/**
 * Created by na-salehnia on 11/6/2018.
 */
public class DiffVisitor implements CommitVisitor{

    @Override
    public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
        int adds = 0;
        int removes = 0;

        for(Modification m : commit.getModifications()) {
            String diff = m.getDiff();
            String[] lines = diff.split("\n");

            for(String line : lines) {
                if(line.trim().startsWith("+")) adds++;
                else if(line.trim().startsWith("-")) removes++;
            }

            writer.write(
                    commit.getHash(),
                    adds,
                    removes
            );

        }

    }

    @Override
    public String name() {
        return "diff";
    }

}