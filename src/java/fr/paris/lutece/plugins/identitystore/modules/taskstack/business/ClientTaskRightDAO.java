package fr.paris.lutece.plugins.identitystore.modules.taskstack.business;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

public class ClientTaskRightDAO implements IClientTaskRightDAO
{

    public static final String BEAN_NAME = "taskstack-identity.client-task-right.dao";

    //Queries
    private static final String SQL_QUERY_INSERT = "INSERT INTO identitystore_client_code_task_right (client_code_source, client_code_task_user, asked_rights) VALUES (?, ?, ?)";
    private static final String SQL_QUERY_SELECT = "SELECT client_code_source, client_code_task_user, asked_rights FROM identitystore_client_code_task_right WHERE client_code_source = ?";
    private static final String SQL_QUERY_SELECT_WITH_ALL_CODES = "SELECT client_code_source, client_code_task_user, asked_rights FROM identitystore_client_code_task_right WHERE client_code_source = ? AND client_code_task_user = ?";
    private static final String SQL_QUERY_DELETE = "DELETE FROM identitystore_client_code_task_right WHERE client_code_source = ? AND client_code_task_user = ? AND asked_rights = ?";

    @Override
    public void insert(ClientTask client, Plugin plugin) throws JsonProcessingException
    {
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, client.getClientCodeSource( ) );
            daoUtil.setString( nIndex++, client.getClientCodeTaskUser( ) );
            daoUtil.setString( nIndex, client.getAskedRights( ) );

            daoUtil.executeUpdate();
        }
    }

    @Override
    public void delete(ClientTask client, Plugin plugin) throws JsonProcessingException
    {
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, client.getClientCodeSource( ) );
            daoUtil.setString( nIndex++, client.getClientCodeTaskUser( ) );
            daoUtil.setString( nIndex, client.getAskedRights( ) );

            daoUtil.executeUpdate();
        }
    }

    @Override
    public List<ClientTask> selectWithAllCodes(String codeClientSource, String codeClientTaskUser, Plugin plugin) throws JsonProcessingException
    {
        final List<ClientTask> clientTasks = new ArrayList<ClientTask>( );
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_WITH_ALL_CODES, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex++, codeClientSource );
            daoUtil.setString( nIndex, codeClientTaskUser );

            daoUtil.executeQuery();
            while ( daoUtil.next( ) )
            {
                clientTasks.add( this.getClientTask( daoUtil ) );
            }
        }
        return clientTasks;
    }

    @Override
    public List<ClientTask> selectWithCodeSource(String codeClientSource, Plugin plugin)
    {
        final List<ClientTask> clientTasks = new ArrayList<ClientTask>( );
        try( final DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin ) )
        {
            int nIndex = 1;
            daoUtil.setString( nIndex, codeClientSource );

            daoUtil.executeQuery();
            while ( daoUtil.next( ) )
            {
                clientTasks.add( this.getClientTask( daoUtil ) );
            }
        }
        return clientTasks;
    }

    private ClientTask getClientTask( DAOUtil daoUtil )
    {
        final ClientTask clientTask = new ClientTask( );

        int nIndex = 1;
        clientTask.setClientCodeSource( daoUtil.getString( nIndex++ ) );
        clientTask.setClientCodeTaskUser( daoUtil.getString( nIndex++ ) );
        clientTask.setAskedRights( daoUtil.getString( nIndex ) );

        return clientTask;
    }
}
