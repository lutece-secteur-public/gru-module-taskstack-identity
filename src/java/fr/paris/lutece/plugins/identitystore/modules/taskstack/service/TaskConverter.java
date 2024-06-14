/*
 * Copyright (c) 2002-2024, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.identitystore.modules.taskstack.service;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskStatusType;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;

public class TaskConverter
{
    private static TaskConverter instance;

    public static TaskConverter instance( )
    {
        if ( instance == null )
        {
            instance = new TaskConverter( );
        }
        return instance;
    }

    private TaskConverter( )
    {
    }

    public TaskDto toCore( final IdentityTaskDto identityTaskDto )
    {
        final TaskDto taskDto = new TaskDto( );
        taskDto.setTaskCode( identityTaskDto.getTaskCode( ) );
        if ( identityTaskDto.getTaskStatus( ) != null )
        {
            taskDto.setTaskStatus( TaskStatusType.valueOf( identityTaskDto.getTaskStatus( ).name( ) ) );
        }
        taskDto.setTaskType( identityTaskDto.getTaskType( ) );
        taskDto.getMetadata( ).putAll( identityTaskDto.getMetadata( ) );
        taskDto.setCreationDate( identityTaskDto.getCreationDate( ) );
        taskDto.setLastUpdateDate( identityTaskDto.getLastUpdateDate( ) );
        taskDto.setLastUpdateClientCode( identityTaskDto.getLastUpdateClientCode( ) );
        taskDto.setResourceId( identityTaskDto.getResourceId( ) );
        taskDto.setResourceType( identityTaskDto.getResourceType( ) );
        return taskDto;
    }

    public IdentityTaskDto fromCore( final TaskDto taskDto )
    {
        final IdentityTaskDto identityTaskDto = new IdentityTaskDto( );
        identityTaskDto.setTaskCode( taskDto.getTaskCode( ) );
        if ( taskDto.getTaskStatus( ) != null )
        {
            identityTaskDto.setTaskStatus( IdentityTaskStatusType.valueOf( taskDto.getTaskStatus( ).name( ) ) );
        }
        identityTaskDto.setTaskType( taskDto.getTaskType( ) );
        identityTaskDto.getMetadata( ).putAll( taskDto.getMetadata( ) );
        identityTaskDto.setCreationDate( taskDto.getCreationDate( ) );
        identityTaskDto.setLastUpdateDate( taskDto.getLastUpdateDate( ) );
        identityTaskDto.setLastUpdateClientCode( taskDto.getLastUpdateClientCode( ) );
        identityTaskDto.setResourceId( taskDto.getResourceId( ) );
        identityTaskDto.setResourceType( taskDto.getResourceType( ) );
        return identityTaskDto;
    }
}
