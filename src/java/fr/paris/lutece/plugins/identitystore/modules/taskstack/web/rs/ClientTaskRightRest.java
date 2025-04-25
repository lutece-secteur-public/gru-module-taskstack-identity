package fr.paris.lutece.plugins.identitystore.modules.taskstack.web.rs;

import fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request.TaskStackIdentityCompleteSearchRequest;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request.TaskStackIdentityCreateRequest;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request.TaskStackIdentityDeleteRequest;
import fr.paris.lutece.plugins.identitystore.modules.taskstack.web.request.TaskStackIdentityPartialSearchRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.IRestService;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.common.ResponseDto;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.search.IdentitySearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightCreateRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightDeleteRequest;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.dto.task.clientTask.ClientTaskRightSearchResponse;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.swagger.SwaggerConstants;
import fr.paris.lutece.plugins.identitystore.v3.web.rs.util.Constants;
import fr.paris.lutece.plugins.identitystore.web.exception.IdentityStoreException;
import fr.paris.lutece.plugins.rest.service.RestConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static fr.paris.lutece.plugins.identitystore.v3.web.rs.error.UncaughtResourceNotFoundExceptionMapper.ERROR_RESOURCE_NOT_FOUND;
import static fr.paris.lutece.plugins.rest.service.mapper.GenericUncaughtExceptionMapper.ERROR_DURING_TREATMENT;

