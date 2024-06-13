package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.AbstractIdentityStoreRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.TaskRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.TaskGetStatusResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;

public class IdentityStoreGetTaskStatusRequest extends AbstractIdentityStoreRequest {

    private final String taskCode;

    public IdentityStoreGetTaskStatusRequest(final String taskCode, final String strClientCode, final String authorName,
                                             final String authorType ) throws IdentityStoreException {
        super(strClientCode, authorName, authorType);
        this.taskCode = taskCode;
    }

    @Override
    protected void validateSpecificRequest() throws IdentityStoreException {
        TaskRequestValidator.instance().validateTaskCode(taskCode);
    }

    @Override
    protected Object doSpecificRequest() throws IdentityStoreException {
        try {
            final TaskDto task = TaskService.instance().getTask(taskCode);
            final TaskGetStatusResponse response = new TaskGetStatusResponse();
            response.setTaskStatus(task.getTaskStatus().name());
            response.setStatus(ResponseStatusFactory.ok().setMessageKey(Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION));
            return response;
        } catch (TaskStackException e) {
            throw new IdentityStoreException("Error while retrieving task " + taskCode + ".", e);
        }
    }
}
