package fr.paris.lutece.plugins.identitystore.modules.taskstack.service;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.business.ClientTask;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.business.ClientTaskMapper;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.business.ClientTaskRightHome;
import fr.paris.lutece.plugins.identitystore.service.user.InternalUserService;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.RequestAuthor;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightDeleteRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.portal.service.security.AccessLogService;
import fr.paris.lutece.portal.service.security.AccessLoggerConstants;
import fr.paris.lutece.util.http.SecurityUtil;
import fr.paris.lutece.util.sql.TransactionManager;

import java.util.ArrayList;
import java.util.List;

public class ClientTaskRightService
{

    private static ClientTaskRightService _instance = new ClientTaskRightService( );


    public static final String CREATE_CLIENT_TASK_RIGHT_EVENT_CODE = "CREATE_CLIENT_TASK_RIGHT";
    public static final String DELETE_CLIENT_TASK_RIGHT_EVENT_CODE = "DELETE_CLIENT_TASK_RIGHT";
    public static final String SEARCH_CLIENT_TASK_RIGHT_EVENT_CODE = "SEARCH_CLIENT_TASK_RIGHT";
    public static final String SPECIFIC_ORIGIN = "BO";

    private final InternalUserService _internalUserService = InternalUserService.getInstance( );

    public static ClientTaskRightService instance ( )
    {
        if( _instance == null )
        {
            _instance = new ClientTaskRightService( );
        }
        return _instance;
    }

    public void create (final ClientTaskRightCreateRequest request , final RequestAuthor author, final String clientCode) throws IdentityStoreException
    {
        ClientTask clientTask = ClientTaskMapper.clientTaskDtoToClientTask(request.getClientTask());
        TransactionManager.beginTransaction( null );
        try
        {
            ClientTaskRightHome.insert(clientTask);
            TransactionManager.commitTransaction( null );

            AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_CREATE, CREATE_CLIENT_TASK_RIGHT_EVENT_CODE,
                    _internalUserService.getApiUser( author, clientCode ), SecurityUtil.logForgingProtect( clientCode ), SPECIFIC_ORIGIN );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new IdentityStoreException( e.getMessage( ), e, Constants.PROPERTY_REST_ERROR_DURING_TREATMENT );
        }
    }

    public void delete (final ClientTaskRightDeleteRequest request , final RequestAuthor author, final String clientCode) throws IdentityStoreException
    {
        ClientTask clientTask = ClientTaskMapper.clientTaskDtoToClientTask(request.getClientTask());
        TransactionManager.beginTransaction( null );

        try
        {
            ClientTaskRightHome.delete(clientTask);
            TransactionManager.commitTransaction( null );

            AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_DELETE, DELETE_CLIENT_TASK_RIGHT_EVENT_CODE,
                    _internalUserService.getApiUser( author, clientCode ), SecurityUtil.logForgingProtect( clientCode ), SPECIFIC_ORIGIN );
        }
        catch( final Exception e )
        {
            TransactionManager.rollBack( null );
            throw new IdentityStoreException( e.getMessage( ), e, Constants.PROPERTY_REST_ERROR_DURING_TREATMENT );
        }
    }

    public List<ClientTaskDto> partialSearch(final String clientCodeSource, final RequestAuthor author, final String clientCode) throws IdentityStoreException
    {
        List<ClientTaskDto> result = new ArrayList<ClientTaskDto>( );

        try
        {
            List<ClientTask> clientTaskList = ClientTaskRightHome.getListClientTaskWithCodeSource(clientCodeSource);

            AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_READ, SEARCH_CLIENT_TASK_RIGHT_EVENT_CODE,
                    _internalUserService.getApiUser( author, clientCode ), SecurityUtil.logForgingProtect( clientCodeSource ),
                    SPECIFIC_ORIGIN );

            clientTaskList.forEach( clientTask ->  result.add(ClientTaskMapper.clientTaskToClientTaskDto(clientTask)));
        }
        catch( final Exception e )
        {
            throw new IdentityStoreException( e.getMessage( ), e, Constants.PROPERTY_REST_ERROR_DURING_TREATMENT );
        }

        return result;
    }

    public List<ClientTaskDto> CompleteSearch(final String clientCodeSource, final String clientCodeTaskUser, final RequestAuthor author, final String clientCode) throws IdentityStoreException
    {
        List<ClientTaskDto> result = new ArrayList<ClientTaskDto>( );

        try
        {
            List<ClientTask> clientTaskList = ClientTaskRightHome.getListClientTaskWithAllCodes( clientCodeSource, clientCodeTaskUser );

            AccessLogService.getInstance( ).info( AccessLoggerConstants.EVENT_TYPE_READ, SEARCH_CLIENT_TASK_RIGHT_EVENT_CODE,
                    _internalUserService.getApiUser( author, clientCode ), SecurityUtil.logForgingProtect( clientCodeSource ),
                    SPECIFIC_ORIGIN );

            clientTaskList.forEach( clientTask ->  result.add(ClientTaskMapper.clientTaskToClientTaskDto(clientTask)));
        }
        catch( final Exception e )
        {
            throw new IdentityStoreException( e.getMessage( ), e, Constants.PROPERTY_REST_ERROR_DURING_TREATMENT );
        }

        return result;
    }
}
