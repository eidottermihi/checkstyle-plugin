package hudson.plugins.checkstyle;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.plugins.analysis.util.model.FileAnnotation;
import org.jenkinsci.plugins.codehealth.IssueProvider;
import org.jenkinsci.plugins.codehealth.model.Issue;
import org.jenkinsci.plugins.codehealth.util.AbstractIssueMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Prankl
 */
@Extension
public class CheckStyleIssueProvider extends IssueProvider {

    private AbstractIssueMapper cim = new CheckStyleIssueMapper();

    @Override
    public Collection<Issue> getExistingIssues(AbstractBuild<?, ?> build) {
        CheckStyleResult checkStyleResult = getResult(build);
        if (checkStyleResult != null) {
            return map(checkStyleResult.getAnnotations());
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<Issue> getFixedIssues(AbstractBuild<?, ?> build) {
        CheckStyleResult checkStyleResult = getResult(build);
        if (checkStyleResult != null) {
            return map(checkStyleResult.getFixedWarnings());
        }
        return Collections.emptyList();
    }

    private List<Issue> map(Collection<FileAnnotation> annotations) {
        final List<Issue> issues = new ArrayList<Issue>(annotations.size());
        for (FileAnnotation annotation : annotations) {
            issues.add(cim.map(annotation));
        }
        return issues;
    }

    private CheckStyleResult getResult(AbstractBuild<?, ?> build) {
        for (Action action : build.getPersistentActions()) {
            if (action instanceof CheckStyleResultAction) {
                return ((CheckStyleResultAction) action).getResult();
            }
        }
        return null;
    }
}
