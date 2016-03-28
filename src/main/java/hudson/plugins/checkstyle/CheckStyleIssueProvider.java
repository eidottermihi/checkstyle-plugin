package hudson.plugins.checkstyle;

import hudson.Extension;
import hudson.model.AbstractBuild;
import hudson.model.Action;
import hudson.plugins.analysis.util.model.FileAnnotation;
import org.jenkinsci.plugins.codehealth.provider.issues.AbstractIssueMapper;
import org.jenkinsci.plugins.codehealth.provider.issues.Issue;
import org.jenkinsci.plugins.codehealth.provider.issues.IssueProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link IssueProvider} for Checkstyle Warnings.
 *
 * @author Michael Prankl
 */
@Extension
public class CheckStyleIssueProvider extends IssueProvider {

    private AbstractIssueMapper<FileAnnotation> issueMapper = new CheckStyleIssueMapper();

    @Override
    public Collection<Issue> getIssues(final AbstractBuild<?, ?> build) {
        CheckStyleResult checkStyleResult = getResult(build);
        if (checkStyleResult != null) {
            return map(checkStyleResult.getAnnotations());
        }
        return Collections.emptyList();
    }

    @Override
    public String getOrigin() {
        return CheckStylePublisher.PLUGIN_NAME;
    }

    @Nonnull
    @Override
    public String getOriginPluginName() {
        return "Checkstyle Plug-in";
    }

    @Nullable
    @Override
    public String getProjectResultUrlName() {
        return CheckStyleDescriptor.PLUGIN_ID;
    }

    @Nullable
    @Override
    public String getBuildResultUrlName() {
        return CheckStyleDescriptor.RESULT_URL;
    }

    private List<Issue> map(final Collection<FileAnnotation> annotations) {
        final List<Issue> issues = new ArrayList<Issue>(annotations.size());
        for (FileAnnotation annotation : annotations) {
            issues.add(issueMapper.map(annotation));
        }
        return issues;
    }

    private CheckStyleResult getResult(final AbstractBuild<?, ?> build) {
        for (Action action : build.getPersistentActions()) {
            if (action instanceof CheckStyleResultAction) {
                return ((CheckStyleResultAction) action).getResult();
            }
        }
        return null;
    }
}
