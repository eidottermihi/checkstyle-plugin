package hudson.plugins.checkstyle;

import hudson.plugins.analysis.util.model.FileAnnotation;
import org.jenkinsci.plugins.codehealth.provider.Priority;
import org.jenkinsci.plugins.codehealth.provider.issues.AbstractIssueMapper;
import org.jenkinsci.plugins.codehealth.provider.issues.Issue;

/**
 * @author Michael Prankl
 */
public class CheckStyleIssueMapper extends AbstractIssueMapper<FileAnnotation> {
    @Override
    public Issue map(FileAnnotation fileAnnotation) {
        return new Issue(fileAnnotation.getContextHashCode(), fileAnnotation.getMessage(),
                Priority.valueOf(fileAnnotation.getPriority().name()));
    }
}
