package hudson.plugins.checkstyle;

import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.analysis.util.model.Priority;
import hudson.plugins.checkstyle.parser.Warning;
import org.jenkinsci.plugins.codehealth.provider.issues.AbstractIssueMapper;
import org.jenkinsci.plugins.codehealth.provider.issues.Issue;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Michael Prankl
 */
public class CheckStyleIssueMapperTest {

    private AbstractIssueMapper<FileAnnotation> checkStyleIssueMapper = new CheckStyleIssueMapper();

    @Test
    public void priority_normal() {
        Warning warning = buildWarning(-1234, Priority.NORMAL, "some message");
        checkMapping(warning, checkStyleIssueMapper.map(warning));
    }

    @Test
    public void priority_low() {
        Warning warning = buildWarning(-1234, Priority.NORMAL, "some message");
        checkMapping(warning, checkStyleIssueMapper.map(warning));
    }

    @Test
    public void priority_high() {
        Warning warning = buildWarning(-1234, Priority.NORMAL, "some message");
        checkMapping(warning, checkStyleIssueMapper.map(warning));
    }

    private void checkMapping(final FileAnnotation warning, final Issue mappedIssue) {
        assertNotNull(mappedIssue);
        assertEquals(warning.getMessage(), mappedIssue.getMessage());
        assertEquals(warning.getContextHashCode(), mappedIssue.getContextHashCode());
        assertEquals(org.jenkinsci.plugins.codehealth.provider.Priority.NORMAL, mappedIssue.getPriority());
    }

    private Warning buildWarning(final long contextHash, final Priority priority, final String message) {
        Warning warning = new Warning(priority, message, "cat1", "type1", 10);
        warning.setContextHashCode(contextHash);
        return warning;
    }
}