@Path(RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_PATH_V3 + Constants.CLIENT_TASK_PATH)
@Api(RestConstants.BASE_PATH + Constants.PLUGIN_PATH + Constants.VERSION_PATH_V3 + Constants.CLIENT_TASK_PATH)
public class ClientTaskRightRest implements IRestService
{
    @POST
    @Path( "" )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Create a ClientTaskRight by a set of attributes", response = ResponseDto.class )
    @ApiResponses( value = {
            @ApiResponse( code = 201, message = "Success" ), @ApiResponse( code = 400, message = ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 403, message = "Forbidden" ), @ApiResponse( code = 404, message = ERROR_RESOURCE_NOT_FOUND ),
            @ApiResponse( code = 409, message = "Conflict" )
    } )
    public Response createClientTask(
            @ApiParam( name = "Request body", value = "ClientTask create Request", type = "ClientTaskRightCreateRequest" ) ClientTaskRightCreateRequest clientTaskRightCreateRequest,
            @ApiParam( name = Constants.PARAM_CLIENT_CODE, value = SwaggerConstants.PARAM_CLIENT_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @ApiParam( name = Constants.PARAM_AUTHOR_NAME, value = SwaggerConstants.PARAM_AUTHOR_NAME_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @ApiParam( name = Constants.PARAM_AUTHOR_TYPE, value = SwaggerConstants.PARAM_AUTHOR_TYPE_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @ApiParam( name = Constants.PARAM_APPLICATION_CODE, value = SwaggerConstants.PARAM_APPLICATION_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws IdentityStoreException
    {
        final TaskStackIdentityCreateRequest request = new TaskStackIdentityCreateRequest( clientTaskRightCreateRequest, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        return this.buildJsonResponse( request.doRequest() );
    }

    @DELETE
    @Path( Constants.DELETE_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Delete a ClientTaskRight by a set of attributes", response = ResponseDto.class )
    @ApiResponses( value = {
            @ApiResponse( code = 201, message = "Success" ), @ApiResponse( code = 400, message = ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 403, message = "Forbidden" ), @ApiResponse( code = 404, message = ERROR_RESOURCE_NOT_FOUND ),
            @ApiResponse( code = 409, message = "Conflict" )
    } )
    public Response deleteClientTask(
            @ApiParam( name = "Request body", value = "ClientTask delete Request", type = "ClientTaskRightDeleteRequest" ) ClientTaskRightDeleteRequest clientTaskRightDeleteRequest,
            @ApiParam( name = Constants.PARAM_CLIENT_CODE, value = SwaggerConstants.PARAM_CLIENT_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @ApiParam( name = Constants.PARAM_AUTHOR_NAME, value = SwaggerConstants.PARAM_AUTHOR_NAME_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @ApiParam( name = Constants.PARAM_AUTHOR_TYPE, value = SwaggerConstants.PARAM_AUTHOR_TYPE_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @ApiParam( name = Constants.PARAM_APPLICATION_CODE, value = SwaggerConstants.PARAM_APPLICATION_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws IdentityStoreException
    {
        final TaskStackIdentityDeleteRequest request = new TaskStackIdentityDeleteRequest( clientTaskRightDeleteRequest, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        return this.buildJsonResponse( request.doRequest() );
    }

    @GET
    @Path( Constants.PARTIAL_SEARCH_PATH + "{" + Constants.PARAM_SOURCE_CLIENT_CODE + "}" )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Get a ClientTask by its source client code", response = ClientTaskRightSearchResponse.class )
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "Identity Found" ), @ApiResponse( code = 400, message = ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 403, message = "Forbidden" ), @ApiResponse( code = 404, message = ERROR_RESOURCE_NOT_FOUND )
    } )
    public Response partialSearchClientTask(
            @ApiParam( name = Constants.PARAM_SOURCE_CLIENT_CODE, value = "Client code of the task creator" ) @PathParam( Constants.PARAM_SOURCE_CLIENT_CODE ) String sourceClientCode,
            @ApiParam( name = Constants.PARAM_CLIENT_CODE, value = SwaggerConstants.PARAM_CLIENT_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @ApiParam( name = Constants.PARAM_AUTHOR_NAME, value = SwaggerConstants.PARAM_AUTHOR_NAME_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @ApiParam( name = Constants.PARAM_AUTHOR_TYPE, value = SwaggerConstants.PARAM_AUTHOR_TYPE_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @ApiParam( name = Constants.PARAM_APPLICATION_CODE, value = SwaggerConstants.PARAM_APPLICATION_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws IdentityStoreException
    {
        final TaskStackIdentityPartialSearchRequest request = new TaskStackIdentityPartialSearchRequest( sourceClientCode, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        return this.buildJsonResponse( request.doRequest() );
    }

    @GET
    @Path( Constants.COMPLETE_SEARCH_PATH )
    @Produces( MediaType.APPLICATION_JSON )
    @Consumes( MediaType.APPLICATION_JSON )
    @ApiOperation( value = "Get a ClientTask by its source client code and task user client code", response = ClientTaskRightSearchResponse.class )
    @ApiResponses( value = {
            @ApiResponse( code = 200, message = "Identity Found" ), @ApiResponse( code = 400, message = ERROR_DURING_TREATMENT + " with explanation message" ),
            @ApiResponse( code = 403, message = "Forbidden" ), @ApiResponse( code = 404, message = ERROR_RESOURCE_NOT_FOUND )
    } )
    public Response completeSearchClientTask(
            @ApiParam( name = Constants.PARAM_SOURCE_CLIENT_CODE, value = "Client code of the task creator" ) @QueryParam( Constants.PARAM_SOURCE_CLIENT_CODE ) final String sourceClientCode,
            @ApiParam( name = Constants.PARAM_CLIENT_CODE_TASK_USER, value = "Client code of the task user" ) @QueryParam( Constants.PARAM_CLIENT_CODE_TASK_USER ) final String clientCodeTaskUser,
            @ApiParam( name = Constants.PARAM_CLIENT_CODE, value = SwaggerConstants.PARAM_CLIENT_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_CLIENT_CODE ) final String strHeaderClientCode,
            @ApiParam( name = Constants.PARAM_AUTHOR_NAME, value = SwaggerConstants.PARAM_AUTHOR_NAME_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_NAME ) final String authorName,
            @ApiParam( name = Constants.PARAM_AUTHOR_TYPE, value = SwaggerConstants.PARAM_AUTHOR_TYPE_DESCRIPTION ) @HeaderParam( Constants.PARAM_AUTHOR_TYPE ) final String authorType,
            @ApiParam( name = Constants.PARAM_APPLICATION_CODE, value = SwaggerConstants.PARAM_APPLICATION_CODE_DESCRIPTION ) @HeaderParam( Constants.PARAM_APPLICATION_CODE ) @DefaultValue( "" ) final String strHeaderAppCode)
            throws IdentityStoreException
    {
        final TaskStackIdentityCompleteSearchRequest request = new TaskStackIdentityCompleteSearchRequest( sourceClientCode, clientCodeTaskUser, strHeaderClientCode, strHeaderAppCode, authorName, authorType);
        return this.buildJsonResponse( request.doRequest() );
    }

}
