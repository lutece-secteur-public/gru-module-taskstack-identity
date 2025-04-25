package fr.paris.lutece.plugins.identitystore.modules.taskstack.business;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskDto;

public class ClientTaskMapper
{

    public static ClientTask clientTaskDtoToClientTask(ClientTaskDto clientTaskDto)
    {
        ClientTask clientTask = new ClientTask( );

        clientTask.setClientCodeSource( clientTaskDto.getClientCodeSource( ) );
        clientTask.setClientCodeTaskUser( clientTaskDto.getClientCodeTaskUser( ) );
        clientTask.setAskedRights( clientTaskDto.getAskedRights( ) );

        return clientTask;
    }

    public static ClientTaskDto clientTaskToClientTaskDto(ClientTask clientTask)
    {
        ClientTaskDto clientTaskDto = new ClientTaskDto( );

        clientTaskDto.setClientCodeSource( clientTask.getClientCodeSource( ) );
        clientTaskDto.setClientCodeTaskUser( clientTask.getClientCodeTaskUser( ) );
        clientTaskDto.setAskedRights( clientTask.getAskedRights( ) );

        return clientTaskDto;
    }

}
