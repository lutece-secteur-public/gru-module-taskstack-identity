package fr.paris.lutece.plugins.identitystore.modules.taskstack.service;

import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;

public class TaskConverter {
    private static TaskConverter instance;
    public static TaskConverter instance( ) {if(instance==null){instance=new TaskConverter( );}return instance;}
    private TaskConverter( ) {}

    public TaskDto toCore(final fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.TaskDto taskDto){
        final TaskDto taskCore = new TaskDto( );
        taskCore.setTaskCode(taskDto.getTaskCode( ));
        taskCore.setTaskStatus(TaskStatusType.valueOf(taskDto.getTaskStatus().name()));
        taskCore.setTaskType(taskDto.getTaskType( ));
        taskCore.setMetadata(taskDto.getMetadata( ));
        taskCore.setCreationDate(taskDto.getCreationDate( ));
        taskCore.setLastUpdateDate(taskDto.getLastUpdateDate( ));
        taskCore.setLastUpdateClientCode(taskDto.getLastUpdateClientCode( ));
        taskCore.setResourceId(taskDto.getResourceId( ));
        taskCore.setResourceType(taskDto.getResourceType( ));
        return taskCore;
    }
}
