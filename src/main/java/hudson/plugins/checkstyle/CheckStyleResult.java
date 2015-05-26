package hudson.plugins.checkstyle;

import com.google.inject.Inject;
import com.thoughtworks.xstream.XStream;

import hudson.model.AbstractBuild;
import hudson.plugins.analysis.core.BuildHistory;
import hudson.plugins.analysis.core.BuildResult;
import hudson.plugins.analysis.core.ParserResult;
import hudson.plugins.analysis.core.ResultAction;
import hudson.plugins.analysis.util.model.FileAnnotation;
import hudson.plugins.checkstyle.parser.Warning;
import jenkins.model.Jenkins;
import org.jenkinsci.plugins.codehealth.model.Issue;
import org.jenkinsci.plugins.codehealth.model.Priority;
import org.jenkinsci.plugins.codehealth.service.JPAIssueRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the results of the Checkstyle analysis. One instance of this class
 * is persisted for each build via an XML file.
 *
 * @author Ulli Hafner
 */
public class CheckStyleResult extends BuildResult {
    private static final long serialVersionUID = 2768250056765266658L;

    @Inject
    private transient JPAIssueRepository jpaIssueRepository;

    /**
     * Creates a new instance of {@link CheckStyleResult}.
     *
     * @param build
     *            the current build as owner of this action
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     * @param result
     *            the parsed result with all annotations
     * @param usePreviousBuildAsReference
     *            determines whether to use the previous build as the reference
     *            build
     * @param useStableBuildAsReference
     *            determines whether only stable builds should be used as
     *            reference builds or not
     */
    public CheckStyleResult(final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result,
            final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference) {
        this(build, defaultEncoding, result, usePreviousBuildAsReference, useStableBuildAsReference,
                CheckStyleResultAction.class);
    }

    /**
     * Creates a new instance of {@link CheckStyleResult}.
     *
     * @param build
     *            the current build as owner of this action
     * @param defaultEncoding
     *            the default encoding to be used when reading and parsing files
     * @param result
     *            the parsed result with all annotations
     * @param useStableBuildAsReference
     *            determines whether only stable builds should be used as
     *            reference builds or not
     * @param actionType
     *            the type of the result action
     */
    protected CheckStyleResult(final AbstractBuild<?, ?> build, final String defaultEncoding, final ParserResult result,
            final boolean usePreviousBuildAsReference, final boolean useStableBuildAsReference,
            final Class<? extends ResultAction<CheckStyleResult>> actionType) {
        this(build, new BuildHistory(build, actionType, usePreviousBuildAsReference, useStableBuildAsReference),
                result, defaultEncoding, true);
    }

    CheckStyleResult(final AbstractBuild<?, ?> build, final BuildHistory history,
            final ParserResult result, final String defaultEncoding, final boolean canSerialize) {
        super(build, history, result, defaultEncoding);

        if (canSerialize) {
            serializeAnnotations(result.getAnnotations());
            // map Annotations to Issues
            Jenkins.getInstance().getInjector().injectMembers(this);
            List<Issue> newIssues = new ArrayList<Issue>();
            for (FileAnnotation annotation : result.getAnnotations()){
                Issue i = new Issue();
                i.setContextHashCode(annotation.getContextHashCode());
                i.setMessage(annotation.getMessage());
                Priority prio;
                switch (annotation.getPriority()){
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
                newIssues.add(i);
            }
            jpaIssueRepository.newIssues(newIssues, (hudson.model.TopLevelItem) build.getProject());
        }
    }

    @Override
    public String getHeader() {
        return Messages.Checkstyle_ResultAction_Header();
    }

    @Override
    protected void configure(final XStream xstream) {
        xstream.alias("warning", Warning.class);
    }

    @Override
    public String getSummary() {
        return "Checkstyle: " + createDefaultSummary(CheckStyleDescriptor.RESULT_URL, getNumberOfAnnotations(), getNumberOfModules());
    }

    @Override
    protected String createDeltaMessage() {
        return createDefaultDeltaMessage(CheckStyleDescriptor.RESULT_URL, getNumberOfNewWarnings(), getNumberOfFixedWarnings());
    }

    /**
     * Returns the name of the file to store the serialized annotations.
     *
     * @return the name of the file to store the serialized annotations
     */
    @Override
    protected String getSerializationFileName() {
        return "checkstyle-warnings.xml";
    }

    @Override
    public String getDisplayName() {
        return Messages.Checkstyle_ProjectAction_Name();
    }

    @Override
    protected Class<? extends ResultAction<? extends BuildResult>> getResultActionType() {
        return CheckStyleResultAction.class;
    }
}
