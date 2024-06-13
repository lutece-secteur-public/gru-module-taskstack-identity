package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.service.AuthorConverter;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.service.TaskConverter;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.web.RequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.AbstractIdentityStoreRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.TaskRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.TaskCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.TaskCreateResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;

public class IdentityStoreCreateTaskRequest extends AbstractIdentityStoreRequest {

    private final TaskCreateRequest taskCreateRequest;

    public IdentityStoreCreateTaskRequest(final TaskCreateRequest taskCreateRequest, final String strClientCode, final String authorName,
                                          final String authorType) throws IdentityStoreException {
        super(strClientCode, authorName, authorType);
        this.taskCreateRequest = taskCreateRequest;
    }

    @Override
    protected void validateSpecificRequest() throws IdentityStoreException {
        TaskRequestValidator.instance().validateTaskCreateRequest(taskCreateRequest);
        RequestValidator.instance().validateTargetIdentity(taskCreateRequest.getTask().getResourceId(), taskCreateRequest.getTask().getTaskType());
    }

    @Override
    protected Object doSpecificRequest() throws IdentityStoreException {
        try {
            final String taskCode = TaskService.instance()
                                               .createTask(TaskConverter.instance().toCore(taskCreateRequest.getTask()),
                                                           AuthorConverter.instance().toCore(_author),
                                                           _strClientCode);
            final TaskCreateResponse response = new TaskCreateResponse();
            response.setTaskCode(taskCode);
            response.setStatus(ResponseStatusFactory.success().setMessageKey(Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION));
            return response;
        } catch (final TaskStackException e) {
            throw new IdentityStoreException("An error occured", e);
        }
    }
}
