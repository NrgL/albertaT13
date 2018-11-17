package tutorial.q5;

/**
 * Created by na-salehnia on 11/6/2018.
 */

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRepository;

public class Q5Study implements Study {

    public static void main(String[] args) {
        new RepoDriller().start(new Q5Study());
    }

    @Override
    public void execute() {
        CSVFile csv = new CSVFile("C:\\Users\\na-salehnia\\Desktop\\tutorial/q6.csv");
        DiffVisitor visitor = new DiffVisitor();

        try {

            new RepositoryMining()
                    .in(GitRepository.singleProject("C:\\Users\\na-salehnia\\Desktop\\tutorial/jfreechart-fse"))
                    .through(Commits.all())
                    .process(visitor, csv)
                    .mine();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}