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
        Issue i = new Issue();
        i.setContextHashCode(fileAnnotation.getContextHashCode());
        i.setMessage(fileAnnotation.getMessage());
        i.setOrigin(CheckStylePublisher.PLUGIN_NAME);
        Priority prio;
        switch (fileAnnotation.getPriority()) {
            case HIGH:
                prio = Priority.HIGH;
                break;
            case NORMAL:
                prio = Priority.NORMAL;
                break;
            case LOW:
                prio = Priority.LOW;
                break;
            default:
                prio = Priority.NORMAL;
        }
        i.setPriority(prio);
        return i;
    }
}
