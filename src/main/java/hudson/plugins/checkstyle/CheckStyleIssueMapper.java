package hudson.plugins.checkstyle;

import hudson.plugins.analysis.util.model.FileAnnotation;
import org.jenkinsci.plugins.codehealth.Issue;
import org.jenkinsci.plugins.codehealth.model.Priority;
import org.jenkinsci.plugins.codehealth.util.AbstractIssueMapper;

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
