package hudson.plugins.checkstyle;

import hudson.plugins.analysis.util.model.FileAnnotation;
import org.jenkinsci.plugins.codehealth.model.Issue;
import org.jenkinsci.plugins.codehealth.model.Priority;
import org.jenkinsci.plugins.codehealth.util.AbstractIssueMapper;

/**
 * @author Michael Prankl
 */
public class CheckStyleIssueMapper extends AbstractIssueMapper<FileAnnotation> {
    @Override
    public Issue map(FileAnnotation fileAnnotation) {
        Issue issue = new Issue();
        issue.setContextHashCode(fileAnnotation.getContextHashCode());
        issue.setMessage(fileAnnotation.getMessage());
        issue.setOrigin(CheckStylePublisher.PLUGIN_NAME);
        issue.setPriority(Priority.valueOf(fileAnnotation.getPriority().name()));
        return issue;
    }
}
