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
package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.service.TaskConverter;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.AbstractIdentityStoreRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.IdentityTaskRequestValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.ResponseStatusFactory;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.CreationDateOrdering;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskStackException;
import fr.paris.lutece.plugins.taskstack.service.TaskService;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdentityStoreSearchTaskRequest extends AbstractIdentityStoreRequest
{

    private final IdentityTaskSearchRequest taskSearchRequest;

    public IdentityStoreSearchTaskRequest( final IdentityTaskSearchRequest taskSearchRequest, final String strClientCode, final String authorName,
            final String authorType ) throws IdentityStoreException
    {
        super( strClientCode, authorName, authorType );
        this.taskSearchRequest = taskSearchRequest;
    }

    @Override
    protected void validateSpecificRequest( ) throws IdentityStoreException
    {
        IdentityTaskRequestValidator.instance( ).validateTaskSearchRequest( taskSearchRequest );
    }

    @Override
    protected Object doSpecificRequest( ) throws IdentityStoreException
    {
        try
        {
            final String strTaskType = taskSearchRequest.getTaskType( ) != null ? taskSearchRequest.getTaskType( ).name( ) : null;
            final List<TaskStatusType> taskStatus = new ArrayList<>( );
            if ( CollectionUtils.isNotEmpty( taskSearchRequest.getTaskStatus( ) ) )
            {
                taskStatus
                        .addAll( taskSearchRequest.getTaskStatus( ).stream( ).map( t -> TaskStatusType.valueOf( t.name( ) ) ).collect( Collectors.toList( ) ) );
            }
            final CreationDateOrdering creationDateOrdering = taskSearchRequest.getCreationDateOrdering( ) != null
                    ? CreationDateOrdering.valueOf( taskSearchRequest.getCreationDateOrdering( ).name( ) )
                    : null;
            final List<TaskDto> searchResults = TaskService.instance( ).search( strTaskType, taskStatus, taskSearchRequest.getNbDaysSinceCreated( ),
                    creationDateOrdering );
            final IdentityTaskSearchResponse response = new IdentityTaskSearchResponse( );
            response.getTasks( ).addAll( searchResults.stream( ).map( task -> TaskConverter.instance( ).fromCore( task ) ).collect( Collectors.toList( ) ) );
            response.setStatus( ResponseStatusFactory.success( ).setMessageKey( Constants.PROPERTY_REST_INFO_SUCCESSFUL_OPERATION ) );
            return response;
        }
        catch( final TaskStackException e )
        {
            throw new IdentityStoreException( "An error occurred during task creation request: " + e.getMessage( ), e );
        }
    }
}
