/*
 * Copyright (c) 2002-2025, City of Paris
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

import fr.paris.lutece.plugins.identitystore.business.application.ClientApplication;
import fr.paris.lutece.plugins.identitystore.business.application.ClientApplicationHome;
import fr.paris.lutece.plugins.identitystore.business.contract.ServiceContract;
import fr.paris.lutece.plugins.taskstack.business.task.TaskChangeType;
import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRight;
import fr.paris.lutece.plugins.taskstack.business.taskright.TaskRightHome;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.DtoConverter;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.application.ClientApplicationDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.IdentityTaskType;
import fr.paris.lutece.plugins.taskstack.rs.dto.TaskRightType;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.taskstack.dto.TaskDto;
import fr.paris.lutece.plugins.taskstack.exception.TaskValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class AttachmentCertificationRequestManagement extends AbstractTaskManagement
{
    @Override
    public String getTaskType( )
    {
        return IdentityTaskType.ATTACHMENT_CERTIFICATION_REQUEST.name( );
    }

    @Override
    public void doBefore( final TaskDto task ) throws TaskValidationException
    {
        switch( task.getTaskStatus( ) )
        {
            case TODO:
                final String files = task.getMetadata( ).get( Constants.METADATA_FILES );
                this.validateAndGetIdentity( task.getResourceId( ) );
                this.validateFiles( files );
                break;
            case IN_PROGRESS:
            case REFUSED:
            case CANCELED:
            case PROCESSED:
            default:
                break;
        }
    }

    private void validateIdentity( final IdentityDto identityDto ) throws TaskValidationException
    {

        if ( identityDto == null )
        {
            throw new TaskValidationException( "The cuid does not correspond to a valid identity" );
        }
    }

    private void validateFiles ( final String metadata ) throws TaskValidationException
    {
        if ( StringUtils.isBlank( metadata ) )
        {
            throw new TaskValidationException( "The Files metadata are null or empty" );
        }
    }

    private ClientApplicationDto validateAndGetClientCode( final String clientCode ) throws TaskValidationException
    {
        ClientApplication clientApplication = ClientApplicationHome.findByCode( clientCode );
        if ( clientApplication == null )
        {
            return null;
        }

        return DtoConverter.convertClientToDto( clientApplication );
    }

    @Override
    public void doAfter(final TaskDto task ) throws TaskValidationException
    {

    }

    @Override
    public void checkAccess(final TaskDto task, TaskChangeType type ) throws TaskValidationException
    {
        if(type.name().equals(TaskChangeType.UPDATED.name()) || type.name().equals(TaskChangeType.READ.name())) {
            final ClientApplicationDto clientApplicationDto = (this.validateAndGetClientCode(task.getMetadata().get(Constants.METADATA_CLIENT_CODE)));
            this.validateRights(clientApplicationDto);
        }
    }

    private void validateRights( ClientApplicationDto clientApplicationDto ) throws TaskValidationException
    {
        List<TaskRight> taskRightList = clientApplicationDto == null
                ? TaskRightHome.searchTaskRight("*", null, null)
                : TaskRightHome.searchTaskRight(clientApplicationDto.getClientCode() , null, null);
        boolean certificationRight = false;
        for( TaskRight taskRight : taskRightList)
        {
            if( StringUtils.equals(taskRight.getTaskType(), TaskRightType.ATTACHMENT_CERTIFICATION.name() )
                    || StringUtils.equals(taskRight.getTaskType(), Constants.PARAM_ALL_TASK_TYPES ) )
            {
                List<ServiceContract> serviceContractList = ClientApplicationHome.selectActiveServiceContract(taskRight.getAuthorizedClientCode());

                for (ServiceContract svcContract : serviceContractList)
                {
                    if (svcContract.getAuthorizedAttachmentCertification())
                    {
                        certificationRight = true;
                        break;
                    }
                }
                if (certificationRight)
                {
                    break;
                }
            }
        }
        if ( !certificationRight )
        {
            throw new TaskValidationException( "Client does not have rights to import files" );
        }
    }
}
