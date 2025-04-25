package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.service.ClientTaskRightService;
import fr.paris.lutece.plugins.identitystore.v3.web.request.AbstractIdentityStoreAppCodeRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseStatus;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.ClientAuthorizationException;
import fr.paris.lutece.plugins.identitystore.web.exception.DuplicatesConsistencyException;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.identitystore.web.exception.RequestContentFormattingException;
import fr.paris.lutece.plugins.identitystore.web.exception.RequestFormatException;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceConsistencyException;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceNotFoundException;

public class TaskStackIdentityPartialSearchRequest extends AbstractIdentityStoreAppCodeRequest
{

    String _clientCode;

    public TaskStackIdentityPartialSearchRequest(String strClientCode, String strAppCode, String authorName,
                                                 String authorType) throws RequestFormatException
    {
        super(strClientCode, strAppCode, authorName, authorType);
    }

    public TaskStackIdentityPartialSearchRequest(String clientCode, String strClientCode,
                                                 String strAppCode, String authorName,
                                                 String authorType) throws RequestFormatException
    {
        super(strClientCode, strAppCode, authorName, authorType);
        _clientCode = clientCode;
    }


    @Override
    protected void fetchResources() throws ResourceNotFoundException
    {

    }

    @Override
    protected void validateRequestFormat() throws RequestFormatException
    {
        IdentityRequestValidator.instance( ).checkClientCode( _clientCode );
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
    protected ClientTaskRightSearchResponse doSpecificRequest() throws IdentityStoreException
    {
        final ClientTaskRightSearchResponse response = new ClientTaskRightSearchResponse();

        response.setClientTaskDtoList(ClientTaskRightService.instance().partialSearch( _clientCode, _author, _strClientCode ));
        response.setStatus( ResponseStatusFactory.success( ) );
        return response;
    }
}
