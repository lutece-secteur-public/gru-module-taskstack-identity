package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.business.ClientTaskRightHome;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.service.ClientTaskRightService;
import fr.paris.lutece.plugins.identitystore.v3.web.request.AbstractIdentityStoreAppCodeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.ClientAuthorizationException;
import fr.paris.lutece.plugins.identitystore.web.exception.DuplicatesConsistencyException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.web.exception.RequestContentFormattingException;
import fr.paris.lutece.plugins.identitystore.web.exception.RequestFormatException;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceConsistencyException;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceNotFoundException;

public class TaskStackIdentityCreateRequest extends AbstractIdentityStoreAppCodeRequest
{

    ClientTaskRightCreateRequest _clientTaskRequest;

    public TaskStackIdentityCreateRequest(String strClientCode, String strAppCode, String authorName,
                                             String authorType) throws RequestFormatException
    {
        super(strClientCode, strAppCode, authorName, authorType);
    }

    public TaskStackIdentityCreateRequest(ClientTaskRightCreateRequest clientTaskRequest, String strClientCode,
                                             String strAppCode, String authorName,
                                             String authorType) throws RequestFormatException
    {
        super(strClientCode, strAppCode, authorName, authorType);
        _clientTaskRequest = clientTaskRequest;
    }


    @Override
    protected void fetchResources() throws ResourceNotFoundException
    {

    }

    @Override
    protected void validateRequestFormat() throws RequestFormatException
    {
        IdentityRequestValidator.instance( ).checkClientTask(_clientTaskRequest.getClientTask());
    }

    @Override
    protected void validateClientAuthorization() throws ClientAuthorizationException
    {

    }

    @Override
    protected void validateResourcesConsistency() throws ResourceConsistencyException
    {

    }

    @Override
    protected void formatRequestContent() throws RequestContentFormattingException
    {

    }

    @Override
    protected void checkDuplicatesConsistency() throws DuplicatesConsistencyException
    {

    }

    @Override
    protected ResponseDto doSpecificRequest() throws IdentityStoreException
    {
        ResponseDto response = new ResponseDto( );

        ClientTaskRightService.instance().create( _clientTaskRequest, _author, _strClientCode );
        response.setStatus( ResponseStatusFactory.success( ) );

        return response;
    }
}
