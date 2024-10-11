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

import fr.paris.lutece.plugins.identitystore.business.attribute.AttributeKey;
import fr.paris.lutece.plugins.identitystore.business.identity.Identity;
import fr.paris.lutece.plugins.identitystore.business.identity.IdentityHome;
import fr.paris.lutece.plugins.identitystore.business.referentiel.RefAttributeCertificationLevel;
import fr.paris.lutece.plugins.identitystore.service.attribute.IdentityAttributeService;
import fr.paris.lutece.plugins.identitystore.service.contract.AttributeCertificationDefinitionService;
import fr.paris.lutece.plugins.identitystore.v3.web.request.validator.IdentityAttributeValidator;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.DtoConverter;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.AttributeDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.IdentityDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.ResourceNotFoundException;
import fr.paris.lutece.plugins.taskstack.exception.TaskValidationException;
import fr.paris.lutece.plugins.taskstack.service.ITaskManagement;
import fr.paris.lutece.portal.service.util.AppPropertiesService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractTaskManagement implements ITaskManagement
{
    protected IdentityDto validateAndGetIdentity( final String cuid ) throws TaskValidationException
    {
        final Identity identity = IdentityHome.findByCustomerId( cuid );
        if ( identity == null )
        {
            throw new TaskValidationException( "No identity was found for the provided CUID " + cuid );
        }
        if ( identity.isDeleted( ) )
        {
            throw new TaskValidationException( "The identity " + cuid + " is deleted" );
        }
        if ( identity.isMerged( ) )
        {
            throw new TaskValidationException( "The identity " + cuid + " is merged" );
        }

        return DtoConverter.convertIdentityToDto( identity );
    }

    protected void validateEmail( final IdentityDto identityDto, final boolean checkCertification ) throws TaskValidationException
    {
        final AttributeDto emailAttr = identityDto.getAttributes( ).stream( ).filter( a -> a.getKey( ).equals( Constants.PARAM_EMAIL ) ).findFirst( )
                .orElseThrow( ( ) -> new TaskValidationException( "The identity does not have an email attribute" ) );
        try
        {
            if ( !IdentityAttributeValidator.instance( ).validateAttribute( Constants.PARAM_EMAIL, emailAttr.getValue( ) ) )
            {
                throw new TaskValidationException( "The identity email has an invalid value format" );
            }
        }
        catch( final ResourceNotFoundException e )
        {
            throw new TaskValidationException( e.getMessage( ), e );
        }
        if ( checkCertification )
        {
            if ( emailAttr.getCertificationLevel( ) != null && emailAttr.getCertificationLevel( ) > 100 )
            {
                throw new TaskValidationException( "The identity email has already been validated" );
            }
        }
    }

    protected void validateAccountRequirement( final IdentityDto identityDto ) throws TaskValidationException
    {
        this.validateEmail( identityDto, false );
        if ( identityDto.isMonParisActive( ) )
        {
            throw new TaskValidationException( "The identity is already connected" );
        }
        this.validateIdentityCertification( identityDto );
    }

    // TODO valider la spec: l'identité doit avoir un niveau minimum de certification (à définir en properties, le niveau de ORIG1 pour commencer)
    private void validateIdentityCertification( final IdentityDto identityDto ) throws TaskValidationException
    {
        final String minCertificationCode = AppPropertiesService.getProperty( "task.account.creation.minimum.certification" );
        final List<String> pivotKeys = IdentityAttributeService.instance( ).getPivotAttributeKeys( ).stream( ).map( AttributeKey::getKeyName )
                .collect( Collectors.toList( ) );
        final List<AttributeDto> pivotAttributes = identityDto.getAttributes( ).stream( ).filter( a -> pivotKeys.contains( a.getKey( ) ) )
                .collect( Collectors.toList( ) );

        // Born in France
        if ( pivotAttributes.size( ) == pivotKeys.size( ) )
        {
            this.validateAttributesCertification( pivotAttributes, minCertificationCode );
        }

        // Not born in france
        else
            if ( pivotAttributes.size( ) == pivotKeys.size( ) - 1
                    && pivotAttributes.stream( ).map( AttributeDto::getKey ).noneMatch( s -> s.equals( Constants.PARAM_BIRTH_PLACE_CODE ) )
                    && pivotAttributes.stream( ).anyMatch( attributeDto -> attributeDto.getKey( ).equals( Constants.PARAM_BIRTH_COUNTRY_CODE )
                            && !attributeDto.getValue( ).equals( "99100" ) ) )
            {
                this.validateAttributesCertification( pivotAttributes, minCertificationCode );
            }

            // invalid
            else
            {
                throw new TaskValidationException( "The identity has missing pivot attributes and cannot be connected" );
            }
    }

    private void validateAttributesCertification( final List<AttributeDto> pivotAttributes, final String minCertificationCode ) throws TaskValidationException
    {
        final List<String> errors = new ArrayList<>( );
        for ( final AttributeDto attributeDto : pivotAttributes )
        {
            final RefAttributeCertificationLevel certificationLevel = AttributeCertificationDefinitionService.instance( ).get( minCertificationCode,
                    attributeDto.getKey( ) );
            final Integer minRequiredLevel = Integer.valueOf( certificationLevel.getRefCertificationLevel( ).getLevel( ) );
            if ( attributeDto.getCertificationLevel( ) < minRequiredLevel )
            {
                errors.add( "[attribute-key=" + attributeDto.getKey( ) + "][attribute-certification-level=" + attributeDto.getCertificationLevel( )
                        + "][expected-level=" + minRequiredLevel + "]" );
            }
        }
        if ( !errors.isEmpty( ) )
        {
            final StringBuilder error = new StringBuilder( "Some errors occurred during pivot attributes validation. The minimum certification processus is " )
                    .append( minCertificationCode ).append( "." );
            errors.forEach( error::append );
            throw new TaskValidationException( error.toString( ) );
        }
    }

}
