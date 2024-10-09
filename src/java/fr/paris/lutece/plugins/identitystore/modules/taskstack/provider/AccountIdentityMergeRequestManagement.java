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
package fr.paris.lutece.plugins.identitystore.modules.taskstack.provider;

import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.taskstack.business.task.TaskStatusType;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskValidationException;

public class AccountIdentityMergeRequestManagement extends AbstractTaskManagement
{
    @Override
    public String getTaskType( )
    {
        return IdentityTaskType.ACCOUNT_IDENTITY_MERGE_REQUEST.name( );
    }

    @Override
    public void doBefore( final TaskDto task ) throws TaskValidationException
    {
        switch( task.getTaskStatus( ) )
        {
            case TODO:
                final String secondCuid = task.getMetadata( ).get( Constants.METADATA_ACCOUNT_MERGE_SECOND_CUID );
                final IdentityDto firstIdentity = this.validateAndGetIdentity( task.getResourceId( ) );
                final IdentityDto secondIdentity = this.validateAndGetIdentity( secondCuid );
                this.validateIdentity( firstIdentity, secondIdentity );
                break;
            case IN_PROGRESS:
            case REFUSED:
            case CANCELED:
            case PROCESSED:
            default:
                break;
        }
    }

    private void validateIdentity( final IdentityDto identityDto1, final IdentityDto identityDto2  ) throws TaskValidationException
    {
        this.validateEmail( identityDto2, false );
        
        if ( !identityDto1.isMonParisActive( ) )
        {
            throw new TaskValidationException( "The identity " + identityDto1.getCustomerId( ) + " is not connected (Mon Paris)" );
        }
    }

    @Override
    public void doAfter( final TaskDto task ) throws TaskValidationException
    {

    }
}
